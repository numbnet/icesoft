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
package com.icefaces.project.memory.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.icefaces.project.memory.game.GameManager;
import com.icefaces.project.memory.util.FacesUtil;
import com.icefaces.project.memory.util.ValidatorUtil;

/**
 * Validator used to check if a game name is unique or not
 */
public class UniqueGameNameValidator implements Validator {
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// Try to grab the current gameManager from the context
		Object baseBean = FacesUtil.getManagedBean("gameManager");
		
		if (baseBean != null) {
			GameManager gameManager = (GameManager)baseBean;
			
			String newName = value.toString();
			
			// Check whether the game name we're validating matches a real game or not
		    if (ValidatorUtil.isValidString(newName)) {
				if (gameManager.getHasGame(newName)) {
					throw new ValidatorException(
							FacesUtil.generateMessage("Game '" + newName + "' already exists. Choose another name."));
				}
		    }
		}
	}
}
