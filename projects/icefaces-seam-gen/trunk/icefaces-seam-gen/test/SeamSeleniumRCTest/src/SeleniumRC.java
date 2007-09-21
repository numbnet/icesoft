/*
 * SeleniumRC.java
 *
 * Created on September 21, 2007, 2:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

//package com.icesoft.test.client.seam;


import org.openqa.selenium.server.SeleniumServer;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


public class SeleniumRC{
	protected Selenium selenium;
	protected SeleniumServer server;
	private static SeleniumRC currentInstance;
	private static boolean created = false;
	
	protected SeleniumRC() throws Exception{
		server = new SeleniumServer(18888);
		//NOTE: these test are more reliable on Firefox than on any other browser.
                selenium = new DefaultSelenium("localhost",18888,"*firefox","http://localhost:8080/");
	}
	
	protected SeleniumRC(String browser) throws Exception{
		server = new SeleniumServer(SeleniumServer.getDefaultPort());
		selenium = new DefaultSelenium("localhost",SeleniumServer.getDefaultPort(),browser,"about:blank");
	}
	
	protected SeleniumRC(int defaultPort, String serverHost, String browser, String startURL) throws Exception{
		server = new SeleniumServer(defaultPort);
		selenium = new DefaultSelenium(serverHost,defaultPort,browser,startURL);
	}
	
	
	public static SeleniumRC getCurrentInstance() throws Exception{
		if (!created){
			currentInstance = new SeleniumRC();
			currentInstance.init();
			created = true;
		}
		return currentInstance;
	}
	

	public static void destoryCurrentInstance() throws Exception{
		if (created){
			currentInstance.dispose();
			created = false;
                       
		}
	}
	
	
	
	
	public void init() throws Exception{
		server.start();
		selenium.start();
	}
	
	public void dispose() throws Exception{
            selenium.stop();
            server.stop();
		
	}
	
	public void open(String url){
		selenium.open(url);
	}

	public Selenium getSelenium() {
		return selenium;
	}

	public void setSelenium(Selenium selenium) {
		this.selenium = selenium;
	}

	public SeleniumServer getServer() {
		return server;
	}

	public void setServer(SeleniumServer server) {
		this.server = server;
	}
	
	
	
}
