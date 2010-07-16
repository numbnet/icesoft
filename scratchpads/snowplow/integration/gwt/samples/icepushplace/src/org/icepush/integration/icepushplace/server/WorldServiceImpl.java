package org.icepush.integration.icepushplace.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
	private HashMap<String,List<User>> regionMap = generateRegionMap();
	private ICEpushPlaceWorld world = null;
	
	private ICEpushPlaceWorld generateWorld() {
        // Temporary to test out connecting to the web service
		try{
			WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			if (applicationContext != null) {
				return (ICEpushPlaceWorld)applicationContext.getBean("icepushPlaceWorld", ICEpushPlaceWorld.class);
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
	
	private HashMap<String,List<User>> generateRegionMap() {
		HashMap<String,List<User>> toReturn =
				new HashMap<String,List<User>>(WorldService.REGIONS.length);
		
		for (String currentRegion : WorldService.REGIONS) {
			toReturn.put(currentRegion, new ArrayList<User>(0));
		}
		
		return toReturn;
	}
	
	private int convertRegionToInt(String region) {
		for (int i = 0; i < ICEpushPlaceWorld.CONTINENT.length; i++) {
			if (region.equals(ICEpushPlaceWorld.CONTINENT[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
	private PersonType convertUserToPerson(User toConvert) {
		PersonType toReturn = new PersonType();
		toReturn.setName(toConvert.getName());
		toReturn.setMood(toConvert.getMood());
		toReturn.setComment(toConvert.getMind());
		toReturn.setTechnology("GWT");
		
		return toReturn;
	}
	
	private User convertPersonToUser(PersonType toConvert, String region) {
		User toReturn = new User();
		toReturn.setName(toConvert.getName());
		toReturn.setMood(toConvert.getMood());
		toReturn.setMind(toConvert.getComment());
		toReturn.setRegion(region);
		
		return toReturn;
	}
	
	@Override
	public Boolean addUser(User user) throws IllegalArgumentException {
		if (getWorld() != null) {
			world.loginPerson(user.getRegion(), convertUserToPerson(user));
			
			System.out.println("INFO - Added user " + user.getName() + " (mood: " + user.getMood() + ") to region " + user.getRegion() + ".");
			
			return Boolean.TRUE;
		}
		
		System.out.println("ERROR - Failed to add user " + user.getName() + " to region " + user.getRegion() + ".");
		
		return Boolean.FALSE;
		
		/*
		List<User> userList = getUsersByRegion(user.getRegion());
		
		if (userList != null) {
			userList.add(user);
            
			System.out.println("INFO - Added user " + user.getName() + " (mood: " + user.getMood() + ") to region " + user.getRegion() + ".");
			
			return Boolean.TRUE;
		}
		
		System.out.println("ERROR - Failed to add user " + user.getName() + " to region " + user.getRegion() + ".");
		
		return Boolean.FALSE;
		*/
	}
	
	@Override
	public Boolean removeUser(String name) throws IllegalArgumentException {
		if (ValidatorUtil.isValidString(name)) {
			Collection<List<User>> allUsers = regionMap.values();
			
			for (List<User> currentUserList : allUsers) {
				if (ValidatorUtil.isValidList(currentUserList)) {
					for (User currentUser : currentUserList) {
						if (name.equals(currentUser.getName())) {
							System.out.println("INFO - Removed user " + name + ".");
							
							return currentUserList.remove(currentUser);
						}
					}
				}
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
