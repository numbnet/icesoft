package org.icepush.gwt.samples.command.client;

import org.icepush.gwt.client.command.ICommand;
import org.icepush.gwt.client.command.ICommandExecuter;


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
