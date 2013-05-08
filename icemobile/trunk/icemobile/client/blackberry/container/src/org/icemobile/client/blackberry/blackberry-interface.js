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

if (!window.ice) {
    window.ice = new Object;
}

if (!window.ice.mobile) {
    (function(namespace) {
        namespace.mobile = true;

        namespace.native = function(command) {
            getIPCIframe().src = 'js-api:' + command;
        }

        namespace.progress = function(amount) {
            var canvas = document.getElementById('progMeterCanvas');
            if (canvas.getContext) {
                var ctx = canvas.getContext('2d');
                ctx.clearRect(0, 0, 200, 200);
                ctx.beginPath();
                var theta = ((Math.PI * 2) * amount) / 100;
                ctx.moveTo(15, 15);
                ctx.arc(15, 15, 12, 0, theta, false);
                ctx.fillStyle = '#0B5383';
                ctx.fill();
                ctx.beginPath();
                ctx.arc(15, 15, 12, 0, theta, false);
                ctx.strokeStyle = 'gray';
                ctx.lineWidth = 2;
                ctx.stroke();
            }
        }

        namespace.setThumbnail = function(id, value) {
            var imageTag = document.getElementById(id);
            if (!imageTag) {
                return;
            }
            imageTag.setAttribute("src", value);
        }

        // ---------------  Javascript media interfaces -----------------

        // TakePhoto function is asynchronous so we cannot add hidden field on return
        // that method is invoked instead from javascript extension
        namespace.camera = function(id, attr) {
            try {
                var result = icefaces.shootPhoto(id, attr);
                return result;
            } catch (e) {
                alert("Exception in camera:" + e);
            }
        }

        // video capture is asynchronous so addHiddenField is called from script extension
        namespace.camcorder = function(id, attr) {
            var result = icefaces.shootVideo(id, attr);
        }
        
        namespace.fetchContacts = function(id, attr) { 
        	var result = icefaces.fetchContacts(id, attr);
        	icefaces.logInContainer("Contact at js layer: " + result); 
        	return result; 
        } 

        namespace.microphone = function(id, attr) {

            try {
                /* var result = icefaces.toggleMic(id, attr); */ 
         		var result = icefaces.recordAudio( id, attr );
            } catch (e) {
                alert('Exception in microphone: ' + e);
            }
        }

        namespace.play = function(id, attr) {
            var result = icefaces.playUrl(id, "autorelease=true");
        }

        namespace.currentScanId = "";
        namespace.scan = function(id, attr) {
            var result = icefaces.scan(id, attr);
            ice.currentScanId = id;
        }
        
        namespace.logInContainer = function(message) { 
            icefaces.logInContainer(message); 
        } 

        namespace.test = function(obj) {
            try {

                icefaces.logInContainer("Starting test...");
                if (namespace.push && namespace.push.pauseBlockingConnection) {
                    icefaces.logInContainer("pauseConnection present");
                } else {
                    icefaces.logInContainer("pauseConnection missing");
                }

                if (icefaces.shootPhoto) {
                    icefaces.logInContainer("shootPhoto present");
                } else {
                    icefaces.logInContainer("shootPhoto missing");
                }

            } catch (e) {
                icefaces.logInContainer("Exception testing namespace: " + e);
            }
        }

        // ----------------- Various ----------------------
        // private don't use
        namespace.addHiddenFormField = function(target, name, value) {
        	var targetElm = document.getElementById(target); 
        	var hidden  = document.createElement("input"); 
        	hidden.setAttribute("type", "hidden"); 
        	hidden.setAttribute("name", name); 
        	hidden.setAttribute("value", value); 
        	targetElm.appendChild(hidden); 
            return hidden;
        }
        
        namespace.addHidden = function(target, name, value, vtype ) { 
        	var hiddenID = name + "-hid"; 
        	var existing = document.getElementById(hiddenID);
        	if (existing) { 
        	    existing.parentNode.removeChild(existing); 
        	} 
        	var targetElm = document.getElementById(target); 
        	var hidden = document.createElement("input"); 
        	hidden.setAttribute("type", "hidden"); 
        	hidden.setAttribute("id", hiddenID); 
        	hidden.setAttribute("name", name); 
        	hidden.setAttribute("value", value); 
        	if (vtype) { 
        		hidden.setAttribute("data-type", vtype); 
            } 
            targetElm.parentNode.insertBefore(hidden, targetElm); 
        } 

        //assume single threaded access with this context object spanning request/response
        var context = {
            onevent: null,
            onerror: function(param) {
//                alert("JSF error " + param.source + " " + param.description);
            }
        }
        var tempInputs = [];

        //override primitive submit function
        
        namespace.submitFunction = function(element, event, options) {
        
            var source = event ? event.target : element;
            var form = ice.formOf(element);
            var formId = form.id;
            var sourceId = element ? element.id : event.target.id;

            if ("@this" === options.execute) {
                options.execute = sourceId;
            } else if ("@form" === options.execute) {
                options.execute = formId;
            }
            if ("@this" === options.render) {
                options.render = sourceId;
            } else if ("@form" === options.render) {
                options.render = formId;
            }
            if (!options.execute) {
                options.execute = "@all";
            }
            if (!options.render) {
                options.render = "@all";
            }

            tempInputs = [];
            tempInputs.push(ice.addHiddenFormField(formId,
                    "javax.faces.source", sourceId));
            tempInputs.push(ice.addHiddenFormField(formId,
                    "javax.faces.partial.execute", options.execute));
            tempInputs.push(ice.addHiddenFormField(formId,
                    "javax.faces.partial.render", options.render));
            tempInputs.push(ice.addHiddenFormField(formId,
                    "javax.faces.partial.ajax", "true"));
            if (event) {
                tempInputs.push(ice.addHiddenFormField(formId,
                        "javax.faces.partial.event", event.type));
            }

            if (options) {
                for (var p in options) {
                    tempInputs.push(ice.addHiddenFormField(formId, p, options[p]));
                }
            }

            context.source = source;
            context.sourceid = sourceId;
            context.formid = formId;
            context.element = element;
            context.onevent = options.onevent;
            context.onerror = options.onerror;
            //context.serialized = ice.serialize(form.id);
            
            ice.upload(formId);

        }
        
        namespace.upload = function(id) {
            try {
            	var form = document.getElementById( id ); 
                context.serialized = ice.serialize(id); 
                var result = icefaces.ajaxUpload( form.action, context.serialized);

            } catch (e) {
                alert('Exception in ajaxUpload: ' + e);
            }
        }        
       

        namespace.handleResponse = function(data, isSimulator) {

            if (isSimulator) {
                icefaces.logInContainer("handleResponse - is Simulator");
            }
            try {
                if (null == context.sourceid) {
                    //was not a jsf upload
                    return;
                }
                var jsfResponse = {};
                var parser = new DOMParser();
                var xmlDoc = parser.parseFromString(unescape(data), "text/xml");

                jsfResponse.responseXML = xmlDoc;
                jsf.ajax.response(jsfResponse, context);

                var form = document.getElementById(context.formid);

                if (form != null) {
                    for (var i in tempInputs) {
                        //icefaces.logInContainer("handleResponse - Clearing input " + tempInputs[i]);
                        form.removeChild(tempInputs[i]);
                    }
                }
                context.source = null;
                context.sourceid = "";
                context.formid = "";
                context.serialized = "";
                context.element = null;
                context.onevent = null;
                context.onerror = null;
            } catch (e) {
            	
                //icefaces.logInContainer("Exception in handleResponse: " + e);
            }
        }


        namespace.formOf = function(element) {
            var parent = element;
            while (null != parent) {
                if ("form" == parent.nodeName.toLowerCase()) {
                    return parent;
                }
                parent = parent.parentNode;
            }
        }

        

        namespace.deviceToken = "blackberrybeef";

        namespace.serialize = function(formId) {
            var form = document.getElementById(formId);
            var els = form.elements;
            var len = els.length;
            var qString = [];
            var addField = function(name, value) {
                var tmpStr = "";
                if (qString.length > 0) {
                    tmpStr = "&";
                }
                tmpStr += encodeURIComponent(name) + "=" + encodeURIComponent(value);
                qString.push(tmpStr);
            };
            for (var i = 0; i < len; i++) {
                var el = els[i];
                if (!el.disabled) {
                    switch (el.type) {
                        case 'submit':
                        case 'button':
                        case 'fieldset':
                            break;
                        case 'text':
                        case 'password':
                        case 'hidden':
                        case 'textarea':
                            addField(el.name, el.value);
                            break;
                        case 'select-one':
                            if (el.selectedIndex >= 0) {
                                addField(el.name, el.options[el.selectedIndex].value);
                            }
                            break;
                        case 'select-multiple':
                            for (var j = 0; j < el.options.length; j++) {
                                if (el.options[j].selected) {
                                    addField(el.name, el.options[j].value);
                                }
                            }
                            break;
                        case 'checkbox':
                        case 'radio':
                            if (el.checked) {
                                addField(el.name, el.value);
                            }
                            break;
                        default:
                            addField(el.name, el.value);
                    }
                }
            }
            // concatenate the array
            return qString.join("");

        }

        namespace.mobiRefresh = function()  {
            if (window.ice.ajaxRefresh)  {
                window.setTimeout(function() {
                    ice.push.resumeBlockingConnection();
                    ice.ajaxRefresh();
                }, 50);
                return;
            }
            window.location.reload();
            return;
        }

    })(window.ice)

    function addConnectionStatus() {
        var croot = document.createElement("canvas");
        croot.setAttribute("width", "30");
        croot.setAttribute("height", "30");
        croot.setAttribute("style", "position:absolute; top:0; right:0;");
        croot.setAttribute("id", "progMeterCanvas");

        document.body.appendChild(croot);
        ice.progress(0);

    }

    function getIPCIframe() {
        var ipciframe = document.getElementById('ipciframe');
        if (null != ipciframe) {
            return ipciframe;
        }
        ipciframe = document.createElement('iframe');
        ipciframe.setAttribute("id", "ipciframe");
        ipciframe.setAttribute("style", "width:0px; height:0px; border: 0px");
        document.body.appendChild(ipciframe);
        return ipciframe;
    }

    function init() {
        // getIPCIframe();
        // document.body.appendChild(document.createTextNode("ice.mobile functions enabled."));
        addConnectionStatus();

    }

    //use below if loaded over network vs embedded use to eval this file
    // document.addEventListener("DOMContentLoaded", init, false);
    init();

}


