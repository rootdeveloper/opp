/* Copyright 2009 Misys PLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License. 
 */


package org.openhealthexchange.openpixpdq.ihe.impl_v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.openhealthexchange.openpixpdq.ihe.HL7Actor;
import org.openhealthexchange.openpixpdq.ihe.IPixManager;
import org.openhealthexchange.openpixpdq.ihe.IPixManagerAdapter;
import org.openhealthexchange.openpixpdq.ihe.audit.IheAuditTrail;
import org.openhealthexchange.openpixpdq.ihe.configuration.IheConfigurationException;
import org.openhealthexchange.openpixpdq.ihe.impl_v2.hl7.HL7Server;

import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionHub;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * This is the Patient Identifier Cross-referencing (PIX) Manager actor, 
 * the server side actor of the IHE PIX profile. This actor accepts HL7 v2 messages
 * such as ADT^A01, ADT^A04, ADT^A05, ADT^A08 and ADT^A40 from a PIX Source, and QBP^Q23 from a PIX Consumer.
 *  The transactions that this actor handles include PIX Feed, PIX Update, PIX Merge, 
 *  PIX Query and PIX Update Notification. See Sections 3.8, 3.9 and 3.10 of 
 * <a href="http://www.ihe.net/Technical_Framework/index.cfm#IT">Vol. 2 (ITI TF-2): Transactions</a>, 
 * available on the IHE site for more details.   
 *
 * @author Wenzhi Li
 * @version 1.0, Mar 1, 2007
 * @see IPixManager
 */
public class PixManager extends HL7Actor implements IPixManager {
    /* Logger for problems during SOAP exchanges */
    private static Logger log = Logger.getLogger(PixManager.class);

    /* The connection description to this PIX manager */
    private IConnectionDescription connection = null;
	/** The XDS Registry Connection */
	private IConnectionDescription xdsRegistryConnection = null;
    /* The connections for PIX Consumers that subscribe to the PIX Update Notification*/
    private Collection<IConnectionDescription> pixConsumerConnections = null;
    /** The PIX Server */
    private HL7Server server = null;
    /** The PIX Manager Adapter between this PIX Manager and eMPI*/
    private IPixManagerAdapter pixAdapter = null;

   /**
    * Creates a new PixManager that will talk to a PIX client over
    * the connection description supplied.
    *
    * @param connection The description of the connection of this PIX manager
    * @param auditTrail The audit trail for this PIX Manager
    * @param xdsRegistryConnection The description of the connection of the XDS
    * 			Registry in the affinity domain
    * @param pixConsumerConnections The connections of PIX Consumers subscribing
    * 			to PIX Update Notification messages
    * @throws IheConfigurationException
    */
    public PixManager(IConnectionDescription connection, IheAuditTrail auditTrail, 
    		IConnectionDescription xdsRegistryConnection,
    		Collection<IConnectionDescription> pixConsumerConnections) 
    	    throws IheConfigurationException {
        super(connection, auditTrail);
        this.connection = connection;
        this.xdsRegistryConnection = xdsRegistryConnection;
        this.pixConsumerConnections = pixConsumerConnections;
        if (this.pixConsumerConnections==null)
        	this.pixConsumerConnections = new ArrayList<IConnectionDescription>();
   }

    
    @Override
    public void start() {
        //call the super one to initiate standard start process
        super.start();
        //now begin the local start, initiate pix manager server
        LowerLayerProtocol llp = LowerLayerProtocol.makeLLP(); // The transport protocol
        server = new HL7Server(connection, llp, new PipeParser());
        Application pixQuery = new PixQueryHandler(this);
        Application pixFeed  = new PixFeedHandler(this);
        
        //PIX Query
        server.registerApplication("QBP", "Q23", pixQuery);  
        //Admission of in-patient into a facility
        server.registerApplication("ADT", "A01", pixFeed);  
        //Registration of an out-patient for a visit of the facility
        server.registerApplication("ADT", "A04", pixFeed);  
        //Pre-admission of an in-patient
        server.registerApplication("ADT", "A05", pixFeed);   
        //Update patient information
        server.registerApplication("ADT", "A08", pixFeed);  
        //Merge patients
        server.registerApplication("ADT", "A40", pixFeed);  
        //now start the Pix Manager server
        log.info("Starting PIX Manager: " + this.getName() );
        server.start();
    }

    @Override
    public void stop() {
        //now end the local stop, stop the pix manager server
        server.stop();

        //call the super one to initiate standard stop process
        super.stop();
        
        log.info("PIX Manager stopped: " + this.getName() );

    }

    /**
     * Registers a PixManagerAdapter which delegates patient creation,
     * update, merge and PIX query from this PIX Manager actor to the 
     * underneath eMPI.
     *
     * @param pixManagerAdapter the adapter
     */
    public void registerPixManagerAdapter(IPixManagerAdapter pixManagerAdapter) {
       pixAdapter = pixManagerAdapter;
    }
    
    /**
     * Gets the adapter for this <code>PixManager</code>
     * 
     * @return the adapter
     */
    IPixManagerAdapter getPixManagerAdapter() {
    	return this.pixAdapter;
    }    
    
	/**
	 * Gets the connection for the XDS Registry. The connect provides the details such as host name 
	 * and port etc which are needed for this PIX Manager to talk to the XDS Registry.
	 * 
	 * @return the connection of XDS Registry
	 */
	IConnectionDescription getXdsRegistryConnection() {
		return xdsRegistryConnection;
	}

	/**
     * Gets a collection of all PIX Consumers who have subscribed to
     * the PIX Update Notification transaction.
     *  
	 * @return the pixConsumerConnections
	 */
    Collection<IConnectionDescription> getPixConsumerConnections() {
		return pixConsumerConnections;
	}

	public static void main(String[] args) throws Exception {

//        Session session = HiberUtil.getSession();
//        Query query = session.createQuery("from ConnectPatient where unique_pid = :p and systemId = :s");
//        query.setParameter("p", "1111");
//        query.setParameter("s", "CPR");
//        List<ConnectPatient> patients = query.list();
//
//        List<PersonIdentifier> ret = new ArrayList<PersonIdentifier>();
//        for (ConnectPatient cp : patients) {
//            query = session.createQuery("from ConnectPatient where connect_pid = :cpid and (unique_pid <> :p or systemId <> :s)");
//            query.setParameter("cpid", cp.getConnectPatientId());
//            query.setParameter("p", "1111");
//            query.setParameter("s", "CPR");
//            Iterator results = query.list().iterator();
//            while (results.hasNext()) {
//                PersonIdentifier pid = new PersonIdentifier();
//                ConnectPatient row = (ConnectPatient)results.next();
//                pid.setIdNumber( row.getUniqueSystemPatientId() );
//                pid.setIdentifier( HL7.getAssigningAuthorityFromCXAuth( row.getSystemId() ) );
//                ret.add( pid );
//
//                System.out.println("id=" + row.getUniqueSystemPatientId() + ", aa=" + row.getSystemId()  );
//            }
//        }
//        HiberUtil.closeSession();

//        Session session = HiberUtil.getSession();
//
//        Query query = session.createQuery("from ConnectPatient a where a.connect_pid in select b.connect_pid from ConnectPatient b where b.unique_pid = :p and b.systemId = :s");
//        query.setParameter("p", "1111");
//        query.setParameter("s", "CPR");
//        List patients = query.list();
//        HiberUtil.closeSession();
//
//        if (patients.size() > 0 )
//            System.out.println("found");
//        else
//            System.out.println("Not found");


//        QBP_Q21 message = new QBP_Q21();
//        MSH msh  = message.getMSH();
//        // MSH-1
//        msh.getFieldSeparator().setValue("|");
//        // MSH-2
//        msh.getEncodingCharacters().setValue("^~\\&");
//        // MSH-3
//        HD hd = msh.getSendingApplication();
//        hd.getNamespaceID().setValue( "sendaName" );
//        hd.getUniversalID().setValue( "sendaU" );
//        hd.getUniversalIDType().setValue( "sendaUIDType" );
//        // MSH-4
//        hd = msh.getSendingFacility();
//        hd.getNamespaceID().setValue( "sendfName" );
//        hd.getUniversalID().setValue( "sendfU" );
//        hd.getUniversalIDType().setValue( "sendfUType" );
//        // MSH-5
//        hd = msh.getReceivingApplication();
//        hd.getNamespaceID().setValue( "reaName" );
//        hd.getUniversalID().setValue( "reaU" );
//        hd.getUniversalIDType().setValue( "reaUType");
//        // MSH-6
//        hd = msh.getReceivingFacility();
//        hd.getNamespaceID().setValue( "refName" );
//        hd.getUniversalID().setValue( "refU" );
//        hd.getUniversalIDType().setValue( "refUType" );
//        // MSH-7
//        msh.getDateTimeOfMessage().getTime().setValue("12345");
//        // MSH-9
//        msh.getMessageType().getMessageCode().setValue("QBP");
//        msh.getMessageType().getTriggerEvent().setValue("Q23");
//        // MSH-10
//        msh.getMessageControlID().setValue("111");
//        // MSH-11
//        msh.getProcessingID().getProcessingID().setValue("P");
//        // MSH-12
//        msh.getVersionID().getVersionID().setValue("2.5");
//
//        QPD qpd = message.getQPD();
//        // QPD-1
//        qpd.getMessageQueryName().getIdentifier().setValue("identifier");
//        qpd.getMessageQueryName().getText().setValue("text");
//        qpd.getMessageQueryName().getNameOfCodingSystem().setValue("codingSystem");
//        // QPD-2
//        qpd.getQueryTag().setValue("QRY_Id");
//        // QPD-3
//        CX cx = new CX(message);
//        cx.getIDNumber().setValue("Pid");
//        cx.getAssigningAuthority().getNamespaceID().setValue("NamespaceId");
//        cx.getAssigningAuthority().getUniversalID().setValue("UniversalId");
//        cx.getAssigningAuthority().getUniversalIDType().setValue("UniversalIdType");
//        cx.getIdentifierTypeCode().setValue("PI");
//        qpd.getUserParametersInsuccessivefields().setData(cx);
//        CX cx2 = new CX(message);
//        cx2.getAssigningAuthority().getNamespaceID().setValue("NamespaceId2");
//        cx2.getAssigningAuthority().getUniversalID().setValue("UniversalId2");
//        cx2.getAssigningAuthority().getUniversalIDType().setValue("UniversalIdType2");
//        qpd.getUserParametersInsuccessivefields().getData().getExtraComponents().getComponent(0).setData(cx2);
//        PipeParser parser = new PipeParser();
//        String str = parser.encode( message );
//        System.out.println(str);

        String msg = "MSH|^~\\&|EHR_MISYS|MISYS|PAT_IDENTITY_X_REF_MGR_IBM1|IBM|20070209210421+0530||QBP^Q23|PIX_2|P|2.5\r" +
                "QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY_PIX_2|348400^^^&1.100.7&ISO^PI\r" +
                "RCP|I";

        Parser p = new GenericParser();
        Message  message = p.parse(msg);
        // The connection hub connects to listening servers
        ConnectionHub connectionHub = ConnectionHub.getInstance();

        // A connection object represents a socket attached to an HL7 server
        Connection connection = connectionHub
                     .attach("localhost", 3600, new PipeParser(), MinLowerLayerProtocol.class);
                 // The initiator is used to transmit unsolicited messages
             Initiator initiator = connection.getInitiator();
             Message response = initiator.sendAndReceive(message);

             PipeParser pipeParser = new PipeParser();
             String responseString = pipeParser.encode(response);
             System.out.println("Received response:\n" + responseString);

              connection.close();

//        try {
//            Message message = pipeParser.parse(msg);
//            if (message instanceof QBP_Q21) {
//                System.out.println("Type= QBP_Q21");
//            } else {
//                System.out.println("Not QBP_Q21");
//            }
//            PixManager.PixManagerHandler handler = new PixManager.PixManagerHandler(null, ConnectPVRegistry.getInstance());
//
//            handler.processMessage( message );
//
//        } catch (HL7Exception e) {
//            e.printStackTrace();
//        }  catch (ApplicationException e) {
//            e.printStackTrace();
//        }

    }
}

