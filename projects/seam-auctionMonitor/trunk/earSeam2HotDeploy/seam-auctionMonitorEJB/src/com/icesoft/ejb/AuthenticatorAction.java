package com.icesoft.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;

import org.jboss.seam.ScopeType;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;

import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import com.icesoft.eb.User;

import java.util.List;

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
        log.info("in authenticate for "+identity.getUsername());
        
        try {
            if (identity != null && user != null) {
                identity.removeRole(user.getRole());
            }
            user = (User) em.createQuery("from User where username=#{identity.username} and password=#{identity.password}").getSingleResult();
            identity.addRole(user.getRole());
            return true;
        } catch (NoResultException e) {
            List results = em.createQuery("select u from User u where u.username=#{identity.username} and u.password=#{identity.password}")
                .getResultList();
            if (results == null)log.info("no users in database");
            else log.info("database has "+results.size()+" users listed");
            FacesMessages.instance().add("Invalid username/password");
            return false;
         //     return true;
        }
    }
}
