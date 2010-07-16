ICEfaces-3.0 Maven2 Support


The following utilities are provided:

1) In /maven2 an ant script is provided with the poms for the jars to install to whatever local repository you have specified in build.properties file (edit build. properties and set the location of the local repository you would like to install to).
2) ant target "get-maven" will copy off the internet (make sure you have internet access)  the required jar to run maven from ant, to icefaces/lib
3) ant target "install" will install icefaces-push and icefaces jar and poms to your local repository.  
4) to install the compat jars, use the compat/maven2/build.xml target install.

This really isn't necessary if you can use the snapshot repository at
http://server.ice:8888/svn/ossrepo/maven2/snapshots
Once the release has been made, it will take a day or so for the proper entries to be accessed.


NOTE: The instructions above assume that you have previously installed maven2.

Each example has it's own build pom and you can run >mvn clean package to create 2 different profiles
See which one in the pom is the default.  (either Tomcat6 or Glassfishv3 for now-->really just builds with 
jsf2 jars (tomcat) or without (glassfish).


...or >mvn clean packet -Ptomcat6 
(which runs the correct profile for a server which doesn't already contain the jsf jars).



