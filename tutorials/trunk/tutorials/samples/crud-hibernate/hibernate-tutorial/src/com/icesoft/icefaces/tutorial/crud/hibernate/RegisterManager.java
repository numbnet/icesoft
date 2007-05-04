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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.Query;
import org.hibernate.Session;
import javax.faces.model.SelectItem;

/**
 * Represents the Utility class for the StudentRegister application which uses
 * Hibernate to connect and interact with a MySql database.  
 **/
public class RegisterManager {
    
    // List of student id's
    private List studentItems = new ArrayList();
    // List of course names
    private List courseItems = new ArrayList();
    // Currently selected Student Id
    private String selectedStudent;
    // Currently selected Course name
    private String selectedCourse;
    // Student object used in adding a new student
    private Student newStudent = new Student();
    // Student object that is currently selected
    private Student currentStudent = new Student();
    // Student object used to update an existing student in the DB
    private Student updateStudent = new Student();
    // Course object that is currently selected
    private Course currentCourse = new Course();
    // List of course id's that a student is registered 
    private List studentCourses = new ArrayList();
    
    public RegisterManager() {
       init();
    }
    
    /**
     * Initializeing class that populates the studentItems and courseItems 
     * objects to be used by the application.
     */
    private synchronized void init(){
        if(studentItems.size() >=1){
            studentItems.clear();
        }
        if(courseItems.size() >= 1){
            courseItems.clear();
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        List studentResult = session.createQuery("select s.studentId from " +
                "Student as s").list();
        List courseResult = session.createQuery("select c.courseName from " +
                "Course as c").list();        
        session.getTransaction().commit();
        studentItems.add(new SelectItem(""));
        for(int i = 0; i < studentResult.size();i++){
            studentItems.add(new SelectItem(studentResult.get(i).toString()));
        }
        selectedStudent = ((SelectItem)studentItems.get(0)).getLabel();
       
        for(int i = 0;i < courseResult.size();i++){
            courseItems.add(new SelectItem(courseResult.get(i)));
        }
        selectedCourse = ((SelectItem)courseItems.get(0)).getLabel();
    }
    
    /**
     * Gets the studentItems list of student id's.
     * @return list of student id's.
     */
    public List getStudentItems(){
        return studentItems;
    }
    
    /**
     * Gets the courseItems list of course names.
     * @return list of course names.
     */
    public List getCourseItems(){
        return courseItems;
    }    
    
    /**
     * Gets the selectedStudent student id.
     * @return selected student id.
     */
    public String getSelectedStudent(){
        return selectedStudent;
    }
    
    /**
     * Gets the selectedCourse course name.
     * @return selected course name.
     */
    public String getSelectedCourse(){
        return selectedCourse;
    }
    
    /**
     * Sets the selectedStudent student id.
     * @param selected student id.
     */
    public void setSelectedStudent(String selectedStudent){
        this.selectedStudent = selectedStudent;
    }
    
    /**
     * Sets the selectedCourse course name.
     * @param selected course name.
     */
    public void setSelectedCourse(String selectedCourse){
        this.selectedCourse = selectedCourse;
    }
    
    /**
     * Gets the currentStudent Student object.
     * @return Student object currently selected.
     */
    public Student getCurrentStudent(){
        return currentStudent;
    }
    
    /**
     * Sets the currentStudent Student object.
     * @param Student object currenty selected.
     */
    public void setCurrentStudent(Student currentStudent){
        this.currentStudent = currentStudent;
    }
    
    /**
     * Gets the newStudent Student object.
     * @return Student object to be added.
     */
    public Student getNewStudent(){
        return newStudent;
    }
    
    /**
     * Sets the newStudent Student object.
     * @param Student object to be added.
     */
    public void setNewStudent(Student newStudent){
        this.newStudent = newStudent;
    }
    
    /**
     * Gets the updateStudent Student object.
     * @return Student object to be updated.
     */
    public Student getUpdateStudent(){
        return updateStudent;
    }
    
    /**
     * Sets the updateStudent Student object.
     * @param Student object to be updated.
     */
    public void setUpdateStudent(Student updateStudent){
        this.updateStudent = updateStudent;
    }
    
    /**
     * Gets the currentCourse Course object.
     * @return Course object currently selected.
     */
    public Course getCurrentCourse(){
        return currentCourse;
    }
    
    /**
     * Sets the currentCourse object.
     * @param Course object currently selected.
     */
    public void setCurrentCourse(Course currentCourse){
        this.currentCourse = currentCourse;
    }
    
    /**
     * Gets the studentCourses list of course id's.
     * @return List of course id's.
     */
    public List getStudentCourses(){
        return studentCourses;
    }
    
    /**
     * Sets the studentCourses with the courses related to the currentStudent 
     * object.
     */
    public void setStudentCourses(){
        if(studentCourses.isEmpty() == false){
                studentCourses.clear();
        }
        Set c = currentStudent.getCourses();
        Iterator it = c.iterator();
        while(it.hasNext()){
            Course course = (Course)it.next();
            studentCourses.add(course);
        }
    
    }
    /**
     * Listener for the student id selectOneMenu component value change action.
     * @param ValueChangeEvent representing the new value.
     */   
    public void studentValueChanged(ValueChangeEvent event){
        if(event.getNewValue() != null){
            int id = Integer.parseInt((String)event.getNewValue());
            Session session = HibernateUtil.getSessionFactory()
                                                    .getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery("from Student as s where " +
                    "s.studentId=:id");
            q.setInteger("id",id);
            List sResult = q.list();
            currentStudent = (Student)sResult.get(0);
            setStudentCourses();
            session.getTransaction().commit();
            
            
        }
        
    }
    /**
     * Listener for the course name selectOneMenu component value change action.
     * @param ValueChangeEvent representing the new value.
     */
    public void courseValueChanged(ValueChangeEvent event){
        if(event.getNewValue() != null){
            String name = (String)event.getNewValue();
            Session session = HibernateUtil.getSessionFactory()
                                                    .getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery("from Course as c where " +
                    "c.courseName=:name");
            q.setString("name", name);
            List cResult = q.list();
            session.getTransaction().commit();
            currentCourse = (Course)cResult.get(0);
            
        }
    }
    
    /**
     * Listener for the add student button click action.
     * @param ActionEvent click action event.
     */
    public void addStudent(ActionEvent event){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Student s = newStudent;
        
        session.save(s);
        
        session.getTransaction().commit();
        
        s.clear();
        init();
    }
    
    /**
     * Listener for the delete student button click action.
     * @param ActionEvent click action event.
     */
    public void deleteStudent(ActionEvent event){
        if(currentStudent != null){
            int id = currentStudent.getStudentId();
            Session session = HibernateUtil.getSessionFactory()
                                                    .getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery("delete from Student as s where " +
                    "s.studentId =:id");
            q.setInteger("id",id);
            int rowCount = q.executeUpdate();
            session.getTransaction().commit();
            System.out.println("Rows affected: " + rowCount);
            currentStudent.clear();
            studentItems.clear();
            init();
        }
    }
    
    /**
     * Listener for the save changes button click action.
     * @param ActionEvent click action event.
     */
    public void updateStudent(ActionEvent event){
        Student temp = new Student();
        if(updateStudent.getFirstName().length()==0){
            temp.setFirstName(currentStudent.getFirstName());
        }
        else{
            temp.setFirstName(updateStudent.getFirstName());
        }           
        if(updateStudent.getLastName().length()==0){
            temp.setLastName(currentStudent.getLastName());
        }
        else{
            temp.setLastName(updateStudent.getLastName());
        }
        if(updateStudent.getAddress().length()==0){
            temp.setAddress(currentStudent.getAddress());
        }
        else{
            temp.setAddress(updateStudent.getAddress());
        }
        String firstName = temp.getFirstName();
        String lastName = temp.getLastName();
        String address = temp.getAddress();
        int id = currentStudent.getStudentId();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query q = session.createQuery("update Student set firstName=:" +
                "firstName, lastName=:lastName, address=:address where " +
                "studentId=:id");
        q.setString("firstName", firstName);
        q.setString("lastName", lastName);
        q.setString("address", address);
        q.setInteger("id", id);
        int rowCount = q.executeUpdate();
        session.getTransaction().commit();
        
        currentStudent = updateStudent;
        updateStudent.clear();
        System.out.println(rowCount);
    } 
    
    /**
     * Listener for the add course button click action.
     * @param ActionEvent click action event.
     */
    public void addCourseToStudent(ActionEvent event){
        Session session = HibernateUtil.getSessionFactory().
                                                    getCurrentSession();
        session.beginTransaction();

        Student s = (Student)session.load(Student.class, 
                                        currentStudent.getStudentId());
        Course c = (Course)session.load(Course.class, 
                                        currentCourse.getCourseId());

        s.getCourses().add(c);
        setStudentCourses();
        session.getTransaction().commit();
    }
    
    /**
     * Listener for the remove course button click action.
     * @param ActionEvent click action event.
     */ 
    public void removeCourseFromStudent(ActionEvent event){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Student s = (Student)session.load(Student.class, 
                                            currentStudent.getStudentId());
        Course c = (Course)session.load(Course.class, 
                                            currentCourse.getCourseId());
        s.getCourses().remove(c);
        setStudentCourses();
        session.getTransaction().commit();
        
    }
    
    
    
    
}
