package org.icepush.ws.samples.icepushplace.wsclient.test;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWsClient;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;
import org.icepush.ws.samples.icepushplace.*;

//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Tests {

    ICEpushPlaceWorld world;
    private static final String testURL = "http://localhost:8080/testURL";
    private static final String testWorld = "TestWorld";
    
    public Tests(ICEpushPlaceWorld world) {
	this.world = world;
    }

    public void testSequence1() {

	System.out.println("**** Running Test Sequence 1 ****");
	System.out.println("Test Driver: Created ICEpush Place WS Client.");
	
	try {
	    ;
	    ArrayList<String> worlds = new ArrayList<String>();
	    worlds.add(testWorld);
	    worlds.add("OtherWorld");
	    long lastSequenceNo = world.getWsClient().registerApp(testURL, worlds);
	    System.out.println("Test Driver: Registered Application - Starting Sequence No = " + lastSequenceNo);

	    PersonType person = new PersonType();
	    person.setName("Frank");

	    person = world.getWsClient().loginPerson(testWorld, person);
	    System.out.println("Test Driver: Login " + person.getName() + 
			       " key=" + person.getKey());
	    person.setName("Joe");
	    person = world.getWsClient().loginPerson(testWorld, person);

	    lastSequenceNo = getWorldUpdate(lastSequenceNo);

	    person.setName("BadJoe");
	    world.getWsClient().updatePerson(testWorld, person);
	    System.out.println("Test Driver: Update " + person.getName() + 
			       " key=" + person.getKey());
	    lastSequenceNo = getWorldUpdate(lastSequenceNo);

	    world.getWsClient().logoutPerson(testWorld, person);
	    System.out.println("Test Driver: Logout " + person.getName() + 
	    " key=" + person.getKey());

	    lastSequenceNo = getWorldUpdate(lastSequenceNo);
	    
	} catch(Exception e) {
	    System.out.println("Test Driver: Exception occured: " + e.toString());	    e.printStackTrace();
	}
    } 

    public void testSequence2() {

	System.out.println("**** Running Test Sequence 2 ****");
	PersonType person = new PersonType();
	person.setName("Frank");

	person = world.loginPerson(0, person);
	person.setName("Joe");
	person = world.loginPerson(1, person);
	updateWorld(world);
	person.setName("BadJoe");
	world.updatePerson(1, person);
	updateWorld(world);
	world.movePerson(1, 2, person);
	updateWorld(world);

    }

    private long getWorldUpdate(long lastSequenceNo) {
	System.out.println("World Update Request: SequenceNo = " + lastSequenceNo);
	WorldResponseType response = world.getWsClient().updateWorld(testURL, testWorld, lastSequenceNo);
	lastSequenceNo = response.getSequenceNo();
	System.out.println("World Update: SequenceNo = " + lastSequenceNo);
	List<PersonType> people = response.getPerson();
	for (ListIterator e = people.listIterator() ; e.hasNext() ;) {
	    PersonType person = (PersonType)e.next();
	    System.out.println(person.getName()+ ": key = " +
			       person.getKey() + ", status = " + 
			       person.getStatus());
	}
	System.out.println("-----------");
	return lastSequenceNo;
    }

    private void updateWorld(ICEpushPlaceWorld world) {
	System.out.println("Updating World View");
	System.out.println(world.CONTINENT[world.AFRICA]);
	List<PersonType> continent = world.getContinent(world.AFRICA);
	printUpdates(continent);
	System.out.println(world.CONTINENT[world.ANTARCTICA]);
	continent = world.getContinent(world.ANTARCTICA);
	printUpdates(continent);
	System.out.println(world.CONTINENT[world.ASIA]);
	continent = world.getContinent(world.ASIA);
	printUpdates(continent);
	System.out.println(world.CONTINENT[world.EUROPE]);
	continent = world.getContinent(world.EUROPE);
	printUpdates(continent);
	System.out.println(world.CONTINENT[world.NORTH_AMERICA]);
	continent = world.getContinent(world.NORTH_AMERICA);
	printUpdates(continent);
	System.out.println(world.CONTINENT[world.SOUTH_AMERICA]);
	continent = world.getContinent(world.SOUTH_AMERICA);
	printUpdates(continent);
	System.out.println("-----------");
    }

    private void printUpdates(List<PersonType> continent) {
	for (ListIterator e = continent.listIterator() ; e.hasNext() ;) {
	    PersonType person = (PersonType)e.next();
	    System.out.println(person.getName()+ ": key = " +
			       person.getKey() + ", status = " + 
			       person.getStatus());
	}
	System.out.println("-----------");
    }
}
