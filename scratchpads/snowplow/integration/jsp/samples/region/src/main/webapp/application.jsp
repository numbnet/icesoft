<jsp:useBean id="applicationNotifier" class="org.icepush.integration.jsp.samples.region.GroupNotificationCounter" scope="application">
</jsp:useBean>
<jsp:setProperty name="applicationNotifier" property="group" value="${param.group}"/>
${applicationNotifier.counter}
