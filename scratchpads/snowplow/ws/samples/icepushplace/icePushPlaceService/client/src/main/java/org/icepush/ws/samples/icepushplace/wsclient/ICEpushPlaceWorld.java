package org.icepush.ws.samples.icepushplace.wsclient;

import org.icepush.ws.samples.icepushplace.*;

import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ListIterator;
import java.util.Hashtable;
import java.util.Vector;

import org.icepush.ws.samples.icepushplace.*;

public class ICEpushPlaceWorld {

    public static final int AFRICA = 0;
    public static final int ANTARCTICA = 1;
    public static final int ASIA = 2;
    public static final int EUROPE = 3;
    public static final int NORTH_AMERICA = 4;
    public static final int SOUTH_AMERICA = 5;

    public static final String[] CONTINENT = {"Africa","Antarctica","Asia","Europe",
					       "North America","South America"};
    
    private Hashtable<Integer,PersonType>[] continents;
    private String webServiceURL;
    private String applicationURL;
    private ICEpushPlaceWsClient wsClient;
    private boolean registered = false;
    private long currentSequenceNo;
    private int currentContinent;
    private int currentPerson;

    public ICEpushPlaceWorld() {
	continents = (Hashtable<Integer,PersonType>[])Array.newInstance(Hashtable.class, CONTINENT.length);
	for (int i=AFRICA; i<=SOUTH_AMERICA; i++) {
	    continents[i] = new Hashtable();
	}
    }

    public void setWebServiceURL(String URL) {
	webServiceURL = URL;
	if (!registered) {
	    registerWithService();
	}
    }
    public String getWebServiceURL() {
	return webServiceURL;
    }
    public void setApplicationURL(String URL) {
        applicationURL = URL;
	if (!registered) {
	    registerWithService();
	}
    }
    public String getApplicationURL() {
	return applicationURL;
    }
    public void setWsClient(ICEpushPlaceWsClient client) {
        wsClient = client;
	if (!registered) {
	    registerWithService();
	}
    }
    public ICEpushPlaceWsClient getWsClient() {
	return wsClient;
    }
    
    public void setContinent(int continent) {
	currentContinent = continent;
    }

    public List<PersonType> getContinent() {
	getContinentUpdates();
	return (List) new Vector<PersonType>(continents[currentContinent].values());
    }

    public List<PersonType> getContinentUpdates() {
	WorldResponseType response = 
	    wsClient.updateWorld(applicationURL, CONTINENT[currentContinent],
				 currentSequenceNo);
	List<PersonType> people = response.getPerson();
	for (ListIterator e = people.listIterator() ; e.hasNext() ;) {
	    PersonType person = (PersonType)e.next();
	    if (person.getStatus() == ICEpushPlaceWsClient.DELETE) {
		continents[currentContinent].remove(person.getKey());
	    } else {
		continents[currentContinent].put(new Integer(person.getKey()), 
						 person);
	    }
	}
	return people;
    }

    public void setPerson(int personKey) {
	currentPerson = personKey;
    }

    public PersonType getPerson() {
	return (PersonType) continents[currentContinent].get(currentPerson);
    }


    public PersonType loginPerson(String continent, PersonType person) {
	try {
	   person = wsClient.loginPerson(continent, person);
	} catch (BadWorldException e) {
	    System.out.println("Oops: Bad World" + e.toString());
	}
	return person;
    }

    public void logoutPerson(String continent, PersonType person) {
	wsClient.logoutPerson(continent, person);
    }

    public void updatePerson(String continent, PersonType person) {
	wsClient.updatePerson(continent, person);
    }

    public PersonType loginPerson(int continent, PersonType person) {
	return loginPerson(CONTINENT[continent], person);
    }

    public void logoutPerson(int continent, PersonType person) {
	logoutPerson(CONTINENT[continent], person);
    }

    public void updatePerson(int continent, PersonType person) {
	updatePerson(CONTINENT[continent], person);
    }

    public PersonType movePerson(String from, String to, PersonType person) {
	logoutPerson(from, person);
	try {
	   person = wsClient.loginPerson(to, person);
	} catch (BadWorldException e) {
	    System.out.println("Oops: Bad World" + e.toString());
	}
	return person;
    }

    public PersonType movePerson(int from, int to, PersonType person) {
	return movePerson(CONTINENT[from], CONTINENT[to], person);
    }

    private void registerWithService() {
	if (wsClient != null && webServiceURL != null & applicationURL !=null) {
	    List<String> allContinents =  Arrays.asList(CONTINENT);
	    currentSequenceNo = wsClient.registerApp(applicationURL, allContinents);
	    registered = true;
	}
    }
}
