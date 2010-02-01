package org.icepush.samples.gwt.client;

import java.io.Serializable;

import org.icepush.integration.gwt.command.ICommand;

public class ParticipantEntryCommand implements ICommand,Serializable{

	private String participantName;

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	
	
}
