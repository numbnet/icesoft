package org.icepush.integration.icepushplace.client;

import java.util.List;

import org.icepush.integration.icepushplace.client.model.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous remote service used by GWT to interact with the world by adding and removing users, getting continents, etc.
 */
public interface WorldServiceAsync {
	public void addUser(String name, String mood, String mind, String region, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void removeUser(User user, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	public void getUser(String name, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void getUserInRegion(String name, String region, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void getUsersByRegion(String region, AsyncCallback<List<User>> callback) throws IllegalArgumentException;
}
