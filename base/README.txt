
                             O p e n P I X P D Q
 

  What is it? 
  -----------
  
  OpenPIXPDQ a server side implementation of the Patient Identifier Cross-
  Reference (PIX) and Patient Demographic Query (PDQ) profiles specified by 
  IHE (IHE.net). The actors implemented are PIX Manager and PDQ Supplier.  


  Contents
  --------

    Included in this release are the following:

    README.txt 	    This file
    LICENSE.txt	    Software license
    NOTICE.txt	    Copyright and contribution Notice
    openempi/openempi.ear
                    OpenEMPI ear file, can be run in a JEE container 
    openempi/conf/ICS.properties
                    The main OpenEMPI configuration file     
    openempi/conf/IcsSql.xml 
    		    The SQL filed used by OpenEMPI
    openempi/conf/IcsLog4j.xml
                    The Log4j properties for OpenEMPI
    openempi/conf/ICS_schema_create-mysql.sql 
    		    The OpenEMPI database creation script for MySQL 
    openempi/conf/ICS_schema_create-postgres.sql
    		    The OpenEMPI database creation script for Postgresql 
    openpixpdq-1.0/openpixpdq.war
		    OpenPIXPDQ war file. PIX/PDQ server can be run 
		    in a web container
    openpixpdq-1.0/openpixpdq-1.0.jar
	            Core OpenPIXPDQ jar file. PIX/PDQ server can be run 
     		    in a stand alone mode outside web container
    openpixpdq-1.0/openempi-adapter-1.0.jar
	            The OpenPIXPDQ adapter for OpenEMPI.
    openpixpdq-1.0/conf/log4j.xml
	   	    The log4j properties for OpenPIXPDQ
    openpixpdq-1.0/conf/mesatests/actors
	            This folder contains PIX/PDQ actors configuration 
		    XML files for mesa tests and Connectathon. It can 
		    also be used as an example for your configuration. 
    openpixpdq-1.0/conf/mesatests/actors/certs
	  	    This folder contains the TLS key and truststore files
    openpixpdq-1.0/conf/tests/actors
	            This folder contains actor configuration for junit tests.
    openpixpdq-1.0/conf/tests/actors/certs
		    Contains TLS key and truststore files, but not currently used
    openpixpdq-1.0/data/ConnectathonTestPatients.txt
	            The data file for bulk loading patients for Connectathon 
		    2009. An integrated test (PixLoadConnectathonPatientTest)
		    will load these patients into your database. 
    openpixpdq-1.0/lib
		    All the libs needed for running stand alone PIX/PDQ server
    openpixpdq-1.0/licenses	
	            All the licenses file for the third part libraries distributed


  Requirements
  ------------

     JDK Version	
	 OpenPIXPDQ supports JDK 1.6 or higher.  Note that we have
  	 currently tested this implementation only with JDK 1.6.

     JBoss
         OpenEMPI is a JEE application, so it requires a JEE container. Our
         tested JEE Application Server is JBoss 4.0.5GA.
    
     Database
         Either Postgresql or MySQL is needed. Our tested database is 
         Postgresql 8.3.
                

  Installation and Configuration
  ------------------------------

  Installation and configuration guide is available on the OpenPIXPDQ Project 
  web site on Source Forge <http://openpixpdq.sourceforge.net/>.

  
  Documentation
  -------------

  Documentation is available on the OpenPIXPDQ Project web site
  on Source Forge <http://openpixpdq.sourceforge.net/>.

   
  The Latest Version
  ------------------

  Details of the latest version can be found on the OpenPIXPDQ Project web 
  site on Source Forge <http://openpixpdq.sourceforge.net/>.


  Problems
  ---------

  Our web page at http://openpixpdq.sourceforge.net/ has pointers where you can post 
  questions, report bugs or request features. You'll also find information on how to
  subscribe to our dev list and forum.


  Licensing
  ---------

  This software is licensed under the terms you may find in the file 
  named "LICENSE.txt" in this directory.
  

  Thanks for using OpenPIXPDQ.

                                    Misys Open Source Solutions - Healthcare                                              
                                         <http://www.misys.com/>
