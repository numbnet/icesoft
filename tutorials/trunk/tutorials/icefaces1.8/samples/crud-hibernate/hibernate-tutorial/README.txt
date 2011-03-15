--------------------------------------------------------------------------------
How to Build Hibernate CRUD Tutorial 
--------------------------------------------------------------------------------

The Hibernate CRUD sample comes with an Ant build script to build the 
application.

Hibernate 3.x libraries must be places in the folder 
./crud-tutorial/hibernate-tutorial/lib/ in order for the application to 
successfully compile. These libraries are available at http://www.jboss.com

The tutorial comes with an SQL creation script which will create the
Database "register".  It is located in the hibernate-tutorial/sqlScripts folder.
This script is simple and should work with most DBMS.

Finally, you will need to download the appropriate JDBC driver for you database 
and configure it for your webserver.  http://www.mysql.com/products/connector/j/ 
works with MySQL.