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
package org.openhealthexchange.openpixpdq.ihe;

/**
 * This exception is generated when there is a problem
 * with PdSupplier operations
 * 
 * @author Wenzhi Li
 * @version 1.0 - Oct 21, 2008
 */
public class PdSupplierException extends Exception {

   private static final long serialVersionUID = -5589454490756195834L;
	/**
	 * Create a new PdSupplierException.
	 * 
	 * @param string A description of the problem
	 */
	public PdSupplierException(String string) {
		super(string);
	}

    public PdSupplierException(String msg, Throwable cause){
        super(msg, cause);
    }

    public PdSupplierException(Throwable cause) {
        super(cause);
    }
}
