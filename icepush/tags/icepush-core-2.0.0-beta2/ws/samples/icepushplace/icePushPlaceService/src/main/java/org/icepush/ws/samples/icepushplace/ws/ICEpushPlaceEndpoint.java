package org.icepush.ws.samples.icepushplace.ws;

import java.util.Hashtable;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;

import org.icepush.ws.samples.icepushplace.service.ICEpushPlaceService;
import org.icepush.ws.samples.icepushplace.*;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

@Endpoint
public class ICEpushPlaceEndpoint {
    private final ICEpushPlaceService icePushPlaceService;
    private ObjectFactory objectFactory;

    public ICEpushPlaceEndpoint(ICEpushPlaceService icePushPlaceService) {
        this.icePushPlaceService = icePushPlaceService;
	this.objectFactory = new ObjectFactory();
    }

    @PayloadRoot(localPart = "appRegisterRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
	public JAXBElement<BigInteger> registerApp(JAXBElement<AppType> requestElement) {
	AppType app = requestElement.getValue();
	Long startingSequenceNo = new Long(icePushPlaceService.registerApp(app));
	return objectFactory.createAppRegisterResponse(new BigInteger(startingSequenceNo.toString()));
    }

    @PayloadRoot(localPart = "personLoginRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
	public JAXBElement<BigInteger> loginPerson(JAXBElement<PersonRequestType> requestElement) {
	PersonRequestType request = requestElement.getValue();
	Integer personKey = icePushPlaceService.
	    loginPerson(request.getWorld(), request.getPerson());
	return objectFactory.createPersonLoginResponse(new BigInteger(personKey.toString()));
    }

    @PayloadRoot(localPart = "personUpdateRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
	public void updatePerson(JAXBElement<PersonRequestType> requestElement) {
	PersonRequestType request = requestElement.getValue();
	icePushPlaceService.updatePerson(request.getWorld(), 
					 request.getPerson());
    }

    @PayloadRoot(localPart = "personLogoutRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
	public void logoutPerson(JAXBElement<PersonRequestType> requestElement) {
	PersonRequestType request = requestElement.getValue();
	icePushPlaceService.logoutPerson(request.getWorld(), 
					 request.getPerson());
    }

    @PayloadRoot(localPart = "worldUpdateRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
	public JAXBElement<WorldResponseType> updateWorld(JAXBElement<WorldRequestType> requestElement) {
	WorldRequestType request = requestElement.getValue();
	WorldResponseType response = icePushPlaceService.
	    updateWorld(request.getApp(), request.getWorld(), 
			request.getLastSequenceNo());
	return objectFactory.createWorldUpdateResponse(response);
    }

}