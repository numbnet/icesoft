package org.icepush.integration.icepushplace.service;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;

@ManagedBean(name="personServiceImpl")
@ApplicationScoped
public class PersonServiceImpl implements PersonService {
	private ICEpushPlaceWorld world = initializeWorld();
	
	protected ICEpushPlaceWorld initializeWorld() {
		ICEpushPlaceWorld toReturn = new ICEpushPlaceWorld();
		
		toReturn.setApplicationURL("http://localhost:8080/icepushplace-icefaces2");
		toReturn.setWebServiceURL("http://localhost:8080/icePushPlaceService");
		
		return toReturn;
	}
	
	public ICEpushPlaceWorld getWorld() {
		if (world == null) {
			initializeWorld();
		}
		return world;
	}
	
	public void setWorld(ICEpushPlaceWorld world) {
		this.world = world;
	}
	
	public PersonType loginPerson(PersonType person) {
		if ((getWorld() != null) && (person != null)) {
			return world.loginPerson(person.getRegion(), person);
		}
		
		return null;
	}

}
