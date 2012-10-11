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
    window.ice = {};
}
if (!window.ice.mobi) {
    window.ice.mobi = {};
}
//should be in ice.mobi namespace
if (!window['mobi']) {
    window.mobi = {};
}
mobi.BEHAVIOR_EVENT_PARAM = "javax.faces.behavior.event";
mobi.PARTIAL_EVENT_PARAM = "javax.faces.partial.event";
mobi.findForm = function (sourceId) {
    var node = document.getElementById(sourceId);
    while (node.nodeName.toLowerCase() != "form" && node.parentNode) {
        node = node.parentNode;
    }
    ice.log.debug(ice.log, 'parent form node =' + node.name);
    return node;
};
mobi.AjaxRequest = function (cfg) {

    if (cfg.onstart && !cfg.onstart.call(this)) {
        return;//cancel request
    }
    var form = mobi.findForm(cfg.source);
    var source = (typeof cfg.source == 'string') ? document.getElementById(cfg.source) : cfg.source;
	if (!source) {
		if (cfg.node) {
			source = cfg.node;
			source.id = cfg.source;
		}
	}
    if (form.length == 0){
        form = source.form;
         ice.log.debug(ice.log, "had to find form via element form length = "+form.length);
    }
    if (form.length == 0){
       form = document.forms[0]; //just return first form in the page
       ice.log.debug(ice.log, 'had to find first form on page');
    }
    var jsfExecute = cfg.execute || '@all';
    var jsfRender = cfg.render || '@all';

    ice.fullSubmit(jsfExecute, jsfRender, null, source || form[0], function (parameter) {
        if (cfg.event) {
            parameter(mobi.BEHAVIOR_EVENT_PARAM, cfg.event);

            var domEvent = cfg.event;
            if (cfg.event == 'valueChange') {
                domEvent = 'change';
            } else if (cfg.event == 'action') {
                domEvent = 'click';
            }

            parameter(mobi.PARTIAL_EVENT_PARAM, domEvent);
        } else {
            parameter(cfg.source, cfg.source);
        }

        if (cfg.params) {
            var cfgParams = cfg.params;
            for (var p in cfgParams) {
                parameter(p, cfgParams[p]);
            }
        }
    }, function (onBeforeSubmit, onBeforeUpdate, onAfterUpdate, onNetworkError, onServerError) {
        var context = {};
        onAfterUpdate(function (responseXML) {
            if (cfg.onsuccess && !cfg.onsuccess.call(context, responseXML, null /*status*/, null /*xhr*/)) {
                return;
            }
        });
        if (cfg.oncomplete) {
            onAfterUpdate(function (responseXML) {
                cfg.oncomplete.call(context, null /*xhr*/, null /*status*/, context.args);
            });
        }
        if (cfg.onerror) {
            onNetworkError(function (responseCode, errorDescription) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, errorDescription /*error description*/)
            });
            onServerError(function (responseCode, responseText) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, responseText /*error description*/)
            });
        }
    });
};


mobi.registerAuxUpload = function (sessionid, uploadURL) {
    var auxiframe = document.getElementById('auxiframe');
    if (null == auxiframe) {
        auxiframe = document.createElement('iframe');
        auxiframe.setAttribute("id", "auxiframe");
        auxiframe.setAttribute("style", "width:0px; height:0px; border: 0px");
        auxiframe.setAttribute("src",
                "icemobile://c=register&r=" +
                        escape(window.location) + "&JSESSIONID=" + sessionid +
                        "&u=" + escape(uploadURL)
        );
        document.body.appendChild(auxiframe);
    }
};

mobi.panelAutoCenter = function (clientId) {
    var windowWidth = mobi._windowWidth();
    var windowHeight = mobi._windowHeight();
    var scrollTop = document.body.scrollTop;
    if (scrollTop == 0) {
        scrollTop = document.documentElement.scrollTop;
    }
    if (windowHeight > 0) {
        var contentElement = document.getElementById(clientId);
        if( contentElement ){
        	var contentHeight = contentElement.offsetHeight;
            var contentWidth = contentElement.offsetWidth;
            if (windowHeight - contentHeight > 0) {
                contentElement.style.position = 'absolute';
                contentElement.style.top = scrollTop + ((windowHeight / 2) - (contentHeight / 2)) + 'px';
                contentElement.style.left = ((windowWidth / 2) - (contentWidth / 2)) + 'px';
            } else {
                contentElement.style.position = 'absolute';
                contentElement.style.top = 0;
                contentElement.style.left = ((windowWidth / 2) - (contentWidth / 2)) + 'px';
            }
        }
    }
};
mobi._windowHeight = function () {
    var windowHeight = 0;
    if (typeof(window.innerHeight) == 'number') {
        windowHeight = window.innerHeight;
    } else {
        if (document.documentElement && document.documentElement.clientHeight) {
            windowHeight = document.documentElement.clientHeight;
        } else {
            if (document.body && document.body.clientHeight) {
                windowHeight = document.body.clientHeight;
            }
        }
    }
    return windowHeight;
};
mobi._windowWidth = function () {
    var windowWidth = 0;
    if (typeof(window.innerWidth) == 'number') {
        windowWidth = window.innerWidth;
    } else {
        if (document.documentElement && document.documentElement.clientWidth) {
            windowWidth = document.documentElement.clientWidth;
        } else {
            if (document.body && document.body.clientWidth) {
                windowWidth = document.body.clientWidth;
            }
        }
    }
    return windowWidth;
};

ice.mobi.serialize = function(form, typed) {
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

function html5getViewState(form) {
    if (!form) {
        throw new Error("jsf.getViewState:  form must be set");
    }
    return ice.mobi.serialize(form, false);
}

function html5handleResponse(context, data) {
    if (null == context.sourceid) {
        //was not a jsf upload
        return;
    }

    var jsfResponse = {};
    var parser = new DOMParser();
    var xmlDoc = parser.parseFromString(data, "text/xml");

    jsfResponse.responseXML = xmlDoc;
    jsf.ajax.response(jsfResponse, context);

}


function html5submitFunction(element, event, options) {
    var source = event ? event.target : element;
    var form = element;
    while ((null != form) && ("form" != form.tagName.toLowerCase())) {
        form = form.parentNode;
    }
    var formData = new FormData(form);
    var formId = form.id;
    var sourceId = element ? element.id : event.target.id;

    if (options.execute) {
        var executeArray = options.execute.split(' ');
        if (executeArray.indexOf("@none") < 0) {
            if (executeArray.indexOf("@all") < 0) {
                options.execute = options.execute.replace("@this", element.id);
                options.execute = options.execute.replace("@form", form.id);
                if (executeArray.indexOf(element.name) < 0) {
                    options.execute = element.name + " " + options.execute;
                }
            } else {
                options.execute = "@all";
            }
        }
    } else {
//        options.execute = element.name + " " + element.id;
        //ICEfaces default render @all
        options.execute = "@all";
    }

    if (options.render) {
        var renderArray = options.render.split(' ');
        if (renderArray.indexOf("@none") < 0) {
            if (renderArray.indexOf("@all") < 0) {
                options.render = options.render.replace("@this", element.id);
                options.render = options.render.replace("@form", form.id);
            } else {
                options.render = "@all";
            }
        }
    } else {
        //ICEfaces default execute @all
        options.execute = "@all";
    }

    formData.append("javax.faces.source", sourceId);
    formData.append(source.name, source.value);
    formData.append("javax.faces.partial.execute", options.execute);
    formData.append("javax.faces.partial.render", options.render);
    formData.append("javax.faces.partial.ajax", "true");

    if (event) {
        formData.append("javax.faces.partial.event", event.type);
    }

    if (options) {
        for (var p in options) {
            if ("function" != typeof(options[p])) {
                formData.append(p, options[p]);
            }
        }
    }

    var context = {
        sourceid: sourceId,
        formid: formId,
        //'begin' event is not triggered since we do not invoke jsf.ajax.request -- onBeforeSubmit relies on this event
        onevent: options.onevent,
        onerror: options.onerror || function (param) {
            alert("JSF error " + param.source + " " + param.description);
        }
    };

    var xhr = new XMLHttpRequest();
    xhr.open("POST", form.getAttribute("action"));
    xhr.setRequestHeader("Faces-Request", "partial/ajax");
    xhr.onreadystatechange = function () {
        if ((4 == xhr.readyState) && (200 == xhr.status)) {
            html5handleResponse(context, xhr.responseText);
        }
    };
    xhr.send(formData);
}

if (window.addEventListener) {
    window.addEventListener("load",
            function () {
                jsf.getViewState = html5getViewState;
                if ((undefined !== window.FormData) &&
                        (undefined === window.ice.mobile) &&
                        ((undefined === window.clientInformation) || ("BlackBerry" !== window.clientInformation.platform)) &&
                        (undefined !== window.Worker)) {
                    ice.submitFunction = html5submitFunction;
                }
            }, false);

    window.addEventListener("pagehide", function () {
        if (ice.push) {
            ice.push.connection.pauseConnection();
        }
    }, false);

    window.addEventListener("pageshow", function () {
        if (ice.push) {
            ice.push.connection.resumeConnection();
        }
    }, false);
}
/* javascript for mobi:commandButton component put into component.js as per MOBI-200 */
mobi.button = {
    select: function(clientId, cfg){
        var params = cfg.params || null;
        if (cfg.snId){
            mobi.submitnotify.open(cfg.snId, clientId, cfg.singleSubmit, params);
            return;
            //if here, then no panelConfirmation as this action is responsible for submit
        }
        //otherwise, just check for behaviors, singleSubmit and go
        var singleSubmit = cfg.singleSubmit || false;
        var hasBehaviors = cfg.behaviors;
        if (hasBehaviors){
            //show the submitNotification panel
            if (cfg.behaviors.click){
                cfg.behaviors.click();
            }
            return;  //ensure no other submits
        }
        var event = cfg.event;
        if (!event){
            event = window.event;
        }
        if (singleSubmit){
            ice.se(event, clientId, params);
        } else {
            ice.s(event, clientId, params);
        }
    }
};

ice.mobi.formOf = function(element) {
    var parent = element;
    while (null != parent) {
        if ("form" == parent.nodeName.toLowerCase()) {
            return parent;
        }
        parent = parent.parentNode;
    }
}

ice.mobi.sx = function (element) {
    var ampchar = String.fromCharCode(38);
    var form = ice.mobi.formOf(element);
    var formAction = form.getAttribute("action");
    var command = element.getAttribute("data-command");
    var id = element.getAttribute("data-id");
    var ub = element.getAttribute("data-ub");
    if ((null == id) || ("" == id))  {
        id = element.getAttribute("id");
    }
    var params = element.getAttribute("data-params");
    var windowLocation = window.location;
    var barURL = windowLocation.toString();
    var baseURL = barURL.substring(0,
            barURL.lastIndexOf("/")) + "/";
    var ubConfig = "";
    if ((null != ub) && ("" != ub))  {
        if ("." === ub)  {
            ubConfig = "ub=" + escape(baseURL) + ampchar;
        } else {
            ubConfig = "ub=" + escape(ub) + ampchar;
        }
    }
    var uploadURL;
    if (0 === formAction.indexOf("/")) {
        uploadURL = window.location.origin + formAction;
    } else if ((0 === formAction.indexOf("http://")) ||
            (0 === formAction.indexOf("https://"))) {
        uploadURL = formAction;
    } else {
        uploadURL = baseURL + formAction;
    }

    var returnURL = window.location;
    if ("" == returnURL.hash) {
        returnURL += "#icemobilesx";
    }

    if ("" != params) {
        params = ubConfig + params;
    }

    var sxURL = "icemobile://c=" + escape(command +
            "?id=" + id + ampchar + params) +
            "&u=" + escape(uploadURL) + "&r=" + escape(returnURL) +
            "&p=" + escape(ice.mobi.serialize(form, false));

    window.location = sxURL;
}

ice.mobi.storeLocation = function(id, coords)  {
    if (!coords) { return; } 
    var el = document.getElementById(id);
    if (!el)  {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {parts = new Array(4)};
    parts[0] = coords.latitude;
    parts[1] = coords.longitude;
    parts[2] = coords.altitude;
    el.value = parts.join();
}

ice.mobi.storeDirection = function(id, orient)  {
    if (orient.webkitCompassAccuracy <= 0)  {
        return;
    }
    var el = document.getElementById(id);
    if (!el)  {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {parts = new Array(4)}
    parts[3] = orient.webkitCompassHeading;
    el.value = parts.join();
}
/* add js marker for progressive enhancement */
document.documentElement.className = document.documentElement.className+' js';
