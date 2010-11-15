package org.icepush.integration.icepushplace.service;

import org.icepush.ws.samples.icepushplace.PersonType;

public interface PersonService {
	public PersonType loginPerson(PersonType person);
	public void updatePerson(PersonType person);
	public PersonType movePerson(int fromRegion, PersonType person);
}
