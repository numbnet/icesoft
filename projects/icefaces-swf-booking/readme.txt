
ICEfaces Spring Web Flow Booking Example

To build and run this example you will need the latest Spring Web Flow
and ICEfaces code:

svn checkout http://anonsvn.icefaces.org/repo/icefaces/trunk/icefaces/
svn checkout https://springframework.svn.sourceforge.net/svnroot/springframework/spring-webflow/trunk

In trunk/spring-webflow-samples add an external link to this demo:

svn propset svn:externals "icefaces-swf-booking http://anonsvn.icefaces.org/repo/projects/icefaces-swf-booking" .
svn update

Build Spring Web Flow by invoking "ant" in "build-spring-webflow".

Build ICEfaces by invoking "ant" in "icefaces"

Copy from "icefaces/lib" to "icefaces-swf-booking/lib/global" the following:

icefaces.jar
icefaces-comps.jar
icefaces-facelets.jar
jsf-api-1.2.jar
jsf-impl-1.2.jar

Build the demo by invoking "ant" in "icefaces-swf-booking".

Copy "target/artifacts/war/swf-booking-jsf.war" to your Tomcat 6 webapps
directory.

http://blog.icefaces.org/blojsom/blog/default/2007/12/10/Ajax-with-Spring-Web-Flow-and-ICEfaces-at-TheSpringExperience

