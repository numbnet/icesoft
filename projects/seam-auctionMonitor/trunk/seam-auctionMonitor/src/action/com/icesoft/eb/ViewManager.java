package com.icesoft.eb;

import java.util.List;

import javax.ejb.Stateful;

import org.jboss.seam.annotations.Name;

@Stateful
@Name("viewManager")
public class ViewManager {
    
    private List views;


}
