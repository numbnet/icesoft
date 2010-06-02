/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/
package com.icefaces.project.memory.user;

public class UserModel {
	protected String name;
	protected int score = 0;
	protected boolean isTurn = false;
	protected boolean isComputer = false;

	public UserModel() {
	}
	
	public UserModel(String name, boolean isComputer) {
		this.name = name;
		this.isComputer = isComputer;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public boolean getIsTurn() {
		return isTurn;
	}

	public void setIsTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}
	
	public boolean getIsComputer() {
		return isComputer;
	}

	public void setIsComputer(boolean isComputer) {
		this.isComputer = isComputer;
	}

	public int incrementScore() {
		return score++;
	}
}
