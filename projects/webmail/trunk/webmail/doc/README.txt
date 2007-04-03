                     WEBMAIL GETTING STARTED GUIDE
                            Rev. 1.1
                         (www.icefaces.org)
--------------------------------------------------------------------------------

TOPICS

1. What is Webmail?
2. Required JAR Files
3. Setting up MySQL
4. Creating the Webmail Database
5. Alternative Hibernate Configurations
6. ICEfaces Location
7. Building Webmail

--------------------------------------------------------------------------------
1. What is Webmail?

Webmail is an ICEfaces demo application that provides a rich web interface for
viewing and sending email messages for either IMAP, POP3 and SMTP mail accounts.
The application also has extended date book features such as contacts, tasks,
and a calendar.

Webmail was designed to be customizable and provides an excellent resource for
familiarizing oneself with more advanced ICEfaces applications. It also
provides a basis for best practices when designing ICEfaces applications. Read
on to learn how to setup Webmail.

--------------------------------------------------------------------------------
2. Required JAR Files

Webmail uses several Java APIs which must be properly configured before running
the web application. Each required JAR file should be placed in the ./lib/
directory of your Webmail checkout. The following is a list of needed JAR files
and brief summary of their use:

 mail.jar (version 1.3)
   JavaMail API implementation
   http://java.sun.com/products/javamail/

 activation.jar (version 1.0)
   JavaBeans Activation Framework
   http://java.sun.com/products/javabeans/glasgow/jaf.html
 
 log4j.jar (version 1.2.X)
   Java-based logging utility
   http://logging.apache.org/log4j/
   
 hibernate (version 3.1.X)
   Hibernate framework for database persistance
   Minimum required set (see README.txt file in the /lib folder of Hibernate
   distribution for detail):
     - antlr.jar (ANother Tool for Language Recognition)
     - ehcache-1.1.jar (EHCache cache required if no other cache provider is set)
     - cglib.jar (CGLIB bytecode generator)
     - asm.jar (ASM bytecode library)
     - asm-attrs.jar (ASM bytecode library)
     - hibernate3.jar (Main archive)
     - c3p0.jar (C3P0 JDBC connection pool)
     - jta.jar (Standard JTA API)
     - dom4j.jar (XML configuration & mapping parser)
   http://www.hibernate.org/

--------------------------------------------------------------------------------
3. Setting up MySQL

Webmail uses JDBC to connect to the DBMS, so it is possible to use Webmail with
almost any DBMS that has version 2.0 or higher of the JDBC driver. To setup
MySQL to use Webmail, follow these instructions:

  1. Download a copy of MySQL 3.0 or higher from http://www.mysql.com.

  2. MySQL JDBC driver can be downloaded from
     http://dev.mysql.com/downloads/connector/j/3.1.html. Follow the README
     instructions to configure the driver for your webserver. If you are using
     Tomcat 5.0 or higher copy the JDBC Driver's JAR into
     $CATALINA_HOME/common/lib.

  3. DBCP uses the Jakarta-Commons Database Connection Pool. It relies on a
     number of Jakarta-Commons components:

     * Jakarta-Commons DBCP 1.0
     * Jakarta-Commons Collections 2.0
     * Jakarta-Commons Pool 1.0

     These JAR files along with your the JAR file for your JDBC driver should be
     installed in $CATALINA_HOME/common/lib.

     NOTE:
     Third Party drivers should be in JAR files, not zipfiles. Tomcat only adds
     $CATALINA_HOME/common/lib/*.jar to the classpath.

     NOTE:
     Do not install these JAR files in your /WEB-INF/lib, or
     $JAVA_HOME/jre/lib/ext, or anywhere else. You will experience problems if
     you install them anyplace other than $CATALINA_HOME/common/lib.

--------------------------------------------------------------------------------
4. Creating the Webmail Database

Conveniently, Webmail comes with a SQL creation script which will create the
Database "webmail". This script is simple and should work with most DBMS. The
script has two main execution tasks: the first is to create the database
"webmail" and propagate it with default data; the second is to create a
new user named "webmail" and grant it access to modify the "webmail" database.

  1. The lib/ folder of the Webmail example contains the file "webmailDB.sql".
     This file is the default creation script for the tables and data needed
     by the Webmail backing beans. You can run this script with the following
     command:

     mysql --user='root'  --password='' < '/webmail/inc/webmailDB.sql'
  
  2. To add a new user to the Webmail database, log in as "admin" and click
     "settings". Clicking "Add new user" will bring up the Add New User panel.
	 
  Note: The default name for admin is "admin", password "admin". When
        configuring your own Webmail, you can change this by modifying the
        context param "adminName" in web.xml.

--------------------------------------------------------------------------------
5. Alternative Hibernate Configurations

Webmail uses MySQL as the default database system to back Hibernate. For
detailed instructions of installing and configuring MySQL, please refer to
"3. Setting up MySQL". You can configure Hibernate for a different database
system. Webmail is pre-configured to use the following open source database
systems in hibernate.cfg.xml:

  - MySQL (http://www.mysql.com/)
  - HSQL (http://hsqldb.sourceforge.net/)
  - PostgreSQL (http://www.postgresql.org/)

To select a database system to use with Webmail, uncomment settings for that
database and comment out settings for the other database systems.

--------------------------------------------------------------------------------
6. ICEfaces Location

Set the ${base.dir} property in the build.xml file to point to the path of your
ICEfaces library folder. For example, if you downloaded the release of ICEfaces
1.5.3 you would use "ICEfaces-1.5.3-bin/icefaces/lib" as the directory. By
default the ${base.dir} property is set to "set/this/path/to/icefaces", so
Webmail will not compile until you have set the location properly.

--------------------------------------------------------------------------------
7. Building Webmail

After the ICEfaces location is set and the database is configured properly, you
can start building Webmail using Java 5.0 or above.
Webmail comes with a build script written in Ant. You can run "ant" in a command
prompt from the root folder of the Webmail project to generate a ".war" file
under the dist/ folder.

The following is required in order to build ICEfaces Webmail:

- JavaMail (available: ${javamail.present})
  Please go to http://java.sun.com/products/javamail/ and download
  and install the latest version. Copy the JAR files to the directory location
  of the ${locallib.dir} build property.
  
- JavaBeans Activation Framework (available: ${activation.present})
  Please go to http://java.sun.com/products/javabeans/glasgow/jaf.html and
  download and install the latest version. Copy the JAR files to the directory
  location of the ${locallib.dir} build property.
  
- Hibernate (available: ${hibernate.present})
  Please go to http://www.hibernate.org/ and download and install
  the latest version. Copy the JAR files to the directory location
  of the ${locallib.dir} build property.

- Log4j (available: ${log4j.present})
  Please go to http://logging.apache.org/log4j/ and download and
  install the latest version. Copy the JAR files to the directory location
  of the ${locallib.dir} build property.

