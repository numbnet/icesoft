This application uses Spring Security 2.0 and ICFaces. It is an adaptation/port of Seema Richard's code for security solution using Acegi and JSF

1. Build
Build using ant script in the lower directory spring-security-basic. Make sure that directory tutorials/lib is checked out. It contains all common libraries shared between the samples. 
buil-common.xml is in tutorials directory. If it is not checked out build will fail

2. Points of interest
    a) web.xml contains basic spring security filters
    b) applicationConfig.xml contains security definitions under security: tag. 
    c) UserDetailService interface, mentioned in the applicationConfig is internal spring security object. Do not look for it in the sample code. 

3. How does it work?
Spring Security has 2 entry points: j_spring_security_check.jsp and j_spring_security_logout.jsp. The first performs log in operation, the second is responsible for clearing up. j_spring_security_check looks for 2 parameters j_username and j_password. They can be provided via post or get. In this sample they are part of the query string. Spring security compares the user definition, provided to j_spring_security_check with user definition provided by UserDetailService implementation. The decision is made based on the username, password and granted roles. There can be three outcomes: 
- success
- access denied based on credentials
- access denied based on roles

If the third occurs, user is still logged in. It is the responsibility of the application to invoke logout procedure. 