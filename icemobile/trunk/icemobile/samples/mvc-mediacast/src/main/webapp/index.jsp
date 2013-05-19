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
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE html >
<html>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<body>
    <style type="text/css">
        i{ margin: 10px}
        i:hover{
            color: #549FD8;
        }
        h1{
            border: none !important;
            color: #549FD8;
            text-shadow: 1px 1px 1px #FFF;
        }
    </style>
    <mobi:pagePanel>
        <mobi:pagePanelHeader>
            <mobi:largeView>
                <img src="resources/images/icemobile_thumb.png"
                style="position: absolute;left: 0.5em;top: 3px;" />
            </mobi:largeView>
            MVC Mediacast
        </mobi:pagePanelHeader>
        <mobi:pagePanelBody>
            <div style="text-align:center">
                <mobi:fieldsetGroup style="box-shadow: 0px 2px 20px #888;">
                    <mobi:fieldsetRow>
                        <h1>Welcome to ICEmobile MVC Mediacast</h1>
                    </mobi:fieldsetRow>
                </mobi:fieldsetGroup>
                
                <h3>A media sharing application powered by ICEmobile-JSP and Spring MVC</h3>
                <img src="resources/images/icemobile_large.png" style="margin:15px"/>
                <div>
                    <i class="icon-eye-open"></i>
                    <i class="icon-facetime-video"></i>
                    <i class="icon-cloud-upload"></i>
                    <i class="icon-group"></i>
                </div>
                <a href="app" style="display:inline-block;margin:20px auto;width:60%;text-decoration:none" 
                    class="mobi-button ui-btn-up-c mobi-button-important">Enter</a>
            </div>
        </mobi:pagePanelBody>
    </mobi:pagePanel>
</body>
</html>
