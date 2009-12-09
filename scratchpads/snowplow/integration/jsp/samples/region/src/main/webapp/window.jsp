<jsp:useBean id="windowNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="session">
</jsp:useBean>
	<jsp:setProperty name="windowNotifier" property="group" value="${param.group}"/>
${windowNotifier.counter}
