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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous remote service used by GWT to interact with the world by adding and removing users, getting continents, etc.
 */
public interface WorldServiceAsync {
	public void addUser(String name, String mood, String mind, String region, String message, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void updateUser(User user, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	public void smartUpdateUser(boolean needUpdate, String oldRegion, User user, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void moveUser(String oldRegion, User user, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void removeUser(User user, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	public void getUser(String name, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void getUserInRegion(String name, String region, AsyncCallback<User> callback) throws IllegalArgumentException;
	public void getUsersByRegion(String region, AsyncCallback<List<User>> callback) throws IllegalArgumentException;
	public void getAllRegions(AsyncCallback<String[]> callback) throws IllegalArgumentException;
}
