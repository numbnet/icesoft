
ICEfaces Spring Web Flow Booking Example

To build and run this example you will need the latest Spring Web Flow
and ICEfaces code:

svn checkout http://anonsvn.icefaces.org/repo/icefaces/trunk/icefaces/
svn checkout https://springframework.svn.sourceforge.net/svnroot/springframework/spring-webflow/tags/spring-webflow-2.0.2.RELEASE

In spring-webflow-2.0.2.RELEASE/spring-webflow-samples add an external link 
to this demo:

svn propset svn:externals "swf-booking-icefaces http://anonsvn.icefaces.org/repo/projects/swf-booking-icefaces/trunk/swf-booking-icefaces" .
svn update

Build Spring Web Flow by invoking "ant" in "build-spring-webflow".

Build ICEfaces by invoking "ant" in "icefaces"

Copy from "icefaces/lib" to "swf-booking-icefaces/lib/global" the following:

backport-util-concurrent.jar
commons-fileupload.jar
icefaces.jar
icefaces-comps.jar
icefaces-facelets.jar
jsf-api-1.2.jar
jsf-impl-1.2.jar

Build the demo by invoking the following in "swf-booking-icefaces":

ant jar
rm target/war-expanded/WEB-INF/lib/com.springsource.com.sun.faces-1.2.0.08.jar target/war-expanded/WEB-INF/lib/com.springsource.javax.faces-1.2.0.08.jar
cp lib/global/*.jar target/war-expanded/WEB-INF/lib/

(this will be fixed once we determine how to add local .jar files to the 
ivy build)

cp -r target/war-expanded to your Tomcat 6 webapps directory.

Launch the application via

http://localhost:8080/swf-booking-icefaces/spring/main

http://blog.icefaces.org/blojsom/blog/default/2007/12/10/Ajax-with-Spring-Web-Flow-and-ICEfaces-at-TheSpringExperience

