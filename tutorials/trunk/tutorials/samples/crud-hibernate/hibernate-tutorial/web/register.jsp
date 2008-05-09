<!--
  ~ Version: MPL 1.1/GPL 2.0/LGPL 2.1
  ~
  ~ "The contents of this file are subject to the Mozilla Public License
  ~ Version 1.1 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://www.mozilla.org/MPL/
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  ~ License for the specific language governing rights and limitations under
  ~ the License.
  ~
  ~ The Original Code is ICEfaces 1.5 open source software code, released
  ~ November 5, 2006. The Initial Developer of the Original Code is ICEsoft
  ~ Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
  ~ 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
  ~
  ~ Contributor(s): _____________________.
  ~
  ~ Alternatively, the contents of this file may be used under the terms of
  ~ the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
  ~ License), in which case the provisions of the LGPL License are
  ~ applicable instead of those above. If you wish to allow use of your
  ~ version of this file only under the terms of the LGPL License and not to
  ~ allow others to use your version of this file under the MPL, indicate
  ~ your decision by deleting the provisions above and replace them with
  ~ the notice and other provisions required by the LGPL License. If you do
  ~ not delete the provisions above, a recipient may use your version of
  ~ this file under either the MPL or the LGPL License."
  ~
-->
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="ice" uri="http://www.icesoft.com/icefaces/component"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ICEfaces Hibernate Tutorial</title>
        <link href="./xmlhttp/css/xp/xp.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <f:view>
            <ice:form>
                <h1>ICEfaces Student Registration</h1>
                    <ice:panelTabSet>
                        <ice:panelTab label="Add Student">
                            <h3>Instructions:</h3>
                            <ul>
                                <li>To add a student enter in the appropriate information.</li>
                                <li>When finished, click the Add Student button.</li>
                                <li>To clear the screen at any time click the Clear button.</li>
                            </ul>
                            <br/>
                            <ice:panelGrid columns="2">
                                <ice:outputText value="First Name:"/>
                                <ice:inputText value="#{registerManager.newStudent.firstName}" required="true"/>
                                <ice:outputText value="Last Name:"/>
                                <ice:inputText value="#{registerManager.newStudent.lastName}" required="true"/>
                                <ice:outputText value="Address:"/>
                                <ice:inputText value="#{registerManager.newStudent.address}" required="true"/>
                                <ice:commandButton type="submit" value="Add Student" 
                                                    actionListener="#{registerManager.addStudent}"/>
                                <ice:commandButton type="submit" value="Clear" action="#{registerManager.newStudent.clear}"/>
                            </ice:panelGrid>
                        </ice:panelTab>
                        <ice:panelTab label="Edit Student">
                            <h3>Instructions:</h3>
                            <ul>
                                <li>To edit a students information, select their id from the box.</li>
                                <li>Their info will be displayed, to edit the information, enter in the new content in the text box beside the old content.</li>
                                <li>To save the changes click the Save Changes button.</li>
                                <li>To delete the student from the system, click the Delete Student button.</li>
                            </ul>
                            <br/>
                            <ice:panelGrid columns="3">
                                <ice:outputText value="Student ID:"/>
                                <ice:selectOneMenu value="#{registerManager.selectedStudent}"
                                                    partialSubmit="true"
                                                    valueChangeListener="#{registerManager.studentValueChanged}">
                                    <f:selectItems value="#{registerManager.studentItems}"/>
                                </ice:selectOneMenu>
                                <br/>
                                <br/><br/><ice:outputText value="Input Changes Below"/>
                                <ice:outputText value="First Name:"/>
                                <ice:outputText value="#{registerManager.currentStudent.firstName}"/>
                                <ice:inputText value="#{registerManager.updateStudent.firstName}"/>
                                <ice:outputText value="Last Name:"/>
                                <ice:outputText value="#{registerManager.currentStudent.lastName}"/>
                                <ice:inputText value="#{registerManager.updateStudent.lastName}"/>
                                <ice:outputText value="Address:"/>
                                <ice:outputText value="#{registerManager.currentStudent.address}"/>
                                <ice:inputText value="#{registerManager.updateStudent.address}"/>
                                <ice:commandButton type="submit" value="Save Changes" actionListener="#{registerManager.updateStudent}"/>
                                <ice:commandButton type="submit" value="Delete Student" actionListener="#{registerManager.deleteStudent}"/>
                            </ice:panelGrid>
                        </ice:panelTab>
                        <ice:panelTab label="Add/Remove Courses">
                            <h3>Instructions:</h3>
                                <ul>
                                    <li>Choose the Student ID of the student you want to add/remove a course.</li>
                                    <li>Choose the course name of the course you want to add, a description apears below.</li>
                                    <li>Click the Add Course or Remove Course buttons depending on what you want to do.</li>
                                    <li>To view the changes select the Student ID again.</li>
                                </ul>
                                <br/>
                            <ice:panelGrid columns="2">
                                
                                <ice:outputText value="Student ID:"/>
                                <ice:selectOneMenu value="#{registerManager.selectedStudent}"
                                                    partialSubmit="true"
                                                    valueChangeListener="#{registerManager.studentValueChanged}">
                                    <f:selectItems value="#{registerManager.studentItems}"/>
                                </ice:selectOneMenu>
                                <ice:outputText value="Course Name:"/>
                                <ice:selectOneMenu value="#{registerManager.selectedCourse}"
                                                    partialSubmit="true"
                                                    valueChangeListener="#{registerManager.courseValueChanged}">
                                    <f:selectItems value="#{registerManager.courseItems}"/>
                                </ice:selectOneMenu>
                            </ice:panelGrid>
                                <ice:dataTable value="#{registerManager.currentCourse}"
                                                   columnWidths="100px,100px,200px">
                                        <ice:column>
                                            <f:facet name="header">
                                                <ice:outputText value="Course ID"/>
                                            </f:facet>
                                            <ice:outputText value="#{registerManager.currentCourse.courseId}"/>
                                        </ice:column>
                                        <ice:column>
                                            <f:facet name="header">
                                                <ice:outputText value="Course Name"/>
                                            </f:facet>
                                            <ice:outputText value="#{registerManager.currentCourse.courseName}"/>
                                        </ice:column>
                                        <ice:column>
                                            <f:facet name="header">
                                                <ice:outputText value="Description"/>
                                            </f:facet>
                                            <ice:outputText value="#{registerManager.currentCourse.description}"/>
                                        </ice:column>
                                        
                                </ice:dataTable>
                                <ice:panelGrid columns="2">
                                    <ice:commandButton type="submit" value="Add Course"
                                                        actionListener="#{registerManager.addCourseToStudent}"/>
                                    <ice:commandButton type="submit" value="Remove Course"
                                                        actionListener="#{registerManager.removeCourseFromStudent}"/>
                                </ice:panelGrid> 
                                <br/>
                                <ice:outputText value="Courses currently registered:"/>
                                <br/>
                                <ice:dataTable var="course" value="#{registerManager.studentCourses}">
                                    <ice:column>
                                        <f:facet name="header">
                                            <ice:outputText value="Course ID:"/>
                                        </f:facet>
                                        <ice:outputText value="#{course.courseId}"/>
                                    </ice:column>
                                    <ice:column>
                                        <f:facet name="header">
                                            <ice:outputText value="Course Name:"/>
                                        </f:facet>
                                        <ice:outputText value="#{course.courseName}"/>
                                    </ice:column>
                                    <ice:column>
                                        <f:facet name="header">
                                            <ice:outputText value="Description"/>
                                        </f:facet>
                                        <ice:outputText value="#{course.description}"/>
                                    </ice:column>
                                </ice:dataTable>
                        </ice:panelTab>
                    </ice:panelTabSet>
                                    
                <ice:messages/>
            </ice:form>
        </f:view>
    </body>
       
</html>