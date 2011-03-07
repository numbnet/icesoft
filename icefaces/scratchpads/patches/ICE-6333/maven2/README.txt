ICEfaces EE Maven2 Support
Build the jars first if using source distribution bundle! Edit build.properties in this directory
to define local m2 repository location.

The following utilities are provided:

1) In icefaces/maven2 an ant script is provided with the poms for the jars to install to whatever local repository you have specified in build.properties file (edit build. properties and set the location of the local repository you would like to install to).
2) ant target "get-maven" will copy off the internet (make sure you have internet access)  the required jar to run maven from ant, to icefaces/lib
3) ant target "install-local" will install icefaces jars and poms to your local repository.  (icefaces.jar, icefaces-comps.jar and icefaces-facelets.jar).
4) ant target "install-jsf-local" if you want to install the version of the jsf-1.2 jars which are included in this distribution bundle.

NOTE: The instructions above assume that you have previously installed maven2.
You can change the jsf.properties property to whatever jsf version you are using. (in icefaces or just-ice pom)
Then in your application pom, you simply have to include your icefaces-compat-ee and possibly icefaces-facelets-ee dependencies,(which have dependency on icefaces-ee, which has dependency on jsf version)
NOTE:  These poms do not include xercesImpl.jar or xml-apis.jar as dependencies (which are required if you using JDK1.4 as these jars are included in JDK1.5 and 1.6).


