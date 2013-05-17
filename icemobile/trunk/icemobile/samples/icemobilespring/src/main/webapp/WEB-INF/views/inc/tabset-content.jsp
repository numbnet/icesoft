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
<form:form id="tabsetform" method="POST" modelAttribute="tabsetBean" cssClass="tabsetpage">

    <mobi:tabSet id="tabsetOne" parentHeader="true"
                            selectedId="${tabsetBean.selectedTab}" fixedPosition="true">
        <mobi:tabPane id="tab1" title="Ice Sailer">
            <%@ include file="fieldset-icesailor.jsp"%>
        </mobi:tabPane>
        <mobi:tabPane id="tab2" title="ICE Breaker">
            <%@ include file="fieldset-icebreaker.jsp"%>
        </mobi:tabPane>
        <mobi:tabPane id="tab3" title="ICE Skate">
            <%@ include file="fieldset-iceskate.jsp"%>
        </mobi:tabPane>
    </mobi:tabSet>

	<mobi:largeView><h3>Tabset</h3></mobi:largeView>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow styleClass="intro">
			The tabset component provides a tabbed layout and controls for easily selecting which child contentPane 
			component is to be displayed. 
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	
</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#tabsetform");
</script>