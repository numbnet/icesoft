Note that, at the moment, there's no Maven archetype to build tutorials for portlets.
So, after building this tutorial with 'mvn package -P servlet', just copy the all the
three jars found in 'icefaces/lib/portlets/liferayfaces' to the lib directory of the 
generated package.