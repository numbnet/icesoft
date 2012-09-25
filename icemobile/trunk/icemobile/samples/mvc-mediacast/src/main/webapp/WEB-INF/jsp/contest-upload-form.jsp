<%@ page session="false"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div id="uploadFormContainer" style="margin-top: 20px;">
     
    <form:form id="cameraUploadForm" method="POST" enctype="multipart/form-data"
         modelAttribute="uploadModel" htmlEscape="true" style="text-align:center;">
        <input type="hidden" name="layout" value="${layout}"/>
        <form:errors path="*" cssClass="errorblock" element="div" />
        
        <mobi:getEnhanced/>
        
        <mobi:fieldSetGroup inset="true" style="margin-top: 10px;">
           <mobi:fieldSetRow style="font-weight: bold;text-align: center;color: #326ADB;">
               Upload a photo and you might win something!!!
           </mobi:fieldSetRow>
            <mobi:fieldSetRow style="text-align:center;">
                <mobi:camera id="upload" style="width:70%;vertical-align:top;max-width:200px;"/>
                    <c:if test="${!desktop}">
                        <mobi:thumb for="upload" style="margin: 0 2px"/>
                    </c:if>
             </mobi:fieldSetRow>
             <mobi:fieldSetRow style="min-height:0;">
                 <mobi:inputtext name="email" 
                    styleClass="input"
                    type="email"
                    autoCorrect="off"
                    placeholder="Email"
                    value="${uploadModel.email}"/>
             </mobi:fieldSetRow>
             <mobi:fieldSetRow style="min-height:0;">
                 <mobi:inputtext name="description" 
                    styleClass="input"
                    type="textarea"
                    autoCorrect="off"
                    placeholder="Description"
                    value="${uploadModel.description}"/>
             </mobi:fieldSetRow>
             <mobi:fieldSetRow style="text-align:center;">
                <div id="msg" style="margin: 0 0 8px 0;font-size:12px;">${uploadModel.uploadMsg}</div>
                <input type="reset" class="mobi-button mobi-button-default"
                       value="Cancel" style="width:100px;margin-top:0;margin-bottom:0;"/>
                <mobi:commandButton name="submit"
                        style="float:none;width:100px;margin-top:0;margin-bottom:0;"
                        buttonType="important"
                        value="Share"/>
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
        <mobi:fieldSetGroup inset="true" style="padding:10px;">
            <mobi:fieldSetRow style="text-align:center;padding:0;">
            Learn more about ICEmobile video, audio, QR code, augmented reality
            and rich UI features at <a href="http://www.icesoft.org/projects/ICEmobile/overview.jsf">www.icesoft.org</a>.
            </mobi:fieldSetRow>
        </mobi:fieldSetGroup>
    </form:form>
    <script type="text/javascript">
        enhanceForm("#cameraUploadForm","#uploadFormContainer");
    </script>
</div>