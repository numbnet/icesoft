This is a WAR deployment of the single page seam-icefaces booking example.   As such, it 
doesn't require an ejb3 container and still uses Seam annotations and contexts. 

It can be deployed in JBoss AS 4.2 as well as jboss-liferay bundled configuration. 
 

1.  download jboss-seam-2.1.A1 (I used the Seam trunk) and unzip all files (as the libraries will be copied from these folders)--may have to build the seam jars if using trunk
    NOTE THAT FOR PORTLET EXAMPLES*** you must have jboss-seam-2.1.0.A1 as a minimum, although
    seam-2.0.2 also works.
2.  download and unzip Icefaces (1.7.0 or 1.7.1 final release) --again I've been using our trunk

3. update the following properties in build.properties file to point to 
   the appropriate directories where the jboss-4.2.* server or portal server,
   icefaces libraries and jboss-seam
   libraries exist on your machine.
	(an example follows or you can just look at the build.properties file in this folder)

 for example:-
 	jboss.home = C:/work/webserver/jboss-4.2.2.GA   (or use the jboss-portal AS for that target)
	icefaces.home = C:/ICEfaces-1.7.0/icefaces  
	jboss.seam.home = C:/Seam/jboss-seam-2.0.1.GA     

4. run target = copy-libs  (will copy all the required libraries from the above locations)

Specific Server instructions:-

JBoss AS 4.2.x  (this is the default target for the ant script):
  * Install JBoss AS with the default profile
  * ant jboss
  * Start JBoss AS 
  * Access the app at http://localhost:8080/icefacesJPA/

Jboss-4.2.2.GA/Liferay5.0.1.RC bundle 
 * Install 
 * Must copy over hibernate.jar and hibernate-validate.jar from seam distribution to 
 *    <serverDist>/server/default/lib
 * Must have jboss-seam-2.1.0.A1 as minimum
 * ant jboss4.2.2-liferay5.0.1.RC
 * copy \dist\jboss-liferay\icefacesJPA.war to \Documents and Settings\<user profile>\liferay
 * start AS
 * use URL:- http://localhost:8080/c/portal/login
 * sign in and create a page and place this portlet on it.  If you want
   certain layouts, you may have to alter the css for layout on the pages. 
   I am no css whiz (which you will notice right away).  I concentrated on 
   showing the functionality of the conversation context with the   
   components.

Jboss-portal
 * I have tested this on jboss-portal-2.6.4 AS.  For now, their is only one
   window on the page (the configuration files are already setup for ICEfaces-booking)
   you could add another portal.  

Things Not implemented:-
1) The Register and change password functionality.  What's the point when the
   login will probably be handled by the portlet container (since this
   example was originally derived as a portlet example).  I also didn't worry
   about the fact that by logging out of one portlet instance of this app
   you would throw an exception on the AS (since it probably wouldn't be used
   anyways).  You can strip the login stuff off of this and the portlet would
   still function. (the conversation will begin in pages.xml then).
2) Since Seam's construct are designed with redirection in mind, you will notice
   some things that may seem odd to you.  You can almost be 100% certain that 
   this is the reason.  With Seam, there is a lot of choice to do even the 
   simplest things.  The original hotel booking example uses a Session-scope
   for the hotel search.  This has been changed to conversation scope in order
   to have multiple windows open (the portlet way!).
   Since we aren't using an ejb3 container, we are instead using
   Seam-Managed Persistence 
   The booking and hotel details, etc are in a conversationally-scoped
   bean (after all it's the same task so carry on the conversation--could have
   nested it though).
   I put a log statement each time this bean is destroyed (conversation ends) so 
   you can see the management of the context is done by Seam.  I could have nested the 
   conversation to do the booking (after the hotel view), but thought to keep it simple
   as it really is part of the same conversation.  The backing beans changed very little.
   Seam conversation Management would be do-able, but again, it just takes time.
3) No styling has been applied--just has defaults from seam-gen css.  
