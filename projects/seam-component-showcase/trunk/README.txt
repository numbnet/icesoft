Please update the following properties in build.properties file to point to 
the appropriate directories where the server, icefaces libraries and jboss-seam
libraries exist on your machine.


jboss.home = C:/work/webserver/jboss-4.0.5.GA
icefacesSourceDirectory = C:/../icesfaces/lib  
component.docs.dir = c:/icefaces/component/doc 
jboss.seam.lib = C:/Seam/jboss-seam-1.2.1.GA

run> ant freshen 
and this will place the libraries where they are required in your seam-componentShowcase/lib
folder ready for building the project.  

The rest of the ant tags are similar to any other project generated with icefaces-seam-gen projects.

