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

        namespace.progress = function(amount)  {
          //remove return line below, return true, and customize
          //progress indicator within application
          return false;
          var canvas = document.getElementById('progMeterCanvas');
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
          return true;
        }
        
        namespace.native = function(command)  {
            getIPCIframe().src ='js-api:' + command;
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
                form.removeChild(tempInputs[i]);
            }
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

            context.source = source;
            context.sourceid = sourceId;
            context.formid = formId;
            context.element = element;
            context.onevent = options.onevent;
            context.onerror = options.onerror;
            ice.upload(formId);

        };

        namespace.formOf = function(element)  {
            var parent = element;
            while (null != parent)  {
                if ("form" == parent.nodeName.toLowerCase())  {
                    return parent;
                }
                parent = parent.parentNode;
            }
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

        namespace.setThumbnail = function(id, value)  {
            var imageTag = document.getElementById(id);
            if (!imageTag)  {
                return;
            }
            imageTag.setAttribute("src", value);
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
                            prefix="selectmultiple-";
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

//iOS specific code follows

        namespace.camera = function(id, atts)  {
            ice.native('camera?id=' + id + (atts ? '&' + atts : ''));
        }

        namespace.camcorder = function(id)  {
            ice.native('camcorder?id=' + id);
        }

        namespace.microphone = function(id)  {
            ice.native('microphone?id=' + id);
        }

        namespace.upload = function(id)  {
            context.serialized = ice.serialize(id, true);
            ice.native('upload?id=' + id);
        }

        namespace.open = function(id, atts)  {
            ice.native('open?id=' + id + (atts ? '&' + atts : ''));
        }

        namespace.play = function(id)  {
            ice.native('play?id=' + id);
        }

        namespace.scan = function(id)  {
            ice.native('scan?id=' + id);
        }

        namespace.aug = function(id, atts)  {
            ice.native('aug?id=' + id + (atts ? '&' + atts : ''));
        }

        namespace.fetchContacts = function(id, atts)  {
            ice.native('fetchContacts?id=' + id + (atts ? '&' + atts : ''));
        }

        namespace.deviceToken = "cafebeef";

    })(window.ice)


    function addConnectionStatus()  {
        var croot = document.createElement("canvas");
        croot.setAttribute("width", "30");
        croot.setAttribute("height", "30");
        croot.setAttribute("style", "position:fixed; top:0; right:0; z-index:9999;");
        croot.setAttribute("id", "progMeterCanvas");
               
        document.body.appendChild(croot);

        ice.progress(0);
    }

    function getIPCIframe()  {
        var ipciframe = document.getElementById('ipciframe');
        if (null != ipciframe)  {
            return ipciframe;
        }
        ipciframe = document.createElement('iframe');
        ipciframe.setAttribute("id", "ipciframe");
        ipciframe.setAttribute("style", "width:0px; height:0px; border: 0px");
        document.body.appendChild(ipciframe);
        return ipciframe;
    }

    function init()  {
        getIPCIframe();
        addConnectionStatus();
    }
    //use below if loaded over network vs embedded use to eval this file
//    document.addEventListener("DOMContentLoaded", init, false);
    init();
}

