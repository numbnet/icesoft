package org.icepush.place.jsp.services.impl;

import java.util.List;
import java.util.ListIterator;
import org.icepush.place.jsp.services.IcepushPlaceService;
import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.WorldResponseType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWsClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IcepushPlaceServiceImpl implements IcepushPlaceService{

    ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("applicationContext.xml", IcepushPlaceServiceImpl.class);

    ICEpushPlaceWsClient client = (ICEpushPlaceWsClient) applicationContext.getBean("icepushPlaceClient", ICEpushPlaceWsClient.class);

    public void register() {
	    client.registerApp("TestWorldJSP", "http://localhost:8080/icepush-place");
	    System.out.println("ICEPUSHPLACESERVICEIMPL JSP: Registered JSP Application.");
    }

    public void login(PersonType person) {
	    try {
            client.loginPerson("TestWorldJSP", person);
            System.out.println("ICEPUSHPLACESERVICEIMPL JSP: Login " + person.getName() +
			       " key=" + person.getKey());
        }catch(Exception e) {
	        System.out.println("ICEPUSHPLACESERVICEIMPL JSP: Exception occurred: " + e.toString());	    e.printStackTrace();
	    }
    }

    public void updatePerson(PersonType person) {
        client.updatePerson("TestWorldJSP", person);
	    System.out.println("ICEPUSHPLACESERVICEIMPL JSP: Update " + person.getName() +
			       " key=" + person.getKey());
    }

    public WorldResponseType updateWorld(long lastSequenceNo) {
	    lastSequenceNo = Long.MIN_VALUE;
	    System.out.println("ICEPUSHPLACESERVICEIMPL World Update Request: SequenceNo = " + lastSequenceNo);
	    WorldResponseType response = client.updateWorld("TestWorldJSP", lastSequenceNo);
	    lastSequenceNo = response.getSequenceNo();
	    System.out.println("ICEPUSHPLACESERVICEIMPL World Update: SequenceNo = " + lastSequenceNo);
	    List<PersonType> people = response.getPerson();
	    for (ListIterator e = people.listIterator() ; e.hasNext() ;) {
		PersonType person = (PersonType)e.next();
		System.out.println(person.getName());
	    }
	    System.out.println("-----------");
        return response;
    }
    
}
