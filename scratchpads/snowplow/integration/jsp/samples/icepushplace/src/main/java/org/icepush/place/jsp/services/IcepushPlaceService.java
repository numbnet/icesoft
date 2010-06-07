package org.icepush.place.jsp.services;

import org.icepush.place.jsp.view.model.Person;

public interface IcepushPlaceService{

    public void register();

    public void login(Person person);

    public void requestUpdate(Person person);

}
