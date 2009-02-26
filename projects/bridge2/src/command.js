var register = operator();
var deserializeAndExecute = operator();

function CommandDispatcher() {
    var commands = [];

    return object(function(method) {
        method(register, function(self, messageName, command) {
            commands = reject(commands, function(cell) {
                return key(cell) == messageName;
            });
            append(commands, Cell(messageName, command));
        });

        method(deserializeAndExecute, function(self, message) {
            var messageName = message.tagName;
            var found = detect(commands, function(cell) {
                return key(cell) == messageName;
            }, function() {
                throw 'Unknown message received: ' + messageName;
            });
            value(found)(message);
        });
    });
}

function Macro(message) {
    each(message.childNodes, curry(deserializeAndExecute, commandDispatcher));
}

function Redirect(element) {
    //replace ampersand entities incorrectly decoded by Safari 2.0.4
    var url = replace(element.getAttribute("url"), /&#38;/g, "&");
    info(logger, 'Redirecting to ' + url);
    window.location.href = url;
}

function Reload(element) {
    info(logger, 'Reloading');
    var url = window.location.href;
    if (containsSubstring(url, 'rvn=')) {
        window.location.reload();
    } else {
        var view = element.getAttribute('view');
        if (view == '') {
            window.location.reload();
        } else {
            var queryPrefix = containsSubstring(url, '?') ? '&' : '?';
            window.location.href = url + queryPrefix + 'rvn=' + view;
        }
    }
}

function SetCookie(message) {
    document.cookie = message.firstChild.data;
}

function ParsingError(message) {
    logger.error('Parsing error');
    var errorNode = message.firstChild;
    logger.error(errorNode.data);
    var sourceNode = errorNode.firstChild;
    logger.error(sourceNode.data);
}
