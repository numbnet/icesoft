<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="tabsetform" method="POST" modelAttribute="tabsetBean" cssClass="tabsetpage">

	<c:if test="${viewSize eq 'large'}">
		<h3>Tabset</h3>
	</c:if>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow styleClass="intro">
			The tabset component provides a tabbed layout.
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

<mobi:fieldsetGroup>
	<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The tabset tag provides controls for easily selecting which child contentPane 
			component is to be displayed. 
		</mobi:fieldsetRow>
</mobi:fieldsetGroup>

<script type="text/javascript">
	MvcUtil.enhanceForm("#tabsetform");
</script>