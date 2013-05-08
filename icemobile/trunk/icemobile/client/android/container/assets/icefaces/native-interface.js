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

        namespace.setThumbnail = function(id, value) {
            var imageTag = document.getElementById(id);
            if (!imageTag) {
                return;
            }
            imageTag.setAttribute("src", value);
        }

        namespace.aug = function(id, attr) {
            var result = window.ARView.arView(id, attr);
        }

        namespace.currentContactId = "";
        namespace.fetchContacts = function(id, attr) {

            ice.currentContactId = id;
            var result = window.ICEContacts.fetchContact(id, attr);
            return result;
        }

        namespace.camera = function(id, attr) {
            alert('into camera');
            var result = window.ICEcamera.shootPhoto(id, attr);
            ice.addHidden(id, id, "" + result, 'file');
        }

        namespace.camcorder = function(id, attr) {
            var result = window.ICEvideo.shootVideo(id + '-thumb', attr);
            ice.addHidden(id, id, "" + result, 'file');
        }

        namespace.microphone = function(id, attr) {
            var result = window.ICEaudio.recordAudio(attr);
            ice.addHidden(id, id, "" + result, 'file');
        }

        namespace.play = function(id) {
            window.ICEaudio.playUrl(id, true);
        }

        namespace.currentScanId = "";
        namespace.scan = function(id, attr) {
            var result = window.ICEqrcode.scan(id, attr);
            ice.currentScanId = id;
        }

        //assume single threaded access with this context object spanning request/response
        var context = {
            onevent: null,
            onerror: function(param) {
                alert("JSF error " + param.source + " " + param.description);
            }
        }

        function encodeParam(name, value)  {
            return "&" + escape("hidden-" + name) + "=" + escape(value);
        }

        namespace.handleResponse = function(data) {
            if (null == context.sourceid) {
                //was not a jsf upload
                return;
            }

            var jsfResponse = {};
            var parser = new DOMParser();
            var xmlDoc = parser.parseFromString(unescape(data), "text/xml");

            jsfResponse.responseXML = xmlDoc;
            jsf.ajax.response(jsfResponse, context);

            context.source = null;
            context.sourceid = "";
            context.formid = "";
            context.serialized = "";
            context.element = null;
            context.onevent = null;
            context.onerror = null;
        }

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

            var serialTail = "";
            serialTail += encodeParam("javax.faces.source", sourceId);
            serialTail += encodeParam("javax.faces.partial.execute", options.execute);
            serialTail += encodeParam("javax.faces.partial.render", options.render);
            serialTail += encodeParam("javax.faces.partial.ajax", "true");
            if (event) {
                serialTail += encodeParam("javax.faces.partial.event", event.type);
            }
            if (options) {
                for (var p in options) {
                    if ("function" != typeof(options[p])) {
                        serialTail += encodeParam(p, options[p]);
                    }
                }
            }

            context.source = source;
            context.sourceid = sourceId;
            context.formid = formId;
            context.element = element;
            context.onevent = options.onevent;
            context.onerror = options.onerror;
            ice.upload(formId, serialTail);
        };

        namespace.formOf = function(element) {
            var parent = element;
            while (null != parent) {
                if ("form" == parent.nodeName.toLowerCase()) {
                    return parent;
                }
                parent = parent.parentNode;
            }
        }

        namespace.upload = function(id, serialTail) {
            var form = document.getElementById(id);
            context.serialized = ice.serialize(id, true);
            // Mobi-512 Guard against people calling ice.upload from
            // someplace other than the submit function
            if (serialTail) {
                window.ICEutil.submitForm(form.action, context.serialized +
                    serialTail);
            } else {
                window.ICEutil.submitForm(form.action, context.serialized );
            }
        }

        namespace.addHidden = function(target, name, value, vtype) {
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

        namespace.addHiddenFormField = function(target, name, value) {
            var targetElm = document.getElementById(target);
            var hidden = document.createElement("input");
            hidden.setAttribute("type", "hidden");
            hidden.setAttribute("name", name);
            hidden.setAttribute("value", value);
            targetElm.appendChild(hidden);
            return hidden;
        }

        namespace.putHiddenFormField = function(target, name, value) {
            var targetElm = document.getElementById(target);
            var existing = document.getElementById(target);
            if (existing) {
                existing.parentNode.removeChild(existing);
            }
            var hidden = document.createElement("input");
            hidden.setAttribute("type", "hidden");
            hidden.setAttribute("name", name);
            hidden.setAttribute("value", value);
            targetElm.appendChild(hidden);
            return hidden;
        }

        namespace.getCurrentSerialized = function() {
            return context.serialized;
        }

        namespace.serialize = function(formId, typed) {
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
                    if (typed) {
                        var vtype = el.getAttribute("data-type");
                        if (vtype) {
                            prefix = vtype + "-";
                        } else {
                            prefix = el.type + "-";
                        }
                    }
                    switch (el.type) {
                        case 'submit':
                        case 'button':
                        case 'fieldset':
                            break;
                        case 'text':
                        case 'password':
                        case 'hidden':
                        case 'textarea':
                            addField(prefix + el.name, el.value);
                            break;
                        case 'select-one':
                            prefix = "selectone-";
                            if (el.selectedIndex >= 0) {
                                addField(prefix + el.name, el.options[el.selectedIndex].value);
                            }
                            break;
                        case 'select-multiple':
                            prefix = "selectmultiple-";
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
                            if (el.type)  {
                                addField(prefix + el.name, el.value);
                            }
                    }
                }
            }
            // concatenate the array
            return qString.join("");
        }

        namespace.mobiRefresh = function() {
            if (window.ice.ajaxRefresh) {
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

    function init() {
    }

    //use below if loaded over network vs embedded use to eval this file
//    document.addEventListener("DOMContentLoaded", init, false);
    init();
}

