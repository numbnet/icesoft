package com.icesoft.eb;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;

@Stateful
@Name("viewManager")
public class ViewManagerAction implements ViewManager{
    
    private List views;

    @Destroy @Remove
    public void destroy() {}
}
