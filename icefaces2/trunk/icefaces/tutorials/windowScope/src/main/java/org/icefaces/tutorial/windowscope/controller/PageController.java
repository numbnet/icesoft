package org.icefaces.tutorial.windowscope.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="pageController")
@SessionScoped
public class PageController implements Serializable {
	public String navigatePage1() {
		System.out.println("Redirect to Page 1");
		
		return "page1";
	}
	
	public String navigatePage2() {
		System.out.println("Redirect to Page 2");
		
		return "page2";
	}
	
	public String action() {
		System.out.println("Action Fired");
		
		return null;
	}
	
	public void actionListener(ActionEvent event) {
		System.out.println("ActionListener Fired");
	}
}
