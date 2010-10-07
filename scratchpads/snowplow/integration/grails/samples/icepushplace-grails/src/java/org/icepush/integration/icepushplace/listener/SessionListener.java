package org.icepush.integration.icepushplace.listener;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;

public class SessionListener implements javax.servlet.http.HttpSessionListener
{
    public static final String PERSON_KEY = "person";
    public static final String WORLD_KEY = "world";
    
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("\n****\nSESSION CREATED for Grails!");
    }
    
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("\n****\nSESSION DESTROYED for Grails!");
        
        if (event.getSession() != null) {
            Object personBase = event.getSession().getAttribute(PERSON_KEY);
            Object worldBase = event.getSession().getAttribute(WORLD_KEY);
            
            System.out.println("PERSON: " + personBase + " AND WORLD: " + worldBase);
            
            if ((personBase != null) && (worldBase != null)) {
                PersonType person = (PersonType)personBase;
                ICEpushPlaceWorld world = (ICEpushPlaceWorld)worldBase;
                
                System.out.println("Grails - About to logout person " + person.getName() + " from " + person.getRegion());
                world.logoutPerson(person.getRegion(), person);
            }
        }
    }
}