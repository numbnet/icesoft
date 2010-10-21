<%--
 *
 * Version: MPL 1.1
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
  --%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="org.icepush.PushContext,org.icepush.ws.samples.icepushplace.PersonType"%>
<%@taglib prefix="icep" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>
<jsp:useBean id="world" class="org.icepush.ws.samples.icepushplace.wsclient.ICEpushPlaceWorld" scope="application">
</jsp:useBean>
<jsp:useBean id="person" class="org.icepush.ws.samples.icepushplace.PersonType" scope="session">
</jsp:useBean>

<%
if (person != null) {
    String messageOut = request.getParameter("msgOut");
    int region = Integer.parseInt(request.getParameter("region"));
    int row = Integer.parseInt(request.getParameter("row"));
    String from = request.getParameter("from");
    PersonType receiver = null;
    receiver = world.getContinent(region).get(row);

    // Set receiver message
    receiver.setMessageIn(from + " says: " + messageOut);
    world.updatePerson(receiver.getRegion(), receiver);
}
%>