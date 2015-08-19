/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2014 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 */

/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

var debug = operator();
var info = operator();
var warn = operator();
var error = operator();
var childLogger = operator();
var log = operator();
var threshold = operator();
var enable = operator();
var disable = operator();
var toggle = operator();

function Logger(category, handler) {
    return object(function(method) {
        each([debug, info, warn, error], function(priorityOperator) {
            method(priorityOperator, function(self, message, exception) {
                log(handler, priorityOperator, category, message, exception);
            });
        });

        method(childLogger, function(self, categoryName, newHandler) {
            return Logger(append(copy(category), categoryName), newHandler || handler);
        });

        method(asString, function(self) {
            return 'Logger[' + join(category, '.') + ']';
        });
    });
}

function ConsoleLogHandler(priority) {
    function formatOutput(category, message) {
        var timestamp = (new Date()).toUTCString();
        return join(['[', join(category, '.'), '] [', timestamp, '] ', message], '');
    }

    function storeLogMessage(level, message, exception) {
        var previousMessages = localStorage['ConsoleLogHandler-store'] || '';

        var fullMessage = '[' + level + '] [' + ice.windowID + '] ' + message;
        if (exception) {
            fullMessage = fullMessage + '\n' + exception.message;
        }
        var messages = previousMessages + '%%' + fullMessage;

        localStorage['ConsoleLogHandler-currentEntry'] = fullMessage;
        localStorage['ConsoleLogHandler-store'] = messages;
    }

    var ieConsole = !window.console.debug;

    var debugPrimitive = ieConsole ?
            function(self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.log(formattedMessage, '\n', exception) : console.log(formattedMessage);
                storeLogMessage('debug', formattedMessage, exception);
            } :
            function(self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.debug(formattedMessage, exception) : console.debug(formattedMessage);
                storeLogMessage('debug', formattedMessage, exception);
            };
    var infoPrimitive = ieConsole ?
            function(self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.info(formattedMessage, '\n', exception) : console.info(formattedMessage);
                storeLogMessage('info ', formattedMessage, exception);
            } :
            function(self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.info(formattedMessage, exception) : console.info(formattedMessage);
                storeLogMessage('info ', formattedMessage, exception);
            };
    var warnPrimitive = ieConsole ?
            function(self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.warn(formattedMessage, '\n', exception) : console.warn(formattedMessage);
                storeLogMessage('warn ', formattedMessage, exception);
            } :
            function(self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.warn(formattedMessage, exception) : console.warn(formattedMessage);
                storeLogMessage('warn ', formattedMessage, exception);
            };
    var errorPrimitive = ieConsole ?
            function (self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.error(formattedMessage, exception) : console.error(formattedMessage);
                storeLogMessage('error', formattedMessage, exception);
            } :
            function (self, category, message, exception) {
                var formattedMessage = formatOutput(category, message);
                exception ? console.error(formattedMessage, exception) : console.error(formattedMessage);
                storeLogMessage('error', formattedMessage, exception);
            };

    var handlers = [
        Cell(debug, object(function(method) {
            method(debug, debugPrimitive);
            method(info, infoPrimitive);
            method(warn, warnPrimitive);
            method(error, errorPrimitive);
        })),
        Cell(info, object(function(method) {
            method(debug, noop);
            method(info, infoPrimitive);
            method(warn, warnPrimitive);
            method(error, errorPrimitive);
        })),
        Cell(warn, object(function(method) {
            method(debug, noop);
            method(info, noop);
            method(warn, warnPrimitive);
            method(error, errorPrimitive);
        })),
        Cell(error, object(function(method) {
            method(debug, noop);
            method(info, noop);
            method(warn, noop);
            method(error, errorPrimitive);
        }))
    ];
    var handler;

    function selectHandler(p) {
        handler = value(detect(handlers, function(cell) {
            return key(cell) == p;
        }));
    }

    selectHandler(priority || debug);

    return object(function (method) {
        method(threshold, function(self, priority) {
            selectHandler(priority);
        });

        method(log, function(self, operation, category, message, exception) {
            operation(handler, category, message, exception);
        });
    });
}

//keep an alias for backward compatibility
var FirebugLogHandler = ConsoleLogHandler;

function WindowLogHandler(thresholdPriority, name) {
    var lineOptions = [25, 50, 100, 200, 400];
    var numberOfLines = lineOptions[3];
    var categoryMatcher = /.*/;
    var closeOnExit = true;
    var logContainer;
    var logEntry = noop;

    function trimLines() {
        var nodes = logContainer.childNodes;
        var trim = size(nodes) - numberOfLines;
        if (trim > 0) {
            each(copy(nodes), function(node, index) {
                if (index < trim) logContainer.removeChild(node);
            });
        }
    }

    function trimAllLines() {
        each(copy(logContainer.childNodes), function(node) {
            logContainer.removeChild(node);
        });
    }

    function toggle() {
        var disabled = logEntry == noop;
        logEntry = disabled ? displayEntry : noop;
        return !disabled;
    }

    function displayEntry(priorityName, colorName, category, message, exception) {
        var categoryName = join(category, '.');

        if (categoryMatcher.test(categoryName)) {
            var elementDocument = logContainer.ownerDocument;
            var timestamp = new Date();
            var completeMessage = join(['[', categoryName, '] : ', message, (exception ? join(['\n', exception.name, ' <', exception.message, '>'], '') : '')], '');
            each(split(completeMessage, '\n'), function(line) {
                if (/(\w+)/.test(line)) {
                    var eventNode = elementDocument.createElement('div');
                    eventNode.style.padding = '3px';
                    eventNode.style.color = colorName;
                    eventNode.setAttribute("title", timestamp + ' | ' + priorityName)
                    logContainer.appendChild(eventNode).appendChild(elementDocument.createTextNode(line));
                }
            });
            logContainer.scrollTop = logContainer.scrollHeight;
        }
        trimLines();
    }

    function showWindow() {
        var logWindow = window.open('', '_blank', 'scrollbars=1,width=800,height=680');
        try {
            var windowDocument = logWindow.document;
            var documentBody = windowDocument.body;
            //erase previous content!
            each(copy(documentBody.childNodes), function(e) {
                windowDocument.body.removeChild(e);
            });
            //create 'Close on exit' checkbox
            documentBody.appendChild(windowDocument.createTextNode(' Close on exit '));
            var closeOnExitCheckbox = windowDocument.createElement('input');
            closeOnExitCheckbox.style.margin = '2px';
            closeOnExitCheckbox.setAttribute('type', 'checkbox');
            closeOnExitCheckbox.defaultChecked = true;
            closeOnExitCheckbox.checked = true;
            closeOnExitCheckbox.onclick = function() {
                closeOnExit = closeOnExitCheckbox.checked;
            };
            documentBody.appendChild(closeOnExitCheckbox);
            //create 'Lines' drop down
            documentBody.appendChild(windowDocument.createTextNode(' Lines '));
            var lineCountDropDown = windowDocument.createElement('select');
            lineCountDropDown.style.margin = '2px';
            each(lineOptions, function(count, index) {
                var option = lineCountDropDown.appendChild(windowDocument.createElement('option'));
                if (numberOfLines == count) lineCountDropDown.selectedIndex = index;
                option.appendChild(windowDocument.createTextNode(asString(count)));
            });
            documentBody.appendChild(lineCountDropDown);
            //create 'Category' input text
            documentBody.appendChild(windowDocument.createTextNode(' Category '));
            var categoryInputText = windowDocument.createElement('input');
            categoryInputText.style.margin = '2px';
            categoryInputText.setAttribute('type', 'text');
            categoryInputText.setAttribute('value', categoryMatcher.source);
            categoryInputText.onchange = function() {
                categoryMatcher = new RegExp(categoryInputText.value);
            };
            documentBody.appendChild(categoryInputText);
            //create 'Level' drop down
            documentBody.appendChild(windowDocument.createTextNode(' Level '));
            var levelDropDown = windowDocument.createElement('select');
            levelDropDown.style.margin = '2px';
            var levels = [Cell('debug', debug), Cell('info', info), Cell('warn', warn), Cell('error', error)];
            each(levels, function(priority, index) {
                var option = levelDropDown.appendChild(windowDocument.createElement('option'));
                if (thresholdPriority == value(priority)) levelDropDown.selectedIndex = index;
                option.appendChild(windowDocument.createTextNode(key(priority)));
            });
            levelDropDown.onchange = function(event) {
                thresholdPriority = value(levels[levelDropDown.selectedIndex]);
            };
            documentBody.appendChild(levelDropDown);
            //create 'Start/Stop' button
            var startStopButton = windowDocument.createElement('input');
            startStopButton.style.margin = '2px';
            startStopButton.setAttribute('type', 'button');
            startStopButton.setAttribute('value', 'Stop');
            startStopButton.onclick = function() {
                startStopButton.setAttribute('value', toggle() ? 'Stop' : 'Start');
            };
            documentBody.appendChild(startStopButton);
            //create 'Clear' button
            var clearButton = windowDocument.createElement('input');
            clearButton.style.margin = '2px';
            clearButton.setAttribute('type', 'button');
            clearButton.setAttribute('value', 'Clear');
            documentBody.appendChild(clearButton);

            logContainer = documentBody.appendChild(windowDocument.createElement('pre'));
            logContainer.id = 'log-window';
            var logContainerStyle = logContainer.style;
            logContainerStyle.width = '100%';
            logContainerStyle.minHeight = '0';
            logContainerStyle.maxHeight = '550px';
            logContainerStyle.borderWidth = '1px';
            logContainerStyle.borderStyle = 'solid';
            logContainerStyle.borderColor = '#999';
            logContainerStyle.backgroundColor = '#ddd';
            logContainerStyle.overflow = 'scroll';

            lineCountDropDown.onchange = function(event) {
                numberOfLines = lineOptions[lineCountDropDown.selectedIndex];
                trimLines();
            };
            clearButton.onclick = trimAllLines;

            onUnload(window, function() {
                if (closeOnExit) {
                    logEntry = noop;
                    logWindow.close();
                }
            });
        } catch (e) {
            logWindow.close();
        }
    }

    onKeyUp(document, function(evt) {
        var event = $event(evt, document.documentElement);
        if (keyCode(event) == 84 && isCtrlPressed(event) && isShiftPressed(event)) {
            showWindow();
            logEntry = displayEntry;
        }
    });

    return object(function(method) {
        method(threshold, function(self, priority) {
            thresholdPriority = priority;
        });

        method(log, function(self, operation, category, message, exception) {
            operation(self, category, message, exception);
        });

        method(debug, function(self, category, message, exception) {
            logEntry('debug', '#333', category, message, exception);
        });

        method(info, function(self, category, message, exception) {
            logEntry('info', 'green', category, message, exception);
        });

        method(warn, function(self, category, message, exception) {
            logEntry('warn', 'orange', category, message, exception);
        });

        method(error, function(self, category, message, exception) {
            logEntry('error', 'red', category, message, exception);
        });
    });
}
