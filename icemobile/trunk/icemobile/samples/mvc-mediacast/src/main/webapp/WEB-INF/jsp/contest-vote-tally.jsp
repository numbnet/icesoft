<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div id="tally">${media.numberOfVotes} Votes so far..</div>
<script type="text/javascript">$('#tally').parent().parent().effect("highlight", {}, 3000);</script>