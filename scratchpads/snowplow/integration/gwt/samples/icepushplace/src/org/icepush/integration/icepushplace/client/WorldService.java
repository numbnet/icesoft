package org.icepush.integration.icepushplace.client;

import java.util.List;

import org.icepush.integration.icepushplace.client.model.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Remote service used to interact with the world by adding and removing users, getting continents, etc.
 */
@RemoteServiceRelativePath("world")
public interface WorldService extends RemoteService {
	// Hardcoded urls used when creating an ICEpushPlaceWorld object
	public static final String APPLICATION_URL = "http://localhost:18080/icepush-place-gwt";
	public static final String WEBSERVICE_URL = "http://localhost:18080/icePushPlaceService";
	// All available moods
	public static final String[] MOODS = {"average",
										  "shocked",
										  "angry",
										  "happy",
										  "sad"};
	// The prefix location of where the mood images are stored
    public static final String MOOD_IMAGE_PREFIX = "images/mood-";
    // The suffix to mood images, in this case a file extension
    public static final String MOOD_IMAGE_SUFFIX = ".png";
    
    /**
     * Method to construct and add a user to the world
     * The created user will be returned
     * 
     * @param name of the user to create
     * @param mood of the user to create
     * @param mind of the user to create
     * @param region to add the user to
     * @return User created as part of the add
     * @throws IllegalArgumentException on error
     */
	public User addUser(String name, String mood, String mind, String region) throws IllegalArgumentException;
	
	/**
	 * Method to remove an existing user from the world
	 * This will use the region the user object currently has (ie: user.getRegion)
	 * 
	 * @param user to remove
	 * @return true on successful removal, false otherwise
	 * @throws IllegalArgumentException on error
	 */
	public Boolean removeUser(User user) throws IllegalArgumentException;
	
	/**
	 * Method to find a user in any part of the world based on their name
	 * 
	 * @param name of the user to find
	 * @return User object that was found or null if not found
	 * @throws IllegalArgumentException on error
	 */
	public User getUser(String name) throws IllegalArgumentException;
	
	/**
	 * Method to find a user in a single region / continent in the world based on their name
	 * 
	 * @param name of the user to find
	 * @param region the user should be looked for in
	 * @return User object that was found or null if not found
	 * @throws IllegalArgumentException on error
	 */
	public User getUserInRegion(String name, String region) throws IllegalArgumentException;
	
	/**
	 * Method to get all users by a single region or continent
	 * 
	 * @param region to get users from
	 * @return list of the users in that region or null if none found
	 * @throws IllegalArgumentException
	 */
	public List<User> getUsersByRegion(String region) throws IllegalArgumentException;
	
	/**
	 * Method to get the names of all regions
	 * 
	 * @return the list of region names
	 * @throws IllegalArgumentException
	 */
	public String[] getAllRegions()  throws IllegalArgumentException;
}
