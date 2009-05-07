/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package org.icefaces.demo.opensocial;

import org.opensocial.client.OpenSocialClient;
import org.opensocial.client.OpenSocialProvider;
import org.opensocial.data.OpenSocialActivity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Bean backing the OpenSocial demo. 
 */

public class ActivityBean  {
    private static Log log = LogFactory.getLog(ActivityBean.class);
    private ArrayList activityList = new ArrayList();
    OpenSocialProvider shindig = OpenSocialProvider.valueOf("ORKUT_SANDBOX");
    OpenSocialClient c;

    /**
     * Constructor initializes time zones.
     */
    public ActivityBean() {
        init();
    }

    /**
     * Initializes this bean's properties.
     */
    private void init() {
//        OpenSocialProvider shindig = OpenSocialProvider.valueOf("ORKUT_SANDBOX");
        shindig.restEndpoint = "http://localhost:8080/shindig/social/rest/";
        shindig.rpcEndpoint = "http://localhost:8080/shindig/social/rpc/";
        shindig.providerName = "localhost";
        shindig.isOpenSocial = false;
//        OpenSocialClient c =
    //      new OpenSocialClient(OpenSocialProvider.valueOf("ORKUT_SANDBOX"));
         c = new OpenSocialClient(shindig);
        c.setProperty(OpenSocialClient.Property.DEBUG, "true");
    c.setProperty(OpenSocialClient.Property.TOKEN,
        "owner:jane.doe:appid:domain:apprul:module:container");

    }


    void refreshActivities()  {
    try {
      // Retrieve the friends of the specified user using the OpenSocialClient
      // method fetchFriends
      Collection<OpenSocialActivity> activities =
//        c.fetchFriends("03067092798963641994");
              c.fetchActivitiesForPerson("jane.doe");
      
      System.out.println("----------");
      
      // The fetchFriends method returns a typical Java Collection object with
      // all of the methods you're already accustomed to like size()
      System.out.println(activities.size() + " activities:");

      // Iterate through the Collection
      for (OpenSocialActivity activity : activities) {
        // Output the name of the current friend
        System.out.println("- " + activity.getTitle() + " [" + activity.getBody() + "]");
      }

      System.out.println("----------");
        activityList.clear();
        activityList.addAll(activities);

    } catch (org.opensocial.client.OpenSocialRequestException e) {
      System.out.println("OpenSocialRequestException thrown: " + e.getMessage());
      e.printStackTrace();
    } catch (java.io.IOException e) {
      System.out.println("IOException thrown: " + e.getMessage());
      e.printStackTrace();
    }
    }

    /**
     * Gets ArrayList of currently active <code>TimeZoneWrapper</code> objects.
     * This list is populated by selectBooleanCheckbox components in UI.
     *
     * @return ArrayList of TimeZoneWrapper objects.
     */
    public ArrayList getActivityList() {
        refreshActivities();
        return activityList;
    }
    
    public String activate()  {

        try {

            c.createActivity("MilliTime", String.valueOf(System.currentTimeMillis()));

        } catch (org.opensocial.client.OpenSocialRequestException e) {
          System.out.println("OpenSocialRequestException thrown: " + e.getMessage());
          e.printStackTrace();
        } catch (java.io.IOException e) {
          System.out.println("IOException thrown: " + e.getMessage());
          e.printStackTrace();
        }
    
        return "success";
    }

}

