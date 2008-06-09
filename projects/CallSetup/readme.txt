
ICEfaces SIP Call Setup Demo

Copy the necessary ICEfaces .jar files into the project:

cp ${icefaces.home}/lib/{backport-util-concurrent.jar,icefaces-facelets.jar,commons-fileupload.jar,icefaces.jar,commons-logging.jar} web/WEB-INF/lib/

ant

cp dist/CallSetup.sar ${sailfin.home}/domains/domain1/autodeploy

Point your browser to

http://localhost:8080/CallSetup/registrations.iface

As SIP phones connect and disconnect, the list is dynamically updated.
Calls can be initiated between the first two selected phones.

