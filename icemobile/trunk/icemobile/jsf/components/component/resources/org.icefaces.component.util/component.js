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
    window.ice = {};
}
if (!window.ice.mobi) {
    window.ice.mobi = {};
}
//should be in ice.mobi namespace
if (!window['mobi']) {
    window.mobi = {};
}

if (!window.console) {
    console = {};
    if (ice.logInContainer) {
        console.log = ice.logInContainer;
    } else {
        log = function() {
        };
    }
}
ice.mobi.BUTTON_UNPRESSED = " ui-btn-up-a";
ice.mobi.BUTTON_PRESSED = " ui-btn-down-a";
mobi.BEHAVIOR_EVENT_PARAM = "javax.faces.behavior.event";
mobi.PARTIAL_EVENT_PARAM = "javax.faces.partial.event";
/* utilities*/
ice.mobi.swapClasses = function(aNode, c1, c2){
    if (!aNode.className){
        aNode.className = c2;
    }else if (ice.mobi.hasClass(aNode, c1)) {
        var tempClass = aNode.className;
        var temp = tempClass.replace(c1, c2 );
      //  console.log(" orig class="+ tempClass+" after replace temp="+temp);
        if (tempClass !== temp){
           aNode.className= temp;
        }
    }
};
ice.mobi.ready = function (callback) {
    if (document.addEventListener){
        document.addEventListener('DOMContentLoaded', callback, false);
    } else {
        window.attachEvent('onload', callback);
    }
};
ice.mobi.escapeJsfId = function(id) {
    return id.replace(/:/g,"\\:");
}
ice.mobi.addListener= function(obj, event, fnc){
    if (obj.addEventListener){
        obj.addEventListener(event, fnc, false);
    } else if (obj.attachEvent) {
        obj.attachEvent(event, fnc);
    } else {
        ice.log.debug(ice.log, 'WARNING:- this browser does not support addEventListener or attachEvent');
    }
} ;
ice.mobi.removeListener= function(obj, event, fnc){
    if (obj.addEventListener){
        obj.removeEventListener(event, fnc, false);
    } else if (obj.attachEvent){
        obj.detachEvent(event, fnc);
    } else {
        ice.log.debug(ice.log, 'WARNING cannot remove listener for event='+event+' node='+obj);
    }
};
ice.mobi.hasClass = function(ele, remove_cls) {
    return ele.className.replace( /(?:^|\s)remove_cls(?!\S)/ , '' );
};
mobi.findForm = function (sourceId) {
    if (!sourceId){
        console.log("source cannot be null to find form for ajax submit") ;
        return null;
    }
    var node = document.getElementById(sourceId);
    while (node.nodeName.toLowerCase() != "form" && node.parentNode) {
        node = node.parentNode;
    }
    ice.log.debug(ice.log, 'parent form node =' + node.name);
    return node;
};
/* copied from icemobile.js in jsp project for menuButton so can use same js */
ice.formOf = function formOf(element) {
    var parent = element;
    while (null != parent) {
        if ("form" == parent.nodeName.toLowerCase()) {
            return parent;
        }
        parent = parent.parentNode;
    }
}
ice.mobi.findFormFromNode = function (sourcenode) {
    if (!sourcenode){
        console.log("source cannot be null to find form for ajax submit") ;
        return null;
    }
    var node = sourcenode;
    while (node.nodeName.toLowerCase() != "form" && node.parentNode) {
        node = node.parentNode;
    }
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
    if (form.length == 0) {
        form = source.form;
        ice.log.debug(ice.log, "had to find form via element form length = " + form.length);
    }
    if (form.length == 0) {
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
    }, function (onBeforeSubmit, onBeforeUpdate, onAfterUpdate, onNetworkError,
                 onServerError) {
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
ice.mobi.extendAjaxArguments = function(callArguments, options) {
    // Return a modified copy of the original arguments instead of modifying the original.
    // The cb arguments, being a configured property of the component will live past this request.
    callArguments = ice.mobi.clone(callArguments);

    var params     = options.params,
        execute    = options.execute,
        render     = options.render,
        node       = options.node,
        onstart    = options.onstart,
        onerror    = options.onerror,
        onsuccess  = options.onsuccess,
        oncomplete = options.oncomplete;

    if (params) {
        if (callArguments['params'])
            ice.mobi.extend(callArguments['params'], params);
        else
            callArguments['params'] = params;
    }

    if (execute) {
        if (callArguments['execute'])
            callArguments['execute'] = callArguments['execute'] + " " + execute;
        else
            callArguments['execute'] = execute;
    }

    if (render) {
        if (callArguments['render'])
            callArguments['render'] = callArguments['render'] + " " + render;
        else
            callArguments['render'] = render;
    }

    if (node) {
        callArguments['node'] = node;
    }

    if (onstart) {
        if (callArguments['onstart']) {
            var existingStartCall = callArguments['onstart'];
            callArguments['onstart'] = function(xhr) {
                existingStartCall(xhr);
                onstart(xhr);
            }
        } else {
            callArguments['onstart'] = onstart;
        }
    }

    if (onerror) {
        if (callArguments['onerror']) {
            var existingErrorCall = callArguments['onerror'];
            callArguments['onerror'] = function(xhr, status, error) {
                existingErrorCall(xhr, status, error);
                onerror(xhr, status, error);
            }
        } else {
            callArguments['onerror'] = onerror;
        }
    }

    if (onsuccess) {
        if (callArguments['onsuccess']) {
            var existingSuccessCall = callArguments['onsuccess'];
            callArguments['onsuccess'] = function(data, status, xhr, args) {
                existingSuccessCall(data, status, xhr, args);
                onsuccess(data, status, xhr, args);
            }
        } else {
            callArguments['onsuccess'] = onsuccess;
        }
    }

    if (oncomplete) {
        if (callArguments['oncomplete']) {
            var existingCompleteCall = callArguments['oncomplete'];
            callArguments['oncomplete'] = function(xhr, status, args) {
                existingCompleteCall(xhr, status, args);
                oncomplete(xhr, status, args);
            }
        } else {
            callArguments['oncomplete'] = oncomplete;
        }
    }

    return callArguments;
};
ice.mobi.clone = function(obj) {
    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        var copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        var copy = [];
        var len;
        for (var i = 0, len = obj.length; i < len; ++i) {
            copy[i] = ice.mobi.clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        var copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = ice.mobi.clone(obj[attr]);
        }
        return copy;
    }

    throw new Error("Unable to copy obj! Its type isn't supported.");
};
ice.mobi.ab = function(cfg) { mobi.AjaxRequest(cfg); };
ice.mobi.extend = function(targetObject, sourceObject) {
    for (var attrname in sourceObject) { targetObject[attrname] = sourceObject[attrname]; }
}
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
ice.mobi.panelCenter = function(clientId, cfg){
    var paneNode = document.getElementById(clientId);
    var containerElem = cfg.containerElem || null;
    if (!paneNode){
       ice.console.log (ice.log,"Element Not Found id="+clientId);
       return;
    }
    var container =document.getElementById(containerElem);
    var contWidth;
    var contHeight;
    var elemWidth = cfg.width || paneNode.offsetWidth;
    var styleVar = "";
    var elemHeight = cfg.height|| paneNode.offsetHeight;
    if (cfg.width){
        var wStr = elemWidth+"px";
        styleVar += " width: "+wStr+";";
    }
    if (cfg.height){
        var hStr = elemHeight+"px";
        styleVar += " height: "+hStr+";";
    }
    var contWidth;
    var contHeight;
    if (container){
        contWidth = container.offsetWidth;
        contHeight = container.offsetHeight;
    } else {
        contWidth = mobi._windowWidth();
        contHeight = mobi._windowHeight();
    }
    var scrollTop = document.body.scrollTop;
    if (scrollTop == 0){
        scrollTop = document.documentElement.scrollTop;
    }
    if (contHeight > 0){
        var posStyle = " position: absolute;";
        var posLeft =((contWidth/2)-(elemWidth/2))+'px';
        var top = scrollTop +((contHeight/2)-(elemHeight/2))+'px';
        if (contHeight - elemHeight >0){
            styleVar += posStyle;
            styleVar += " top: " +top +";";
            styleVar +=" left:"+posLeft+";";
        }else {
            styleVar += posStyle;
            styleVar +=" left:"+posLeft+";";
        }
        if (cfg.style){
            styleVar+=cfg.style;
        }
        paneNode.setAttribute('style',styleVar);
    }  else {
        ice.log.debug(ice.log," Containing div or window has no height to autocenter popup of id="+clientId);
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
        if (contentElement) {
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
    source = source ? source : element;
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
        element: element,
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
                        ((undefined === window.clientInformation) ||
                            ("BlackBerry" !== window.clientInformation.platform) ||
                            (navigator.userAgent.indexOf(" Version/6.0") < 0)) &&
                        (undefined !== window.Worker)) {
                    ice.submitFunction = html5submitFunction;
                }
            }, false);

    window.addEventListener("pagehide", function () {
        if (ice.push) {
            ice.log.debug(ice.log, 'PAUSING from pagehide');
            ice.push.connection.pauseConnection();
        }
    }, false);

    window.addEventListener("pageshow", function () {
        if (ice.push) {
            ice.log.debug(ice.log, 'RESUMING from pageshow');
            ice.push.connection.resumeConnection();
        }
    }, false);

    window.addEventListener("hashchange", function () {
        if ("#icemobilesx" !== window.location.hash)  {
            return;
        }
        setTimeout( function(){
            var loc = window.location;
            history.pushState("", document.title, loc.pathname + loc.search);
        }, 1);
    }, false);

    document.addEventListener("webkitvisibilitychange", function () {
	if (document.webkitHidden) {
            if (ice.push) {
		ice.log.debug(ice.log, 'PAUSING from visibility change');
		ice.push.connection.pauseConnection();
            }
	}
	else {
            if (ice.push) {
		ice.log.debug(ice.log, 'RESUMING from visibility change');
		ice.push.connection.resumeConnection();
            }
	}
    }, false);

    window.addEventListener("blur", function () {
        if (ice.push) {
            ice.log.debug(ice.log, 'PAUSING from blur');
            ice.push.connection.pauseConnection();
        }
    }, false);

    window.addEventListener("focus", function () {
        if (ice.push) {
            ice.log.debug(ice.log, 'RESUMING from focus');
            ice.push.connection.resumeConnection();
        }
    }, false);
}
/* javascript for mobi:commandButton component put into component.js as per MOBI-200 */
ice.mobi.button = {
    select: function(clientId, event, cfg) {
        //get class and add the pressed state
        var me = document.getElementById(clientId);
        if (cfg.pDisabled){
            return; // no change on which button can be selected
        }
        var curClass = me.className;
  //      console.log("curClass="+curClass);
        if (ice.mobi.hasClass(me, ice.mobi.BUTTON_UNPRESSED )){
            var newCls = me.className.replace('up','down');
            me.className = newCls;
        } else {
            //what to do?  we always will render it this way...
        }
        //check if it's part of a commandButtonGroup
        if (cfg.groupId){
            var groupElem = document.getElementById(cfg.groupId+"_hidden");
            if (groupElem){
            //    console.log("for groupId "+cfg.groupId+" value is "+clientId);
                groupElem.value = clientId; //update group to this button selected
            }
        }
        var params = cfg.params || null;

        //otherwise, just check for behaviors, singleSubmit and go
        var singleSubmit = cfg.singleSubmit || false;
        var behaviors = cfg.behaviors || null;
        var event = event || window.event;
        var keyCall = function(xhr, status, args) {ice.mobi.button.unSelect(clientId, curClass);};
        var options = {
            onsuccess: keyCall,
            source : clientId
        };
        if (behaviors && behaviors.click){
            /* does not yet support mobi ajax for panelConf or submitNotification need to modify first */
            /* need to rework AjaxBehaviorRenderer before I can combine the options and cfg */
             behaviors.click();
            /* once I rework mobi ajax support will be able to support all the callbacks in next call */
            //ice.mobi.ab(ice.mobi.extendAjaxArguments(behaviors, options));
        }else{
            if (singleSubmit){
                options.execute="@this";
            } else {
                options.execute="@all";
            }
            if (params !=null){
                options.params = params;
            }else {
                options.params = {};
            }
            options.render = "@all";
            if (cfg.pcId) {
              //  console.log("throw control to panelConfirmation id="+cfg.pcId);
                ice.mobi.panelConf.init(cfg.pcId, clientId, cfg, options);
                return;
             }
             if (cfg.snId) {
                 ice.mobi.submitnotify.open(cfg.snId, clientId, cfg, options);
                 return;
             }
            //if here, then no panelConfirmation as this action is responsible for submit
             else {
                 mobi.AjaxRequest(options);
            }
        }
    },
    unSelect: function(clientId, classNm){
        var me = document.getElementById(clientId);
        var oldClass = me.className;
        me.className=oldClass.replace('down','up');
      //  console.log('id='+clientId+' unSelect call back -> class='+document.getElementById(clientId).className);
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

ice.mobi.sx = function (element, uploadURL) {
    var ampchar = String.fromCharCode(38);
    var form = ice.mobi.formOf(element);
    var formAction = form.getAttribute("action");
    var command = element.getAttribute("data-command");
    var id = element.getAttribute("data-id");
    var sessionid = element.getAttribute("data-jsessionid");
    var ub = element.getAttribute("data-ub");
    if ((null == id) || ("" == id)) {
        id = element.getAttribute("id");
    }
    var params = element.getAttribute("data-params");
    var windowLocation = window.location;
    var barURL = windowLocation.toString();
    var baseURL = barURL.substring(0,
            barURL.lastIndexOf("/")) + "/";
    var ubConfig = "";
    if ((null != ub) && ("" != ub)) {
        if ("." === ub) {
            ubConfig = "ub=" + escape(baseURL) + ampchar;
        } else {
            ubConfig = "ub=" + escape(ub) + ampchar;
        }
    }

    if (!uploadURL) {
        uploadURL = element.getAttribute("data-posturl");
    }
    if (!uploadURL) {
        if (0 === formAction.indexOf("/")) {
            uploadURL = window.location.origin + formAction;
        } else if ((0 === formAction.indexOf("http://")) ||
                (0 === formAction.indexOf("https://"))) {
            uploadURL = formAction;
        } else {
            uploadURL = baseURL + formAction;
        }
    }
//    } else {
//        uploadURL += '/';
//    }

    var returnURL = "" + window.location;
    if ("" == window.location.hash) {
        var lastHash = returnURL.lastIndexOf("#");
        if (lastHash > 0) {
            returnURL = returnURL.substring(0, lastHash);
        }
        returnURL += "#icemobilesx";
    }

    if ("" != params) {
        params = ubConfig + params;
    }

    var sessionidClause = "";
    if ("" != sessionid) {
        sessionidClause = "&JSESSIONID=" + escape(sessionid);
    }
    var sxURL = "icemobile://c=" + escape(command +
            "?id=" + id + ampchar + params) +
            "&u=" + escape(uploadURL) + "&r=" + escape(returnURL) +
            sessionidClause +
            "&p=" + escape(ice.mobi.serialize(form, false));

    window.location = sxURL;
}

ice.mobi.invoke = function(element)  {
    var command = element.getAttribute("data-command");
    if (ice[command])  {
        var params = element.getAttribute("data-params");
        var id = element.getAttribute("data-id");
        if ((null == id) || ("" == id)) {
            id = element.getAttribute("id");
        }
        ice[command](id,params);
    } else {
        ice.mobi.sx(element);
    }
}

ice.mobi.storeLocation = function(id, coords) {
    if (!coords) {
        return;
    }
    var el = document.getElementById(id);
    if (!el) {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {
        parts = new Array(4)
    }
    ;
    parts[0] = coords.latitude;
    parts[1] = coords.longitude;
    parts[2] = coords.altitude;
    el.value = parts.join();
}

ice.mobi.storeDirection = function(id, orient) {
    if (orient.webkitCompassAccuracy <= 0) {
        return;
    }
    var el = document.getElementById(id);
    if (!el) {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {
        parts = new Array(4)
    }
    parts[3] = orient.webkitCompassHeading;
    el.value = parts.join();
}


ice.mobi.geolocation = {
    watchId: 0,
    clientId: "",

    /**
     * Perform a call to watchPosition to fetch running updates to position.
     *
     * @param pClientId base id of hidden field
     * @param highAccuracy true if highAccuracy results to be fetched
     * @param maxAge oldest acceptable cached results (in ms)
     * @param timeout longest time to wait for value (in ms)
     */
    watchLocation: function (pClientId, highAccuracy, maxAge, timeout) {

        ice.mobi.geolocation.clientId = pClientId;
        ice.mobi.geolocation.clearWatch();
        // It seems like on Android that passing any argument at all for enableHighAccuracy
        // enables high accuracy.
        var geoParams = {};
        if (maxAge > 0)  {
            geoParams.maximumAge = maxAge * 1000;
        }
        if (timeout > 0)  {
            geoParams.timeout = timeout * 1000;
        }
        if (highAccuracy != 'false')  {
            geoParams.enableHighAccuracy = true;
        }
        console.log('Launching watchPosition, ' +
            'maxAge: ' + geoParams.maximumAge + '(ms),' +
            ' timeout: ' + geoParams.timeout + '(ms)' +
            ' highAccuracy: ' + geoParams.enableHighAccuracy);

        ice.mobi.geolocation.watchId = navigator.geolocation.watchPosition(
                this.successCallback, this.errorCallback,
                geoParams );

        window.addEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
        ice.onElementRemove(pClientId, ice.mobi.geolocation.clearWatch);
        console.log('Lauching positionWatch for client: ' + pClientId +
                ' watchId: ' + ice.mobi.geolocation.watchId);
    },

    /**
     * Perform a single call to the navigator getCurrentPosition call.
     */
    getLocation: function (pClientId, highAccuracy, maxAge, timeout) {

        ice.mobi.geolocation.clientId = pClientId;
        ice.mobi.geolocation.clearWatch();

        var geoParams = {};
        if (maxAge > 0)  {
            geoParams.maximumAge = maxAge * 1000;
        }
        if (timeout > 0)  {
            geoParams.timeout = timeout * 1000;
        }
        if (highAccuracy != 'false')  {
            geoParams.enableHighAccuracy = true;
        }
        console.log('Launching getCurrentPosition, ' +
            'maxAge: ' + geoParams.maximumAge + '(ms),' +
            ' timeout: ' + geoParams.timeout + '(ms)' +
            ' highAccuracy: ' + geoParams.enableHighAccuracy);

       navigator.geolocation.getCurrentPosition(this.successCallback, this.errorCallback,
                    geoParams );

        window.addEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
        ice.onElementRemove(pClientId, ice.mobi.geolocation.clearWatch);
    },

    successCallback: function(pos) {
        console.log('Position update for client: ' + ice.mobi.geolocation.clientId);
        try {
            inputId = ice.mobi.geolocation.clientId + "_locHidden";
            console.log('LOGGING Position TO hidden field: ' + inputId);
            ice.mobi.storeLocation(inputId, pos.coords);

        } catch(e) {
            console.log('Exception: ' + e);
        }
    },

    errorCallback: function(positionError) {
        console.log('Error in watchPosition, code: ' + positionError.code + ' Message: ' + positionError.message);
        ice.mobi.geolocation.clearWatch();
    },

    orientationCallback: function(orient) {
        inputId = ice.mobi.geolocation.clientId + "_locHidden";
        ice.mobi.storeDirection(inputId, orient);
    },

    // Clear any existing positionUpdate listeners
    clearWatch: function() {
        if (ice.mobi.geolocation.watchId > 0) {
            console.log('Existing positionWatch: ' + ice.mobi.geolocation.watchId + ' removed');
            navigator.geolocation.clearWatch(ice.mobi.geolocation.watchId);
            ice.mobi.geolocation.watchId = 0;
        }
        window.removeEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
    }
};

(function() {
    function DataView(clientId, cfg) {
        var self = this,
            selectorId = '#' + ice.mobi.escapeJsfId(clientId),
            headSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-head > thead',
            footSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-foot > tfoot',
            bodyDivSelector = selectorId + ' > .mobi-dv-mst > div',
            element, details, headCells, footCells, bodyRows;

        function initElementVars() {
            var headCellSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-head > thead > tr > th',
                footCellSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-foot > tfoot > tr > td',
                bodyRowSelector = selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tbody > tr',
                detailsSelector = selectorId + ' > .mobi-dv-det';

            details = document.querySelector(detailsSelector);
            element = document.getElementById(clientId);
            headCells = document.querySelectorAll(headCellSelector);
            bodyRows = document.querySelectorAll(bodyRowSelector);
            footCells = document.querySelectorAll(footCellSelector);
        }

        function getScrollableContainer() {
            var height = element.clientHeight,
                parent = element.parentNode;

            while (parent != null && parent.scrollHeight == parent.clientHeight)
                parent = parent.parentNode;

            return parent;
        }

        function getIndexInput() {
            var r = Array.prototype.filter.call(details.children, function(n) {
                return n.nodeName == "INPUT";
            });
            return r[0];
        }

        function initTableAlignment() {
            var dupeHead = document.querySelector(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > thead'),
                dupeHeadCells = document.querySelectorAll(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > thead > tr > th'),
                dupeFoot = document.querySelector(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tfoot'),
                dupeFootCells = document.querySelectorAll(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tfoot > tr > td'),
                head = document.querySelector(headSelector),
                foot = document.querySelector(footSelector),
                bodyDivWrapper = document.querySelector(bodyDivSelector);

            var firstRowBodyCells = Array.prototype.filter.call(bodyRows[0].children, function(val){
                return val.nodeName == "TD"; /* remove hidden input */
            });

            var scrollWidth = ice.mobi.dataView.getScrollWidth();

//            if (scrollWidth) {
//                head.parentNode.parentNode.style.paddingRight = scrollWidth + "px";
//            }

            var frbcWidths = Array.prototype.map.call(
                firstRowBodyCells,
                function(n) {
                    var compd = document.defaultView.getComputedStyle(n, null);
                    return n.clientWidth - parseInt(compd.paddingLeft) - parseInt(compd.paddingRight);
            });

            /* fix body column widths */
            for (var i = 0; i < firstRowBodyCells.length; i++) {
                firstRowBodyCells[i].style.width = frbcWidths[i] + 'px';
            }

            var dupeHeadCellWidths = Array.prototype.map.call(
                dupeHeadCells,
                function(n) {
                    var compd = document.defaultView.getComputedStyle(n, null);
                    return n.clientWidth - parseInt(compd.paddingLeft) - parseInt(compd.paddingRight);
            });

            var dupeFootCellWidths = Array.prototype.map.call(
                dupeFootCells,
                function(n) {
                    var compd = document.defaultView.getComputedStyle(n, null);
                    return n.clientWidth - parseInt(compd.paddingLeft) - parseInt(compd.paddingRight);
                });

            /* copy head col widths from duplicate header */
            for (var i = 0; i < dupeHeadCellWidths.length; i++) {
                headCells[i].style.width = dupeHeadCellWidths[i] + 'px';
            }

            /* copy foot col widths from duplicate footer */
            for (var i = 0; i < dupeFootCellWidths.length; i++) {
                footCells[i].style.width = dupeFootCellWidths[i] + 'px';
            }

            /* hide duplicate header */
            dupeHead.style.display = 'none';
            dupeFoot.style.display = 'none';

            recalcScrollHeight(head, foot, bodyDivWrapper);

//            if (scrollWidth) {
//                head.parentNode.parentNode.style.paddingRight = '';
//                head.parentNode.style.width = (head.parentNode.clientWidth - scrollWidth + 1) + "px";
//                foot.parentNode.style.width = (foot.parentNode.clientWidth - scrollWidth + 1) + "px";
//            }
        }

        /* arguments optional to avoid lookup */
        function recalcScrollHeight(inHead, inFoot, inDivWrap) {
            /* set scroll body to maximum height, reserving space for head / foot */
            var head = inHead ? inHead : document.querySelector(headSelector),
                foot = inFoot ? inFoot : document.querySelector(footSelector),
                bodyDivWrapper = inDivWrap ? inDivWrap : document.querySelector(bodyDivSelector),
                dim = element.getBoundingClientRect(),
                maxHeight = window.innerHeight - dim.top,
                headHeight = head.clientHeight,
                footHeight = foot.clientHeight,
                fullHeight = maxHeight - headHeight - footHeight - 1;

            /* set height to full visible size of parent */
            bodyDivWrapper.style.height = fullHeight + 'px';
            bodyDivWrapper.style.overflow = 'auto';

            /* set height to full visible size of parent minus
             height of all following elements */
            var container = getScrollableContainer(),
                    bottomResize = function() {
                        bodyDivWrapper.style.height = fullHeight
                                - (container.scrollHeight - container.clientHeight) + 'px';
                    };

            if (container) bottomResize();
        }

        function initActivationEvents() {
            for (var i = 0; i < bodyRows.length; ++i) {
                var row = bodyRows[i];
                row.addEventListener("click", activateRow);
                row.addEventListener("touchend", function() {alert('touchend');});
            }
        }

        function activateRow(event) {
            var newIndex = event.currentTarget.getAttribute('data-index'),
                newValue = Array.prototype.filter.call(
                        event.currentTarget.children,
                        function(n) { return n.nodeName == "INPUT"; }
                ),
                valueParts = newValue[0].getAttribute('value').split('|');


            /* lookup elem by id and insert value */
            for (var i = 0; i < valueParts.length; i++) {
                var v = valueParts[i].split('='),
                    elem = document.getElementById(v[0]),
                    dir = v[1],
                    value;

                switch (dir) {
                    case 'html':
                        elem.innerHTML = v[2];
                    case 'attr':
                        if (v[2] == 'checked') {
                            if (v[3] == 'true') elem.checked = true;
                            else elem.checked = false;
                        } else {
                            elem.setAttribute(v[2], v[3]);
                        }
                    default :
                        value = '';
                }
            };


            getIndexInput().setAttribute("value", newIndex);
            details.setAttribute("data-index", newIndex);

            // if vertical orientation
            recalcScrollHeight();
        }

        function update(newCfg){
            initElementVars();
            initActivationEvents();
            initTableAlignment();
        }

        initElementVars();
        initActivationEvents();

        /* first alignment needs to occur shortly after script eval
        *  else heights are miscalculated for following elems */
        setTimeout(initTableAlignment, 100);

        /* resize height adjust */
        window.addEventListener("resize", function() { self.recalcScrollHeight(); });

        /* Instance API */
        return { update: update }
    }

    var width;
    var getScrollWidth = function() {
        if (width) return width;

        var inner = document.createElement("p");
        inner.style.width = '100%';
        inner.style.height = '100%';

        var outer = document.createElement("div");
        outer.style.position = 'absolute';
        outer.style.width = '100px';
        outer.style.height = '100px';
        outer.style.top = '0';
        outer.style.left = '0';
        outer.style.visibility = 'hidden';
        outer.style.overflow = 'hidden';
        outer.appendChild(inner);

        document.body.appendChild(outer);

        var w1 = outer.clientWidth;
        outer.style.overflow = 'scroll';
        var w2 = outer.clientWidth;

        document.body.removeChild(outer);
        width = (w1 - w2);

        return width;
    };

    ice.mobi.dataView = {
        getScrollWidth : getScrollWidth,
        instances: {},
        create: function(clientId, cfg) {
            if (this.instances[clientId]) this.instances[clientId].update(cfg);
            else this.instances[clientId] = DataView(clientId, cfg);

            return this.instances[clientId];
        }
    }

})();

/* touch active state support */
document.addEventListener("touchstart", function(){}, true);


