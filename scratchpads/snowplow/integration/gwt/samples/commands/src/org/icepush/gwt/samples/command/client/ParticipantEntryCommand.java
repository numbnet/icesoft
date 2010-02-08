package org.icepush.gwt.samples.command.client;

import java.io.Serializable;

import org.icepush.gwt.client.command.ICommand;

public class ParticipantEntryCommand implements ICommand,Serializable{

	private String participantName;

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	
	
}
