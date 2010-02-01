package org.icepush.samples.gwt.client;

import org.icepush.integration.gwt.command.ICommand;
import org.icepush.integration.gwt.command.ICommandExecuter;

import com.google.gwt.user.client.Window;

public class ParticipantEntryCommandHandler implements ICommandExecuter {

	private MessagesPopup popup;
	public ParticipantEntryCommandHandler(MessagesPopup popup){
		this.popup = popup;
	}
	public void execute(ICommand command) {
		ParticipantEntryCommand castedCommand = (ParticipantEntryCommand) command;
		this.popup.setName(castedCommand.getParticipantName());
		this.popup.show();
	}

}
