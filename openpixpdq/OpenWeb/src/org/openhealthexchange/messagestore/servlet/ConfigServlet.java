package org.openhealthexchange.messagestore.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openhealthexchange.openpixpdq.ihe.PatientBroker;

;/**
 *The starting servlet.Main functionality is to destroy all Actors 
 *
 */
 public class ConfigServlet extends HttpServlet  {
   static final long serialVersionUID = 1L;
   
	public ConfigServlet() {
	} 
	
	/* 
	 * Destroys all Actors 
	 */
	public void destroy() {
		PatientBroker.getInstance().unregisterPixManagers(null);
		PatientBroker.getInstance().unregisterPdSuppliers(null);

	}   	 	  	  	  
	public void init() throws ServletException {
	}   
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		//do nothing
	}
}