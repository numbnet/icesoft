

If you prefer to work from the command line, you can use Ant to build your
project. (http://ant.apache.org/)  Ant uses the generated 'build.xml' file
which describes exactly how to build your project.  This file has been tested
to work against Ant 1.7.1.  The following assumes 'ant' is on your command
line path.

To run development mode, just type 'ant devmode'.

To compile the project for deployment, just type 'ant'.

To compile and also bundle into a .war file, type 'ant war'.

If you supplied the junit path when invoking webAppCreator, you can type 'ant
test' to run tests in development and production mode.
 
For a full listing of other targets, type 'ant -p'.

