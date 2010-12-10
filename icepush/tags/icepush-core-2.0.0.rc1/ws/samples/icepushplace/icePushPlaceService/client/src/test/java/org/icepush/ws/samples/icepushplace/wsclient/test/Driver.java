package org.icepush.ws.samples.icepushplace.wsclient.test;

//import java.util.List;
//import java.util.ListIterator;

import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWsClient;
import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld;
import org.icepush.ws.samples.icepushplace.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Driver {

    public static void main(String[] args) {
	ICEpushPlaceWorld world =  new ICEpushPlaceWorld();
	world.setApplicationURL("http://myApp.com/");
	world.setWebServiceURL("http://localhost:8080/icePushPlaceService");
	Tests tests = new Tests(world);
	tests.testSequence2();
    }
}
