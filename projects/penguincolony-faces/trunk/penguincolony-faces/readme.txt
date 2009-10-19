--------------------------------------------------------------------------------
Welcome to the MetaWidget + ICEfaces "Penguin Colony" Demo!
--------------------------------------------------------------------------------

It is a whimsical demo concerning details of penguins in a colony. 
The demo uses Metawidget to generate ICEfaces components based on an annotated business model.
Metawidget dynamically creates the input boxes and command buttons, 
and dynamically changes the UI in response to user events.

Note that MetaWidget requires ICEfaces 1.8.2+.

--------------------------------------------------------------------------------
About Metawidget
--------------------------------------------------------------------------------

Metawidget is a 'smart User Interface widget' that populates itself,
 at runtime, with UI components to match the properties of business objects.
Metawidget does this by inspecting an application's existing back-end architecture
 and creating UI widgets native to its existing front-end framework. 
This includes creating widgets from third party component libraries like ICEfaces.

See http://www.metawidget.org/ for more information.

--------------------------------------------------------------------------------
How to Build 
--------------------------------------------------------------------------------

1. In build.properties ensure the icefaces.dir property is pointing to ICEfaces
   install location (ie. /ICEfaces-1.8.2-bin/icefaces).
   
2. Version 1.6.5 or later of Ant is required.  From the installation directory, 
   invoke the following:
   
   >ant

   This will build "penguincolony-faces.war" and place it in the "build/examples/faces/" directory.
   By default, "ant" will build for tomcat6.0.  The following command will give you a list of 
   the available targets:
   
   >ant -p
   

3. Copy the WAR file in the "build/examples/faces/" directory to the deployment directory of the 
   Servlet container of your choice.  The application should deploy as expected.  