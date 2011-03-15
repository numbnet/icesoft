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

package com.icesoft.icefaces.samples.datatable.jpa;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER", uniqueConstraints = {})
public class Customer implements java.io.Serializable {

    // Fields

    private Integer customernumber;
    private String customername;
    private String contactlastname;
    private String contactfirstname;
    private String phone;
    private String addressline1;
    private String addressline2;
    private String city;
    private String state;
    private String postalcode;
    private String country;
    private Integer salesrepemployeenumber;
    private Double creditlimit;

    // Constructors

    /**
     * default constructor
     */
    public Customer() {
    }

    /**
     * minimal constructor
     */
    public Customer(Integer customernumber) {
        this.customernumber = customernumber;
    }

    /**
     * full constructor
     */
    public Customer(Integer customernumber, String customername,
                    String contactlastname, String contactfirstname, String phone,
                    String addressline1, String addressline2, String city,
                    String state, String postalcode, String country,
                    Integer salesrepemployeenumber, Double creditlimit) {
        this.customernumber = customernumber;
        this.customername = customername;
        this.contactlastname = contactlastname;
        this.contactfirstname = contactfirstname;
        this.phone = phone;
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
        this.city = city;
        this.state = state;
        this.postalcode = postalcode;
        this.country = country;
        this.salesrepemployeenumber = salesrepemployeenumber;
        this.creditlimit = creditlimit;
    }

    // Property accessors
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CUSTOMERNUMBER", unique = true, nullable = false, insertable = true, updatable = true)
    public Integer getCustomernumber() {
        return this.customernumber;
    }

    public void setCustomernumber(Integer customernumber) {
        this.customernumber = customernumber;
    }

    @Column(name = "CUSTOMERNAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getCustomername() {
        return this.customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    @Column(name = "CONTACTLASTNAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getContactlastname() {
        return this.contactlastname;
    }

    public void setContactlastname(String contactlastname) {
        this.contactlastname = contactlastname;
    }

    @Column(name = "CONTACTFIRSTNAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getContactfirstname() {
        return this.contactfirstname;
    }

    public void setContactfirstname(String contactfirstname) {
        this.contactfirstname = contactfirstname;
    }

    @Column(name = "PHONE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "ADDRESSLINE1", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getAddressline1() {
        return this.addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    @Column(name = "ADDRESSLINE2", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getAddressline2() {
        return this.addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    @Column(name = "CITY", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "STATE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "POSTALCODE", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
    public String getPostalcode() {
        return this.postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    @Column(name = "COUNTRY", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "SALESREPEMPLOYEENUMBER", unique = false, nullable = true, insertable = true, updatable = true)
    public Integer getSalesrepemployeenumber() {
        return this.salesrepemployeenumber;
    }

    public void setSalesrepemployeenumber(Integer salesrepemployeenumber) {
        this.salesrepemployeenumber = salesrepemployeenumber;
    }

    @Column(name = "CREDITLIMIT", unique = false, nullable = true, insertable = true, updatable = true, precision = 52, scale = 0)
    public Double getCreditlimit() {
        return this.creditlimit;
    }

    public void setCreditlimit(Double creditlimit) {
        this.creditlimit = creditlimit;
    }

}