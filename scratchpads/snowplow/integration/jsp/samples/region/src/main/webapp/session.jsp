<jsp:useBean id="sessionNotifier" class="org.icepush.integration.jsp.samples.region.IntervalTrigger" scope="session">
</jsp:useBean>
	<jsp:setProperty name="sessionNotifier" property="group" value="${param.group}"/>
${sessionNotifier.counter}
