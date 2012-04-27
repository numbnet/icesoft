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


if (!window['ice']) {
    window.ice = {};
}

ice.registerAuxUpload = function (sessionid, uploadURL) {
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

ice.mobilesx = function (element) {
    var formAction = ice.formOf(element).getAttribute("action");
    var command = element.getAttribute("data-command");
    var id = element.getAttribute("data-id");
    var uploadURL;
    if (0 === formAction.indexOf("/"))  {
        uploadURL = window.location.origin + formAction;
    } else if ((0 === formAction.indexOf("http://")) || 
        (0 === formAction.indexOf("https://"))) {
        uploadURL = formAction;
    } else {
        var baseURL = window.location.toString();
        uploadURL = baseURL.substring(0, baseURL.lastIndexOf("/")) +
            "/" + formAction;
    }

    var returnURL = window.location;
    if ("" == returnURL.hash)  {
        returnURL += "#icemobilesx";
    }

    var sxURL = "icemobile://c=" + escape(command + "?id=" + id) +
        "&u=" + escape(uploadURL) + "&r=" + escape(returnURL);
        
    window.location = sxURL;
}

ice.formOf = function(element)  {
    var parent = element;
    while (null != parent)  {
        if ("form" == parent.nodeName.toLowerCase())  {
            return parent;
        }
        parent = parent.parentNode;
    }
}
