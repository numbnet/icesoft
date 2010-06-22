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
<jsp:useBean id="regions" class="org.icepush.place.jsp.view.model.Regions" scope="application">
</jsp:useBean>
<jsp:useBean id="person" class="org.icepush.ws.samples.icepushplace.PersonType" scope="session">
</jsp:useBean>

<%
if (person != null) {
    String nickName = request.getParameter("nickName");
    String mood = request.getParameter("mood");
    String comment = request.getParameter("comment");
    String region = request.getParameter("region");
    boolean changed = false;
    if(!person.getName().equals(nickName)){
        person.setName(nickName);
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
    if(person.getKey() != Integer.parseInt(region)){
        // Remove from previous region
        switch(person.getKey()){
            case 1: regions.getNorthAmerica().remove(person);break;
            case 2: regions.getEurope().remove(person);break;
            case 3: regions.getSouthAmerica().remove(person);break;
            case 4: regions.getAsia().remove(person);break;
            case 5: regions.getAfrica().remove(person);break;
            case 6: regions.getAntarctica().remove(person);break;
            default: System.out.println("Problem Removing Person from Region");
        }
        // Add to new region
        switch(Integer.parseInt(region)){
            case 1: regions.getNorthAmerica().add(person);break;
            case 2: regions.getEurope().add(person);break;
            case 3: regions.getSouthAmerica().add(person);break;
            case 4: regions.getAsia().add(person);break;
            case 5: regions.getAfrica().add(person);break;
            case 6: regions.getAntarctica().add(person);break;
            default: System.out.println("Problem Adding Person to Region");
        }
        // Push to remove from old region
        //PushContext pushContext = PushContext.getInstance(getServletContext());
        //pushContext.push(person.getRegion());
        service.updateWorld(person.getKey());
        // Set person in new region
        person.setKey(Integer.parseInt(region));
        changed = true;
    }
    if(changed){
        // Push to update region
        //PushContext pushContext = PushContext.getInstance(getServletContext());
        //pushContext.push(person.getRegion());
        service.updateWorld(person.getKey());
        // TODO: WILL BE REPLACED WITH SOMETHING LIKE:
        // Service call to display message in all applications
        // service.requestUpdate(person);
        // THE SERVICE CALL WILL HAVE TO CHECK THE PERSON'S REGION.
        // IF IT HAS CHANGED, A PUSH WILL HAVE TO BE CALLED ON THE OLD REGION AS WELL.
    }
}
%>