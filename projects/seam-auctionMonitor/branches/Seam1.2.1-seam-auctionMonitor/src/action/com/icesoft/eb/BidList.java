/*
 * BidList.java
 *
 * Created on June 6, 2007, 10:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.eb;

import javax.ejb.Local;

@Local
public interface  BidList {

   public void getBids();
   public void destroy();
}

