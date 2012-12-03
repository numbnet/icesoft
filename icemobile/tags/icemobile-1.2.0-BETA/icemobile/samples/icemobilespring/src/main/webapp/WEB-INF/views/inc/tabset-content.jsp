<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="tabsetform" method="POST" modelAttribute="tabsetBean">

	<c:if test="${viewSize eq 'large'}"><h3>Tabset</h3></c:if>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			The tabset provides controls for easily selecting
			which child contentPane tag is to be displayed. The tabset
			selection tabs can be oriented on the top or bottom of the child
			content for handhelds.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:tabSet id="tabsetOne" selectedTab="${tabsetBean.tabsetOne}"
		autoWidth="${viewSize eq 'small'}">
		<mobi:headers>
			<mobi:header>
				<span class="mobitab">Ice Sailer</span>
			</mobi:header>
			<mobi:header>
				<span class="mobitab">Ice Breaker</span>
			</mobi:header>
			<mobi:header>
				<span class="mobitab">Ice Skater</span>
			</mobi:header>
		</mobi:headers>
		<mobi:content>
			<mobi:contentPane>
				<%@ include file="fieldset-icesailor.jsp"%>
			</mobi:contentPane>
			<mobi:contentPane>
				<%@ include file="fieldset-icebreaker.jsp"%>
			</mobi:contentPane>
			<mobi:contentPane>
				<%@ include file="fieldset-iceskate.jsp"%>
			</mobi:contentPane>
		</mobi:content>
	</mobi:tabSet>
</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#tabsetform");

	
</script>