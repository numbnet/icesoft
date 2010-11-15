package org.icepush.integration.icepushplace.service;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icepush.integration.icepushplace.util.FacesUtil;
import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;

@ManagedBean(name="personServiceImpl")
@ApplicationScoped
public class PersonServiceImpl implements PersonService {
	private ICEpushPlaceWorld world = initializeWorld();
	
	protected ICEpushPlaceWorld initializeWorld() {
		ICEpushPlaceWorld toReturn = new ICEpushPlaceWorld();
		
		toReturn.setApplicationURL(FacesUtil.getFacesParameter("applicationURL"));
		toReturn.setWebServiceURL(FacesUtil.getFacesParameter("webServiceURL"));
		
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
	
	public String convertContinent(int continent) {
		return ICEpushPlaceWorld.CONTINENT[continent];
	}
	
	public int convertContinent(String continent) {
		for (int i = 0; i < ICEpushPlaceWorld.CONTINENT.length; i++) {
			if (continent.equals(ICEpushPlaceWorld.CONTINENT[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
	public String[] getContinents() {
		return ICEpushPlaceWorld.CONTINENT;
	}
	
	public PersonType loginPerson(PersonType person) {
		if ((getWorld() != null) && (person != null)) {
			return world.loginPerson(person.getRegion(), person);
		}
		
		return null;
	}
	
	public void updatePerson(PersonType person) {
		if ((getWorld() != null) && (person != null)) {
			world.updatePerson(person.getRegion(), person);
		}
	}
	
	public PersonType movePerson(int fromRegion, PersonType person) {
		if ((getWorld() != null) && (person != null)) {
			return world.movePerson(fromRegion, person.getRegion(), person);
		}
		
		return null;
	}
}
