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
package com.icesoft.icefaces.tutorial.crud.hibernate;


import com.icesoft.icefaces.tutorial.crud.hibernate.util.HibernateUtil;
import java.util.HashSet;
import java.util.Set;
import javax.faces.event.ActionEvent;
import org.hibernate.Session;



/**
 * Represents a student object in the hibernate tutorial
 * Student Register example.
 */
public class Student {
    // unique student id
    private int studentId;
    // first name of the student
    private String firstName;
    // last name of the student
    private String lastName;
    // address of the student
    private String address;
    // set of courses that the student is related/registered for
    private Set courses = new HashSet();
    
    /**
     * Default constructor
     */
    public Student() {
    }
    
    /**
     * Creates a new instance of Student.
     * @param firstName first name.
     * @param lastName last name.
     * @param address address.
     */
    public Student(String firstName, String lastName, String address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
    
    /**
     * Gets the student id for this student.
     * @return student id.
     */
    public int getStudentId(){
        return studentId;
    }
    
    /**
     * Sets the student id for this student.
     * @return student id.
     */
    public void setStudentId(int studentId){
        this.studentId = studentId;
    }
    
    /**
     * Gets the first name for this student.
     * @return first name.
     */
    public String getFirstName(){
        return firstName;
    }
    
    /**
     * Sets the first name for this student.
     * @param first name.
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    /**
     * Gets the last name for this student.
     * @return last name.
     */
    public String getLastName(){
        return lastName;
    }
    
    /**
     * Sets the last name for this student.
     * @param last name.
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    /**
     * Gets the address for this student.
     * @return address.
     */
    public String getAddress(){
        return address;
    }
    
    /**
     * Sets the address for this student.
     * @param address.
     */
    public void setAddress(String address){
        this.address = address;
    }
    
    /**
     * Gets the Set of courses for this student.
     * @return Set of courses.
     */
    public Set getCourses(){
        return courses;
    }
    
    /**
     * Sets the Set of courses for this student.
     * @return Set of courses.
     */
    public void setCourses(Set courses){
        this.courses = courses;
    }
    
    /**
     * Method used by the UI to clear information on the screen.
     * @return String used in the navigation rules.
     */
    public String clear(){
        firstName="";
        lastName="";
        address="";
        return "clear";
    }
    
}
