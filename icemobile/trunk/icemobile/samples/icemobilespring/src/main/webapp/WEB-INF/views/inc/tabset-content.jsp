<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="tabsetform" method="POST" modelAttribute="tabsetBean">

	<h2>TabSet Component</h2>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<p>The tabset component provides controls for easily selecting
				which child contentPane component is to be displayed. The tabset
				selection tabs can be orientated on the top or bottom of the child
				content.</p>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:tabSet id="tabsetOne" selectedTab="${tabsetBean.tabsetOne}"
		autoWidth="${viewSize eq 'small'}">
		<mobi:headers>
			<mobi:header>
				<span class="mobitab">Desktop</span>
			</mobi:header>
			<mobi:header>
				<span class="mobitab">Mobile</span>
			</mobi:header>
			<mobi:header>
				<span class="mobitab">Ultra Mobile</span>
			</mobi:header>
		</mobi:headers>
		<mobi:content>
			<mobi:contentPane>
				<div>
					<img src="resources/desktop.png"></img>
				</div>
			</mobi:contentPane>
			<mobi:contentPane>
				<div>
					<img src="resources/laptop.png"></img>
				</div>
			</mobi:contentPane>
			<mobi:contentPane>
				<div>
					<img src="resources/pda.png"></img>
				</div>
			</mobi:contentPane>
		</mobi:content>
	</mobi:tabSet>
</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#tabsetform");

	
</script>