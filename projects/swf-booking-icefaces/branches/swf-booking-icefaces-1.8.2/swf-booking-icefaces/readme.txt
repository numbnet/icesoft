
ICEfaces Spring Web Flow Booking Example

To build and run this example you will need the latest Spring Web Flow
and ICEfaces code:

svn checkout http://anonsvn.icefaces.org/repo/icefaces/trunk/icefaces/
svn checkout https://springframework.svn.sourceforge.net/svnroot/springframework/spring-webflow/tags/spring-webflow-2.0.3.RELEASE

In trunk/spring-webflow-samples add an external link to this demo (this step 
is also essential for building spring-webflow without errors as it replaces 
the password protected externals definitions for other sample applications 
in spring-webflow) :


svn propset svn:externals "swf-booking-icefaces http://anonsvn.icefaces.org/repo/projects/swf-booking-icefaces/trunk/swf-booking-icefaces" .
svn update

Build Spring Web Flow by invoking "ant" in "build-spring-webflow".

Build ICEfaces by invoking "ant" in "icefaces"

Copy from "icefaces/lib" to "swf-booking-icefaces/src/main/webapp/WEB-INF/lib" the following:
(this will be fixed once we determine how to add local .jar files to the
ivy build)

backport-util-concurrent.jar
commons-fileupload.jar
icefaces.jar
icefaces-comps.jar
icefaces-facelets.jar

Build the demo by invoking the following in "swf-booking-icefaces":

ant jar
(repeat or "ant clean" if it fails)

cp target/artifacts/swf-booking-faces.war  to your Tomcat 6 webapps directory.

Launch the application via

http://localhost:8080/swf-booking-icefaces/spring/main

http://blog.icefaces.org/blojsom/blog/default/2007/12/10/Ajax-with-Spring-Web-Flow-and-ICEfaces-at-TheSpringExperience

