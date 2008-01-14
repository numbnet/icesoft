/*
 * ChangePassword.java
 *
 * Created on June 5, 2007, 3:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *  really just copied from seam-booking example
 */

package com.icesoft.ejb;


import static org.jboss.seam.ScopeType.EVENT;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import org.jboss.seam.security.Identity;

import com.icesoft.eb.User;

@Stateful
@Scope(EVENT)
@Name("register")
public class RegisterAction implements Register {

    @In
    private User user;

    @PersistenceContext
    private EntityManager em;

    @In
    private FacesMessages facesMessages;

    @In(create=true)
    Identity identity;

    private String verify;
    private boolean registered;

    public void register() {
        if (user.getPassword().equals(verify)) {
            List existing = em.createQuery("select u.username from User u where u.username=:username")
                    .setParameter("username", user.getUsername())
                    .getResultList();
            if (existing.size() == 0) {
                identity.removeRole(user.getRole());
                user.setRole("user");
                em.persist(user);
                identity.setUsername(user.getUsername());
                identity.setPassword(user.getPassword());
                identity.setRememberMe(true);
                identity.login();
                facesMessages.add("Successfully registered as #{user.username}");
                registered=true;
            } else {
                facesMessages.add("Username #{user.username} already exists");

            }
        } else {
            facesMessages.add("verify", "Re-enter your password");
            verify = null;
        }
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    public void invalid()
    {
       facesMessages.add("Please try again");
    }
    
    public boolean isRegistered()
    {
       return registered;
    }

}
