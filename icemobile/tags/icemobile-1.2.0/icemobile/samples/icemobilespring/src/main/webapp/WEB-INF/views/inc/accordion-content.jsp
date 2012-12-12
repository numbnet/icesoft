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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="accordionform" method="POST"
	modelAttribute="accordionBean">

	<c:if test="${viewSize eq 'large'}">
		<h3>Accordion</h3>
	</c:if>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The accordion tag shows one active content pane at time. Non-active content panes are represented as collapsed views. 
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<h3>Accordion with autoHeight</h3>

	<mobi:accordion id="basicAccordion"
		selectedId="${accordionBean.selectedId1}">
		<mobi:accordionPane title="ICE Sailor" id="accordionPane1">
			<%@ include file="inc-icesailor.jsp"%>
		</mobi:accordionPane>
		<mobi:accordionPane title="ICE Breaker" id="accordionPane2">
			<%@ include file="inc-icebreaker.jsp"%>
		</mobi:accordionPane>
		<mobi:accordionPane title="ICE Skate" id="accordionPane3">
			<%@ include file="inc-iceskate.jsp"%>
		</mobi:accordionPane>
	</mobi:accordion>

	<h3 style="margin-top: 30px;">Accordion with fixedHeight</h3>

	<mobi:accordion id="accordionNoAutoHeight"
		selectedId="${accordionBean.selectedId2}" autoHeight="false"
		height="200px">
		<mobi:accordionPane title="ICE Sailor" id="accordionPane4">
			<%@ include file="inc-icesailor.jsp"%>
		</mobi:accordionPane>
		<mobi:accordionPane title="ICE Breaker" id="accordionPane5">
			<%@ include file="inc-icebreaker.jsp"%>
		</mobi:accordionPane>
		<mobi:accordionPane title="ICE Skate" id="accordionPane6">
			<%@ include file="inc-iceskate.jsp"%>
		</mobi:accordionPane>
	</mobi:accordion>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The accordion tag manages a stack of accordionPane's and
				will show one active pane at a time. The attribute "selectedId"
				determines which of the panes in the stack is currently visible. All
				other non-selected accordionPane's are represented as collapsed
				views.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>
</form:form>
<script type="text/javascript">
	MvcUtil.enhanceForm("#accordionform");
</script>