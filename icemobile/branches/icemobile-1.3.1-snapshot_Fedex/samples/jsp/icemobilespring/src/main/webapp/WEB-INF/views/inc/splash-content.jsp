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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>
<%@ taglib prefix="push" uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<div id="splash">
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow styleClass="intro">
            <figure>
                <img
                        style="width:125px;height:125px;"
                        src="<c:url value="/resources/qrcode.png"/>"/>
            </figure>
            <header>
                <h3>ICEmobile Showcase</h3>
                <h4>ICEmobile 1.3</h4>
                <h5>ICEsoft Technologies Inc.</h5>
            </header>
            
            <mobi:smallView>
                <a href="<c:url value="/menu"/>" id="menuLink2" class="ui-link examplesLink">View the examples</a>
                <script type="text/javascript">
                    MvcUtil.enhanceLink(document.getElementById('menuLink2'),"body");
                </script>
            </mobi:smallView>
    
            <p class="first-para">A show case of the ICEmobile-JSP Suite where you can experience all
                the ICEmobile tags in action. Each tag includes a working example,
                descriptions on how to use it, and source code resources. This demo is
                optimized for mobile, but can be viewed using any browser. The ICEmobile
                Container is not required, but is recommended to fully experience the
                capabilities of native integration.</p>
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
    
    <h3>ICEmobile Overview</h3>
    
    <mobi:fieldsetGroup>
        <mobi:fieldsetRow>
    
            <p>ICEmobile is an open source project for the development of hybrid mobile applications using pure web-based techniques. ICEmobile supports a range of Java EE presentation layer technologies, but this sample application highlights ICEmobile's integration with JSF and the ICEfaces Framework.</p>
    
            <p>ICEmobile leverages mobile browsers' support for HTML5, CSS3, and Ajax to create web-based applications where the client presentation matches the device's native look and feel, but is delivered via a secure, enterprise-strength, Java backend. The web-based approach ensures seamless cross-platform support for today's most advanced smart phones and tablets. ICEpush is integrated into the ICEmobile environment to deliver revolutionary, real-time, collaborative capabilities to your mobile application, and includes extensions for mobile, cloud-based push mechanisms. And where HTML5 capabilities are not enough, ICEmobile offers native device containers that extend browser capabilities, providing seamless integration with native device capabilities such as the camera, camcorder and microphone, to produce hybrid applications without any native development required.</p>
    
            <p>Java Enterprise standards are at the heart of ICEmobile, so Enterprise Java developers can leverage existing core competencies, development tools, and best practices to deliver solutions that span the entire spectrum from desktop to mobile devices. ICEmoible's integration with the ICEfaces framework is featured in this application, providing an excellent example of how to build hybrid mobile applications in JSP.</p>
    
        </mobi:fieldsetRow>
    </mobi:fieldsetGroup>
</div>