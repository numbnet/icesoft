This Hibernate sample app comes with an Ant build script configured to use ICEfaces distribution as a library source. To configure the location of this distribution, open build.xml and edit the value attribute of the property tag on line 4. 

Not being available in the ICEfaces distribution Hibernate 3.5+, its dependencies, and your database driver of choice (HSQL by default) should be placed in the /lib folder manually for the script to build the app correctly. 

These libraries are available at http://www.jboss.com and http://www.hsqldb.com


The following is a complete list of dependencies for this application,
the majority are available in an ICEfaces distribution:

slf4j-jdk14-1.6.1.jar
antlr-2.7.6.jar
cglib-2.2.jar
dom4j-1.6.1.jar
hibernate3.jar
hibernate-jpa-2.0-api-1.0.0.Final.jar
hsqldb.jar
javassist-3.12.1.GA.jar
jta-1.1.jar
slf4j-api-1.6.1.jar
commons-collections-3.1.jar
commons-logging.jar


This set of dependencies was tested on Glassfish 3 and Tomcat 7.