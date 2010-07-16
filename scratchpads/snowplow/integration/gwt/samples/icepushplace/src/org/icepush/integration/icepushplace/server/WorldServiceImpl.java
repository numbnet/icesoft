package org.icepush.integration.icepushplace.server;

import java.util.ArrayList;
import java.util.List;

import org.icepush.integration.icepushplace.client.WorldService;
import org.icepush.integration.icepushplace.client.model.User;
import org.icepush.integration.icepushplace.shared.ValidatorUtil;
import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class WorldServiceImpl extends RemoteServiceServlet implements WorldService {
	private ICEpushPlaceWorld world = null;
	
	private ICEpushPlaceWorld generateWorld() {
		try{
			WebApplicationContext applicationContext =
				WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			
			if (applicationContext != null) {
				return (ICEpushPlaceWorld)applicationContext.getBean(
							"icepushPlaceWorld", ICEpushPlaceWorld.class);
			}
		}catch (Exception failedTest) {
			failedTest.printStackTrace();
			System.err.println("Failed to create an ICEpushPlaceWorld service object.");
		}
		
		return null;
	}
	
	private ICEpushPlaceWorld getWorld() {
		if (world == null) {
			world = generateWorld();
		}
		
		return world;
	}
	
	private int convertRegionToInt(String region) {
		for (int i = 0; i < ICEpushPlaceWorld.CONTINENT.length; i++) {
			if (region.equals(ICEpushPlaceWorld.CONTINENT[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
	private User buildUser(String name, String mood, String mind, String region) {
		return new User(name, mood, mind, region);
	}
	
	private PersonType convertUserToPerson(User toConvert) {
		PersonType toReturn = new PersonType();
		if (toConvert.hasKey()) {
			toReturn.setKey(toConvert.getKey());
		}
		toReturn.setName(toConvert.getName());
		toReturn.setMood(toConvert.getMood());
		toReturn.setComment(toConvert.getMind());
		toReturn.setTechnology("GWT");
		
		return toReturn;
	}
	
	private User convertPersonToUser(PersonType toConvert, String region) {
		User toReturn = new User();
		toReturn.setKey(toConvert.getKey());
		toReturn.setName(toConvert.getName());
		toReturn.setMood(toConvert.getMood());
		toReturn.setMind(toConvert.getComment());
		toReturn.setRegion(region);
		
		return toReturn;
	}
	
	@Override
	public User addUser(String name, String mood, String mind, String region) throws IllegalArgumentException {
		if (getWorld() != null) {
			User user = buildUser(name, mood, mind, region);
			PersonType resultPerson = world.loginPerson(region, convertUserToPerson(user));
			
			if (resultPerson != null) {
				user.setKey(resultPerson.getKey());
				
				System.out.println("INFO - Added user " + user.getName() + " (mood: " + user.getMood() + ") to region " + user.getRegion() + " with key " + user.getKey() + ".");
				
				return user;
			}
		}
		
		System.out.println("ERROR - Failed to add user " + name + " to region " + region + ".");
		
		return null;
	}
	
	@Override
	public Boolean removeUser(User user) throws IllegalArgumentException {
		if (getWorld() != null) {
			if ((user != null) && (user.hasKey())) {
				world.logoutPerson(user.getRegion(), convertUserToPerson(user));
				
				System.out.println("INFO - Removed user " + user.getName() + " with key " + user.getKey() + ".");
				
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	@Override
	public User getUser(String name) throws IllegalArgumentException {
		if (getWorld() != null) {
			User toReturn = null;
			
			for (String currentRegion : WorldService.REGIONS) {
				toReturn = getUserInRegion(name, currentRegion);
				
				if (toReturn != null) {
					System.out.println("INFO - Get user " + name + " found in region " + currentRegion + ".");
					
					return toReturn;
				}
			}
		}
		
		System.out.println("WARN - Failed to get user " + name + " from any region.");
		
		return null;
	}
	
	@Override
	public User getUserInRegion(String name, String region) throws IllegalArgumentException {
		if (getWorld() != null) {
			world.setContinent(convertRegionToInt(region));
			List<PersonType> personList = world.getContinent();
			
			if (ValidatorUtil.isValidList(personList)) {
				for (PersonType currentPerson : personList) {
					if (name.equals(currentPerson.getName())) {
						System.out.println("INFO - Get user " + name + " from region " + region + " was found.");
						
						return convertPersonToUser(currentPerson, region);
					}
				}
			}
		}
		
		System.out.println("WARN - Get user " + name + " from region " + region + " was not found.");
		
		return null;
	}
	
	@Override
	public List<User> getUsersByRegion(String region) throws IllegalArgumentException {
		if (getWorld() != null) {
			world.setContinent(convertRegionToInt(region));
			List<PersonType> personList = world.getContinent();
			
			if (ValidatorUtil.isValidList(personList)) {
				List<User> toReturn = new ArrayList<User>(personList.size());
				
				for (PersonType currentPerson : personList) {
					toReturn.add(convertPersonToUser(currentPerson, region));
				}
				
				return toReturn;
			}
		}
		
		return null;
	}
}
