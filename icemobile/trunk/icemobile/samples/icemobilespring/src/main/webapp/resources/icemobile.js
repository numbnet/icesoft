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

ice.mobiserial = function(formId, typed)  {
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

ice.mobilesx = function (element) {
    var ampchar = String.fromCharCode(38);
    var form = ice.formOf(element);
    var formID = form.getAttribute('id');
    var formAction = form.getAttribute("action");
    var command = element.getAttribute("data-command");
    var id = element.getAttribute("data-id");
    var params = element.getAttribute("data-params");
    var windowLocation = window.location;
    var barURL = windowLocation.toString();
    var baseURL = barURL.substring(0, 
        barURL.lastIndexOf("/")) + "/";

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

    if ("" != params)  {
        params = "ub=" + escape(baseURL) + ampchar + params;
    }

    var sxURL = "icemobile://c=" + escape(command + 
            "?id=" + id + ampchar + params) +
            "&u=" + escape(uploadURL) + "&r=" + escape(returnURL) +
            "&p=" + escape(ice.mobiserial(formID, false));

    window.location = sxURL;
}

ice.formOf = function(element) {
    var parent = element;
    while (null != parent) {
        if ("form" == parent.nodeName.toLowerCase()) {
            return parent;
        }
        parent = parent.parentNode;
    }
}
if (!window['ice.mobi']) {
    window.ice.mobi = {};
}

ice.mobi.flipvalue = function(id, vars) {

    try {
        this.id = id;
        this.flipperEl = vars.elVal;

        this.event = vars.event;
        var hasBehaviors = false;

        if (this.flipperEl) {
            var oldClass = this.flipperEl.className;
            var value = "off";
            if (oldClass.indexOf('-off ') > 0) {
                this.flipperEl.className = 'mobi-flip-switch mobi-flip-switch-on ';
                value = true;
            } else {
                this.flipperEl.className = 'mobi-flip-switch mobi-flip-switch-off ';
                value = false;
            }
            var hidden = this.id + "_hidden";
            var thisEl = document.getElementById(hidden);
            if (thisEl) {
                thisEl.value = value.toString();
            }
        }

    } catch (e) {
        alert('Exception finding switcher: ' + e);
    }
}


ice.mobi.carousel = {
    acarousel: null,
    loaded: function(clientId) {
        var carouselId = clientId + '_carousel';
        //carousel iscroll loading
        //    ice.log.debug(ice.log, 'in the carouselLoaded method clientId is '+clientId);
        setTimeout(function () {
            if (this.acarousel) {
                //   ice.log.debug(ice.log, 'REFRESH existing carousel='+this.acarousel);
                mobi.carousel.refresh(clientId);
            }
            else {
                this.acarousels = new iScroll(carouselId, {
                    snap: 'li',
                    momentum: false,
                    hScrollbar: false,
                    onScrollEnd: function () {
                        ice.mobi.carousel.scrollUpdate(clientId, this.currPageX);
                    }
                });
            }
        }, 100);
//	               ice.log.debug(ice.log,"after setTimeout function");
    },
    unloaded: function(clientId) {
        if (this.acarousel != null) {
            //        ice.log.debug(ice.log, 'DESTROY carousel with id='+clientId);
            this.acarousel.destroy();
            this.acarousel = null;
        }
    },

    scrollUpdate: function(clientId, pageVal) {
        //only update if different than last one.
        //         ice.log.debug(ice.log, 'scrollUpdate and current page is='+pageVal);
        var hidden = document.getElementById(clientId + '_hidden');
        var changedVal = false;
        if (hidden) {

            var temp = hidden.value;
            if (temp != pageVal) {
                changedVal = true;
                hidden.value = pageVal;
//                     'old hidden='+temp+ ' updated to hidden.value = '+hidden.value);
                document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
                document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (pageVal + 1) + ')').className = 'active';
            }
        }
    },

    refresh: function(clientId) {
        if (this.acarousel) {
//               ice.log.debug(ice.log, "  have a carousel to refresh from hidden value");
            var currPageX = 0;
            var hidden = document.getElementById(clientId + "_hidden");
            if (hidden) {
                currPageX = hidden.value;
//				   ice.log.debug(ice.log, 'in refresh and currPageX ='+this.currPageX+' hiddenVal is ='+hidden.value);
            }
            //if this.current is different from hidden, then scroll to hidden value.
            this.acarousel.scrollToPage(currPageX);
            document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
            document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (this.currPageX + 1) + ')').className = 'active';
            setTimeout(function() {
                this.acarousel.refresh();

            }, 0);

        }

        if (!this.acarousel) {
            //    ice.log.debug(ice.log, "REFRESH HAS NO OBJECT FOR CAROUSEL clientId="+clientId+' ss -'+singleSubmit);
            this.acarousel = null;
            this.loaded(clientId);
        }
    }
}

