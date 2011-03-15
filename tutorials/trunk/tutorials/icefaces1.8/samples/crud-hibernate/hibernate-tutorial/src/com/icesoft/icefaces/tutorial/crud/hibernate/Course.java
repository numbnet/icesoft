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

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Course in the hibernate tutorial 
 * Student Register example.
 */
public class Course {
    // unique course id
    private int courseId;
    // name of the course
    private String courseName;
    // description of the course
    private String description;
    // set of students that are related/registered for the course
    private Set students = new HashSet();
    
    /**
     * Default contructor.
     */
    public Course() {
    }
    
    /**
     * Creates a new instance of Course.
     * @param courseId course id.
     * @param courseName course name.
     * @param description description of the course.
     */
    public Course(int courseId, String courseName, String description){
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
    }
    
    /**
     * Gets the course Id of this course.
     * @return course id.
     */
    public int getCourseId(){
        return courseId;
    }
    
    /**
     * Sets the course id of this course.
     * @param course id.
     */
    public void setCourseId(int courseId){
        this.courseId = courseId;
    }
    
    /**
     * Gets the course name for this course.
     * @return course name.
     */
    public String getCourseName(){
        return courseName;
    }
    
    /**
     * Sets the course name for this course.
     * @param course name.
     */
    public void setCourseName(String courseName){
        this.courseName = courseName;
    }
    
    /**
     * Gets the description for this course.
     * @return description.
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Sets the description for this course.
     * @param description.
     */
    public void setDescription(String description){
        this.description = description;
    }
    
    /**
     * Gets the set of students for this course.
     * @return Set of students.
     */
    public Set getStudents(){
        return students;
    }
    
    /**
     * Sets the set of students for this course.
     * @param Set of students.
     */
    public void setStudents(Set students){
        this.students = students;
    }
    
    /**
     * Method used by the UI to clear information on the screen.
     * @return String used in the navigation rules.
     */
    public String clear(){
        courseId=0;
        courseName="";
        description="";
        return "clear";
    }
}
