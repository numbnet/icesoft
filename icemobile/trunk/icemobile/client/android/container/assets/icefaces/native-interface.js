/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
        
        namespace.progress = function(amount)  {
            var canvas = document.getElementById('progMeterCanvas');
            if (null == canvas)  {
                return;
            }
            if (canvas.getContext){
                var ctx = canvas.getContext('2d');
                ctx.clearRect(0,0,200, 200);
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

        namespace.setThumbnail = function(id, value)  {
            var imageTag = document.getElementById(id);
            if (!imageTag)  {
                return;
            }
            imageTag.setAttribute("src", value);
        }

        namespace.aug = function(id, attr)  {
            var result = window.ARView.arView(id ,attr);					
        }

        namespace.camera = function(id, attr)  {
	    alert('into camera');
            var result = window.ICEcamera.shootPhoto(id ,attr);					
            ice.addHidden(id, id, "" + result, 'file');
        }

        namespace.camcorder = function(id, attr)  {
            var result = window.ICEvideo.shootVideo(id + '-thumb', attr);
            ice.addHidden(id, id, "" + result, 'file');
        }

        namespace.microphone = function(id, attr)  {
            var result = window.ICEaudio.recordAudio(attr);
            ice.addHidden(id, id, "" + result, 'file');
        }

        namespace.play = function(id)  {
	    window.ICEaudio.playUrl(id, true);
        }

        namespace.currentScanId = "";
        namespace.scan = function(id, attr)  {
            var result = window.ICEqrcode.scan(id, attr);
            ice.currentScanId = id;
        }

        //assume single threaded access with this context object spanning request/response
        var context = {
            onevent: null,
            onerror: function(param)  {
                alert("JSF error " + param.source + " " + param.description);
            }
        }
        var tempInputs = [];

        namespace.handleResponse = function(data)  {
            if (null == context.sourceid)  {
                //was not a jsf upload
                return;
            }

            var jsfResponse = {};
            var parser = new DOMParser();
            var xmlDoc = parser.parseFromString(unescape(data), "text/xml");

            jsfResponse.responseXML = xmlDoc;
            jsf.ajax.response(jsfResponse, context);

            var form = document.getElementById(context.formid);
            for (var i in tempInputs)  {
                if (form == tempInputs[i].parentNode)  {
                    form.removeChild(tempInputs[i]);
                }
            }
            context.sourceid = "";
            context.formid = "";
            context.serialized = "";
        }

        //override primitive submit function
        namespace.submitFunction = function(element, event, options) {
            var source = event ? event.target : element;
            var form = ice.formOf(element);
            if (form.elements['javax.faces.source'])  {
                //submit is in progress, but callback not completed by container
                return;
            }
            var formId = form.id;
            var sourceId = element ? element.id : event.target.id;

            if ("@this" === options.execute)  {
                options.execute = sourceId;
            } else if ("@form" === options.execute)  {
                options.execute = formId;
            }
            if ("@this" === options.render)  {
                options.render = sourceId;
            } else if ("@form" === options.render)  {
                options.render = formId;
            }
            if (!options.execute)  {
                options.execute = "@all";
            }
            if (!options.render)  {
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
                    if ("function" != typeof(options[p]))  {
                        tempInputs.push(
                                ice.addHiddenFormField(formId, p, options[p]));
                    }
                }
            }

            context.sourceid = sourceId;
            context.formid = formId;
            ice.upload(formId);
        };

        namespace.formOf = function(element)  {
            var parent = element;
            while (null != parent)  {
                if ("form" == parent.nodeName.toLowerCase()) {
                    return parent;
                }
                parent = parent.parentNode;
            }
        }

        namespace.upload = function(id)  {
            var form = document.getElementById(id);
            context.serialized = ice.serialize(id, true);
            window.ICEutil.submitForm(form.action, context.serialized);
        }

        namespace.addHidden = function(target, name, value, vtype)  {
            var hiddenID = name + "-hid";
            var existing = document.getElementById(hiddenID);
            if (existing)  {
                existing.parentNode.removeChild(existing);
            }
            var targetElm = document.getElementById(target);
            var hidden = document.createElement("input");
            hidden.setAttribute("type", "hidden");
            hidden.setAttribute("id", hiddenID);
            hidden.setAttribute("name", name);
            hidden.setAttribute("value", value);
            if (vtype)  {
                hidden.setAttribute("data-type", vtype);
            }
            targetElm.parentNode.insertBefore(hidden, targetElm);
        }

        namespace.addHiddenFormField = function(target, name, value)  {
            var targetElm = document.getElementById(target);
            var hidden = document.createElement("input");
            hidden.setAttribute("type", "hidden");
            hidden.setAttribute("name", name);
            hidden.setAttribute("value", value);
            targetElm.appendChild(hidden);
            return hidden;
        }

        namespace.getCurrentSerialized = function()  {
            return context.serialized;
        }

        namespace.serialize = function(formId, typed)  {
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
                    var prefix = "";
                    if (typed)  {
                        var vtype = el.getAttribute("data-type");
                        if (vtype)  {
                            prefix = vtype + "-";
                        } else {
                            prefix = el.type + "-";
                        }
                    }
                    switch (el.type) {
                        case 'submit':
                        case 'button':
                            break;
                        case 'text':
                        case 'password':
                        case 'hidden':
                        case 'textarea':
                            addField(prefix + el.name, el.value);
                            break;
                        case 'select-one':
                            if (el.selectedIndex >= 0) {
                                addField(prefix + el.name, el.options[el.selectedIndex].value);
                            }
                            break;
                        case 'select-multiple':
                            for (var j = 0; j < el.options.length; j++) {
                                if (el.options[j].selected) {
                                    addField(prefix + el.name, el.options[j].value);
                                }
                            }
                            break;
                        case 'checkbox':
                        case 'radio':
                            if (el.checked) {
                                addField(prefix + el.name, el.value);
                            }
                            break;
                        default:
                            addField(prefix + el.name, el.value);
                    }
                }
            }
            // concatenate the array
            return qString.join("");
        }

    })(window.ice)

    function addConnectionStatus()  {
        var croot = document.createElement("canvas");
        croot.setAttribute("width", "30");
        croot.setAttribute("height", "30");
        croot.setAttribute("style", "position:fixed;top:0;right:0;z-index:990;");
        croot.setAttribute("id", "progMeterCanvas");

        document.body.appendChild(croot);

        ice.progress(100);
    }

    function init()  {
//        var iframe = document.createElement('iframe');
//        iframe.setAttribute("id", "ipciframe");
//        iframe.setAttribute("frameborder", "0");
//        iframe.setAttribute("style", "width: 0; height: 0; border: 0;position:fixed;top:0;left:0;z-index:1;");
//        document.body.appendChild(iframe);
        addConnectionStatus();
    }
    //use below if loaded over network vs embedded use to eval this file
//    document.addEventListener("DOMContentLoaded", init, false);
    init();
}

