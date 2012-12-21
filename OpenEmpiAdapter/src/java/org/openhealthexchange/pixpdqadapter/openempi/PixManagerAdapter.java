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
package org.openhealthexchange.pixpdqadapter.openempi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;
import org.openempi.data.DateOfBirth;
import org.openempi.data.DocumentHeader;
import org.openempi.data.DomainIdentifier;
import org.openempi.data.DriversLicense;
import org.openempi.data.EthnicGroup;
import org.openempi.data.Gender;
import org.openempi.data.MaritalStatus;
import org.openempi.data.Person;
import org.openempi.data.PersonIdentifier;
import org.openempi.data.Race;
import org.openempi.data.Religion;
import org.openempi.data.SocialSecurityNumber;
import org.openempi.data.TelephoneNumber;
import org.openempi.ics.pids.IdentityService;
import org.openempi.ics.pids.IdentityServiceHome;
import org.openempi.ics.pids.PersonIdService;
import org.openempi.ics.pids.PersonIdServiceHome;
import org.openhealthexchange.openpixpdq.data.MessageHeader;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openpixpdq.data.PersonName;
import org.openhealthexchange.openpixpdq.ihe.IPixManagerAdapter;
import org.openhealthexchange.openpixpdq.ihe.PixManagerException;

import com.misyshealthcare.connect.base.demographicdata.Address;
import com.misyshealthcare.connect.base.demographicdata.PhoneNumber;
import com.misyshealthcare.connect.net.Identifier;

/**
 * This adapter implements the <code>IPixManagerAdapter</code> interface. It
 * is the bridge between the <code>PIXManager</code> of OpenPixPDq and
 * OpenEMPI. PixManager Patient data are passed via this adapter.
 * 
 * @author Wenzhi Li
 * @version 1.0, Dec 15, 2008
 */
public class PixManagerAdapter implements IPixManagerAdapter {

	private static Logger log = Logger.getLogger(PixManagerAdapter.class);
	private PersonIdServiceHome personIdServiceHome;
	private IdentityServiceHome identityServiceHome;
	private PersonIdService personIdService;
	private IdentityService identityService;

	@Override	
	public boolean isValidPatient(PatientIdentifier pid, MessageHeader header)
			throws PixManagerException {
		boolean flag = false;
		Person person = new Person();

		DocumentHeader dh = getHeader(header);
		person.addPersonIdentifier(getPid(dh, pid));

		identityService = getIdentityService();

		List<Patient> persons = null;
		try {
			persons = identityService.findPersons(person);
		
		} catch (Exception e) {
			throw new PixManagerException(e);
		}

		if (persons != null && persons.size() != 0) {
			flag = true;
		}

		return flag;
	}

	private boolean isValidIdentifier(Identifier id) {
		if (id == null || id.getNamespaceId() == null)
			return false;
		else
			return true;
	}
	
	@Override
	public List<PatientIdentifier> findPatientIds(PatientIdentifier pid,
			MessageHeader header) throws PixManagerException {

		List<PatientIdentifier> ret = new ArrayList<PatientIdentifier>();
		List persons = null;
		Person person = new Person();
		DocumentHeader dh = getHeader(header);
		person.addPersonIdentifier(getPid(dh, pid));

		identityService = getIdentityService();
		try {
			persons = identityService.findPersons(person);
			for (int i=0; i<persons.size(); i++) {
				Person p = (Person)persons.get(i);
				List<PatientIdentifier> pids = getPatientIds(p);
				for (PatientIdentifier id : pids) {
					if (!id.equals(pid)) {
						//filter out original id
						ret.add(id);
					}
				}
				
			}
		} catch (Exception e) {
			
			throw new PixManagerException(e);
		}
		return ret;
	}
	/**
	 * Extracts all the patient ids from a <code>Person</code> object,
	 * and converts them to a list of <code>PatientIdentifier</code>.
	 * 
	 * @param person the <code>Person</code> where to get the person ids
	 * @return a list of <code>PatientIdentifier</code>
	 */
	private List<PatientIdentifier> getPatientIds(Person person) {
		List<PatientIdentifier> ret = new ArrayList<PatientIdentifier>();
		if (person != null) {
			List pids = person.getPersonIdentifiers();
			for (int i=0; i<pids.size(); i++) {
				PersonIdentifier from = (PersonIdentifier)pids.get(i);
				PatientIdentifier to = toPatientIdentifier(from);
				ret.add( to );
			}
		}
		return ret;
	}
	/**
	 * Converts from <code>PersonIdentifier</code> used by OpenEMPI to
	 * a <code>PatientIdentifier</code> used by OpenPIXPDQ.
	 * 
	 * @param from the <code>PersonIdentifier</code> to be converted from
	 * @return a <code>PatientIdentifier</code> 
	 */
	private PatientIdentifier toPatientIdentifier(PersonIdentifier from) {
		if (from == null) return null;
 
		PatientIdentifier ret = new PatientIdentifier();
		ret.setId(from.getId());
		ret.setAssigningAuthority(toIdentifier(from.getAssigningAuthority()));
		ret.setAssigningFacility(toIdentifier(from.getAssigningFacility()));
		ret.setIdentifierTypeCode(from.getIdentifierTypeCode());
		return ret;
	}
	/**
	 * Converts from a <code>DomainIdentifier</code> used by OpenEMPI to an
	 * <code>Identifier</code> used by OpenPIXPDQ
	 * 
	 * @param from the <code>DomainIdentifier</code> object to be converted from
	 * @return an <code>Identifier</code>
	 */
	private Identifier toIdentifier(DomainIdentifier from) {
		Identifier ret = new Identifier(from.getNameSpaceID(), 
				from.getUniversalID(), from.getUniversalIDType());
		return ret;
	}

	@Override
	public List<PatientIdentifier> createPatient(Patient patient, MessageHeader header)
			throws PixManagerException {
		Person person = getPerson(patient, header);
		personIdService = getPersonIdService();
		try {
			personIdService.addPerson(person);
		} catch (Exception e) {
			throw new PixManagerException(e);
		}
		
		//Find the updated matching list for PIX Update Notification
		PatientIdentifier patientId = patient.getPatientIds().get(0);
		List<PatientIdentifier> matching = findPatientIds(patientId, header);
		
		//If there is any matching, send PIX Update Notification. 
		//If no matching, it must be the first time registration, then no need to notify.
		if (matching.size() == 0) 
			return matching;
		else {
			 //Add original patient id to the matching list since 
			 //findPatientIds does not including the original patient id. 
			 matching.add(patientId);
			 return matching;
		}
	}

	@Override
	public List<List<PatientIdentifier>> updatePatient(Patient patient, MessageHeader header)
			throws PixManagerException {
		List<List<PatientIdentifier>> ret = new ArrayList<List<PatientIdentifier>>();
		
		Person person = getPerson(patient, header);
		personIdService = getPersonIdService();
		
		//1. find the original matching patient
		PatientIdentifier patientId = patient.getPatientIds().get(0);
		List<PatientIdentifier> oldMatching = findPatientIds(patientId, header);
		
		//2. Update Patient
		try {
			personIdService.updatePerson(person);
		} catch (Exception e) {
			throw new PixManagerException(e);
		}
		
		//3. Find lists of patients to be updated
		List<PatientIdentifier> newMatching = findPatientIds(patientId, header);
		List<PatientIdentifier> unmatching = new ArrayList<PatientIdentifier>();
		for (PatientIdentifier oldPid : oldMatching) {
			if (!newMatching.contains(oldPid)) {
				unmatching.add(oldPid);
			}					
		}
		
		//4.PIX Update Notification to PIX consumers					
		//If there is any update on the matching
		if (!newMatching.equals(oldMatching)) {
			//Add the original patient id since findPatientIds 
			//does not include the original patient id.
			newMatching.add(patientId);
			ret.add(newMatching);
		}
		if (unmatching.size() > 0) {
			ret.add(unmatching);			
		}
		return ret;
	}

    @Override
	public List<List<PatientIdentifier>> mergePatients(Patient patientMain, Patient patientOld,
			MessageHeader header) throws PixManagerException {
		List<List<PatientIdentifier>> ret = new ArrayList<List<PatientIdentifier>>();
    	
		//1. Find the old matching of mergePatient
		PatientIdentifier patientMainId = patientMain.getPatientIds().get(0);
		List<PatientIdentifier> oldMrgMatching = findPatientIds(patientMainId, header);

		//2. Merge Patients
		Person persons[] = new Person[2];
		persons[0] = getPerson(patientMain, header);
		persons[1] = getPerson(patientOld, header);
		personIdService = getPersonIdService();
		try {
			personIdService.mergePersons(persons);
		} catch (Exception e) {
			throw new PixManagerException(e);
		}

		//3. Find lists of patients to be updated
		List<PatientIdentifier> newMatching = findPatientIds(patientMainId, header);
		List<PatientIdentifier> unmatching = new ArrayList<PatientIdentifier>();
		for (PatientIdentifier oldMrg : oldMrgMatching) {
			if (!newMatching.contains(oldMrg)) {
				unmatching.add(oldMrg);
			}
		}

		//4.PIX Update Notification to PIX consumers					
		//If there is any update on the matching
		if (!newMatching.equals(oldMrgMatching)) {
			//Add the original patient id since findPatientIds 
			//does not include the original patient id.
			newMatching.add(patientMainId);
			ret.add(newMatching);
		}
		if (unmatching.size() > 0) {
			ret.add(unmatching);			
		}
		return ret;
	}

	private Person getPerson(Patient patient, MessageHeader mh) {

		DocumentHeader dh = getHeader(mh);
		Person person = new Person();

		person.setBirthPlace(patient.getBirthPlace());
		person.setExpired(patient.isDeathIndicator());
		person.setNationality(patient.getCitizenship());
		person.setPrimaryLanguage(patient.getPrimaryLanguage());
		person.addDocumentHeader(dh);
		person.addName(getPersonName(dh, patient.getPatientName()));
		person.addSocialSecurityNumber(new SocialSecurityNumber(handleSsn(patient.getSsn())));
		if (patient.getAdministrativeSex() != null) {
			person.addGender(new Gender(patient.getAdministrativeSex().getCDAValue()));
		}
		if (patient.getBirthDateTime() != null) {
			person.addDateOfBirth(new DateOfBirth(patient.getBirthDateTime().getTime()));
		}
		person.addRace(new Race(dh, patient.getRace()));
		person.addReligion(new Religion(dh, patient.getReligion()));
		person.addMaritalStatus(new MaritalStatus(dh, patient.getMaritalStatus()));
		person.addEthnicGroup(new EthnicGroup(dh, patient.getEthnicGroup()));
		person.addDriversLicense(getDriversLicence(dh, patient));
		for (Address add : patient.getAddresses()) {
			person.addAddress(getAddress(dh, add));
		}
		for (PhoneNumber phn : patient.getPhoneNumbers()) {
			person.addTelephoneNumber(getPhoneNumber(dh, phn));
		}
		for (PatientIdentifier pid : patient.getPatientIds()) {
			person.addPersonIdentifier(getPid(dh, pid));
		}

		return person;
	}
	private String handleSsn(String ssn) {
		//OpenEMPI only takes 9 digit ssn. Need to remove "-". 
		if (ssn == null) return null;
		
		String ret = ssn; 
		if (ssn.length() > 9) {
			ret = ssn.replaceAll("-", "");
		}
		return ret;
	}
	private DocumentHeader getHeader(MessageHeader mh) {
		DocumentHeader dh = new DocumentHeader();

		dh.setMessageType(mh.getMessageCode());
		dh.setEventCode(mh.getTriggerEvent());
		if(mh.getMessgeDate()!= null){
		dh.setMessageDate(mh.getMessgeDate().getTime());
		}
		if(mh.getSendingApplication() != null){
		dh.setSendingApplication(mh.getSendingApplication().getNamespaceId());
		}
		if(mh.getSendingFacility()!= null){
		dh.setSendingFacility(mh.getSendingFacility().getNamespaceId());
		}
		if(mh.getReceivingApplication() != null){
		dh.setReceivingApplication(mh.getReceivingApplication().getNamespaceId());
		}
		if(mh.getReceivingFacility() != null){
		dh.setReceivingFacility(mh.getReceivingFacility().getNamespaceId());
		}
		return dh;
	}

	private DriversLicense getDriversLicence(DocumentHeader dh, Patient patient) {

		Date date = null;
		DriversLicense license = null;
		String licenseno = null;
		String issuingdate = null;
		if (patient.getDriversLicense() != null) {
			if (patient.getDriversLicense().getExpirationDate() != null) {
				date = patient.getDriversLicense().getExpirationDate()
						.getTime();
			}
			licenseno = patient.getDriversLicense().getLicenseNumber();
			issuingdate = patient.getDriversLicense().getIssuingState();
		}
			license = new DriversLicense(dh, licenseno, issuingdate, date);
		
		return license;
	}

	private org.openempi.data.Address getAddress(DocumentHeader dh,
			Address address) {

		org.openempi.data.Address add = new org.openempi.data.Address(dh,
							address.getAddLine1(), address.getAddLine2(),
							address.getAddCity(), address.getAddState(),
							address.getAddZip());
		if(address.getAddType() != null){
		add.setAddressType(address.getAddType().getHL7Value());
		}
		add.setCountry(address.getAddCountry());

		return add;
	}

	private TelephoneNumber getPhoneNumber(DocumentHeader dh, PhoneNumber phone) {

		TelephoneNumber tpn = new TelephoneNumber(dh, phone.getAreaCode(),phone.getNumber());

		tpn.setCountryCode(phone.getCountryCode());
		tpn.setExtension(phone.getExtension());
		tpn.setTelecomUseCode(phone.getType().getValue());

		return tpn;
	}

	private org.openempi.data.PersonName getPersonName(DocumentHeader dh,
			PersonName patientname) {

		//TODO:Need to revisit whether to use toUpperCase.
		String lastName = patientname.getLastName();
		if (lastName != null) lastName = lastName.toUpperCase();
		String firstName = patientname.getFirstName();
		if (firstName != null) firstName = firstName.toUpperCase();
		org.openempi.data.PersonName name = new org.openempi.data.PersonName(
				dh, lastName, firstName,
				patientname.getSecondName());

		name.setPrefix(patientname.getPrefix());
		name.setSuffix(patientname.getSuffix());
		name.setNameTypeCode(patientname.getNameTypeCode());
		name.setDegree(patientname.getDegree());
		name.setNameRepresentationCode(patientname.getNameRepresentationCode());

		return name;
	}

	private PersonIdentifier getPid(DocumentHeader dh, PatientIdentifier pid) {

		DomainIdentifier assigningAuthority = getDomainId(dh, pid.getAssigningAuthority());

		//OpenEMPI requires assigning facility which may not be available from a PIX Source/Consumer,
		//so use assigning authority as a workaround.
		DomainIdentifier assigningFacility = isValidIdentifier(pid.getAssigningFacility()) ? 
										     getDomainId(dh, pid.getAssigningAuthority()) : assigningAuthority;
		
		PersonIdentifier personid = new PersonIdentifier(dh, pid.getId(),
				assigningAuthority, assigningFacility, pid.getIdentifierTypeCode());
		if (pid.getEffectiveDate() != null) {
			personid.setEffectiveDate(pid.getEffectiveDate().getTime());
		}
		if (pid.getExpirationDate() != null) {
			personid.setExpirationDate(pid.getExpirationDate().getTime());
		}
		return personid;
	}

	private DomainIdentifier getDomainId(DocumentHeader dh, Identifier id) {
		if(id == null) return null;
		DomainIdentifier did = new DomainIdentifier(dh, id.getNamespaceId(), 
				id.getUniversalId(), id.getUniversalIdType());
		return did;
	}

	private DomainIdentifier getDomainId(Identifier id) {
		if(id == null) return null;
		DomainIdentifier domainid = new DomainIdentifier(id.getNamespaceId(),
				id.getUniversalId(), id.getUniversalIdType());
		return domainid;
	}

	/*
	 * Cofiguration for calling EJB object.
	 */
	// PatientIdService
	private PersonIdService getPersonIdService() {
		try {
			if (personIdService == null) {
				personIdService = getPersonIdServiceHome().create();
			}
		} catch (Exception e) {
			log.error("Failed while attempting to retrieve reference to the PersonIdService. Error: " + e);
			throw new RuntimeException("Failed while obtaining reference to the Person Id Service.");
		}
		return personIdService;
	}

	private PersonIdServiceHome getPersonIdServiceHome() throws NamingException {
		if (personIdServiceHome == null) {
			personIdServiceHome = getPersonIdHome();
		}
		return personIdServiceHome;
	}

	/**
	 * Get the home interface
	 */
	protected PersonIdServiceHome getPersonIdHome() throws NamingException {
		Context ctx = this.getInitialContext();
		Object o = ctx.lookup("ejb/PersonIdService");
		PersonIdServiceHome intf = (PersonIdServiceHome) PortableRemoteObject
				.narrow(o, PersonIdServiceHome.class);
		return intf;
	}

	/**
	 * Get the initial naming context
	 */
	protected Context getInitialContext() throws NamingException {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		props.put(Context.URL_PKG_PREFIXES,
				"org.jboss.naming:org.jnp.interfaces");
		props.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		Context ctx = new InitialContext(props);
		return ctx;
	}

	// Identity Service
	private IdentityService getIdentityService() {
		try {
			if (identityService == null) {
				identityService = getIdentityServiceHome().create();
			}
		} catch (Exception e) {
			log.error("Failed while attempting to retrieve reference to the IdentityService. Error: "+ e);
			throw new RuntimeException("Failed while obtaining reference to the Identity Service.");
		}
		return identityService;
	}

	private IdentityServiceHome getIdentityServiceHome() throws NamingException {
		if (identityServiceHome == null) {
			identityServiceHome = getIdentityHome();
		}
		return identityServiceHome;
	}

	/**
	 * Get the home interface
	 */
	protected IdentityServiceHome getIdentityHome() throws NamingException {
		Context ctx = this.getInitialContext();
		Object o = ctx.lookup("ejb/IdentityService");
		IdentityServiceHome intf = (IdentityServiceHome) PortableRemoteObject
				.narrow(o, IdentityServiceHome.class);
		return intf;
	}

}
