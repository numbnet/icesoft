package org.icepush.integration.icepushplace.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.icepush.integration.icepushplace.client.WorldService;
import org.icepush.integration.icepushplace.client.model.User;
import org.icepush.integration.icepushplace.shared.ValidatorUtil;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class WorldServiceImpl extends RemoteServiceServlet implements WorldService {
	private HashMap<String,List<User>> regionMap = generateRegionMap();
	
	private HashMap<String,List<User>> generateRegionMap() {
		HashMap<String,List<User>> toReturn =
				new HashMap<String,List<User>>(WorldService.REGIONS.length);
		
		for (String currentRegion : WorldService.REGIONS) {
			toReturn.put(currentRegion, new ArrayList<User>(0));
		}
		
		return toReturn;
	}
	
	@Override
	public Boolean addUser(User user) throws IllegalArgumentException {
		List<User> userList = getUsersByRegion(user.getRegion());
		
		if (userList != null) {
			userList.add(user);
			
			System.out.println("INFO - Added user " + user.getName() + " (mood: " + user.getMood() + ") to region " + user.getRegion() + ".");
			
			return Boolean.TRUE;
		}
		
		System.out.println("ERROR - Failed to add user " + user.getName() + " to region " + user.getRegion() + ".");
		
		return Boolean.FALSE;
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
		User toReturn = null;
		
		for (String currentRegion : WorldService.REGIONS) {
			toReturn = getUserInRegion(name, currentRegion);
			
			if (toReturn != null) {
				System.out.println("INFO - Get user " + name + " found in region " + currentRegion + ".");
				
				return toReturn;
			}
		}
		
		System.out.println("WARN - Failed to get user " + name + " from any region.");
		
		return null;
	}
	
	@Override
	public User getUserInRegion(String name, String region) throws IllegalArgumentException {
		List<User> userList = getUsersByRegion(region);
		
		if (ValidatorUtil.isValidList(userList)) {
			for (User currentUser : userList) {
				if (name.equals(currentUser)) {
					System.out.println("INFO - Get user " + name + " from region " + region + " was found.");
					
					return currentUser;
				}
			}
		}
		
		System.out.println("WARN - Get user " + name + " from region " + region + " was not found.");
		
		return null;
	}
	
	@Override
	public List<User> getUsersByRegion(String region) throws IllegalArgumentException {
		return regionMap.get(region);
	}
}
