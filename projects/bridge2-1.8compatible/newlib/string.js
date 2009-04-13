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

var asNumber = Number;

var asBoolean = function(s) {
    return 'true' == s || 'any' == s;
};

var asRegexp = function(s) {
    return new RegExp(s);
};



