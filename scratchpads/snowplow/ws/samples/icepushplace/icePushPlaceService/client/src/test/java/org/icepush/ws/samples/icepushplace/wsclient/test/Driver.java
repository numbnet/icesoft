package org.icepush.ws.samples.icepushplace.wsclient.test;

import java.util.List;
import java.util.ListIterator;

import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWsClient;
import org.icepush.ws.samples.icepushplace.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Driver {

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("applicationContext.xml", Driver.class);

        ICEpushPlaceWsClient client = (ICEpushPlaceWsClient) applicationContext.getBean("icepushPlaceClient", ICEpushPlaceWsClient.class);
	System.out.println("Test Driver: Created ICEpush Place WS Client.");
	
	try {
	    client.registerApp("TestWorld", "http://localhost:8080/testURL");
	    System.out.println("Test Driver: Registered Application.");

	    PersonType person = new PersonType();
	    person.setName("Joe");

	    person = client.loginPerson("TestWorld", person);
	    System.out.println("Test Driver: Login " + person.getName() + 
			       " key=" + person.getKey());
	    person.setName("BadJoe");

	    client.updatePerson("TestWorld", person);
	    System.out.println("Test Driver: Update " + person.getName() + 
			       " key=" + person.getKey());

	    /*	    client.logoutPerson("TestWorld", person);
	    System.out.println("Test Driver: Logout " + person.getName() + 
	    " key=" + person.getKey()); */
	    long lastSequenceNo = Long.MIN_VALUE;
	    System.out.println("World Update Request: SequenceNo = " + lastSequenceNo);
	    WorldResponseType response = client.updateWorld("TestWorld", lastSequenceNo);
	    lastSequenceNo = response.getSequenceNo();
	    System.out.println("World Update: SequenceNo = " + lastSequenceNo);
	    List<PersonType> people = response.getPerson();
	    for (ListIterator e = people.listIterator() ; e.hasNext() ;) {
		person = (PersonType)e.next();
		System.out.println(person.getName());
	    }
	    System.out.println("-----------");

	    
	} catch(Exception e) {
	    System.out.println("Test Driver: Exception occured: " + e.toString());	    e.printStackTrace();
	}
    }
}
