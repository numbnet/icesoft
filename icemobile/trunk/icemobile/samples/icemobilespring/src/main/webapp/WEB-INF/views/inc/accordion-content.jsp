<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="accordionform" method="POST" modelAttribute="accordionBean">

    <h2>Accordion Component</h2>

     <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
            The accordion component shows one active content pane at time.
            Non-active content panes are represented as collapsed views.
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <h3>Basic Accordion</h3>

    <mobi:accordion id="basicAccordion" selectedId="${accordionBean.selectedId1}">
        <mobi:content>
            <mobi:contentPane title="ICE Sailor" id="accordionPane1" >
                <%@ include file="inc-icesailor.jsp" %>
            </mobi:contentPane>
            <mobi:contentPane title="ICE Breaker" id="accordionPane2">
                 <%@ include file="inc-icebreaker.jsp" %>
            </mobi:contentPane>
            <mobi:contentPane title="ICE Skate" id="accordionPane3">
                <%@ include file="inc-iceskate.jsp" %>
            </mobi:contentPane>
        </mobi:content>
    </mobi:accordion>
    
    <h3>Accordion without autoHeight</h3>
    
    <mobi:accordion id="accordionNoAutoHeight" selectedId="${accordionBean.selectedId2}"
        autoHeight="false">
        <mobi:content>
            <mobi:contentPane title="ICE Sailor" id="accordionPane4" >
                <%@ include file="inc-icesailor.jsp" %>
            </mobi:contentPane>
            <mobi:contentPane title="ICE Breaker" id="accordionPane5">
                 <%@ include file="inc-icebreaker.jsp" %>
            </mobi:contentPane>
            <mobi:contentPane title="ICE Skate" id="accordionPane6">
                <%@ include file="inc-iceskate.jsp" %>
            </mobi:contentPane>
        </mobi:content>
    </mobi:accordion>
    
</form:form>
<script type="text/javascript">
    MvcUtil.enhanceForm("#accordionform");
</script>