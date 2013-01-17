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

var indexOf = function(s, substring) {
    var index = s.indexOf(substring);
    if (index >= 0) {
        return index;
    } else {
        throw '"' + s + '" does not contain "' + substring + '"';
    }
};

var lastIndexOf = function(s, substring) {
    var index = s.lastIndexOf(substring);
    if (index >= 0) {
        return index;
    } else {
        throw 'string "' + s + '" does not contain "' + substring + '"';
    }
};

var startsWith = function(s, pattern) {
    return s.indexOf(pattern) == 0;
};

var endsWith = function(s, pattern) {
    return s.lastIndexOf(pattern) == s.length - pattern.length;
};

var containsSubstring = function(s, substring) {
    return s.indexOf(substring) >= 0;
};

var blank = function(s) {
    return /^\s*$/.test(s);
};

var split = function(s, separator) {
    return s.length == 0 ? [] : s.split(separator);
};

var replace = function(s, regex, replace) {
    return s.replace(regex, replace);
};

var toLowerCase = function(s) {
    return s.toLowerCase();
};

var toUpperCase = function(s) {
    return s.toUpperCase();
};

var substring = function(s, from, to) {
    return s.substring(from, to);
};

var trim = function(s) {
    s = s.replace(/^\s+/, '');
    for (var i = s.length - 1; i >= 0; i--) {
        if (/\S/.test(s.charAt(i))) {
            s = s.substring(0, i + 1);
            break;
        }
    }

    return s;
};

var asNumber = Number;

var asBoolean = function(s) {
    return 'true' == s || 'any' == s;
};

var asRegexp = function(s) {
    return new RegExp(s);
};



