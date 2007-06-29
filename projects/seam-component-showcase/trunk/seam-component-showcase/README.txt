**This has been tested on a jboss4.0.5.GA server and JBoss AS 4.2.0.GA (with seam1.2.1.GA and seam1.3.0.ALPHA) 
To run this on an Apache Tomcat Server, please see the documentation that comes with jboss-seam.

1.  download jboss-seam1.2.1.GA or 2.0.0.Beta1 and unzip all files (as the libraries will be copied from these folders)
2.  download and unzip Icefaces 1.6.0 final release (or if you're eager and get this before the final release, just build the icefaces jars from the head)

3. update the following properties in build.properties file to point to 
the appropriate directories where the server, icefaces libraries and jboss-seam
libraries exist on your machine.
	(an example follows or you can just look at the build.properties file in this folder)

	jboss.home = C:/work/webserver/jboss-4.0.5.GA 
	icefacesSourceDirectory = C:/../icesfaces/  
	jboss.seam.home = C:/Seam/jboss-seam-1.2.1.GA

or for Seam2.0.0.beta1 you will want:-
 	jboss.home = C:/work/webserver/jboss-4.2.0.GA 
	icefacesSourceDirectory = C:/../icesfaces/  
	jboss.seam.home = C:/Seam/jboss-seam-2.0.0.Beta1     


for seam1.2.1 & myfaces:-

run> ant build-myFaces  


and this will copy the libraries from the locations you specifed in the build.properties file to the 
seam-componentShowcase/lib folder, compile and build the exploded archives as well as the ear file to deploy.  

if all goes well, 
run> ant deploy

this deploys the ear to your specified server....

for jboss-seam1.2.1.GA and jsf1.2 libs (NOTE**had this working but now it's being stubborn, so
	bear with me while I get this working again!  It's just configuration & packaging issues & fixing the build
       script, just need the time to fix it up)  with JBoss AS 4.2.0.GA you choose
>ant build-Seam1.2WithJsf1.2
>ant deploy

for jboss-seam-2.0.0.Beta1 and jsf1.2 you choose
>ant build-Seam2.0WithJsf1.2
>ant deploy

Enjoy!
