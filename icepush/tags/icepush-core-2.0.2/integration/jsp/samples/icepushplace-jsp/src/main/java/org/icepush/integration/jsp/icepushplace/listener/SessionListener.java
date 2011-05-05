package org.icepush.integration.jsp.icepushplace.listener;

import javax.servlet.http.HttpSessionEvent;

import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;

public class SessionListener implements javax.servlet.http.HttpSessionListener
{
    public static final String PERSON_KEY = "person";
    public static final String WORLD_KEY = "world";
    
    public void sessionCreated(HttpSessionEvent event) {
    }
    
    public void sessionDestroyed(HttpSessionEvent event) {
        if (event.getSession() != null) {
            Object personBase = event.getSession().getAttribute(PERSON_KEY);
            Object worldBase = event.getSession().getAttribute(WORLD_KEY);
            
            if ((personBase != null) && (worldBase != null)) {
                PersonType person = (PersonType)personBase;
                ICEpushPlaceWorld world = (ICEpushPlaceWorld)worldBase;
                
                System.out.println("About to logout person " + person.getName() + " from " + person.getRegion());
                world.logoutPerson(person.getRegion(), person);
            }
        }
    }
}