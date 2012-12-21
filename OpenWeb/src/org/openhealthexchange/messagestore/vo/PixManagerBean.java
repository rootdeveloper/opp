package org.openhealthexchange.messagestore.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;

import com.misyshealthcare.connect.base.demographicdata.Address;

public class PixManagerBean extends ActionForm{

	private String lName;
	private String fName;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String email;
	private String dob;
	private String gender;
	private String localid;
	private String systemid;
	private String action;
	private Map<String,String> assigninglist =new HashMap<String,String>();
	private String fullAddress; 
	private List<PatientIdentifier> pidlist = new ArrayList<PatientIdentifier>();		
	
	
	private String nameString;
	
	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
	}
	/**
	 * @param name the lName to set
	 */
	public void setlName(String name) {
		lName = name;
	}
	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}
	/**
	 * @param name the fName to set
	 */
	public void setfName(String name) {
		fName = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the localid
	 */
	public String getLocalid() {
		return localid;
	}
	/**
	 * @param localid the localid to set
	 */
	public void setLocalid(String localid) {
		this.localid = localid;
	}
	/**
	 * @return the systemid
	 */
	public String getSystemid() {
		return systemid;
	}
	/**
	 * @param systemid the systemid to set
	 */
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the nameString
	 */
	public String getNameString() {
		return nameString;
	}
	/**
	 * @param nameString the nameString to set
	 */
	public void setNameString(String nameString) {
		this.nameString = nameString;
	}
	/**
	 * @return the fullAddress
	 */
	public String getFullAddress() {
		return fullAddress;
	}
	/**
	 * @param fullAddress the fullAddress to set
	 */
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	/**
	 * @return the pidlist
	 */
	public List<PatientIdentifier> getPidlist() {
		return pidlist;
	}
	/**
	 * @param pidlist the pidlist to set
	 */
	public void setPidlist(List<PatientIdentifier> pidlist) {
		this.pidlist = pidlist;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) { 
		this.lName = null;
		this.fName = null;
		this.address = null;
		this.city = null;
		this.state = null;
		this.zip = null;
		this.country = null;
		this.email = null;
		this.dob = null;
		this.gender = null;
		this.localid = null;
		this.systemid = null;
		this.action = null;
		this.nameString = null;
		this.fullAddress = null;
		this.pidlist = null;
		this.assigninglist = null;
	  }
	/**
	 * @return the assigninglist
	 */
	public Map<String, String> getAssigninglist() {
		return assigninglist;
	}
	/**
	 * @param assigninglist the assigninglist to set
	 */
	public void setAssigninglist(Map<String, String> assigninglist) {
		this.assigninglist = assigninglist;
	}
}
