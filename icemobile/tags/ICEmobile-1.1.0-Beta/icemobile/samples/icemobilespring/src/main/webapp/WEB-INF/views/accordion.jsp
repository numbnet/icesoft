<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ICEmobile | Accordion demo</title>
	<link href="<c:url value="/resources/style.css" />" rel="stylesheet"
              type="text/css"/>
    <script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="code.icepush"></script>
</head>
<body>
</c:if>
    <div class="ajaxzone">

		<h2>Accordion</h2>
		<form:form id="accordionform" method="POST"  modelAttribute="accordionBean" cssClass="cleanform">

            <mobi:accordion id="accordionOne" selectedId="${accordionBean.accordionOne}" autoheight="true"  >
                <mobi:content>
                    <mobi:contentPane title="Fixed (desktop) items" id="tab1" >

                        <div>
                          <img src="resources/desktop.png"></img>
                        </div>
                    </mobi:contentPane>
                    <mobi:contentPane title="Mobile items" id="tab2">
                         <div>
                          <img src="resources/laptop.png"></img>
                        </div>
                    </mobi:contentPane>
                    <mobi:contentPane title="Ultra mobile items" id="tab3">
                        <div>
                          <img src="resources/pda.png"></img>
                        </div>
                    </mobi:contentPane>
                </mobi:content>
            </mobi:accordion>
		</form:form>
    <script type="text/javascript">
        MvcUtil.enhanceForm("#accordionform");
    </script>
	</div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>