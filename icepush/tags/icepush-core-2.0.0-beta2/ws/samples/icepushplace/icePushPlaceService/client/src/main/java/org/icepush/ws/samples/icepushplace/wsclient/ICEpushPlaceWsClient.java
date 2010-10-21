package org.icepush.ws.samples.icepushplace.wsclient;

import org.icepush.ws.samples.icepushplace.*;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
import java.util.List;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public class ICEpushPlaceWsClient extends WebServiceGatewaySupport {

    public static final int ADD = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;

    private ObjectFactory objectFactory = new ObjectFactory();

    public ICEpushPlaceWsClient(SaajSoapMessageFactory messageFactory) {
        super(messageFactory);
    }

    public long registerApp(String url, List<String> newWorlds ) throws SoapFaultClientException {
	AppType request = new AppType();
	request.setURL(url);
	List<String> oldWorlds = request.getWorld();
	oldWorlds.addAll(newWorlds);
        JAXBElement<AppType> registerAppRequest = objectFactory.createAppRegisterRequest(request);
	System.out.println("Register App request: URL = " + request.getURL() +
			   " Size = " + request.getWorld().size() +
			   " First = " + request.getWorld().get(0));
        JAXBElement<BigInteger> startingSequenceNo =
            (JAXBElement<BigInteger>) getWebServiceTemplate().marshalSendAndReceive(registerAppRequest);
	return startingSequenceNo.getValue().longValue();
    }
    public PersonType loginPerson(String world, PersonType person) throws SoapFaultClientException, BadWorldException {
	PersonRequestType request = new PersonRequestType();
	request.setWorld(world);
	request.setPerson(person);
	
        JAXBElement<PersonRequestType> loginPersonRequest = objectFactory.createPersonLoginRequest(request);
        JAXBElement<BigInteger> personKey = 
	    (JAXBElement<BigInteger>) getWebServiceTemplate().
	    marshalSendAndReceive(loginPersonRequest);
	/* Check for a zero key */
	person.setKey(personKey.getValue().intValue());
	if (person.getKey() == 0) {
	    throw new BadWorldException("Invalid World: " + world);
	}
	return person;
    }

    public void updatePerson(String world, PersonType person) throws SoapFaultClientException {
	PersonRequestType request = new PersonRequestType();
	request.setWorld(world);
	request.setPerson(person);
	
        JAXBElement<PersonRequestType> updatePersonRequest = objectFactory.createPersonUpdateRequest(request);
	getWebServiceTemplate().marshalSendAndReceive(updatePersonRequest);
    }

    public void logoutPerson(String world, PersonType person) throws SoapFaultClientException {
	PersonRequestType request = new PersonRequestType();
	request.setWorld(world);
	request.setPerson(person);
	
        JAXBElement<PersonRequestType> logoutPersonRequest = objectFactory.createPersonLogoutRequest(request);
	getWebServiceTemplate().marshalSendAndReceive(logoutPersonRequest);
    }

    public WorldResponseType updateWorld(String URL, String world, long lastSequenceNo) {
	WorldRequestType request = new WorldRequestType();
	request.setWorld(world);
	request.setApp(URL);
	request.setLastSequenceNo(lastSequenceNo);

        JAXBElement<WorldRequestType> updateWorldRequest = objectFactory.createWorldUpdateRequest(request);
	JAXBElement<WorldResponseType> response = (JAXBElement<WorldResponseType>) getWebServiceTemplate().marshalSendAndReceive(updateWorldRequest);
	return response.getValue();
    }
}
