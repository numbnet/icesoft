package org.springframework.webflow.samples.sellitem;


/**
 * Just a demonstration of a standalone navigation bean. Could be done without 
 * nav rules, a simple anchor would do. 
 */
public class NavBean {

    public NavBean () {
    } 

    public String whereToStart() {
        return "startOver";
    }

}
