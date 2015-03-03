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

/**
 * Created by IntelliJ IDEA.
 * User: mircea
 * Date: Dec 14, 2010
 * Time: 2:43:07 PM
 * To change this template use File | Settings | File Templates.
 */
var callStack = operator();
var exceptionMessage = operator();
var originalException = operator();
var Exception;

(function() {
    var functionDefinitionPattern = /function\s*([\w\-$]+)?\s*\(/i;
    var anonymous = '\u03BB';

    function argumentAsString(arg) {
        var result;
        if (arg === undefined) {
            result = 'undefined';
        } else if (arg === null) {
            result = 'null';
        } else if (typeof arg == 'number') {
            result = arg;
        } else if (arg.constructor) {
            if (arg.constructor === Array) {
                if (arg.length < 3) {
                    result = '[' + asString(arg) + ']';
                } else {
                    result = '[' + argumentAsString(arg[0]) + '...' + argumentAsString(arg[arg.length - 1]) + ']';
                }
            } else if (arg.constructor === Object) {
                result = '[object]';
            } else if (arg.constructor === Function) {
                var functionDefinition = arg.toString();
                result = '[' + substring(functionDefinition, 0, indexOf(functionDefinition, ')') + 1) + ']';
            } else if (arg.constructor === String) {
                result = '\'' + arg + '\'';
            } else {
                result = asString(arg);
            }
        } else {
            result = asString(arg);
        }

        return result;
    }

    function captureCallStack(caller) {
        var stack = [];
        var maxStackSize = 10;
        while (caller && stack.length < maxStackSize) {
            var functionDefinition = caller.toString();
            var functionName = functionDefinitionPattern.test(functionDefinition) ? RegExp.$1 || anonymous : anonymous;
            var argumentNames = collect(split(substring(functionDefinition, indexOf(functionDefinition, '(') + 1, indexOf(functionDefinition, ')')), ','), trim);
            var argumentValues = caller.arguments;
            var nameValueArguments = [];
            each(argumentNames, function(name, index) {
                var andValue = argumentValues.length <= index ? '' : (': ' + argumentAsString(argumentValues[index]));
                append(nameValueArguments, name + andValue);
            });
            stack[stack.length] = functionName + '(' + join(nameValueArguments, ', ') + ')';
            caller = caller.caller;
        }
        if (caller) {
            append(stack, '.......');
        }

        return stack;
    }

    Exception = function(message, originalException) {
        var stack = captureCallStack(arguments.callee.caller);

        return object(function(method) {
            method(callStack, function(self) {
                return stack;
            });

            method(originalException, function(self) {
                return originalException;
            });

            method(exceptionMessage, function(self) {
                return message;
            });

            method(asString, function(self) {
                return join(concatenate(['exception <' + message + '>', ''], stack), '\n');
            });
        });
    }
})();
