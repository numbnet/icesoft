ICEfaces Asynchronous HTTP Server (AHS) is an HTTP server capable of handling
long-lived asynchronous XMLHttpRequests in a scalable fashion.



Quick Start

1. Edit the build.properties file by specifying the path to the ICEfaces 1.6.1 
   installation directory using the icefaces.dir property.

2. Build the ICEfaces Asynchronous HTTP Server library and application by
   invoking "ant" in the installation directory. This will build the
   "icefaces-ahs.jar" in the "lib/" directory and the "async-http-server.war" in
   the "dist/" directory. Version 1.6.5 or later of Ant is required.

3. Copy the WAR file in the "dist/" directory to the deployment directory of an
   application server.

4. Further configuration of a web server, an application server and ICEfaces
   application(s) is required and described in "ICEfaces Asynchronous HTTP
   Server - Development Guide".

Further Information

Consult the documentation in the "docs/" directory:
-  ReleaseNotes.html
-  ICEfacesAsyncServer.pdf

The latest information is always available at:
-  http://www.icefaces.org/

