package org.icepush.gwt.samples.command.server;

import org.icepush.gwt.samples.command.client.GreetingService;
import org.icepush.gwt.samples.command.client.ParticipantEntryCommand;
import org.icepush.gwt.server.ServerPushCommandContext;

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
    
    ParticipantEntryCommand command = new ParticipantEntryCommand();
    command.setParticipantName(input);
    ServerPushCommandContext.getInstance(getServletContext()).pushCommand(command, getServletContext());
    
    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }
  
  
}
