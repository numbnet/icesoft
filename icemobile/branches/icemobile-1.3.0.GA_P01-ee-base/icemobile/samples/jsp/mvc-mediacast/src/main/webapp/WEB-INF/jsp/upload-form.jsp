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
<div id="uploadFormContainer">
     
    <form:form id="uploadForm" method="POST" enctype="multipart/form-data" 
        htmlEscape="true">
        <input type="hidden" name="fullPost" id="fullPost" value="false"/>
        <input type="hidden" name="operation" id="operation"/>
        <form:errors path="*" cssClass="errorblock" element="div" />
        
        <mobi:getEnhanced/>
        
        <mobi:fieldsetGroup inset="true">
           <mobi:fieldsetRow style="font-weight: bold;color: #326ADB;text-align:center">
                Share a photo, video clip, or audio recording.
           </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
        
        <div id="msg" style="clear: both;margin:10px;font-size:12px;color:#326ADB;font-weight:bold">${msg}</div>
        
        <mobi:fieldsetGroup inset="true">
             <mobi:fieldsetRow>
                <c:if test="${!sxPhotoUploadReady}">
                    <c:if test="${!mobiClient.enhancedBrowser}">
                        <label for="camera" style="width:33%;display:inline-block">Image file:</label>
                    </c:if>
                    <mobi:camera id="camera" />
                </c:if>
                <c:if test="${!sxVideoUploadReady}">
                    <c:if test="${!mobiClient.enhancedBrowser}">
                        <br/><label for="camcorder" style="width:33%;display:inline-block">Video file:</label>
                    </c:if>
                    <mobi:camcorder id="camcorder"/>
                </c:if>
                <c:if test="${!sxAudioUploadReady}">
                    <c:if test="${!mobiClient.enhancedBrowser}">
                        <br/><label for="microphone" style="width:33%;display:inline-block">Audio file:</label>
                    </c:if>
                    <mobi:microphone id="microphone" buttonLabel="Audio"/>
                </c:if>
             </mobi:fieldsetRow>  
             <mobi:fieldsetRow>
                 <c:if test="${!sessionScope['sxRegistered']}">
                    <mobi:thumbnail for="camera"/>
                </c:if>
                <c:if test="${sxPhotoUploadReady and not empty sessionScope['sxPhotoUpload']}">
                    <img src='resources/uploads/${sxThumbnail.name}'/>
                </c:if>
                <c:if test="${sxVideoUploadReady}">
                    <img src='resources/images/movieIcon.png' style="height:85px;width:81px"/>
                </c:if>
                <c:if test="${sxAudioUploadReady}">
                    <img src='resources/images/soundIcon.png' style="height:85px;width:81px"/>
                </c:if>
             </mobi:fieldsetRow>     
             <mobi:fieldsetRow>
                 <mobi:inputText name="description" 
                    styleClass="input"
                    type="textarea"
                    autoCorrect="off"
                    placeholder="Description" style="width:100%"/>
             </mobi:fieldsetRow>
             <mobi:fieldsetRow style="text-align:center">
                <mobi:commandButton id="cancelBtn"
                       value="Cancel" style="width:45%;margin:1%"
                       />
                <mobi:commandButton value="Share" buttonType="important"
                    style="width:45%;margin:1%"/>
             </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
        <mobi:fieldsetGroup>
            <mobi:fieldsetRow>
            Learn more about ICEmobile video, audio, QR code, augmented reality
            and rich UI features at <a href="http://www.icesoft.org/projects/ICEmobile/overview.jsf">www.icesoft.org/projects/ICEmobile/</a>.
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
    </form:form>
    <script type="text/javascript">
        enhanceForm();
        cancelBtnHandler();
        window.onhashchange = function()  {
            if ("#icemobilesx" === window.location.hash)  {
                window.location.hash = "";
                window.location.reload();
            }
        }
        window.scrollTo(0, 0);
    </script>
</div>