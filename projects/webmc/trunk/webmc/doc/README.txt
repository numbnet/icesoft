                     WEBMC GETTING STARTED GUIDE
                            Rev. 1.3
                         (www.icefaces.org)
---------------------------------------------------------------------------

TOPICS

1. What is Webmc?
2. Required JAR Files
3. Mail Configuration
4. ICEfaces Location
5. Default Presentations
6. Building Webmc

---------------------------------------------------------------------------
1. What is Webmc?

WebMC Beta is a basic web conferencing system that allows participants to share 
PowerPoint presentations over the web. WebMC uses ICEfaces built in Ajax-Push 
capabilities to deliver a collaborative web-based PowerPoint sharing experience 
without the use of special browser plugins.

---------------------------------------------------------------------------
2. Required JAR Files

Webmc uses the Javamail libraries which must be properly configured before
running the web application. Each required JAR file should be placed in the
./lib/ directory of your Webmc checkout. The Javamail jars are not provided by
default because of licensing differences. To ensure the application will
compile, acquire the necessary jars from the links provided.

 dsn.jar  imap.jar  mail.jar  mailapi.jar  pop3.jar  smtp.jar
  JavaMail API implementation (version 1.3 or greater)
  http://java.sun.com/products/javamail/

---------------------------------------------------------------------------
3. Mail Configuration

If you require the mail functionality, and have setup the Javamail jars as
described above, you will need to modify the mailaccount.prop file with your
mailserver information. An example of the file is shown below:

 # src/com/icesoft/faces/presenter/mail/mailaccount.prop 
 # General
 protocol=imap
 host=your.mail.host
 user=mail
 password=secret

 # Incoming
 incomingHost=your.incoming.host
 incomingPort=888
 incomingSSL=false

 # Outgoing
 outgoingHost=your.outgoing.host
 outgoingPort=888
 outgoingSSL=false
 outgoingVerification=false

Fill in each property to use your own data.

---------------------------------------------------------------------------
4. ICEfaces Location

Set the base.dir property in the build.properties file to point to the path of
your ICEfaces library folder. For example, if you downloaded the release of
ICEfaces 1.8.0 you would use "ICEfaces-1.8.0-bin/icefaces/lib" as the directory.
By default the base.dir property is set to build from the local ossrepo trunk.

---------------------------------------------------------------------------
5. Default Presentations

Webmc will unpack and autocreate presentations based on the zip files present
in the basepres folder. Simply add a properly formatted zip file to
web/basepres/, and when Webmc is deployed and started an unmoderated slideshow
version of the presentation will be created.

---------------------------------------------------------------------------
6. Building Webmc

After the ICEfaces location is set, any mailserver configuration is done, and
the base presentations you desire have been added, you can begin building Webmc
using Java 1.4.0 or above.
Webmc comes with a build script written in Ant. You can run "ant" in a command
prompt from the root folder of the Webmc project to generate a ".war" file
under the dist/ folder.

Once the build is successful (if it is not, ensure you have completed Step #4
properly), you can deploy the created war file to the application server of your
choice (Apache Tomcat or JBoss are recommended).

