                     COMPONENT SHOWCASE GETTING STARTED GUIDE
                            Rev. 2.0
                         (www.icefaces.org)
--------------------------------------------------------------------------------

TOPICS (DEVELOPER NOTES)

1. What is Component Showcase?
2. Project Layout
3. Building the Project
4. Code Conventions

--------------------------------------------------------------------------------
1. What is Component Showcase?

    The component showcase is a collection of small examples intended to aid 
users in the evaluation of ICEfaces feature and provide developers with simple 
straight forward examples of component usage.  

The component showcase as a complete application comes in many different 
varieties; JSP, Facelets, Seam, Portlets and Enhanced.  Each variety show how 
ICEfaces component can be configured to run in the respective environment.  As 
a result of this the component examples will be designed in way which will 
minimize duplicate application code.  This notion of reusable code should be
taken into consideration when working on the project.  

   JSP
    Default implementation using Sun JSF RI or MyFaces.  Simple navigation 
    system which has a link for every component example.  The main layout
    construct is a panel stack which is manipulated by the navigation system.

   Facelets
    Similar to JSP version but uses dynamic includes and Facelets templating
    for the application.

    Facelets based example which has been heavily productized by Marketing.
    All other showcase example will be plain.  This example will be "new"
    showcase which will be visible by public on .org.

   Seam (not implemented)
    JBoss Seam variation of component showcase.  All common base beans are to 
    be extended for Seam annotation.  Application layout should have some
    sort of conversation based approach for testing purposes

   Portlets (not implemented)
    Extension of base portlet demo that Deryk and Jack completed.  Would 
    recommend that build environment is preserved so different Portal versions
    can be build.  LifeRay should be primary focus.


--------------------------------------------------------------------------------
2. Project Layout
    
    svn/repo/applications/component-showcase/trunk/
       
       ./common-src/   # common Java source code
       ./common-web/   # common JSP code 
       ./enhanced/     # enhanced marketing showcase
       ./facelets/     # plain/simple facelets showcase
       ./jsp/         # plain/simple regular jsp showcase
       ./portlet/      # plain/simple portlet showcase
       ./seam/         # plain/simple seam showcase
       build.xml      # sub ant task for each showcase version. 
       build.properties
  
    Each version of showcase will have a seperate build file.  The root build
    file will have targets that calls all showcase examples.  JSP and Facelets
    will be simple as they can extend the ./etc/build-common.xml file.  Portals
    and Seam will be more complicating.  Seam build should be similar to what 
    is provided by SeamGen and portlets similar to Deryks sample project. 
       
--------------------------------------------------------------------------------
3. Building the Project

   The component showcase currently has two implementations; one for JSP, and
   the other for Facelets.  Each sub project can be built without modification
   if build from within the samples directory.   Otherwise, the following ant
   properties must be defined:

     common.build.file - path to the common build file, build-common.xml
     component.docs.dir - documents directory path
     component.tld.dir - tld directory path

--------------------------------------------------------------------------------
4. Code Conventions
   
   The main focus in the short term will be to build the common-* code/examples
   and build a simple JSP sample application.  Here are some convension that 
   must be considered for each checkin:
   
   - reference corresponding bug.
   - full JavaDoc and JSP comments.
   - accessibility best practises where ever possible.
   - Internationalization much be used for all descriptions, values, and labels. 
   - insure that proper licensing headers are inserted for each file.