package org.icepush.ws.samples.icepushplace.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Enumeration;
import java.math.BigInteger;

import org.icepush.ws.samples.icepushplace.*;

public class ICEpushPlaceService {

    public static final int ADD = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;

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
	    System.out.println("Registered Application: World='" +
			       app.getWorld() + "' URL='" + 
			       app.getURL() + "'");
	}
    }

    public Integer loginPerson(String myWorld, PersonType person) {
	World world = worlds.get(myWorld);
	int key = 0;
	if (world != null) {
	    //Build a unique key for this person;
	    String keyGen = new String(person.getName() +
				       String.valueOf(System.currentTimeMillis()));
	    key = keyGen.hashCode();
	    while (key==0 || world.getPeople().containsKey(key)) {
		keyGen = new String(keyGen + 
				    String.valueOf(System.currentTimeMillis()));
		key = keyGen.hashCode();
	    }

	    // Add the person.
	    person.setStatus(ADD);
	    Person newPerson = new Person(person);
	    newPerson.setLastSequenceNo(++masterSequenceNo);
	    world.getPeople().put(new Integer(key), newPerson);
	    System.out.println("Logged In: World='" +
			       myWorld + "' Name='" + 
			       person.getName() + "' key='" + key + 
			       "' seqNo=" + masterSequenceNo);
	}
	else {
	    System.out.println("Logged In Failed: World='" + myWorld + "'");
	}

	return new Integer(key);
    }

    public void updatePerson(String myWorld, PersonType person) {
	World world = worlds.get(myWorld);
	if (world != null) {
	    Person oldPerson = world.getPeople().get(new Integer(person.getKey()));
	    if (oldPerson != null && oldPerson.getPersonData().getStatus() != DELETE) {
		// Update the person;
		person.setStatus(UPDATE);
		oldPerson.setPersonData(person);
		oldPerson.setLastSequenceNo(++masterSequenceNo);
		System.out.println("Update: World='" +
				   myWorld + "' Name='" + 
				   person.getName() + "' seqNo=" + masterSequenceNo);
	    } else {
		System.out.println("Update Failed - Person Not Found: World='" +
				   myWorld + "' Name='" + 
				   person.getName() + "' seqNo=" + masterSequenceNo);
	    }
	}
	else {
	    System.out.println("Update Failed - World Not Found: World='" +
			       myWorld + "' Name='" + 
			       person.getName() + "' seqNo=" + masterSequenceNo);
	}
    }

    public void logoutPerson(String myWorld, PersonType person) {
	World world = worlds.get(myWorld);
	if (world != null) {
	    Person oldPerson = world.getPeople().get(new Integer(person.getKey()));
	    if (oldPerson != null && oldPerson.getPersonData().getStatus() != DELETE) {
		// Update the person;
		person.setStatus(DELETE);
		oldPerson.setPersonData(person);
		oldPerson.setLastSequenceNo(++masterSequenceNo);
		System.out.println("Log Out: World='" +
				   myWorld + "' Name='" + 
				   person.getName() + "' seqNo=" + masterSequenceNo);
	    } else {
		System.out.println("Log Out Failed - Person Not Found: World='" +
				   myWorld + "' Name='" + 
				   person.getName() + "' seqNo=" + masterSequenceNo);
	    }
	}
	else {
	    System.out.println("Log Out Failed - World Not Found: World='" +
			       myWorld + "' Name='" + 
			       person.getName() + "' seqNo=" + masterSequenceNo);
	}
    }

    public WorldResponseType updateWorld(String myWorld, long lastSequenceNo) {
	World world = worlds.get(myWorld);
	WorldResponseType response = new WorldResponseType();
	response.setSequenceNo(masterSequenceNo);
	List<PersonType> changedPeople = response.getPerson();
	System.out.println("World Update Starting: world='" +
			   myWorld + "'");
	if (world != null) {
	    Hashtable<Integer,Person> people = world.getPeople();
	    for (Enumeration e = people.elements() ; e.hasMoreElements() ;) {
		Person person = (Person)e.nextElement();
		if (person.getLastSequenceNo() > lastSequenceNo) {
		    changedPeople.add(person.getPersonData());
		}
	    }
	}
	System.out.println("World Update: world='" +
			   myWorld + "' length = " + 
			   changedPeople.size() + "' seqNo=" + 
			   response.getSequenceNo());
	return response;
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

