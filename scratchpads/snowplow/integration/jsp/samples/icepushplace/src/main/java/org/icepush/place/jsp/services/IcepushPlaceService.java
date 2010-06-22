package org.icepush.place.jsp.services;

import org.icepush.ws.samples.icepushplace.PersonType;
import org.icepush.ws.samples.icepushplace.WorldResponseType;

public interface IcepushPlaceService{

    public void register();

    public void login(PersonType person);
    
    public void updatePerson(PersonType person);
    
    public WorldResponseType updateWorld(long lastSequenceNo);

}
