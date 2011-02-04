package org.icepush.ws.samples.icepushplace.service;

import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Enumeration;

import org.icepush.ws.samples.icepushplace.*;
import org.icepush.PushClient;
import org.icepush.client.PushClientException;

public class ICEpushPlaceService {

    public static final int ADD = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;

    private Hashtable<String,Application> apps;
    private Hashtable<String,World> worlds;
    private long masterSequenceNo;
    private Application oldestApp;

    public ICEpushPlaceService() {
	apps = new Hashtable<String,Application>();
	worlds = new Hashtable<String,World>();
	masterSequenceNo = Long.MIN_VALUE;
    }

    public long registerApp(AppType app) {
	Application application = apps.get(app.getURL());
	if (application == null) {
	    application = new Application(masterSequenceNo, app.getWorld(),
					  app.getURL());
	    apps.put(app.getURL(), application);
	    System.out.println("Registered Application: " + app.getURL());
	}

	for (Iterator i=application.getWorlds().descendingIterator(); i.hasNext();) {
	    String newWorld = (String)i.next();
	    World world = worlds.get(newWorld);
	    if (world == null) {
		world = new World(newWorld);
		worlds.put(newWorld, world);
		System.out.println("New World: " + newWorld);
	    }
	    world.addApp(application);
	}
	return Long.MIN_VALUE;
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
	    person.setKey(key);
	    Person newPerson = new Person(person);
	    newPerson.setLastSequenceNo(++masterSequenceNo);
	    world.getPeople().put(new Integer(key), newPerson);
	    System.out.println("Logged In: World='" +
			       myWorld + "' Name='" + 
			       person.getName() + "' key='" + key + 
			       "' seqNo=" + masterSequenceNo);
	    notifyApps(myWorld);
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
		notifyApps(myWorld);
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
		notifyApps(myWorld);
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

    public WorldResponseType updateWorld(String myApp, String myWorld, long lastSequenceNo) {
	World world = worlds.get(myWorld);
	WorldResponseType response = new WorldResponseType();
	response.setSequenceNo(masterSequenceNo);
	response.setWorld(myWorld);
	List<PersonType> changedPeople = response.getPerson();

	Application app = apps.get(myApp);
	if (world != null && app != null) {

	    // Find tardiest app;
	    app.setLastSequenceNo(masterSequenceNo);
	    long oldestUpdate = world.getOldestApp();

	    // Build response;
	    Hashtable<Integer,Person> people = world.getPeople();
	    for (Enumeration<Person> ee = people.elements() ; ee.hasMoreElements() ;) {
		Person person = ee.nextElement();
		if (person.getLastSequenceNo() > lastSequenceNo) {
		    changedPeople.add(person.getPersonData());
		} else if (person.getLastSequenceNo() < oldestUpdate &&
			   person.getPersonData().getStatus() == DELETE) {
		    // Person is obsolete;
		    /*
		    // TEMPORARY COMMENT OUT TO FIX SINGLE TAB DUPLICATION ISSUE
		    people.remove(person.getPersonData().getKey());
		    */
		    System.out.println("OBSOLETE " + person.getPersonData().getName() + " FOR " + myApp);
		    /*
		    System.out.println("Removing obsolete person: " +
				       person.getPersonData().getName() + 
				       " key = " + person.getPersonData().getKey());
            */
		}
	    }
	}
	System.out.println("World Update: world='" +
			   myWorld + "' length = " + 
			   changedPeople.size() + "' seqNos=" + 
			   response.getSequenceNo() + "," + lastSequenceNo);
	return response;
    }

    private void notifyApps(String myWorld) {
	for (Enumeration<Application> ee = apps.elements() ; ee.hasMoreElements() ;) {
	    Application app = ee.nextElement();
	    if (app.containsWorld(myWorld)) {
		try {
		    app.getPushClient().notify(myWorld);
		} catch (PushClientException e) {
		    System.out.println("Could not notify " + app.getURL() +
				       " : " + e.toString());
		}
		System.out.println("Notifying: " + app.getURL() + " World: " +
				   myWorld);
	    }
	}
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

    private class Application {
	private long lastSequenceNo;
	private TreeSet<String> worlds;
	private PushClient pushClient;
	private String url;

	public Application(long startingSequenceNo, List<String> newWorlds,
			   String appURL) {
	    url = appURL;
	    worlds = new TreeSet<String>();
	    addWorlds(newWorlds);
	    lastSequenceNo = startingSequenceNo;
	    try {
	    	pushClient = new PushClient(appURL);
	    } catch (Exception e) {
	    	System.out.println("Could not establish PushClient to: " + url 
				  + " : " + e.toString());
	    }
	}

	public long getLastSequenceNo() {
	    return lastSequenceNo;
	}
	public void setLastSequenceNo(long lastNo) {
	    lastSequenceNo = lastNo;
	}
	public TreeSet getWorlds() {
	    return worlds;
	}
	public void addWorlds(List<String> newWorlds) {
	    for (ListIterator e = newWorlds.listIterator() ; e.hasNext() ;) {
		worlds.add((String)e.next());
	    }
	}
	public PushClient getPushClient() {
	    return pushClient;
	}
	public String getURL() {
	    return url;
	}
	public boolean containsWorld(String world) {
	    return worlds.contains(world);
	}
    }	

    private class ApplicationComparator implements Comparator{

	public int compare(Object firstApp, Object secondApp) {
	    Application app1 = (Application) firstApp;
	    Application app2 = (Application) secondApp;
	    if (app1.getLastSequenceNo() < app2.getLastSequenceNo()) {
		return -1;
	    } else if (app1.getLastSequenceNo() > app2.getLastSequenceNo()) {
		return 1;
	    }
	    return 0;
	}

	public boolean equals(Object obj) {
	    return this.equals(obj);
	}
    }	

    private class World {
	private final String name;
	private final Hashtable<Integer,Person> people;
	private TreeSet apps;

	public World(String name) {
	    this.name = name;
	    people = new Hashtable<Integer,Person>();
	    apps = new TreeSet(new ApplicationComparator());
	}

	public String getName() {
	    return name;
	}

	public Hashtable<Integer,Person> getPeople() {
	    return people;
	}

	public void addApp(Application app) {
	    apps.add(app);
	}

	public long getOldestApp() {
	    return ((Application)apps.first()).getLastSequenceNo();
	}
    }	
}

