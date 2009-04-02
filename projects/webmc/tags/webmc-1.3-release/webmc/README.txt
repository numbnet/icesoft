--------------------------------------------------------------------------------
How to Build WebMC
--------------------------------------------------------------------------------

1. In build.properties ensure the common.build.dir property is pointing to the right 
   location.  This property gives you access to build-common.xml and the required 
   .jar files in the ICEfaces lib folder.  If you have downloaded all the content 
   from http://anonsvn.icefaces.org/repo/ this will point to the correct location 
   by default.

2. Version 1.6.5 or later of Ant is required.  From the installation directory, 
   invoke the following:
   
   >ant

   This will build "webmc.war" and place it in the "dist/" directory.  By default, 
   "ant" will build for tomcat6.0.  The following command will give you a list of 
   the available targets:
   
   >ant -p
   
   Main targets:

   common.clean  clean the directory
   glassfishv2   build war file for SUN Glassfish V2
   jboss4.0      JBoss 4.0.5
   jboss4.2      build war file for JBoss 4.2
   jetty6.1      build war file for Jetty 6.1
   oc4j          OC4J
   pe9           build war file for SUN Application Server PE9
   tomcat5.x     Tomcat 5.x
   tomcat6.0     build war file for Tomcat 6.0
   weblogic8.1   Weblogic 8.1
   weblogic9.2   Weblogic 9.2
   websphere6.0  IBM Websphere 6.0.2
   websphere6.1  IBM Websphere 6.1
   Default target: tomcat6.0   

3. Copy the WAR file in the "dist/" directory to the deployment directory of the 
   Servlet container of your choice.  The application should deploy as expected.  