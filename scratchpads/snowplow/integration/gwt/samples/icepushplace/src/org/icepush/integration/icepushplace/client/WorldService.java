package org.icepush.integration.icepushplace.client;


import java.util.List;

import org.icepush.integration.icepushplace.client.model.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("world")
public interface WorldService extends RemoteService {
	public static final String[] REGIONS = {"North America",
											"Europe",
											"South America",
											"Asia",
											"Africa",
											"Antarctica"};
	public static final String[] MOODS = {"average",
										  "shocked",
										  "angry",
										  "happy",
										  "sad"};
    public static final String MOOD_IMAGE_PREFIX = "images/mood-";
    public static final String MOOD_IMAGE_SUFFIX = ".png";
    
	public User addUser(String name, String mood, String mind, String region) throws IllegalArgumentException;
	public Boolean removeUser(User user) throws IllegalArgumentException;
	public User getUser(String name) throws IllegalArgumentException;
	public User getUserInRegion(String name, String region) throws IllegalArgumentException;
	public List<User> getUsersByRegion(String region) throws IllegalArgumentException;
}
