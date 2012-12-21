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

package org.openhealthexchange.openpixpdq.mesatests.pdq;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.openhealthexchange.openpixpdq.ihe.PatientBroker;
import org.openhealthexchange.openpixpdq.ihe.configuration.ConfigurationLoader;
import org.openhealthexchange.openpixpdq.mesatests.MesaTestLogger;
import org.openhealthexchange.openpixpdq.mesatests.TestKit;

import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * Test for PD Supplier 
 * See http://ihewiki.wustl.edu/wiki/index.php/MESA/Patient_Demographics_Supplier
 * 
 * This Class can handle the following Mesa Test Cases:
 * # 11311: Exact Name Search
 * # 11312: Name Search - No Match 
 * # 11315: Partial Name Search 
 * # 11320: Complete ID Search - Unspecified Domain 
 * # 11325: Complete ID Search - Single Domain 
 * # 11330: Complete ID Search - Multiple Domains 
 * # 11335: Partial ID Search - Single Domain 
 * # 11340: Date of Birth Search 
 * # 11345: Age Range Search Search 
 * # 11350: Multi Key Search 1 
 * # 11355: Multi Key Search 2 
 * # 11360: Attending Doctor 
 * # 11365: Continuation Test 1 
 * 
 * @author Wenzhi Li
 * @version 1.0, Dec 23, 2008
 */
public class TestPDQ {
	public static void main(String[] args) {
		String test = "PDSupplier";
    	MesaTestLogger logger = new MesaTestLogger(System.out);
		logger.writeTestBegin(test);
		
		TestKit.configActor(logger, "pdsup");
		ConfigurationLoader loader = ConfigurationLoader.getInstance();
        ConfigurationLoader.ActorDescription actor = loader.getDescriptionById("pdsup");
        IConnectionDescription connection = actor.getConnection();

        try {
        	while (true) {
        		System.out.println("Enter \"q\" to quit>");
		        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		        String in = br.readLine();
		        if (in.equals("q")) {        
			        //gracefully exit and shut down the PDQ Server
			        PatientBroker.getInstance().unregisterPdSuppliers(null);
			        break;
		        }
        	}
        }
        catch(Exception e) {
        	e.printStackTrace();
        	logger.writeString( "Error: " + e.getMessage());
        }
        
		logger.writeTestEnd(test);

	}

}
