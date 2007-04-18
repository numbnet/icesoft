**This has been tested on a jboss4.0.5.GA server.  To run this on an Apache Tomcat Server
please see the documentation that comes with jboss-seam.

1.  download jboss-seam1.2.1.GA and unzip all files (as the libraries are needed for this)
2.  download and unzip Icefaces 1.6.0DR3

3. update the following properties in build.properties file to point to 
the appropriate directories where the server, icefaces libraries and jboss-seam
libraries exist on your machine.
	(an example follows or you can just look at the build.properties file in this folder)

	jboss.home = C:/work/webserver/jboss-4.0.5.GA
	icefacesSourceDirectory = C:/../icesfaces/lib  
	component.docs.dir = c:/icefaces/component/doc 
	jboss.seam.lib = C:/Seam/jboss-seam-1.2.1.GA

run> ant freshen 
and this will place the libraries where they are required in your seam-componentShowcase/lib
folder ready for building the project.  

The rest of the ant tags are similar to any other project generated with icefaces-seam-gen projects.
   > ant explode will compile, create the war, jar and ear and deploy it to the Server you have 
	specified in teh build.properties file.

Enjoy!
