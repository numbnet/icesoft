package org.icepush.samples.icechat.gwt.client.service;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
  public Credentials register(User name);
  public Credentials login(String username, String password);
}
