/**
 *
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
*/
package org.icepush.integration.icepushplace.server;

import java.util.ArrayList;
import java.util.List;

import org.icepush.integration.icepushplace.client.WorldService;
import org.icepush.integration.icepushplace.client.model.User;
import org.icepush.integration.icepushplace.shared.ValidatorUtil;
import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Remote service implementation used to interact with the world by adding and removing users, getting continents, etc.
 */
@SuppressWarnings("serial")
public class WorldServiceImpl extends RemoteServiceServlet implements WorldService {
	public static final String[] REGIONS = ICEpushPlaceWorld.CONTINENT;
	
	private ICEpushPlaceWorld world = null;
	
	/**
	 * Method to create and setup the ICEpushPlaceWorld object
	 * This is a representation of the backend web service
	 * 
	 * @return the world, or null on error
	 */
	private ICEpushPlaceWorld generateWorld() {
		try{
			ICEpushPlaceWorld world = new ICEpushPlaceWorld();
			
			world.setApplicationURL(WorldService.APPLICATION_URL);
			world.setWebServiceURL(WorldService.WEBSERVICE_URL);
			
			return world;
		}catch (Exception failedTest) {
			failedTest.printStackTrace();
			System.out.println("ERROR - Failed to create an ICEpushPlaceWorld service object.");
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
		toReturn.setMessageIn(toConvert.getMessage());
		toReturn.setTechnology(WorldService.OUR_TECHNOLOGY);
		
		return toReturn;
	}
	
	private User convertPersonToUser(PersonType toConvert) {
		return convertPersonToUser(toConvert, ICEpushPlaceWorld.CONTINENT[toConvert.getRegion()]);
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
		toReturn.setMessage(toConvert.getMessageIn());
		toReturn.setTechnology(toConvert.getTechnology());
		
		return toReturn;
	}
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.addUser
	 */
	@Override
	public User addUser(String name, String mood, String mind, String region, String message) throws IllegalArgumentException {
		if (getWorld() != null) {
			// Construct the user object based on the passed values
			User user = new User(name, mood, mind, region, message, WorldService.OUR_TECHNOLOGY);
			
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
	 * @see org.icepush.integration.icepushplace.client.WorldService.updateUser
	 */
	@Override
	public Boolean updateUser(User user) throws IllegalArgumentException {
		if (getWorld() != null) {
			if (user != null) {
				world.updatePerson(user.getRegion(), convertUserToPerson(user));
				
				System.out.println("INFO - Updated user " + user.getName() + " on continent " + user.getRegion() + ".");
				
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.smartUpdateUser
	 */
	@Override
	public User smartUpdateUser(boolean needUpdate, String oldRegion, User user) throws IllegalArgumentException {
		if (user != null) {
		    Boolean returnStatus = Boolean.TRUE;
		    
		    // Check if we need to perform an update call
		    if (needUpdate) {
		        // Use the old region instead of any changed value
		        // This will ensure we update where the user CURRENTLY is (ie: pre move), instead of where they WILL be (ie: post move)
                String newRegion = user.getRegion();
                user.setRegion(oldRegion);
                returnStatus = updateUser(user);
                user.setRegion(newRegion);
            }
			
			if (returnStatus) {
			    // If the region has changed we'll want to move the (potentially updated) user to their new continent
				if ((oldRegion != null) && (!oldRegion.equals(user.getRegion()))) {
					return moveUser(oldRegion, user);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.moveUser
	 */
	@Override
	public User moveUser(String oldRegion, User user) throws IllegalArgumentException {
		if (getWorld() != null) {
			if (user != null) {
			    String newRegion = user.getRegion();
			    user.setRegion(oldRegion);
				PersonType returnPerson = world.movePerson(user.getRegion(), newRegion, convertUserToPerson(user));
				
				System.out.println("INFO - Moved user " + user.getName() + " from " + oldRegion + " to " + newRegion + ".");
				
				return convertPersonToUser(returnPerson);
			}
		}
		
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
			for (String currentRegion : WorldServiceImpl.REGIONS) {
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
			List<PersonType> personList = world.getContinent(convertRegionToInt(region));
			
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
			List<PersonType> personList = world.getContinent(convertRegionToInt(region));
			
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

	/**
	 * @see org.icepush.integration.icepushplace.client.WorldService.getAllRegions
	 */
	@Override
	public String[] getAllRegions() throws IllegalArgumentException {
		return REGIONS;
	}
}
