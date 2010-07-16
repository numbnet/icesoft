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

/**
 * Remote service implementation used to interact with the world by adding and removing users, getting continents, etc.
 */
@SuppressWarnings("serial")
public class WorldServiceImpl extends RemoteServiceServlet implements WorldService {
	private ICEpushPlaceWorld world = null;
	
	/**
	 * Method to create and setup the ICEpushPlaceWorld object
	 * This is a representation of the backend web service
	 * 
	 * @return the world, or null on error
	 */
	private ICEpushPlaceWorld generateWorld() {
		try{
			// Use Spring to try to get our WEB-INF/applicationContext.xml through the servlet context
			// This will allow us to lookup Spring beans
			WebApplicationContext applicationContext =
				WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

			// Get the ICEpushPlaceWorld bean from the application context, if possible
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
	
	/**
	 * Method to get the current ICEpushPlaceWorld object, and lazily load it if needed
	 * 
	 * @return the world
	 */
	private ICEpushPlaceWorld getWorld() {
		if (world == null) {
			world = generateWorld();
		}
		
		return world;
	}
	
	/**
	 * Method to convert a String region name into an Integer representation the web service can understand
	 * @param region to convert
	 * @return the region as an int
	 */
	private int convertRegionToInt(String region) {
		// Look through all our continents and try to find a valid match
		for (int i = 0; i < ICEpushPlaceWorld.CONTINENT.length; i++) {
			if (region.equals(ICEpushPlaceWorld.CONTINENT[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Method to convert a User object to a PersonType object
	 * This is useful for converting what the client side UI works with into something the web service understands
	 * If the passed User has a key we will also try to associate that with the converted PersonType
	 * 
	 * @param toConvert to change into a PersonType object
	 * @return the constructed PersonType object
	 */
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
	
	/**
	 * Method to convert a PersonType object to a User object
	 * This is useful for converting what the web service returns into something usable by the client side UI
	 * 
	 * @param toConvert to change into a User object
	 * @param region of the person, for use with the new User object
	 * @return the constructed User object
	 */
	private User convertPersonToUser(PersonType toConvert, String region) {
		User toReturn = new User();
		toReturn.setKey(toConvert.getKey());
		toReturn.setName(toConvert.getName());
		toReturn.setMood(toConvert.getMood());
		toReturn.setMind(toConvert.getComment());
		toReturn.setRegion(region);
		
		return toReturn;
	}
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.addUser
	 */
	@Override
	public User addUser(String name, String mood, String mind, String region) throws IllegalArgumentException {
		if (getWorld() != null) {
			// Construct the user object based on the passed values
			User user = new User(name, mood, mind, region);
			
			// Attempt to use the web service to log the user in
			PersonType resultPerson = world.loginPerson(region, convertUserToPerson(user));
			
			if (resultPerson != null) {
				// Store the web service key for future use
				user.setKey(resultPerson.getKey());
				
				System.out.println("INFO - Added user " + user.getName() + " (mood: " + user.getMood() + ") to region " + user.getRegion() + " with key " + user.getKey() + ".");
				
				return user;
			}
		}
		
		System.out.println("ERROR - Failed to add user " + name + " to region " + region + ".");
		
		return null;
	}
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.removeUser
	 */	
	@Override
	public Boolean removeUser(User user) throws IllegalArgumentException {
		if (getWorld() != null) {
			// Ensure we have a valid user to remove
			if ((user != null) && (user.hasKey())) {
				// Attempt to use the web service to log the user out
				world.logoutPerson(user.getRegion(), convertUserToPerson(user));
				
				System.out.println("INFO - Removed user " + user.getName() + " with key " + user.getKey() + ".");
				
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.getUser
	 */	
	@Override
	public User getUser(String name) throws IllegalArgumentException {
		if (getWorld() != null) {
			User toReturn = null;
			
			// Loop through all regions in the world
			// We'll then search in each region for the desired user
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
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.getUserInRegion
	 */
	@Override
	public User getUserInRegion(String name, String region) throws IllegalArgumentException {
		if (getWorld() != null) {
			// Get a list of all users in the passed region
			world.setContinent(convertRegionToInt(region));
			List<PersonType> personList = world.getContinent();
			
			// If the list is valid we'll loop through it and try to find a match for our passed name
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
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.getUsersByRegion
	 */	
	@Override
	public List<User> getUsersByRegion(String region) throws IllegalArgumentException {
		if (getWorld() != null) {
			// Get a list of all the users in the passed region
			world.setContinent(convertRegionToInt(region));
			List<PersonType> personList = world.getContinent();
			
			// If the list is valid we'll want to convert it to a list of Users instead of PersonTypes and return that
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
