Building Update Center modules

1) Build Glassfish + AHS versions of the apps (AHS, Auction Monitor, Component Showcase, and WebMC) for whatever version of ICEfaces you need to test against.  For now, we're assuming the 1.7.1 tag in the repository.  We have dedicated Glassfish builds for all the apps but currently don't have any way to include icefaces-ahs.jar into the WEB-INF/lib directory of the three examples.  So you either have to do this manually or modify the build targets appropriately.

To the etc/build-common.xml file change:

    <patternset id="additional.icefaces.lib.jars.to.include">
        <include name="just-ice.jar" if="just-ice" />
    </patternset>

to:

    <patternset id="additional.icefaces.lib.jars.to.include">
        <include name="just-ice.jar" if="just-ice" />
        <include name="icefaces-ahs.jar" if="ahs" />
    </patternset>
    
Component Showcase has it's own patternset for this so you need to make the change there as well.  So:

    <patternset id="additional.icefaces.lib.jars.to.include"
                includes="
                krysalis-jCharts-1.0.0-alpha-1.jar
                "
            />

becomes:

    <patternset id="additional.icefaces.lib.jars.to.include"
                includes="
                krysalis-jCharts-1.0.0-alpha-1.jar
                "
            >
         <include name="icefaces-ahs.jar" if="ahs" />
    </patternset>


To build AHS, Auction Monitor, and Component Showcase (assuming that you've made the changes above and start from ossrepo/icefaces/tags/icefaces-1.7.1/icefaces/) :

cd ./ahs
ant clean build.servlet.glassfish.war

cd ./samples/auctionMonitor
ant clean -Dahs="true" glassfishv2

cd ./samples/component-showcase/facelets
ant clean -Dahs="true" glassfishv2

To build WebMC takes a bit more work.  It isn't in the normal sample area so you to build it out of the projects directory.  In it's build.properties, you can point at the same "common build" file that you edited above.  You just need to point it (e.g. modify the build.properties) at the common build dir of your "release" build.

From ossrepo/projects/webmc/trunk/webmc/
ant clean -Dahs="true" glassfishv2


2) Build Update Center modules (ossrepo/projects/update-center/trunk/)

The Update Center module has a top-level build file that assemble and gather everything that you need together. First, modify the common.properties file and change the icefaces.root property points at your 1.7.1 tag directory.

icefaces.root=<yourpath>/icefaces/tags/icefaces-1.7.1/icefaces

You also need to change:

war.loc.webmc=<yourpath>/webmc/trunk/webmc/dist

Then, from the trunk directory, just type:

ant

The default build target should gather all the war files, wrap them up as module jars, including the relevant info and resource files, and put everything into a build directory.  The contents of the build directory can then be copied to any HTTP server (Tomcat, Glassfish, etc) to be served up for the Update Center.  If you put the modules somewhere other than a location of localhost:8080, then you'll need to modify the update-center.xml file and change the URLs.
