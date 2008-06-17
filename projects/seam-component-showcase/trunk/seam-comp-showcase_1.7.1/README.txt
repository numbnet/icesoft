This is a WAR deployment of  seam component showcase example implemented in Seam.   As such, it 
doesn't require an ejb3 container and still uses Seam annotations and contexts. 

It can be deployed in JBoss AS 4.x, WebLogic, Glassfish and Tomcat without the EJB3 container.  
Note that the j2ee AS have asynch issues with the Seam2.0.0.GA since the jsf-1.2 implementation is used.
The old distribution of seam-comp-showcase which is an ear deployment and uses the myfaces (jsf1.1 implementation)
works better for these older servers.  The j5ee AS work much better with jsf1.2 implementations of which Seam2.0.x recommends.

1.  download jboss-seam-2.x and unzip all files (as the libraries will be copied from these folders)--may have to build the jars
    NOTE THAT FOR PORTLET EXAMPLES*** you should have jboss-seam-2.0.2.SP1 or jboss-seam-2.1.0.A1 as a minimum.
2.  download and unzip Icefaces (1.7.1final release) 

3. update the following properties in build.properties file to point to 
the appropriate directories where the jboss-4.2.* server, icefaces libraries and jboss-seam
libraries exist on your machine.
	(an example follows or you can just look at the build.properties file in this folder)

 for example:-
 	jboss.home = C:/work/webserver/jboss-4.2.2.GA 
	icefacesSourceDirectory = C:/ICEfaces-1.7.0/icefaces  
	jboss.seam.home = C:/Seam/jboss-seam-2.0.1.GA     

4. run target = copy-libs  (will copy all the required libraries from the above locations)
5. run target = copy-source (will copy over the jspx pages and beans from component-showcase)

Specific Server instructions:-

JBoss AS 4.2.x  (this is the default target for the ant script):
  * Install JBoss AS with the default profile
  * ant jboss
  * Deploy dist-jboss/seam-comp-showcase.war
  * Start JBoss AS 
  * Access the app at http://localhost:8080/seam-comp-showcase/

JBoss Portal 2.6.4  :
  * Install JBoss-portal AS with the default profile
  * copy <jboss-portal-2.6.x>/setup/portal-hsqldb-ds.xml to <jboss-portal-2.6.x>/server/default/deploy
  * ant jboss-portal
  * set jboss.home in build.properties to this AS
  * Start AS
  * Access the app at http://localhost:8080/portal/
  * each window from seam-comp-showcase will have it's own portlet window automatically (xml files specify instances already)
Known-issues:- ICE-2045 
  

WebLogic 10 :  
  * Install WebLogic 10
  * copy hsql.jar to libs for weblogic domain (just regular domain with autodeploy enabled. I created a domain called
     seam-examples and copied this jar into \bea\user-projects\domains\seam-examples\lib)
  * create Datasource for componentDB (jndi name as well)
      Database type & drive: "other"
      DatabaseName: hsqldb
      HOst Name:    127.0.0.1
      Port:         9001
      username:     sa
      Driver Class Name:  org.hsqldb.jdbcDriver
      URL: jdbc:hsqldb:.
      select domain that you are using and all other settings the same  (my domain = seam-examples)  
  * ant weblogic
  * Start the WebLogic server domain you have created and are using with this datasource
  * copy dist-weblogic/seam-comp-showcase.war to autodeploy folder of domain  
        (for example, copy to  \bea\user-projects\domains\seam-examples\autodeploy)
  * Access the app at http://localhost:7001/seam-comp-showcase/
  
  
Plain Tomcat  (no embedded jboss container--just ootb)
  * Install  Tomcat 6  
  * copy hsqldb.jar to <tomcat6 server dir>/lib 
  * ant tomcat6
  * Deploy  dist-tomcat6/seam-comp-showcase.war to $TOMCAT_HOME/webapps/jboss-seam-hibernate.war
  * Start Tomcat
  * Access the app at http://localhost:8080/seam-comp-showcase/

Tomcat with embeddable JBoss (the build is the same as JBoss 4.2.2 GA WAR):
  * Install Tomcat
  * Install Embeddable JBoss
  * ant jboss
  * Deploy dist-jboss/seam-comp-showcase.war
  * Start Tomcat
  * Access the app at http://localhost:8080/seam-comp-showcase/

Glassfish (tested with v2 & v2.1  get hibernate/caching exceptions for asynch with v2
   HOWEVER---works well with v2.1)
  * Install Glassfish v2.1
  * ant glassfish
  * Start GlassFish
  * Load the admin console http://localhost:4848/
  * Deploy dist-glassfish/seam-comp-showcase.war as Web App
  * Access the app at http://localhost:8080/seam-comp-showcase/

Glassfish/Liferay bundle (Liferay 4.4.2 on Glassfish V2 UR1)
 * Install 
 * Must have jboss-seam-2.1.0.A1 as minimum
 * ant glassfishv2-liferay4.42
 * copy \dist\glassfish-liferay\seam-comp-showcase to \Documents and Settings\<user profile>\liferay
 * make sure you have both the embedded Derby database as well as AS running
 * use URL:- http://localhost:8080/c/portal/login
 * sign in and create some pages and place some of the portlets on them.  If you want
   certain layouts, you may have to alter the css for layout on the pages.  The basic xp-portlet.css
   in ICEfaces libraries works well for most in full page of portal, but you may have to override some
   of the style classes if you use a different layout.

Jboss-4.2.2.GA/Liferay5.0.1.RC bundle 
 * Install 
 * Must have jboss-seam-2.1.0.A1 as minimum
 * ant jboss4.2.2-liferay5.0.1.RC
 * copy \dist\jboss-liferay\seam-comp-showcase to \Documents and Settings\<user profile>\liferay
 * start AS
 * use URL:- http://localhost:8080/c/portal/login
 * sign in and create some pages and place some of the portlets on them.  If you want
   certain layouts, you may have to alter the css for layout on the pages.  The rime-portlet.css
   in ICEfaces libraries works well for most in full page of portal, but you may have to override some
   of the style classes if you use a different layout.
