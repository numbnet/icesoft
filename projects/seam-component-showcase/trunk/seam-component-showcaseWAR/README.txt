This is a WAR deployment of  seam component showcase example implemented in Seam.   As such, it 
doesn't require an ejb3 container and still uses Seam annotations and contexts. 

It can be deployed in JBoss AS 4.x, WebLogic, Glassfish and Tomcat without the EJB3 container.  
Note that the j2ee AS have asynch issues with the Seam2.0.0.GA since the jsf-1.2 implementation is used.
The old distribution of seam-comp-showcase which is an ear deployment and uses the myfaces (jsf1.1 implementation)
works better for these older servers.  The j5ee AS work much better with jsf1.2 implementations of which Seam2.0.0.GA is one.

1.  download jboss-seam-2.0.0.GA and unzip all files (as the libraries will be copied from these folders)--may have to build the jars
2.  download and unzip Icefaces (post 1.6.2 final release) (or if you're eager and get this before the final release, just build the icefaces jars from the repository trunk)

3. update the following properties in build.properties file to point to 
the appropriate directories where the jboss-4.2.* server, icefaces libraries and jboss-seam
libraries exist on your machine.
	(an example follows or you can just look at the build.properties file in this folder)

 for example:-
 	jboss.home = C:/work/webserver/jboss-4.2.2.GA 
	icefacesSourceDirectory = C:/ICEfaces-1.7.0.DR3/icesfaces/  
	jboss.seam.home = C:/Seam/jboss-seam-2.0.0.GA     


Specific Server instructions:-

JBoss AS 4.2.x  (this is the default target for the ant script):
  * Install JBoss AS with the default profile
  * ant jboss
  * Deploy dist-jboss/seam-comp-showcase.war
  * Start JBoss AS 
  * Access the app at http://localhost:8080/seam-comp-showcase/

JBoss AS 4.0.5.GA:  (note everything BUT asynch is working with this right now)
  * Install JBoss AS with the default profile (with or without EJB3)
  * ant jboss405
  * Deploy dist-jboss/seam-comp-showcase.war
  * Start JBoss AS 
  * Access the app at http://localhost:8080/seam-comp-showcase/

WebLogic :  (note that the asynch stuff doesn't work for 9.2 and the dictionary for autocomplete isn't being loaded 
                       yet for either v10 or 9.2)
  * Install WebLogic 10 or  9.2
  * ant weblogic
  * Start the WebLogic "examples" server
  * Load the admin console http://localhost:7001/console/
  * Deploy dist-weblogic/seam-comp-showcase.war
  * Access the app at http://localhost:7001/seam-comp-showcase/
  
  
Plain Tomcat  (no embedded jboss container--just ootb)
  * Install Tomcat 5.5 or Tomcat 6  (Tomcat 6 everything works but with Tomcat 5.5, everything but the asynch stuff works)
  * ant tomcat55 or ant tomcat6
  * Deploy dist-tomcat55/seam-comp-showcase.war or dist-tomcat6/seam-comp-showcase.war to $TOMCAT_HOME/webapps/jboss-seam-hibernate.war
  * Start Tomcat
  * Access the app at http://localhost:8080/seam-comp-showcase/

Tomcat with embeddable JBoss (the build is the same as JBoss 4.2.2 GA WAR):
  * Install Tomcat
  * Install Embeddable JBoss
  * ant jboss
  * Deploy dist-jboss/seam-comp-showcase.war
  * Start Tomcat
  * Access the app at http://localhost:8080/seam-comp-showcase/

Glassfish (tested with v1 & v2 but asynch stuff doesn't work yet properly with v1)
  * Install Glassfish v1 UR1
  * ant glassfish
  * Start GlassFish
  * Load the admin console http://localhost:4848/
  * Deploy dist-glassfish/seam-comp-showcase.war as Web App
  * Access the app at http://localhost:8080/seam-comp-showcase/

Problems/Incompatibilities
  * Tomcat 6.0 version 13 (Error listenerStart) version 14 - OK
