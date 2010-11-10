package org.icepush.integration.icepushplace.bean;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.icepush.integration.icepushplace.service.PersonServiceImpl;
import org.icepush.integration.icepushplace.util.FacesUtil;
import org.icepush.ws.samples.icepushplace.PersonType;

@ManagedBean(name="personController")
@ApplicationScoped
public class PersonController {
	@ManagedProperty(value="#{personServiceImpl}")
	private PersonServiceImpl personService;
	
	public String login() {
        PersonType personModel = (PersonType)FacesUtil.getManagedBean("personModel");
        personModel.setTechnology("ICEfaces 2.0");
        
        personService.loginPerson(personModel);
        
		return "world";
	}

	public PersonServiceImpl getPersonService() {
		return personService;
	}

	public void setPersonService(PersonServiceImpl personService) {
		this.personService = personService;
	}

}
