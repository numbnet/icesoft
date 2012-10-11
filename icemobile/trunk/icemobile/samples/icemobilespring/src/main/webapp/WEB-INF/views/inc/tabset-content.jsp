<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="tabsetform" method="POST"  modelAttribute="tabsetBean" >

    <mobi:tabset id="tabsetOne" selectedTab="${tabsetBean.tabsetOne}">
        <mobi:headers>
            <mobi:header><span class="mobitab">Desktop</span></mobi:header>
            <mobi:header><span class="mobitab">Mobile</span></mobi:header>
            <mobi:header><span class="mobitab">Ultra Mobile</span></mobi:header>
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
    </mobi:tabset>
</form:form>

<script type="text/javascript">
MvcUtil.enhanceForm("#tabsetform");
</script>