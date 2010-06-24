/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.place.grails.services;

import java.util.List;
import java.util.ListIterator;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.WorldResponseType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWsClient;

/**
 *
 * @author bkroeger
 */
public class IcepushPlaceService {

    ICEpushPlaceWsClient client = (ICEpushPlaceWsClient) ApplicationHolder.getApplication().getMainContext().getBean("icepushPlaceClient");

    public void register() {
	    client.registerApp("TestWorldGRAILS", "http://localhost:8080/icepushplace-grails-0.1");
	    System.out.println("ICEPUSHPLACESERVICEIMPL GRAILS: Registered GRAILS Application.");
    }

    public void login(PersonType person) {
	    try {
            client.loginPerson("TestWorldGRAILS", person);
            System.out.println("ICEPUSHPLACESERVICEIMPL GRAILS: Login " + person.getName() +
			       " key=" + person.getKey());
        }catch(Exception e) {
	        System.out.println("ICEPUSHPLACESERVICEIMPL GRAILS: Exception occurred: " + e.toString());
            e.printStackTrace();
	    }
    }

    public void updatePerson(PersonType person) {
        client.updatePerson("TestWorldGRAILS", person);
	    System.out.println("ICEPUSHPLACESERVICEIMPL GRAILS: Update " + person.getName() +
			       " key=" + person.getKey());
    }

    public WorldResponseType updateWorld(long lastSequenceNo) {
	    lastSequenceNo = Long.MIN_VALUE;
	    System.out.println("ICEPUSHPLACESERVICEIMPL GRAILS World Update Request: SequenceNo = " + lastSequenceNo);
	    WorldResponseType response = client.updateWorld("TestWorldJSP", lastSequenceNo);
	    lastSequenceNo = response.getSequenceNo();
	    System.out.println("ICEPUSHPLACESERVICEIMPL GRAILS World Update: SequenceNo = " + lastSequenceNo);
	    List<PersonType> people = response.getPerson();
	    for (ListIterator e = people.listIterator() ; e.hasNext() ;) {
		PersonType person = (PersonType)e.next();
		System.out.println(person.getName());
	    }
	    System.out.println("-----------");
        return response;
    }
}
