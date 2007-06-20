package action.com.icesoft.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import com.icesoft.eb.User;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Stateless
@Name("authenticator")
public class AuthenticatorAction implements Authenticator{
	@Logger
	Log log;

	@In
	Identity identity;
    
       @PersistenceContext EntityManager em;
       
       @Out(required=false, scope = ScopeType.SESSION)
       private User user;

	public boolean authenticate() {
/*		log.info("authenticating #0", identity.getUsername());
        
          List results = em.createQuery("select u from User u where u.username=#{identity.username} and u.password=#{identity.password}")
          .getResultList();
    
        if ( results.size()==0 )
        {
           return false;
        }
        else
        {
           user = (User) results.get(0);
        }
*/    

        //write your authentication logic here,
		//return true if the authentication was
		//successful, false otherwise
        user = new User("brad","demo","Brad Kroeger");
        
		identity.addRole("admin");
		return true;
	}
}
