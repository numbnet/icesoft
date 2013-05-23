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
    uri="http://www.icepush.org/icepush/jsp/icepush.tld"
%>
<form:form id="audioPlayerForm" modelAttribute="audioBean"  method="POST">

    <mobi:fieldsetGroup>
       <mobi:fieldsetRow>
           <label class="ui-input-text">Audio Controls: </label>
           <mobi:flipswitch id="fs2" labelOn="true" labelOff="false" style="float:right"
                            name="controls" value="${audioBean.controls}"/>&#160;
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label class="ui-input-text">Loop audio: </label>
           <mobi:flipswitch id="fs3" labelOn="true" labelOff="false" style="float:right"
                            name="loop" value="${audioBean.loop}"/>&#160;
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label class="ui-input-text">Auto play audio:</label>
           <mobi:flipswitch id="fs4" labelOn="true" labelOff="false" style="float:right"
                            name="autoPlay" value="${audioBean.autoPlay}"/>&#160;
       </mobi:fieldsetRow>
       <mobi:fieldsetRow>
           <label class="ui-input-text">Mute audio:</label>
           <mobi:flipswitch id="mutfs" labelOn="true" labelOff="false" style="float:right"
                            name="muted" value="${audioBean.muted}"/>&#160;
       </mobi:fieldsetRow>
        <mobi:fieldsetRow>
            <label class="ui-input-text">Link Label:</label>
            <mobi:inputText name="linkLabel" type="text" autoCorrect="off" style="width:50%;float:right"
                placeholder="Link label" value="${audioBean.linkLabel}" />
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <mobi:commandButton buttonType="important" styleClass="submit"
            value="Submit" type="submit" />
            
    <h3>Audio Player</h3>

    <mobi:fieldsetGroup>
        <mobi:fieldsetRow style="text-align:center">
            <mobi:audioPlayer id="audioOne"
                            url="${audioBean.src}"
                            style="width:50%"
                            type="${audioBean.type}"
                            loop = "${audioBean.loop}"
                            preload="true"
                            muted="${audioBean.muted}"
                            linkLabel="${audioBean.linkLabel}"
                            controls="${audioBean.controls}"/>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
</form:form>


<script type="text/javascript">
    MvcUtil.enhanceForm("#audioPlayerForm");
    mobi.AjaxRequest = function( options){
        if (options.jspForm){
            $(options.jspForm).submit();
        }
    };
</script>
