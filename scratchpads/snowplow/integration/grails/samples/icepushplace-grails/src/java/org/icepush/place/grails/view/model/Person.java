/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.place.grails.view.model;

/**
 *
 * @author bkroeger
 */
public class Person {
    
    private String nickname = "";
    private String mood = "";
    private String comment = "";
    private String region = "";
    private String technology = "";
    private String messageIn = "";
    private String messageOut = "";

    public Person() {
 
    }

    public String getNickname() {
	    return nickname;
    }
    
    public void setNickname(String nn) {
	    nickname = nn;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getMessageIn() {
        return messageIn;
    }

    public void setMessageIn(String messageIn) {
        this.messageIn = messageIn;
    }

    public String getMessageOut() {
        return messageOut;
    }

    public void setMessageOut(String messageOut) {
        this.messageOut = messageOut;
    }
}
