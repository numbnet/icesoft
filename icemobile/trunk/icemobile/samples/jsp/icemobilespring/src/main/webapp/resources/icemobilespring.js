/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

MvcUtil = {};
MvcUtil.showSuccessResponse = function (text, element) {
    MvcUtil.showResponse("success", text, element);
};
MvcUtil.showErrorResponse = function showErrorResponse(text, element) {
    MvcUtil.showResponse("error", text, element);
};
MvcUtil.showResponse = function (type, text, element) {
    var responseElementId = element.attr("id") + "Response";
    var responseElement = $("#" + responseElementId);
    if (responseElement.length == 0) {
        responseElement = $('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>').insertAfter(element);
    } else {
        responseElement.replaceWith('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>');
        responseElement = $("#" + responseElementId);
    }
    responseElement.fadeIn("slow");
};
MvcUtil.xmlencode = function (xml) {
    //for IE
    var text;
    if (window.ActiveXObject) {
        text = xml.xml;
    }
    // for Mozilla, Firefox, Opera, etc.
    else {
        text = (new XMLSerializer()).serializeToString(xml);
    }
    return text.replace(/\&/g, '&' + 'amp;').replace(/</g, '&' + 'lt;')
            .replace(/>/g, '&' + 'gt;').replace(/\'/g, '&' + 'apos;').replace(/\"/g, '&' + 'quot;');
};
MvcUtil.enhanceForm = function(theForm)  {
    //submitting the form will update 
    //the containing div with class ajaxzone
    $(document).ready(function () {
        $(theForm).click(function (e) {
            window.iceButtonTracker = e.target;
        });
        $(theForm).submit(function () {
            var updateRegion = $(this).closest("div.ajaxzone");
            if (window.ice && ice.upload) {
                window.ice.handleResponse = function (data) {
                    var tracker = document
                            .getElementById("iceButtonTrackerHidden");
                    if (tracker) {
                        tracker.parentNode.removeChild(tracker);
                    }
                    updateRegion.html(unescape(data));
                }
                if (window.iceButtonTracker) {
                    $('<input>').attr({
                        type:'hidden',
                        id:'iceButtonTrackerHidden',
                        name:window.iceButtonTracker.name,
                        value:window.iceButtonTracker.value
                    }).appendTo($(this));
                    window.iceButtonTracker = null;
                }
                ice.upload($(this).attr("id"));
                return false;
            }
            
            try{
                var formData;
                var mimeType = false;
                if ((undefined !== window.FormData) && 
                   (!window.clientInformation || 
                       ("BlackBerry" !== window.clientInformation.platform)) )  {
                    formData = new FormData(this);
                    formData.append("x-ignored-field","makeie10work");
                } else {
                    formData = $(theForm).serialize();
                    mimeType = "application/x-www-form-urlencoded";
                }

console.log("submitting form");
                $.ajax({
                    url:$(this).attr("action"),
                    data:formData,
                    cache:false,
                    contentType:mimeType,
                    processData:false,
                    type:'POST',
                    success:function (html) {
                        updateRegion.html(html);
                        var msgElem = $("#message");
                        if( msgElem.length > 0 ){
                            $('html, body').animate({ scrollTop:msgElem.offset().top }, 500);
                        }
                    }
                });
            }
            catch(err){
                if( window.console ){
                    console.error(err);
                }
                else{
                    alert(err);
                }
            }
            return false;
        });
    });
};
MvcUtil.addClickLinkHandler = function(link, updateRegion){
    $(link).click( function(e) {
        if( window.history && window.history.pushState ){
            $('#menu').find('a').each( function(){
                this.className = '';
            });
            $(updateRegion).load(link.href, function(){ 
                ice.mobi.sizePagePanelsToViewPort();
                $('html, body').animate({ scrollTop:0 }, 400);
            });
            history.pushState({ src: link.href }, null, link.href);
            e.preventDefault();
        }
      });
}
MvcUtil.enhanceLink = function(link, updateRegion)  {
    $(document).ready(function () {
        MvcUtil.addClickLinkHandler(link,updateRegion);
    });
};
MvcUtil.enhanceAllLinks = function(parent, updateRegion)  {
    $(document).ready(function () {
        $(parent).find('a').each( function(){
            MvcUtil.addClickLinkHandler(this,updateRegion);
        });
    });
};
MvcUtil.enhanceAllLinksWithRemoveGeolocationWatch = function(parent)  {
    $(document).ready(function () {
        $(parent).find('a').each( function(){
            $(this).click( function(e) { ice.mobi.geolocation.clearWatch(); } );
        });
    });
};
window.onload = function() {
    if( window.history && window.history.pushState ){
        window.setTimeout(function() {
            window.addEventListener("popstate", function(e) {
                if (ice.mobi.getDeviceCommand())  {
                    return;
                }
                $('.ajaxzone').load(location.pathname);
            }, false);
        }, 1);
    }
}

