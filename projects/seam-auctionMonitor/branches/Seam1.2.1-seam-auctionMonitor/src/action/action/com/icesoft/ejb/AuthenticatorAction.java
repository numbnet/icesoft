package action.com.icesoft.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.core.FacesMessages;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import com.icesoft.eb.User;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Stateless
@Name("authenticator")
public class AuthenticatorAction implements Authenticator {

    @Logger
    Log log;
    
    @In
    Identity identity;
    
    @PersistenceContext
    EntityManager em;
    
    @In(required = false, scope = ScopeType.SESSION)
    @Out(required = false, scope = ScopeType.SESSION)
    private User user;

    public boolean authenticate() {
        log.info("authenticating #0", identity.getUsername());

        try {
            if (identity != null && user != null) {
                identity.removeRole(user.getRole());
            }
            user = (User) em.createQuery("from User where username=#{identity.username} and password=#{identity.password}").getSingleResult();
            identity.addRole(user.getRole());
            return true;
        } catch (NoResultException e) {
            FacesMessages.instance().add("Invalid username/password");
            return false;
        }
    }
}
