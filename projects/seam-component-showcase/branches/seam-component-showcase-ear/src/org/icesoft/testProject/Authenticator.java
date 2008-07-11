package org.icesoft.testProject;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.annotations.Scope;
import static org.jboss.seam.ScopeType.APPLICATION;

@Scope(APPLICATION)
@Name("authenticator")
public class Authenticator
{
    @Logger Log log;

    @In Identity identity;

    public boolean authenticate()
    {
        log.info("authenticating #0", identity.getUsername());
        //write your authentication logic here,
        //return true if the authentication was
        //successful, false otherwise
      //  identity.addRole("admin");
        return true;
    }
}
