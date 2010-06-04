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
	public void registerApp(JAXBElement<AppType> requestElement) {
	AppType app = requestElement.getValue();
        icePushPlaceService.registerApp(app);
    }

    @PayloadRoot(localPart = "personLoginRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
	public JAXBElement<PersonKeyType> loginPerson(JAXBElement<PersonRequestType> requestElement) {
	PersonRequestType request = requestElement.getValue();
        BigInteger key = icePushPlaceService.loginPerson(request.getWorld(), request.getPerson());
	PersonKeyType personKey = new PersonKeyType();
	personKey.setKey(key);
	return objectFactory.createPersonLoginResponse(personKey);
    }
    /*
    @PayloadRoot(localPart = "personLogoutRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
    public void logoutPerson(personLogoutRequest request) {
        icePushPlaceService.logoutPerson(request.getWorld(), request.getPerson());
    }

    @PayloadRoot(localPart = "personUpdateRequest", namespace = "http://www.icepush.org/ws/samples/icepushplace")
    public void updatePerson(personLoginRequest request) {
        icePushPlaceService.updatePerson(request.getWorld(), request.getPerson());
	}*/

}