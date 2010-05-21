/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

var ice_inputFiles = {
    iframeLoaded : function(context, id) {
        //alert("iframeLoaded()  begin");
        
        var i = document.getElementById(id);
        if (i.contentDocument) {
            //var doc = i.contentDocument.document.XMLDocument ?
            //    i.contentDocument.document.XMLDocument :
            //    i.contentDocument.document;
            var d = i.contentDocument;
            if (d instanceof XMLDocument) {
                var serializer = new XMLSerializer();
                var responseText = serializer.serializeToString(d);
                //i.contentDocument.document.body?i.contentDocument.document.body.innerHTML:null;
                ice_inputFiles.response(d, responseText, context);
            }
        } else if (i.contentWindow &&
                   i.contentWindow.document) {
			var d = i.contentWindow.document.XMLDocument ?
                i.contentWindow.document.XMLDocument :
                i.contentWindow.document;
            if (d.xml) {
                var responseText = d.xml;
                //i.contentWindow.document.body?ii.contentWindow.document.body.innerHTML:null;
                ice_inputFiles.response(d, responseText, context);
            }
        }
        
        //alert("iframeLoaded()  end");
	},
        
    response : function(responseXML, responseText, context) {
        //alert(responseText);
        
        var request = {};
        request.status = 200;
        request.responseText = responseText;
        request.responseXML = responseXML;
        
        jsf.ajax.response(request, context);
    },
    
    captureFormOnsubmit : function(formId, iframeId) {
        var f = document.getElementById(formId);
        f.onsubmit = function(event) {
            ice_inputFiles.formOnsubmit(event, f, iframeId);
        };
    },
    
    formOnsubmit : function(event, formElem, iframeId) {
        //alert("formOnsubmit()  begin");
        
        var context = {};
        context.sourceid = "";        // "fileForm:commandSubmit"; //TODO Not sure how to get this
        context.formid = formElem.id;
        context.render = "@all";      // "fileForm:testGrid";
        var context_execute = "@all"; // "fileForm:testGrid";
        
        var oldEncoding;
        if (formElem.encoding) {
            oldEncoding = formElem.encoding;
            formElem.encoding = 'multipart/form-data';
        }
        else {
            oldEncoding = formElem.enctype;
            formElem.enctype = 'multipart/form-data';
        }
        
        var hSrc = ice_inputFiles.addHiddenInput(
            formElem, 'javax.faces.source', context.sourceid);
        var hParEx = ice_inputFiles.addHiddenInput(
            formElem, 'javax.faces.partial.execute', context_execute);
        var hParRend = ice_inputFiles.addHiddenInput(
            formElem, 'javax.faces.partial.render', context.render);
        var hParAjax = ice_inputFiles.addHiddenInput(
            formElem, 'javax.faces.partial.ajax', 'true');
        
        formElem.target = iframeId;
        var iframeElem = document.getElementById(iframeId);
        var iframeOnloadHandler = function() {
            //alert("onload()  begin");

            // Cleanup the form before proceeding
            if (formElem.encoding) {
                formElem.encoding = oldEncoding;
            } else {
                formElem.enctype = oldEncoding;
            }
            formElem.target = "_self";
            
            // This worked in FF, but not IE 8, so use explicit vars below
            // formElem.removeChild( formElem.elements['javax.faces.source'] );
            
            formElem.removeChild( hSrc );
            formElem.removeChild( hParEx );
            formElem.removeChild( hParRend );
            formElem.removeChild( hParAjax );
            
            ice_inputFiles.iframeLoaded(context, iframeId);
            
            // Cleanup the response iframe, after finished using it
            if (iframeElem.removeEventListener) {
                iframeElem.removeEventListener("load", iframeOnloadHandler, false);
            } else if (iframeElem.detachEvent) {
                iframeElem.detachEvent("onload", iframeOnloadHandler);
            }
            
            if (iframeElem.hasChildNodes()) {
                while (iframeElem.childNodes.length >= 1) {
                    iframeElem.removeChild(iframeElem.firstChild);
                }
            }

            //alert("onload()  end");
        };
        if (iframeElem.addEventListener) {
            iframeElem.addEventListener("load",iframeOnloadHandler,false);
        }
        else if (iframeElem.attachEvent) {
            iframeElem.attachEvent("onload",iframeOnloadHandler);
        }

        //alert("formOnsubmit()  end");
    },
        
    addHiddenInput : function(formElem, hiddenName, val) {
        var inputElem = document.createElement('input');
        inputElem.setAttribute('type','hidden');
        inputElem.setAttribute('name',hiddenName);
        inputElem.setAttribute('value',val);
        formElem.appendChild(inputElem);
        return inputElem;
    }
};
