package org.icepush.gwt.samples.basic.server;

import org.icepush.PushContext;
import org.icepush.gwt.samples.basic.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

  public String greetServer(String input) {
    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");
    
    PushContext pushContext = PushContext.getInstance(this.getServletContext());
    pushContext.push("new-user");
    
    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }
}
