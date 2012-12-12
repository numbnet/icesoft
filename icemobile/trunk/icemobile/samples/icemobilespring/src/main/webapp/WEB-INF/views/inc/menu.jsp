<%--
  ~ Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
    <mobi:outputListItem><a href="accordion">Accordion</a></mobi:outputListItem>
    <mobi:outputListItem><a href="carousel">Carousel</a></mobi:outputListItem>
    <mobi:outputListItem><a href="fieldset">Fieldset</a></mobi:outputListItem>
    <mobi:outputListItem><a href="list">Lists</a></mobi:outputListItem>
    <mobi:outputListItem><a href="panelPopup">Panel Popup</a></mobi:outputListItem>
    <mobi:outputListItem><a href="tabset">Tab Set</a></mobi:outputListItem>
    
    <mobi:outputListItem group="true">Input & Selection</mobi:outputListItem>
    <mobi:outputListItem><a href="buttons">Buttons</a></mobi:outputListItem>
    <mobi:outputListItem><a href="datetime">Date Time</a></mobi:outputListItem>
    <mobi:outputListItem><a href="flipswitch">Flip Switch</a></mobi:outputListItem>
    <mobi:outputListItem><a href="geolocation">Geolocation</a></mobi:outputListItem>
    <mobi:outputListItem><a href="inputtext">HTML5 Input</a></mobi:outputListItem>
    
    <mobi:outputListItem group="true">Native Integration</mobi:outputListItem>
    <mobi:outputListItem><a href="mediaspot">Augmented Reality</a></mobi:outputListItem>
    <mobi:outputListItem><a href="camera">Camera</a></mobi:outputListItem>
    <mobi:outputListItem><a href="camcorder">Camcorder</a></mobi:outputListItem>
    <mobi:outputListItem><a href="notifications">Cloud Push</a></mobi:outputListItem>
    <mobi:outputListItem><a href="contact">Contact List</a></mobi:outputListItem>
    <mobi:outputListItem><a href="microphone">Microphone</a></mobi:outputListItem>
    <mobi:outputListItem><a href="qrscan">QR Codes</a></mobi:outputListItem>
    
</mobi:outputList>
