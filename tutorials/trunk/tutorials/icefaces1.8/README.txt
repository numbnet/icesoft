--------------------------------------------------------------------------------
How to Build Tutorials
--------------------------------------------------------------------------------

- simply run >ant buld.war

Two folders will be created at the root level of the turorials folder, dist
and build.

 * build - contains all files taht will be included in the war
 * dist  - contains the final tutorial.war along with *.war which are the
           deoploy demos.

--------------------------------------------------------------------------------
How to Deploy the application
--------------------------------------------------------------------------------
 1.  The following libraries must be copied into the servers
     common/lib or common/endorsed folder.
        backport-util-concurrent.jar
        commons-beanutils.jar
        commons-collections.jar
        commons-digester.jar
        commons-discovery.jar
        commons-el.jar
        commons-fileupload.jar
        commons-logging.jar
        el-api.jar
        jstl.jar
        standard.jar
        xercesImpl.jar
        xml-apis.jar

    All escential jars except for icefaces.jar and icefaces-comps.jar. 

2.  Copy the /dist/*.war to a Servlet container of your choice.  The applicaiton
    should deploy as expected. 
