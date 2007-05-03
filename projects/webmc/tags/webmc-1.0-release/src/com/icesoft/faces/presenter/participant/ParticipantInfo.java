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
package com.icesoft.faces.presenter.participant;

/**
 * Class used to store the backend specific information about a participant.
 * This means no UI work is done or even considered, instead just core
 * information about a user is managed This is meant to be extended by a front
 * end Participant bean
 */
public class ParticipantInfo {
    public static final int ROLE_VIEWER = 0;
    public static final int ROLE_MODERATOR = 1;

    protected String firstName = "";
    protected String lastName = "";
    protected String email = "";

    /**
     * Method to get the first name
     *
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Method to set the first name
     *
     * @param firstName new
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Method to get the last name
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Method to set the last name
     *
     * @param lastName new
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Method to get the email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to set the email
     *
     * @param email new
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method to reset the participant info fields
     */
    public void clearFields() {
        firstName = "";
        lastName = "";
        email = "";
    }
}