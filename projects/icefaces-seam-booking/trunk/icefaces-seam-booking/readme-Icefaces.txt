Icefaces-Seam Booking Example
====================
This example demonstrates the use of Icefaces-1.6.1 with Seam in a Java EE 5 
environment and how, with very little change, a Seam application can become 
ajax-enhanced with ICEfaces


The following lines must be added to jboss-seam\examples\build.xml
int the  OPTIONAL DEPENDENCIES FOR USING SEAM section:-

	<!-- Dependencies for using Seam with ICEfaces -->
	<fileset id="icefaces.jar" dir="${lib.dir}">
		<include name="icefaces*.jar" if="icefaces.lib" />
		<include name="commons-digester*.jar" if="icefaces.lib" />
		<include name="backport-util*.jar" if="icefaces.lib" />
		<include name="commons-fileupload*.jar" if="icefaces.lib" />
	</fileset>

***Don't forget to ensure these jars are included in jboss-seam/lib 
	
and in the <target name="war" depends="compile">
for <copy todir="${war.dir}/WEB-INF/lib">
    .....
    .... (a bunch of filesets so just add another )
       <fileset refid="icefaces.jar"/>

also for tomcat.war & noejb.war targets add the same fileset to reference the 
icefaces jars


