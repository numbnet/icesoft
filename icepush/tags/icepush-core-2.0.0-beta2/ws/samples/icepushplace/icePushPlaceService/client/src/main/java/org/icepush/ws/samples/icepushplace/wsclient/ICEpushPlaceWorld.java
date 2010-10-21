package org.icepush.ws.samples.icepushplace.wsclient;

import org.icepush.ws.samples.icepushplace.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ByteArrayResource;

import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ListIterator;
import java.util.Hashtable;
import java.util.Vector;

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
    private long[] currentSequenceNo;
    private int continent;
    private int currentPerson;

    public ICEpushPlaceWorld() {

	continents = (Hashtable<Integer,PersonType>[])Array.newInstance(Hashtable.class, CONTINENT.length);
	currentSequenceNo = new long[CONTINENT.length];

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

    public ICEpushPlaceWsClient getWsClient() {
	return wsClient;
    }

    public List<PersonType> getAfrica() {
	return getContinent(AFRICA);
    }

    public List<PersonType> getAntarctica() {
	return getContinent(ANTARCTICA);
    }

    public List<PersonType> getAsia() {
	return getContinent(ASIA);
    }

    public List<PersonType> getEurope() {
	return getContinent(EUROPE);
    }

    public List<PersonType> getNorthAmerica() {
	return getContinent(NORTH_AMERICA);
    }

    public List<PersonType> getSouthAmerica() {
	return getContinent(SOUTH_AMERICA);
    }

    public List<PersonType> getContinent(int continent) {
	getContinentUpdates(continent);
	return (List) new Vector<PersonType>(continents[continent].values());
    }

    public List<PersonType> getContinentUpdates(int continent) {
	WorldResponseType response = 
	    wsClient.updateWorld(applicationURL, CONTINENT[continent],
				 currentSequenceNo[continent]);
	List<PersonType> people = response.getPerson();
	currentSequenceNo[continent] = response.getSequenceNo();
	for (ListIterator e = people.listIterator() ; e.hasNext() ;) {
	    PersonType person = (PersonType)e.next();
	    if (person.getStatus() == ICEpushPlaceWsClient.DELETE) {
		continents[continent].remove(person.getKey());
	    } else {
		continents[continent].put(new Integer(person.getKey()), 
					  person);
	    }
	}
	return people;
    }

    public PersonType loginPerson(String continent, PersonType person) {
	setProperRegion(person, continent);
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
	setProperRegion(person, to);
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
	if (webServiceURL != null & applicationURL !=null) {
	    String appContext = new String (
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd\">\n" +
"    <bean id=\"client\" abstract=\"true\">" +
"        <property name=\"defaultUri\" value=\"" + webServiceURL + "\"/>\n" +
"    </bean>\n" +
"    <bean id=\"icepushPlaceClient\" class=\"org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWsClient\" parent=\"client\">\n" +
"        <constructor-arg>\n" +
"            <bean class=\"org.springframework.ws.soap.saaj.SaajSoapMessageFactory\"/>\n" +
"        </constructor-arg>\n" +
"        <property name=\"marshaller\" ref=\"marshaller\"/>\n" +
"        <property name=\"unmarshaller\" ref=\"marshaller\"/>\n" +
"    </bean>\n" +
"    <bean id=\"marshaller\" class=\"org.springframework.oxm.jaxb.Jaxb2Marshaller\">\n" +
"        <property name=\"contextPath\" value=\"org.icepush.ws.samples.icepushplace\"/>\n" +
"        <property name=\"mtomEnabled\" value=\"true\"/>\n" +
"    </bean>\n" +
"</beans>\n");
	    ByteArrayResource bytes = new ByteArrayResource(appContext.getBytes());
	    XmlBeanFactory factory = new XmlBeanFactory(bytes);
	    wsClient =  (ICEpushPlaceWsClient) factory.getBean("icepushPlaceClient");

	    List<String> allContinents =  Arrays.asList(CONTINENT);
	    long startingSequenceNo = wsClient.registerApp(applicationURL, 
							   allContinents);
	    for (int i=AFRICA; i<=SOUTH_AMERICA; i++) {
		currentSequenceNo[i] = startingSequenceNo;
	    }
	    registered = true;
	}
    }

    private void setProperRegion(PersonType person, String continent) {
	if (!CONTINENT[person.getRegion()].equals(continent)) {
	    person.setRegion(findContinent(continent));
	}
    }

    private int findContinent(String continent) {
	int i = AFRICA;
	while (i <= SOUTH_AMERICA) {
	    if (CONTINENT[i].equals(continent)) {
		return i;
	    } else {
		i++;
	    }
	}
	System.out.println("Cound not find Continent " + continent);
	return -1;
    }
}
