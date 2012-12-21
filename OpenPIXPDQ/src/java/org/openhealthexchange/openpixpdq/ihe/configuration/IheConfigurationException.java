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
package org.openhealthexchange.openpixpdq.ihe.configuration;

/**
 * The exception is thrown when a connection is not configured properly
 * and information cannot be translated to IHE form.
 * 
 * @author Jim Firby
 * @version 1.0 - Nov 14, 2005
 */
public class IheConfigurationException extends Exception {

	private static final long serialVersionUID = 4506446482932000759L;

	public IheConfigurationException(String message) {
		super(message);
	}
	
	public IheConfigurationException(String message, Throwable e) {
		super(message, e);
	}
}
