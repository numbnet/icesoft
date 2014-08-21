ICEmobile-1.3.0 Maven2 Support

If this is the ICEmobile source distribution, you need to first build the jars so that they are residing in the lib directories 
of their respective folders. 

NOTE: Thsese instructions assume that you have maven2 installed.

The following utilities are provided:

1) In /maven2 an ant build script is provided along with the poms for the jars that will be installed to the local repository 
specified in the build.properties file. You will need to edit the build.properties and set the location of the local repository 
you would like to install to.

2) The ant target "get-maven" requires an internet connection in order to obtain the jar needed to run maven from ant. This jar 
will be placed into icemobile/lib.

3) The ant target "install" will install icefaces-mobi and poms to the local repository specified in
the build.properties file.

These steps are not needed if you choose to use the releases repository at
http://anonsvn.icefaces.org/repo/maven2/releases/. 
Once a release has been made, it will take a day or so for the proper entries to be made available.

Each JSF example in the ICEmobile bundle has it's own build pom and >mvn clean package can be run to create 1 of 3 different
profiles: web, servlet, servlet-myfaces or web-myfaces.
For example:

>mvn clean package -Pservlet-myfaces runs the profile for a server which doesn't already contain the jsf jars using the
myfaces implementation of JSF

Another approach is to use the mvn command line (or your IDE) to install from this directory.  Only open source releases
are available from the icefaces svn mvn2 repo.


 



