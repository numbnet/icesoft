/*
 * Register.java
 *
 * Created on June 5, 2007, 3:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.ejb;

import java.io.Serializable;

import javax.ejb.Local;

@Local
public interface Register extends Serializable
{
   public void register();
   public void invalid();
   public String getVerify();
   public void setVerify(String verify);
   public boolean isRegistered();   
   public void destroy();
}