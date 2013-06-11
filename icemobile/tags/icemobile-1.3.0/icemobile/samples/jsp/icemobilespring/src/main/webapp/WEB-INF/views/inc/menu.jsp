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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<mobi:outputList id="menu" inset="false">
    <mobi:outputListItem group="true">Layout and Navigation</mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/accordion"/>">Accordion</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/carousel"/>">Carousel</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/fieldset"/>">Fieldset</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/list"/>">Lists</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/panelconfirmation"/>">Confirmation Panel</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/panelPopup"/>">Panel Popup</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/tabset"/>">Tab Set</a></mobi:outputListItem>
    
    <mobi:outputListItem group="true">Input & Selection</mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/buttons"/>">Buttons</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/datetime"/>">Date Time</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/flipswitch"/>">Flip Switch</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/geolocation"/>">Geolocation</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/inputtext"/>">HTML5 Input</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/menubutton"/>">Menu Button</a></mobi:outputListItem>
    
    <mobi:outputListItem group="true">Media</mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/audioplayer"/>">Audio</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/videoplayer"/>">Video</a></mobi:outputListItem>
    
    <mobi:outputListItem group="true">Native Integration</mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/mediaspot"/>">Augmented Reality</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/camera"/>">Camera</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/camcorder"/>">Camcorder</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/notifications"/>">Cloud Push</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/contact"/>">Contact List</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/microphone"/>">Microphone</a></mobi:outputListItem>
    <mobi:outputListItem><a class="ui-link-inherit" href="<c:url value="/qrscan"/>">QR Codes</a></mobi:outputListItem>
    
</mobi:outputList>
