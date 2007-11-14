**This has been tested on a jboss4.0.5.GA server and JBoss AS 4.2.*.GA (with seam1.2.1.GA and seam2.0.0.GA) 
To run this on an Apache Tomcat Server, please see the documentation that comes with jboss-seam.

1.  download jboss-seam1.2.1.GA or 2.0.0.GA and unzip all files (as the libraries will be copied from these folders)
2.  download and unzip Icefaces 1.6.1 final release (or if you're eager and get this before the final release, just build the icefaces jars from the head)

3. update the following properties in build.properties file to point to 
the appropriate directories where the server, icefaces libraries and jboss-seam
libraries exist on your machine.
	(an example follows or you can just look at the build.properties file in this folder)

	jboss.home = C:/work/webserver/jboss-4.0.5.GA 
	icefacesSourceDirectory = C:/../icesfaces/  
	jboss.seam.home = C:/Seam/jboss-seam-1.2.1.GA

or for Seam2.0.0.GA you will want:-
 	jboss.home = C:/work/webserver/jboss-4.2.2.GA 
	icefacesSourceDirectory = C:/../icesfaces/  
	jboss.seam.home = C:/Seam/jboss-seam-2.0.0.GA     


for seam1.2.1 & myfaces:-

run> ant build-myFaces  


and this will copy the libraries from the locations you specifed in the build.properties file to the 
seam-componentShowcase/lib folder, compile and build the exploded archives as well as the ear file to deploy.  

if all goes well, 
run> ant deploy

this deploys the ear to your specified server....

for jboss-seam1.2.1.GA and jsf1.2 libs ( with JBoss AS 4.2.0.GA or 4.2.1.GA or 4.2.2.GA you choose
>ant build-Seam1.2WithJsf1.2
>ant deploy

for jboss-seam-2.0.0.GA and jsf1.2 you choose
>ant build-Seam2.0WithJsf1.2
Then (as in regular seam 1.2.1.GA with jsf1.2 specs...see this url:- http://docs.jboss.com/seam/1.2.1.GA/reference/en/html/tutorial.html#d0e2067 )
you have to then copy the el-*.jar files to your jsf-libs in the embeded Tomcat server.

Enjoy!
