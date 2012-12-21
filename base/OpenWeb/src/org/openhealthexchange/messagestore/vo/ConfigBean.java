package org.openhealthexchange.messagestore.vo;

/**
 * Copyright 2005 Misys Healthcare Systems. All rights are reserved.
 * This software is protected by international copyright laws and
 * treaties, and may be protected by other law. Violation of copyright
 * laws may result in civil liability and criminal penalties.
 */

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 *
 * @author Anil kumar
 * @date Jan 23, 2009
 */

public class ConfigBean extends ActionForm {

	private String action;
	private String configFile;
	private String logfile;
	private String[] actors;

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return Returns the actors.
	 */
	public String[] getActors() {
		return actors;
	}

	/**
	 * @param actors The actors to set.
	 */
	public void setActors(String[] actors) {
		this.actors = actors;
	}

	/**
	 * @return Returns the configFile.
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * @param configFile The configFile to set.
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/**
	 * @return Returns the logfile.
	 */
	public String getLogfile() {
		return logfile;
	}

	/**
	 * @param logfile The logfile to set.
	 */
	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}
	/**
	 * Reset all properties to their default values.
	 *
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.action = null;
		this.configFile = null;
		this.logfile = null;
		this.actors = null;
	}
}
