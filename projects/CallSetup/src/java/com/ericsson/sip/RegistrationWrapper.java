/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.  
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright (c) Ericsson AB, 2004-2007. All rights reserved.
 */

package com.ericsson.sip;

import java.util.Date;

/**
 *
 * @author 
 */
public class RegistrationWrapper  {

    private String id;
    
    private String contact;
    
    private boolean selected;

    private Date registrationTime;
    
    private Date expirationTime;
    
    private Registration registration;

    private CallTracker callTracker;

    /** Creates a new instance of Registration */
    public RegistrationWrapper(CallTracker callTracker, Registration registration) {
        this.callTracker = callTracker;
        this.registration = registration;
    }

    public String getId() {
        return registration.getId();
    }

    public void setId(String id) {
        registration.setId(id);
    }

    public int hashCode() {
        return registration.hashCode();
    }

    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistrationWrapper)) {
            return false;
        }
        RegistrationWrapper other = (RegistrationWrapper) object;
        return registration.equals(other.getInnerRegistration());
    }

    public String toString() {
        return "com.ericsson.sip.RegistrationWrapper[id=" + getId() + "]";
    }

    public String getContact() {
        return registration.getContact();
    }

    public void setContact(String contact) {
        registration.setContact(contact);
    }

    public Date getRegistrationTime() {
        return registration.getRegistrationTime();
    }

    public void setRegistrationTime(Date registrationTime) {
        registration.setRegistrationTime(registrationTime);
    }

    public Date getExpirationTime() {
        return registration.getExpirationTime();
    }

    public void setExpirationTime(Date expirationTime) {
        registration.setExpirationTime(expirationTime);
    }
    
    public boolean getSelected() {
        return callTracker.getSelected(registration);
    }

    public void setSelected(boolean selected) {
        callTracker.setSelected(registration, selected);
    }
    
    public Registration getInnerRegistration()  {
        return registration;
    }
}
