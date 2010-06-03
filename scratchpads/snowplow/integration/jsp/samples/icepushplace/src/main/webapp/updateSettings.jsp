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
<%@page import="org.icepush.PushContext"%>
<%@taglib prefix="icep" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
	response.setHeader("Pragma", "no-cache");//HTTP 1.0
	response.setHeader("Expires", "0");//prevents proxy caching
%>
<jsp:useBean id="service" class="org.icepush.place.jsp.services.impl.IcepushPlaceServiceImpl" scope="application">
</jsp:useBean>
<jsp:useBean id="person" class="org.icepush.place.jsp.view.model.Person" scope="session">
</jsp:useBean>
<%
if (person != null) {
	String nickName = request.getParameter("nickName");
        String mood = request.getParameter("mood");
        String comment = request.getParameter("comment");
        String region = request.getParameter("region");
	boolean changed = false;
        System.out.println("nickname: " + nickName + " " + person.getNickname());
        System.out.println("mood: " + mood + " " + person.getMood());
        System.out.println("comment: " + comment + " " + person.getComment());
        System.out.println("region: " + region + " " + person.getRegion());
        if(!person.getNickname().equals(nickName)){
            person.setNickname(nickName);
            changed = true;
        }
        if(!person.getMood().equals(mood)){
            person.setMood(mood);
            changed = true;
        }
        if(!person.getComment().equals(comment)){
            person.setComment(comment);
            changed = true;
        }
        if(!person.getRegion().equals(region)){
            person.setRegion(region);
            changed = true;
        }
        if(changed){
            PushContext pushContext = PushContext.getInstance(getServletContext());
	    pushContext.push("all");
            // Service call to update settings in all applications
            // service.updateSettings(person);
        }
}
%>