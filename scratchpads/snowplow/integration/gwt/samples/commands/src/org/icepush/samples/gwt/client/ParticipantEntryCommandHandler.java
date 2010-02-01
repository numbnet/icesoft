package org.icepush.samples.gwt.client;

import org.icepush.integration.gwt.command.ICommand;
import org.icepush.integration.gwt.command.ICommandExecuter;

import com.google.gwt.user.client.Window;

public class ParticipantEntryCommandHandler implements ICommandExecuter {

	public void execute(ICommand command) {
		Window.alert("recieved command: " + command.getClass().getName());
	}

}
