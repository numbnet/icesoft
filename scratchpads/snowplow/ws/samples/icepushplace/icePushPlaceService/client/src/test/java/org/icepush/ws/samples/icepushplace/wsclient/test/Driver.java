package org.icepush.ws.samples.icepushplace.wsclient.test;

//import java.util.List;
//import java.util.ListIterator;

import org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWsClient;
import org.icepush.ws.samples.icepushplace.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Driver {

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("applicationContext.xml", Driver.class);

    ICEpushPlaceWsClient client =  (ICEpushPlaceWsClient) applicationContext.getBean("icepushPlaceClient", ICEpushPlaceWsClient.class);
	Tests tests = new Tests(client);
	tests.testSequence1();
    }
}
