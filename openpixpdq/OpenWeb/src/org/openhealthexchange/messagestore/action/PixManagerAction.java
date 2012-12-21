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
package org.openhealthexchange.messagestore.action;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.openhealthexchange.messagestore.dao.MessageStoreDAOImpl;
import org.openhealthexchange.messagestore.vo.PixManagerBean;
import org.openhealthexchange.openpixpdq.data.MessageHeader;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openpixpdq.data.PersonName;
import org.openhealthexchange.openpixpdq.ihe.IPdSupplierAdapter;
import org.openhealthexchange.openpixpdq.ihe.IPixManagerAdapter;
import org.openhealthexchange.openpixpdq.ihe.PdSupplierException;
import org.openhealthexchange.openpixpdq.ihe.PixManagerException;
import org.openhealthexchange.openpixpdq.ihe.configuration.ConfigurationLoader;
import org.openhealthexchange.openpixpdq.ihe.configuration.IheActorDescription;
import org.openhealthexchange.openpixpdq.ihe.pdq.PdqQuery;
import org.openhealthexchange.openpixpdq.ihe.pdq.PdqResult;

import com.misyshealthcare.connect.base.SharedEnums;
import com.misyshealthcare.connect.base.demographicdata.Address;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.net.Identifier;
/**
 * Action class for PixRegestration, PixQuery and PDQQuery.
 * @author Anil kumar
 * @version 1.0, Feb 02, 2009
 */
public class PixManagerAction extends Action {
	private static Logger log = Logger.getLogger(MessageStoreDAOImpl.class);
	PatientIdentifier pid = null;
	List<Identifier> ids = null;
	ConfigurationLoader loader = null;
	IConnectionDescription connection = null;
	public ActionForward execute(
            ActionMapping mapping,
             ActionForm form,
             HttpServletRequest request,
             HttpServletResponse response) throws Exception{
		
		Map<String,String> assignList = new HashMap<String,String>();
		IPixManagerAdapter pixAdapter = null;
		IPdSupplierAdapter pdsupp = null;
		List<PixManagerBean> beanList = null;
		PdqQuery query = null;
		Patient patient = null; 
		MessageHeader header = null;
		 String ip = null; 
        
		 try {
				loader = ConfigurationLoader.getInstance();
			 	Collection<IheActorDescription> actors = loader.getActorDescriptions();
				ConfigurationLoader.ActorDescription actor = null;
			 	for (IheActorDescription actorDescription : actors) {
			 		if (actorDescription.getActorType().equalsIgnoreCase("PixManager")) {
			 			actor = (ConfigurationLoader.ActorDescription)actorDescription;
			 			break;
			 		}
			 	}
			 	if (actor != null) {
				connection = actor.getConnection();
		        ids = connection.getAllIdentifiersByType("domain");
		        if (ids != null) {
					for (Identifier id : ids) {
						assignList.put(id.getNamespaceId(),id.getNamespaceId());
					}
				}
		        InetAddress addr = InetAddress.getLocalHost();
		        ip = addr.getHostAddress();
		        request.setAttribute("serverport",ip);
				}
			 	else
			 	{
			 		ip = "No PIX Manager actor is configured";
					 request.setAttribute("serverport",ip);
				}
			PixManagerBean pm = (PixManagerBean) form;
			pm.setAssigninglist(assignList);
			if ((pm == null || pm.getAction() == null || pm.getAction().equals(""))) {
				beanList = new ArrayList<PixManagerBean>();
				request.setAttribute("PixManagerBean", pm);
				request.setAttribute("beanList", beanList);
				return mapping.findForward("success");
			}
			if (pm.getAction().equalsIgnoreCase("Save")) {
				try{
				patient = toPatient(pm);
				header = getHeader(pm);
				header.setMessageCode("ADT^A04");
				String pixManagerAdapterClass = connection.getProperty("pixManagerAdapter");
				Class c = Class.forName(pixManagerAdapterClass);
				pixAdapter = (IPixManagerAdapter) c.newInstance();
				pixAdapter.createPatient(patient, header);
				return mapping.findForward("success");
				}catch(PixManagerException e){
					log.error(e);
					request.setAttribute("PixManagerBean",pm);
				}
			}
			else if (pm.getAction().equalsIgnoreCase("Submit Query")) {
				try{
					for (IheActorDescription actorDescription : actors) {
				 		if (actorDescription.getActorType().equalsIgnoreCase("PdSupplier")) {
				 			actor = (ConfigurationLoader.ActorDescription)actorDescription;
				 			break;
				 		}
				 	}
				connection = actor.getConnection();  
				String pdsuppAdapterClass = connection.getProperty("PdSupplierAdapter");
				Class c = Class.forName(pdsuppAdapterClass);
				pdsupp = (IPdSupplierAdapter) c.newInstance();
				query = toQuery(pm);
				header = getHeader(pm);
				PdqResult result = pdsupp.findPatients(query, header);
				if(result != null){					
					beanList = toBean(result);
				}else{
					beanList = new ArrayList<PixManagerBean>();
				}
				request.setAttribute("beanList", beanList);
				return mapping.findForward("success");
			}catch (PdSupplierException e) {
				log.error(e);
				request.setAttribute("PixManagerBean",pm);
				request.setAttribute("beanList", new ArrayList<PixManagerBean>());
			}

		} 
	}catch (Exception e) {
		log.error(e);
		request.setAttribute("beanList", new ArrayList<PixManagerBean>());
		request.setAttribute("PixManagerBean", new PixManagerBean());
		return mapping.findForward("success");
	}
return mapping.findForward("success");
}
	/**
	 * Gives Complete Name of the patient as String.
	 * @param lname 
	 * 				the <code>LastName</code> 
	 * @param fname 
	 * 				the <code>FirstName</code>
	 * @return String
	 */
	
	private String getName(String lname, String fname){
		if (lname == null && fname == null)
			return null;
		String ret = null;
		if(lname != null)
			ret= fname+",";
		if(fname != null)
			ret = ret+ lname;
	return ret;	
	}
	
	/**
	 * Gives Complete Address for the patient as String.
	 * @param addresslist
	 * 				List of <code>Address</code> 
	 * @return String.
	 */
	private String getAddressString(List<Address> addresslist){
		Address address = addresslist.get(0);
		StringBuffer addString = new StringBuffer();
		if(address != null){
		if(address.getAddLine1() != null && address.getAddLine1().equalsIgnoreCase("") != true)
			addString.append(address.getAddLine1()).append(",");
		if(address.getAddLine2() != null && address.getAddLine2().equalsIgnoreCase("")  != true)
			addString.append(address.getAddLine2()).append(",");
		if(address.getAddCity() != null && address.getAddCity().equalsIgnoreCase("") != true)
			addString.append(address.getAddCity()).append(",");
		if(address.getAddState()!= null && address.getAddState().equalsIgnoreCase("") != true)
			addString.append(address.getAddState()).append(",");
		if(address.getAddCountry()!= null && address.getAddCountry().equalsIgnoreCase("") != true)
			addString.append(address.getAddCountry()).append(",");
		if(address.getAddZip()!=null && address.getAddZip().equalsIgnoreCase("") != true)
			addString.append(address.getAddZip());
		}
	return addString.toString();	
	}
	/**
	 * Converts <code>PixManagerBean</code> to the <code>PdqQuery</code>
	 * @param bean 
	 * 				the <code>PixManagerBean</code>
	 * @return <code>PdqQuery</code>
	 */
	private PdqQuery toQuery(PixManagerBean bean){
		PdqQuery query = new PdqQuery();
		query.setAddress(getAddress(bean));
		query.setBirthDate(_convertStringToCalendar(bean.getDob()));
		query.setPatientIdentifier(getPatientID(bean));
		query.setPersonName(getPersonName(bean.getlName(),bean.getfName()));
		if(bean.getGender()!= null)
			if(bean.getGender().equalsIgnoreCase("male")||bean.getGender().equalsIgnoreCase("M")){
				query.setSex(SharedEnums.SexType.MALE);
			}else if(bean.getGender().equalsIgnoreCase("female")||bean.getGender().equalsIgnoreCase("F")){
				query.setSex(SharedEnums.SexType.FEMALE);
			}
		
	return query;
	}
	/**
	 * Converts <code>PixManagerBean</code> to the <code>Patient</code>
	 * @param bean 
	 * 				the <code>PixManagerBean</code>
	 * @return <code>Patient</code>
	 */
	private Patient toPatient(PixManagerBean bean){
		Patient patient = new Patient();
		patient.setPatientName(getPersonName(bean.getlName(),bean.getfName()));
		if(bean.getGender()!=null)
			if(bean.getGender().equalsIgnoreCase("male")||bean.getGender().equalsIgnoreCase("M")){
				patient.setAdministrativeSex(SharedEnums.SexType.MALE);
			}else if(bean.getGender().equalsIgnoreCase("female")||bean.getGender().equalsIgnoreCase("F")){
				patient.setAdministrativeSex(SharedEnums.SexType.FEMALE);
			}
		patient.setBirthDateTime(_convertStringToCalendar(bean.getDob()));
		List<Address> addList = new ArrayList<Address>();
		addList.add(getAddress(bean));
		patient.setAddresses(addList);
		List<PatientIdentifier> pids = new ArrayList<PatientIdentifier>();
		pids.add(getPatientID(bean));
		patient.setPatientIds(pids);
		// TODO: Create new filed Email Address in Patient 
		return patient;
	}
	/**
	 * Converts <code>PdqResult</code> to the list of <code>PixManagerBean</code>
	 * @param pdqresult 
	 * 				the <code>PdqResult</code>
	 * @return the list of <code>PixManagerBean</code>
	 */
	private List<PixManagerBean> toBean(PdqResult result){
		List<PixManagerBean> beanList = new ArrayList<PixManagerBean>();
		for(List<Patient> patientlist: result.getPatients()){
			for(Patient patient: patientlist){
				PixManagerBean bean = new PixManagerBean();
				if(patient.getPatientName() != null)
				bean.setNameString(getName(patient.getPatientName().getLastName(), patient.getPatientName().getFirstName()));
				if(patient.getAdministrativeSex() != null)
				bean.setGender(patient.getAdministrativeSex().equals(SharedEnums.SexType.MALE)? "Male" : "Female");
				bean.setEmail("");// TODO:Include field Email Address in Patient
				if(patient.getAddresses() != null)
				bean.setFullAddress(getAddressString(patient.getAddresses()));
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				if(patient.getBirthDateTime() != null)
				bean.setDob(sdf.format(patient.getBirthDateTime().getTime()));
				bean.setPidlist(patient.getPatientIds());
				beanList.add(bean);
				}
		}
		return beanList;
	}
	/**
	 * Gets <code>PersonName</code>
	 * @param lastName 
	 * 				the <code>LastName</code> of the patient 
	 * @param firstName the <code>FirstName</code> of the patinet
	 * @return <code>PersonName</code>
	 */
	private PersonName getPersonName(String lastName, String firstName){
		if ((lastName == null || lastName.equalsIgnoreCase("")) &&
				(firstName == null || firstName.equalsIgnoreCase("")) )
			return null;
		
		PersonName	pname= new PersonName();
		pname.setLastName(lastName);
		pname.setFirstName(firstName);
		
		return pname;
	}
	/**
	 * Gets <code>Address</code> form <code>PixManagerBean</code>
	 * @param bean 
	 * 			the <code>PixManagerBean</code> where to get <code>Address</code>
	 * @return <code> Address </code>
	 */
	private Address getAddress(PixManagerBean bean){
		if ((bean.getAddress() == null || bean.getAddress().equalsIgnoreCase("")) && 
			(bean.getCity() ==null || bean.getCity().equalsIgnoreCase("")) &&
			(bean.getState() == null || bean.getState().equalsIgnoreCase("")) && 
			(bean.getCountry() == null || bean.getCountry().equalsIgnoreCase("")) &&
			(bean.getZip() == null || bean.getZip().equalsIgnoreCase("")))
			return null;
		
		Address address = new Address();
		address.setAddLine1(bean.getAddress());
		address.setAddCity(bean.getCity());
		address.setAddState(bean.getState());
		address.setAddCountry(bean.getCountry());
		address.setAddZip(bean.getZip());
		return address;
		}
	/**
	 * Gets <code>PatientIdentifier</code> form <code>PixManagerBean</code>
	 * @param bean 
	 * 			the <code>PixManagerBean</code> where to get <code>PatientIdentifier</code>
	 * @return <code>PatientIdentifier</code>
	 */
	private PatientIdentifier getPatientID(PixManagerBean bean){
		if(bean.getSystemid()== null && bean.getLocalid()== null )
			return null;
		PatientIdentifier pid = new PatientIdentifier();
		for(Identifier id: ids){
			if(id.getNamespaceId().equalsIgnoreCase(bean.getSystemid()))
			pid.setAssigningAuthority(id);
		}
		if(bean.getLocalid() != null && !bean.getLocalid().equalsIgnoreCase(""))
		pid.setId(bean.getLocalid());
		
		return pid;
	}
	/**
	 * Gets <code>MessageHeader</code> from <code>PixManagerBean</code> and <code>IConnectionDescription</code>
	 * @param bean 
	 * 			the <code>PixManagerBean</code>
	 * @return <code>MessageHeader</code>
	 */
	private MessageHeader getHeader(PixManagerBean bean){
		MessageHeader mh = new MessageHeader();
		mh.setReceivingFacility(connection.getIdentifier("ReceivingFacility"));
		mh.setReceivingApplication(connection.getIdentifier("ReceivingApplication"));
		mh.setSendingApplication(connection.getIdentifier(bean.getSystemid()));
		mh.setMessgeDate(Calendar.getInstance());
		return mh;
	}
	/**
	 * Converts String to <code>Calendar</code>.
	 * @param String 
	 * @return <code>Calendar</code>
	 */
	  private Calendar _convertStringToCalendar(String date) {
		  SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
	        if(date == null)
	            return null;

	        try {
	            Date date1 = null;		            
	                date1 = dateFormat.parse(date);		            
	            Calendar cal=Calendar.getInstance();
	                cal.setTime(date1);

	            return cal;
	        } catch(ParseException pex) {            
	            return null;
	        }
	    }
}
