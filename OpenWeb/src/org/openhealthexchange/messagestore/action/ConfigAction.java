package org.openhealthexchange.messagestore.action;

/*
 * Copyright 2005 Misys Healthcare Systems. All rights reserved.
 * This software is protected by international copyright laws and
 * treaties, and may be protected by other law. Violation of copyright
 * laws may result in civil liability and criminal penalties.
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.openhealthexchange.messagestore.vo.ConfigBean;
import org.openhealthexchange.openpixpdq.ihe.configuration.ConfigurationLoader;
import org.openhealthexchange.openpixpdq.ihe.configuration.IheActorDescription;

/**
 * A Struts Tiles action which implements the header of each page,
 * checking for the 
 */
public class ConfigAction extends Action {

	/**
	 * TODO document
	 *
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 *
	 * @exception Exception if business logic throws an exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			ConfigBean cb = (ConfigBean) form;
			if (cb == null || cb.getAction() == null || cb.getAction().equals("")) {
				if (cb == null) {
					cb = new ConfigBean();
				}
				List<IheActorDescription> l = (Vector) ConfigurationLoader.getInstance().getActorDescriptions();
				
				Collections.sort(l, new compareTypes());
				request.setAttribute("ActorList", l);
				List aList = new LinkedList();
				String[] sList = new String[l.size()];
				int x = 0;
				for (IheActorDescription ida : l) {
					if (ida.isInstalled()) {
						sList[x++] = ida.getId();
					}
				}
				cb.setActors(sList);
				request.setAttribute("ConfiBean", cb);
				return mapping.findForward("success");
			}
			if (cb.getAction().equalsIgnoreCase("load")) {
				//First reset the config settings before loading
				ConfigurationLoader.getInstance()
						.resetConfiguration(null, null);
				ConfigurationLoader.getInstance().loadConfiguration(cb.getConfigFile(), false);
				List<IheActorDescription> l = (Vector) ConfigurationLoader.getInstance().getActorDescriptions();
				Collections.sort(l, new compareTypes());
				request.setAttribute("ActorList", l);
				List aList = new LinkedList();
				String[] sList = new String[l.size()];
				int x = 0;
				for (IheActorDescription ida : l) {
					if (ida.isInstalled()) {
						sList[x++] = ida.getId();
						//aList.add(ida.getId());
					}
				}
				cb.setActors(sList);
				request.setAttribute("ConfiBean", cb);
				return mapping.findForward("success");
			} else if (cb.getAction().equalsIgnoreCase("save")) {
				List<Object> lString = new LinkedList<Object>();
				StringBuffer selectedActors = new StringBuffer();
				for (String s : cb.getActors()) {
					if (selectedActors.length() > 0)
						selectedActors.append(",");
					selectedActors.append(s);
					lString.add(s);
				}
				String sLogFile = cb.getLogfile();
				if (sLogFile != null && !sLogFile.equals("")) {
					ConfigurationLoader.getInstance().resetConfiguration(lString, sLogFile);
				} else {
					ConfigurationLoader.getInstance().resetConfiguration(lString, null);
				}
				List<IheActorDescription> l = (Vector) ConfigurationLoader.getInstance().getActorDescriptions();
				Collections.sort(l, new compareTypes());
				request.setAttribute("ActorList", l);
				request.setAttribute("ConfiBean", cb);
				return mapping.findForward("success");
			} else if (cb.getAction().equalsIgnoreCase("stop all")) {
				ConfigurationLoader.getInstance()
						.resetConfiguration(null, null);
				List<IheActorDescription> l = (Vector) ConfigurationLoader.getInstance().getActorDescriptions();
				Collections.sort(l, new compareTypes());
				request.setAttribute("ActorList", l);
				cb.setActors(null);
				request.setAttribute("ConfiBean", cb);
				return mapping.findForward("success");
			}

		} catch (Exception e) {
			return null;
		}
		return (mapping.findForward("success"));
	}

	private class compareTypes implements Comparator {

		public int compare(Object first, Object second) {
			try {
				IheActorDescription f = (IheActorDescription) first;
				IheActorDescription s = (IheActorDescription) second;
				return f.getType().compareToIgnoreCase(s.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

}
