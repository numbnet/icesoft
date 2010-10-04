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
    public static final String APPLICATION_URL = "${replace.applicationURL}";
    public static final String WEBSERVICE_URL = "${replace.webserviceURL}";
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
    public static final String OUR_TECHNOLOGY = "GWT";

    /**
     * Method to construct and add a user to the world
     * The created user will be returned
     *
     * @param name of the user to create
     * @param mood of the user to create
     * @param mind of the user to create
     * @param region to add the user to
     * @param message to send
     * @return User created as part of the add
     * @throws IllegalArgumentException on error
     */
    public User addUser(String name, String mood, String mind, String region, String message) throws IllegalArgumentException;
    
    /**
     * Method to update an existing user, such as changing their name or mood
     * 
     * @param user to update
     * @return true on successful update, false otherwise
     * @throws IllegalArgumentException on error
     */
    public Boolean updateUser(User user) throws IllegalArgumentException;
    
    /**
     * Method to intelligently update a user. This means we'll check if their continent changed,
     *  if it did we'll perform a moveUser
     * Regardless of whether we move, we'll also perform an updateUser
     * This method saves the user from having to determine this logic themselves
     * 
     * @param needUpdate if we need to update as well as try to move
     * @param oldRegion the region the user last came from
     * @param user to intelligently update and/or move
     * @return User modified as part of the update
     * @throws IllegalArgumentException on error
     */
    public User smartUpdateUser(boolean needUpdate, String oldRegion, User user) throws IllegalArgumentException;
    
    /**
     * Method to move a user from one continent to another
     * 
     * @param oldRegion continent to leave
     * @param user to move
     * @return User modified as part of the move
     * @throws IllegalArgumentException on error
     */
    public User moveUser(String oldRegion, User user) throws IllegalArgumentException;

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
