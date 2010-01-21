ICEfaces-2.0 Maven2 Support


The following utilities are provided:

1) In core/maven2 an ant script is provided with the poms for the jars to install to whatever local repository you have specified in build.properties file (edit build. properties and set the location of the local repository you would like to install to).
2) ant target "get-maven" will copy off the internet (make sure you have internet access)  the required jar to run maven from ant, to icefaces/lib
3) ant target "install" will install icefaces-push and icefaces jar and poms to your local repository.  
4) to install the compat jars, use the compat/maven2/build.xml target install.


NOTE: The instructions above assume that you have previously installed maven2.



