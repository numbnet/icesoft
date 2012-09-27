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

(function() {
    var broadcast = function(funcs, args) {
        args = args || [];

        for (var i in funcs) {
            if (!isNaN(parseInt(i))) {
                funcs[i].apply(funcs[i],args);
            }
        }
    };

    var fold = function(items, initialValue, injector) {
        var tally = initialValue;
        var size = items.length;
        for (var i = 0; i < size; i++) {
            tally = injector(tally, items[i]);
        }

        return tally;
    };

    function Overlay(cfg, container) {
        // If the request is processed before our timeouts for adding the
        // elements, then we need to never add them.
        var addElements = true;
        var addDelay = 250;

        var overlayWidth = 0, overlayHeight = 0;
        if (container == document.body) {
            overlayWidth = Math.max(document.documentElement.scrollWidth,
                Math.max(document.body.scrollWidth, document.body.parentNode.offsetWidth));
            overlayHeight = Math.max(document.documentElement.scrollHeight,
                Math.max(document.body.scrollHeight, document.body.parentNode.offsetHeight));
        } else {
            overlayWidth = container.offsetWidth;
            overlayHeight = container.offsetHeight;
        }
        var overlay = document.createElement('div');
        overlay.style.cssText = 'top: 0px; left: 0px; width: '+overlayWidth+'px; height: '+overlayHeight+'px; background-color: black; position: absolute; z-index: 28000; opacity: 0.3; filter: alpha(opacity = 30); zoom: 1;';

        setTimeout(function() {
            if (!addElements) {
                return;
            }
            if (container == document.body) {
                container.appendChild(overlay);
            } else {
                container.parentNode.appendChild(overlay);
                ice.ace.jq(overlay).position({
                    my: 'left top',
                    at: 'left top',
                    of: container,
                    collision: 'none'});
            }
        }, addDelay);
        
        var cloneToRemove;
        var revertElem;
        var revertZIndex;
        if (cfg.autoCenter) {
            cloneToRemove = ice.ace.jq(ice.ace.escapeClientId(cfg.id)+"_display").clone(false,true);
            cloneToRemove.attr('id', cfg.id + '_clone');
            cloneToRemove.addClass('clone');
            cloneToRemove.css('z-index', '28001');
            if (container == document.body) {
                setTimeout(function() {
                    if (!addElements) {
                        return;
                    }
                    cloneToRemove.appendTo(container);
                    cloneToRemove.css('position', 'fixed');
                    cloneToRemove.css('display', '');
                    cloneToRemove.position({
                        my: 'center center',
                        at: 'center center',
                        of: window,
                        collision: 'fit'});
                }, addDelay);
            } else {
                setTimeout(function() {
                    if (!addElements) {
                        return;
                    }
                    cloneToRemove.appendTo(container.parentNode);
                    cloneToRemove.css('position', 'absolute');
                    cloneToRemove.css('display', '');
                    cloneToRemove.position({
                        my: 'center center',
                        at: 'center center',
                        of: container,
                        collision: 'fit'});
                }, addDelay);
            }
        } else {
            revertElem = ice.ace.jq(ice.ace.escapeClientId(cfg.id)+"_display");
            if (revertElem) {
                revertZIndex = revertElem.css('z-index');
                revertElem.css('z-index', '28001');
                revertElem.css('display', '');
            }
        }

        return function() {
            addElements = false;
            if (overlay) {
                try { overlay.parentNode.removeChild(overlay); }
                catch (e) { //ignore, the overlay does not match the document after a html/body level update
                }
            }
            if (cloneToRemove) {
                try { cloneToRemove.remove(); }
                catch (e) { //ignore, the cloneToRemove does not match the document after a html/body level update
                }
            }
            if (revertElem) {
                try {
                    revertElem.css('z-index', revertZIndex);
                    revertElem.css('display', 'none');
                }
                catch (e) { //ignore, the cloneToRemove does not match the document after a html/body level update
                }
            }
        };
    }

    var anticipationStrings = ['unanticipated', 'anticipated', 'commenced'];
    var UNANTICIPATED = 0, ANTICIPATED = 1, COMMENCED = 2;
    var anticipatePossibleSecondSubmit = UNANTICIPATED;

    var NOOP = function () {
        //console.log('stopBlockingUI NOOP');
    };
    //console.log('stopBlockingUI = NOOP  from  init');
    var stopBlockingUI = NOOP;

    if (!ice.ace) ice.ace = {};

    var uniqueCounter = 0;

    ice.ace.SubmitMonitor = function (cfg) {
        var jqId = ice.ace.escapeClientId(cfg.id);
        var uniqueId = uniqueCounter++;

        function isMonitoringElement(source) {
            var mf = cfg.monitorFor;
            if (mf == undefined || mf.length == 0) {
                return true;
            }
            if (!source) {
                return false;
            }
            var monitoredElementIds = mf.split(" ");
            var curr = source;
            while (true) {
                var currId = curr.id;
                if (currId) {
                    if (-1 < ice.ace.jq.inArray(currId, monitoredElementIds)) {
                        return true;
                    }
                }
                if (curr == document.body) {
                    break;
                }
                curr = curr.parentNode;
                if (!curr) {
                    break;
                }
            }
            return false;
        }

        function getBlockUIProperty() {
            return (cfg.blockUI == undefined) ? '@all' : cfg.blockUI;
        }

        function isBlockUIEnabled() {
            return (getBlockUIProperty() != '@none');
        }

        function isBlockUITypeAmenableToCombining() {
            var rawBlockUI = getBlockUIProperty();
            return ( (rawBlockUI != '@source') && (rawBlockUI != '@none') );
        }

        function resolveBlockUIElement(source) {
            var rawBlockUI = getBlockUIProperty();
            if (rawBlockUI == '@all') {
                return document.body;
            } else if (rawBlockUI == '@source') {
                return source;
            } else if (rawBlockUI == '@none') {
                return null;
            } else {
                var elem = ice.ace.jq(ice.ace.escapeClientId(rawBlockUI));
                if (elem && elem.length > 0) {
                    return elem[0];
                }
                return null;
            }
        }

        function eventSink(element) {
            return function(e) {
				e = e || window.event;
                var eventType = ( (e.type != undefined && e.type != null) ? e.type : null );
                var triggeringElement = e.srcElement ? e.srcElement : e.target;
                var capturingElement = element;
                /*
                console.log('Monitor '+uniqueId+'>'+jqId+'  event [type: ' + eventType +
                        ', triggered by: ' + (triggeringElement.id || triggeringElement) +
                        ', captured in: ' + (capturingElement.id || capturingElement) + '] was discarded.');
                */
                return false;
            }
        }

        var allStates = ['idle', 'active', 'serverError', 'networkError', 'sessionExpired'];
        var IDLE = 0, ACTIVE = 1, SERVER_ERROR = 2, NETWORK_ERROR = 3, SESSION_EXPIRED = 4;
        var currentState = IDLE;

        var changeState = function(state) {
            currentState = state;
            //console.log('Monitor '+uniqueId+'>'+jqId+'  changeState: ' + state + ' : ' + allStates[state]);
            ice.ace.jq(jqId+'_display > div.ice-sub-mon-mid').hide().filter('.'+allStates[state]).show();
            ice.ace.jq(jqId+'_clone > div.ice-sub-mon-mid').hide().filter('.'+allStates[state]).show();
        };


        var begunApplicableToThis = false;

        var doOverlayIfBlockingUI = function(source) {
            //Only block the UI for client-initiated requests (not push requests)
            if (isBlockUIEnabled()) {
                //console.log('Monitor '+uniqueId+'>'+jqId+'  Blocking UI');

                var eventSinkFirstClickCount = 0;
                function eventSinkFirstClick(firstSubmitSource, element, originalOnclick, regularSink) {
                    return function(e) {
                        //console.log('Monitor '+uniqueId+'>'+jqId+'  eventSinkFirstClick()  eventSinkFirstClickCount: ' + eventSinkFirstClickCount);
                        if (eventSinkFirstClickCount > 0) {
                            //console.log('eventSinkFirstClick()  not first click');
                            return regularSink(e);
                        }
                        eventSinkFirstClickCount++;

						e = e || window.event;
                        var triggeringElement = ( (e.srcElement != undefined && e.srcElement != null) ? e.srcElement : e.target);
                        /*
                        console.log('event [type: ' + e.type +
                                ', triggered by: ' + (triggeringElement.id || triggeringElement) +
                                ', captured in: ' + (element.id || element) + ']');
                        console.log('first submit element: ' + (firstSubmitSource.id || firstSubmitSource));
                        */
                        if ((firstSubmitSource == triggeringElement) || (firstSubmitSource == element)) {
                            //console.log('eventSinkFirstClick()  clicked on same element as first submit');
                            regularSink(e);
                            // checkbox in Firefox:  onclick, onchange, but in Chrome: onchange, onclick
                            // If icecore:singleSubmit submits onchange, then onclick is trapped, and must
                            //  return true or else it will revert what the onchange submitted.
                            return true;
                        }

                        //console.log('eventSinkFirstClick()  calling original onclick');
                        // Might not be an onclick directly on that element, it might
                        // have to bubble up, like with icecore:singleSubmit
                        anticipatePossibleSecondSubmit = ANTICIPATED;
                        if (originalOnclick) {
                            return originalOnclick.call(element, e);
                        }
                    }
                }
                
                var overlayContainerElem = resolveBlockUIElement(source);
                var blockUIOverlay = Overlay(cfg, overlayContainerElem);
                var rollbacks = fold(['input', 'select', 'textarea', 'button', 'a'], [], function(result, type) {
                    return result.concat(
                            ice.ace.jq.map(overlayContainerElem.getElementsByTagName(type), function(e) {
                        var sink = eventSink(e);
                        var onkeypress = e.onkeypress;
                        var onkeyup = e.onkeyup;
                        var onkeydown = e.onkeydown;
                        var onclick = e.onclick;
                        var sinkClick = eventSinkFirstClick(source, e, onclick, sink);
                        e.onkeypress = sink;
                        e.onkeyup = sink;
                        e.onkeydown = sink;
                        e.onclick = sinkClick;

                        return function() {
                            try {
                                e.onkeypress = onkeypress;
                                e.onkeyup = onkeyup;
                                e.onkeydown = onkeydown;
                                e.onclick = onclick;
                            } catch (ex) {
                                //don't fail if element is not present anymore
                            }
                        };
                    })
                    );
                });

                stopBlockingUI = function() {
                    broadcast(rollbacks);
                    blockUIOverlay();
                    stopBlockingUI = NOOP;
                    //console.log('Monitor '+uniqueId+'>'+jqId+'  Unblocked UI');
                };
            } else {
                //console.log('Monitor '+uniqueId+'>'+jqId+'  stopBlockingUI = NOOP  from  else of isBlockUIEnabled()');
                stopBlockingUI = NOOP;
            }
        };

        var CLEANUP_UNNECESSARY = 0, CLEANUP_PENDING = 1, CLEANUP_ACKNOWLEDGED = 2;
        var cleanup = CLEANUP_UNNECESSARY;

        function handleCleanup(isBeforeSubmit) {
            if (cleanup == CLEANUP_ACKNOWLEDGED) {
                //console.log('Monitor '+uniqueId+'>'+jqId+'  handleCleanup  DEAD');
                return true;
            } else if (cleanup == CLEANUP_PENDING) {
                cleanup = CLEANUP_ACKNOWLEDGED;
                //console.log('Monitor '+uniqueId+'>'+jqId+'  handleCleanup  CLEANUP PENDING -> ACKNOWLEDGED');
                //TODO Remove all listeners
                return isBeforeSubmit;
            }
            return false;
        }

        window.ice.onElementUpdate(cfg.id+'_script', function() {
            cleanup = CLEANUP_PENDING;
            //console.log('Monitor '+uniqueId+'>'+jqId+'  onElementUpdate  -> CLEANUP_PENDING');
        });

        window.ice.onBeforeSubmit(function(source, isClientRequest) {
            if (handleCleanup(true)) {
                return;
            }
            if (!isClientRequest) {
                return;
            }
            if (!isMonitoringElement(source)) {
                //console.log('Monitor '+uniqueId+'>'+jqId+'  onBeforeSubmit()  NOT monitoring source: ' + source + '  id: ' + source.id);
                return;
            }
            begunApplicableToThis = true;

            //console.log('Monitor '+uniqueId+'>'+jqId+'  onBeforeSubmit()  IS  monitoring source: ' + source + '  id: ' + source.id);
            //console.log('Monitor '+uniqueId+'>'+jqId+'  onBeforeSubmit()  ' + anticipationStrings[anticipatePossibleSecondSubmit]);
            if (isBlockUITypeAmenableToCombining() && (anticipatePossibleSecondSubmit == ANTICIPATED)) {
                //console.log('onBeforeSubmit()  anticipated -> commenced');
                anticipatePossibleSecondSubmit = COMMENCED;
            } else {
                //console.log('onBeforeSubmit()  regular');
                changeState(ACTIVE);
                doOverlayIfBlockingUI(source);
            }
        });

        var whenUpdate = function(xmlContent, source) {
            //console.log('Monitor '+uniqueId+'>'+jqId+'  whenUpdate()  stopping');
            anticipatePossibleSecondSubmit = UNANTICIPATED;
            stopBlockingUI();
            changeState(IDLE);
        };

        window.ice.onBeforeUpdate(function(xmlContent, source) {
            if (handleCleanup(false)) {
                return;
            }
            // Can't use isMonitoringElement(source) here since source is from
            // before the update, so doesn't necessarily exist any more, nor
            // a new component with the same id.
            if (!begunApplicableToThis) {
                //console.log('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  NOT begunApplicableToThis for source: ' + source + '  id: ' + source.id);
                return;
            }
            begunApplicableToThis = false;
            //console.log('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  IS  begunApplicableToThis for source: ' + source + '  id: ' + source.id);

            //console.log('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  ' + anticipationStrings[anticipatePossibleSecondSubmit]);
            if (isBlockUITypeAmenableToCombining() && (anticipatePossibleSecondSubmit == ANTICIPATED)) {
                setTimeout(function() {
                    //console.log('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  DELAYED  ' + anticipationStrings[anticipatePossibleSecondSubmit]);
                    if (anticipatePossibleSecondSubmit != COMMENCED) {
                        whenUpdate(xmlContent, source);
                    }
                }, 260);
            } else {
                whenUpdate(xmlContent, source);
            }
        });

        window.ice.onServerError(function() {
            if (handleCleanup(false)) {
                return;
            }
            anticipatePossibleSecondSubmit = UNANTICIPATED;
            changeState(SERVER_ERROR);
        });

        window.ice.onNetworkError(function() {
            if (handleCleanup(false)) {
                return;
            }
            anticipatePossibleSecondSubmit = UNANTICIPATED;
            changeState(NETWORK_ERROR);
        });

        window.ice.onSessionExpiry(function() {
            if (handleCleanup(false)) {
                return;
            }
            anticipatePossibleSecondSubmit = UNANTICIPATED;
            changeState(SESSION_EXPIRED);
        });

        changeState(IDLE);
    }
})();