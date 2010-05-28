package com.personal.memory.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.personal.memory.game.GameManager;
import com.personal.memory.util.FacesUtil;
import com.personal.memory.util.ValidatorUtil;

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
