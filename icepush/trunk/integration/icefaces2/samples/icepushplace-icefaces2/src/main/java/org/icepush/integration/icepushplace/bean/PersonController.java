package org.icepush.integration.icepushplace.bean;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;

import org.icefaces.application.PushRenderer;
import org.icepush.integration.icepushplace.service.PersonServiceImpl;
import org.icepush.integration.icepushplace.util.FacesUtil;
import org.icepush.ws.samples.icepushplace.PersonType;

@ManagedBean(name="personController")
@ApplicationScoped
public class PersonController {
    private static final String LOGIN_SUCCESS_REDIRECT = "map";
    
	@ManagedProperty(value="#{personServiceImpl}")
	private PersonServiceImpl personService;
	
	public String login() {
        PersonType personModel = (PersonType)FacesUtil.getManagedBean("personModel");
        personModel.setTechnology("ICEfaces 2.0");
        
        PersonType returned = personService.loginPerson(personModel);
        
        if (returned != null) {
        	personModel.setKey(returned.getKey());
        	
        	for (String currentContinent : personService.getContinents()) {
        		PushRenderer.addCurrentSession(currentContinent);
        	}
        	
        	return LOGIN_SUCCESS_REDIRECT;
        }
        else {
        	FacesUtil.addErrorMessage("Error while adding user '" + personModel.getName() + "' to the ICEpushplace.");
        }
        
        return null;
	}
	
	public void update(ActionEvent event) {
		PersonType personModel = (PersonType)FacesUtil.getManagedBean("personModel");
		
		personService.updatePerson(personModel);
	}

	public PersonServiceImpl getPersonService() {
		return personService;
	}

	public void setPersonService(PersonServiceImpl personService) {
		this.personService = personService;
	}

}
