package org.icepush.ws.samples.icepushplace.service;

import java.util.Hashtable;
import java.math.BigInteger;

import org.icepush.ws.samples.icepushplace.*;

public class ICEpushPlaceService {

    private Hashtable<String,Application> apps;
    private Hashtable<String,World> worlds;
    private long masterSequenceNo;

    public ICEpushPlaceService() {
	apps = new Hashtable<String,Application>();
	worlds = new Hashtable<String,World>();
	masterSequenceNo = Long.MIN_VALUE;
    }

    public void registerApp(AppType app) {
	if (!apps.containsKey(app.getURL())) {
	    Application application = new Application(app);
	    apps.put(app.getURL(), application);
	}
	if (!worlds.containsKey(app.getWorld())) {
	    World world = new World(app.getWorld());
	    worlds.put(app.getWorld(), world);
	}
    }

    public BigInteger loginPerson(String myWorld, PersonType person) {
	World world = worlds.get(myWorld);
	BigInteger bigKey = new BigInteger("0");
	if (world != null) {
	    //Build a unique key for this person;
	    String keyGen = new String(person.getName() +
				       String.valueOf(System.currentTimeMillis()));
	    int key = keyGen.hashCode();
	    while (key==0 || world.getPeople().containsKey(key)) {
		keyGen = new String(keyGen + 
				    String.valueOf(System.currentTimeMillis()));
		key = keyGen.hashCode();
	    }

	    // Add the person.
	    Person newPerson = new Person(person);
	    newPerson.setLastSequenceNo(++masterSequenceNo);
	    bigKey = new BigInteger(new Long(key).toString());
	    person.setKey(bigKey);
	    world.getPeople().put(new Integer(key), newPerson);
	}
	return bigKey;
    }

    private class Person {
	private PersonType personData;
	private long lastSequenceNo;

	public Person(PersonType person) {
	    personData = person;
	    lastSequenceNo = Long.MIN_VALUE;
	}

	public long getLastSequenceNo() {
	    return lastSequenceNo;
	}
	public void setLastSequenceNo(long lastNo) {
	    lastSequenceNo = lastNo;
	}

	public PersonType getPersonData() {
	    return personData;
	}
	public void setPersonData(PersonType person) {
	    personData = person;
	}
    }

    private class Application extends AppType {
	private long lastSequenceNo;

	public Application(AppType app) {
	    this.setURL(app.getURL());
	    this.setWorld(app.getWorld());
	    lastSequenceNo = Long.MIN_VALUE;
	}

	public long getLastSequenceNo() {
	    return lastSequenceNo;
	}
	public void setLastSequenceNo(long lastNo) {
	    lastSequenceNo = lastNo;
	}
    }	

    private class World {
	private final String name;
	private final Hashtable<Integer,Person> people;

	public World(String name) {
	    this.name = name;
	    people = new Hashtable<Integer,Person>();
	}

	public String getName() {
	    return name;
	}

	public Hashtable<Integer,Person> getPeople() {
	    return people;
	}
    }	
}

