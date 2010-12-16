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

var ice_fileEntry = {
    iframeLoaded : function(context, id) {
        //alert("iframeLoaded()  begin");
        
        var i = document.getElementById(id);
        if ((typeof XMLDocument != "undefined") && i.contentDocument) {
            //var doc = i.contentDocument.document.XMLDocument ?
            //    i.contentDocument.document.XMLDocument :
            //    i.contentDocument.document;
            var d = i.contentDocument;
            if (d instanceof XMLDocument || d.xmlEncoding) {
                var serializer = new XMLSerializer();
                var responseText = serializer.serializeToString(d);
                //i.contentDocument.document.body?i.contentDocument.document.body.innerHTML:null;
                ice_fileEntry.response(d, responseText, context);
            }
        } else if (i.contentWindow &&
                   i.contentWindow.document) {
			var d = i.contentWindow.document.XMLDocument ?
                i.contentWindow.document.XMLDocument :
                i.contentWindow.document;
            if (d.xml) {
                var responseText = d.xml;
                //i.contentWindow.document.body?ii.contentWindow.document.body.innerHTML:null;
                ice_fileEntry.response(d, responseText, context);
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
    
    captureFormOnsubmit : function(formId, iframeId, progressPushId, progressResourcePath) {
        var f = document.getElementById(formId);
        var encodedURL = f.elements['javax.faces.encodedURL'];
        if(encodedURL){
            f.action = encodedURL.value;
        }
        f.onsubmit = function(event) {
            ice_fileEntry.formOnsubmit(event, f, iframeId, progressPushId, progressResourcePath);
        };
    },
    
    formOnsubmit : function(event, formElem, iframeId, progressPushId, progressResourcePath) {
        //alert("formOnsubmit()  begin");

        // Set every fileEntry component in the form into the indeterminate
        // state, before progress notifications arrive, if icepush is present
        ice_fileEntry.setFormFileEntryStates(formElem, "uploading");

        if (progressPushId) {
            //alert("formOnsubmit()  progressPushId: " + progressPushId);

            //POLL: Comment this section
            var regPushIds = new Array(1);
            regPushIds[0] = progressPushId;
            window.ice.push.register(regPushIds, function(pushedIds) {
                ice_fileEntry.onProgress(pushedIds, progressResourcePath);
            });
        }

        //TODO To get context.sourceid, use on of the following techniques
        //Firefox || Opera || IE || unsupported (No WebKit)
        //var orignalSource = event.explicitOriginalTarget || event.relatedTarget || document.activeElement || {};

        //var submitted = e.originalEvent.explicitOriginalTarget || e.originalEvent.relatedTarget || document.activeElement;
        //Look if it was a text node (IE bug)
        //submitted = submitted.nodeType == 1 ? submitted : submitted.parentNode;

        var context = {};
        context.sourceid = "";             //TODO Not sure how to get this
        context.formid = formElem.id;
        context.render = "@all";
        var context_execute = formElem.id; // Don't do "@all" or else FacesMessagePhaseListener doesn't work
        
        var oldEncoding;
        if (formElem.encoding) {
            oldEncoding = formElem.encoding;
            formElem.encoding = 'multipart/form-data';
        }
        else {
            oldEncoding = formElem.enctype;
            formElem.enctype = 'multipart/form-data';
        }
        
        var hSrc = ice_fileEntry.addHiddenInput(
            formElem, 'javax.faces.source', context.sourceid);
        var hParEx = ice_fileEntry.addHiddenInput(
            formElem, 'javax.faces.partial.execute', context_execute);
        var hParRend = ice_fileEntry.addHiddenInput(
            formElem, 'javax.faces.partial.render', context.render);
        var hParAjax = ice_fileEntry.addHiddenInput(
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

            // Set every fileEntry component in the form into the indeterminate
            // state, before progress notifications arrive, if icepush is present
            ice_fileEntry.setFormFileEntryStates(formElem, "complete");

            ice_fileEntry.iframeLoaded(context, iframeId);
            
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

            if (progressPushId) {
                //POLL: Comment this section
                /*
                var unregPushIds = new Array(1);
                unregPushIds[0] = progressPushId;
                window.ice.push.deregister(unregPushIds);
                */
            }

            //alert("onload()  end");
        };
        if (iframeElem.addEventListener) {
            iframeElem.addEventListener("load",iframeOnloadHandler,false);
        }
        else if (iframeElem.attachEvent) {
            iframeElem.attachEvent("onload",iframeOnloadHandler);
        }

        /*
        //POLL: Uncomment this section
        var progressTimeout = function() {
            ice_fileEntry.onProgress(null, progressResourcePath);
            setTimeout(progressTimeout, 2000);
        };
        setTimeout(progressTimeout, 2000);
        */

        //alert("formOnsubmit()  end");
    },
        
    onProgress : function(pushIds, progressResourcePath) {
        //alert('onProgress()  progressResourcePath: ' + progressResourcePath);

        //var fileDiv2 = document.getElementById('fileform:fileEntryComp');
        //var span2 = document.createElement('span');
        //span2.innerHTML = "P";
        //fileDiv2.appendChild(span2);

        window.ice.push.post(progressResourcePath, function(parameter) {}, function(statusCode, contentAsText, contentAsDOM) {
            //alert('onProgress()  GET  contentAsText: ' + contentAsText);
            var progressInfo = eval("(" + contentAsText + ")");
            var percent = progressInfo['percent'];
            var percentStr = percent + "%";
            var results = progressInfo['results'];
            var res;
            for (res in results) {
                var fileDiv = document.getElementById(results[res]);
                if (fileDiv) {
                    var outerDiv = fileDiv.childNodes[1];
                    if (outerDiv) {
                        var progDiv = outerDiv.firstChild;
                        if (progDiv) {
                            progDiv.style.width = percentStr;
                        }
                        //if (percent == 100) {
                        //    outerDiv.className = "complete";
                        //} else {
                            outerDiv.className = "progress";
                        //}
                    }
                    //var span = document.createElement('span');
                    //span.innerHTML = percentStr;
                    //fileDiv.appendChild(span);
                }
            }
        });
    },

    addHiddenInput : function(formElem, hiddenName, val) {
        var inputElem = document.createElement('input');
        inputElem.setAttribute('type','hidden');
        inputElem.setAttribute('name',hiddenName);
        inputElem.setAttribute('value',val);
        formElem.appendChild(inputElem);
        return inputElem;
    },

    setFormFileEntryStates : function(formElem, className) {
        var fileEntryDivs = ice_fileEntry.getElementsByClass(
                "ice-file-entry",formElem,"div");
        var fileEntryIndex;
        for (fileEntryIndex in fileEntryDivs) {
            var fileDiv = fileEntryDivs[fileEntryIndex];
            if (fileDiv) {
                var outerDiv = fileDiv.childNodes[1];
                if (outerDiv) {
                    var progDiv = outerDiv.firstChild;
                    if (progDiv) {
                        progDiv.style.width = "100%";
                    }
                    outerDiv.className = className;
                }
            }
        }
    },

    getElementsByClass : function(searchClass,node,tag) {
        var classElements = new Array();
        if (node == null) {
            node = document;
        }
        if (tag == null) {
            tag = '*';
        }
        var els = node.getElementsByTagName(tag);
        var elsLen = els.length;
        var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
        for (i = 0, j = 0; i < elsLen; i++) {
            if ( pattern.test(els[i].className) ) {
                classElements[j] = els[i];
                j++;
            }
        }
        return classElements;
    }
};
