function lookupCookieValue(name) {
    var tupleString = detect(split(asString(document.cookie), '; '), function(tuple) {
        return startsWith(tuple, name);
    }, function() {
        throw 'Cannot find value for cookie: ' + name;
    });

    return decodeURIComponent(contains(tupleString, '=') ? split(tupleString, '=')[1] : '');
}

function lookupCookie(name, failThunk) {
    try {
        return Cookie(name, lookupCookieValue(name));
    } catch (e) {
        if (failThunk) {
            return failThunk();
        } else {
            throw e;
        }
    }
}

function existsCookie(name) {
    var exists = true;
    lookupCookie(name, function() {
        exists = false;
    });
    return exists;
}

var update = operator();
var remove = operator();
function Cookie(name, val, path) {
    val = val || '';
    path = path || '/';
    document.cookie = name + '=' + val + '; path=' + path;

    return object(function(method) {
        method(value, function(self) {
            return lookupCookieValue(name);
        });

        method(update, function(self, val) {
            document.cookie = name + '=' + encodeURIComponent(val) + '; path=' + path;
            return self;
        });

        method(remove, function(self) {
            var date = new Date();
            date.setTime(date.getTime() - 24 * 60 * 60 * 1000);
            document.cookie = name + '=; expires=' + date.toGMTString() + '; path=' + path;
        });

        method(asString, function(self) {
            return 'Cookie[' + name + ', ' + value(self) + ', ' + path + ']';
        });
    });
}