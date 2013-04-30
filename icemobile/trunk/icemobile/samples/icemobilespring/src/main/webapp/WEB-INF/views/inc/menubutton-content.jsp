<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
    uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="menubutton" method="POST" enctype="multipart/form-data" cssClass="form">

    <mobi:largeView><h3>Menu Button</h3></mobi:largeView>

    <mobi:fieldsetGroup styleClass="intro">
        <mobi:fieldsetRow>
            The menuButton allows a user to select and execute a menu item.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
           <mobi:menuButton id="buttonOne"
                            name="selectedValue"
                            selectedValue="${menuButtonBean.selectedValue}"
                            buttonLabel="${menuButtonBean.buttonLabel}"
                            selectTitle="${menuButtonBean.selectTitle}"
                            disabled="${menuButtonBean.disabled}"
                            style="${menuButtonBean.style}"
                            styleClass="${menuButtonBean.styleClass}">
                      <mobi:menuButtonGroup label="Group1" >
                          <mobi:menuButtonItem id="item1a" label="oneA" value="1A"
                                  panelConfirmation="${menuButtonBean.panelConfirmation}"
                                  submitNotification="${menuButtonBean.submitNotification}"/>
                          <mobi:menuButtonItem id="item2a" label="twoA" value="2A"
                                  submitNotification="sn1"/>
                          <mobi:menuButtonItem id="item3a" label="threeA" value="3A"/>
                      </mobi:menuButtonGroup>
                      <mobi:menuButtonGroup label="Group2">
                          <mobi:menuButtonItem id="item1b" label="oneB" value="1B"/>
                          <mobi:menuButtonItem id="item2b" label="twoB" value="2B"
                                  panelConfirmation="pc1"
                                  submitNotification="sn1"/>
                          <mobi:menuButtonItem id="item3b" label="threeB" value="3B"/>
                      </mobi:menuButtonGroup>
                 </mobi:menuButton>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</form:form>

