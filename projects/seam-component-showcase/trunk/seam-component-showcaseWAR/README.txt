This is the seam component showcase example implemented in Seam.  It can be deployed in JBoss AS 4.x, WebLogic, Glassfish and Tomcat without the EJB3 container

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

Server instructions:-

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

WebLogic 9.2:
  * Install WebLogic 9.2
  * ant weblogic92
  * Start the WebLogic "examples" server
  * Load the admin console http://localhost:7001/console/
  * Deploy dist-weblogic92/seam-comp-showcase.war
  * Access the app at http://localhost:7001/seam-comp-showcase/

WebSphere 6.1: (Special thanks to Denis Forveille)

  * Install and run WebSphere 6.1
  * In Application Servers -> <server> -> Web Container Settings -> Web Container -> Custom Properties, set "com.ibm.ws.webcontainer.invokefilterscompatibility" to "true"
  * ant websphere61
  * Install dist-websphere61/seam-comp-showcase.war and specify a context_root
  * From the "Enterprise Applications" list select: "seam-comp-showcase_war" --> "Manager Modules" --> "seam-comp-showcase.war" --> "Classes loaded with application class loader first", and then Apply
  * Start the application
  * Access it at http://localhost:9080/context_root/index.html

Plain Tomcat (special thanks to Ralph Schaer)
  * Install Tomcat 5.5 or Tomcat 6  (Tomcat 6 everything works but with Tomcat 5.5, everything but the asynch stuff works)
  * Copy the lib/hsqldb.jar into $TOMCAT_HOME/common/lib (Tomcat 5.5) or $TOMCAT_HOME/lib (Tomcat 6)
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

Glassfish (tested with v1 & v2)
  * Install Glassfish v1 UR1
  * ant glassfish
  * Start GlassFish
  * Load the admin console http://localhost:4848/
  * Deploy dist-glassfish/seam-comp-showcase.war as Web App
  * Access the app at http://localhost:8080/seam-comp-showcase/

