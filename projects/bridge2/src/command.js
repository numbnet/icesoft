var register = operator();
var deserializeAndExecute = operator();
var CommandDispatcher;
var SetCookie;
var ParsingError;
var Redirect;
var Macro;

(function() {
    CommandDispatcher = function() {
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
    };

    Macro = function(message) {
        each(message.childNodes, curry(deserializeAndExecute, commandDispatcher));
    };

    Redirect = function(element) {
        //replace ampersand entities incorrectly decoded by Safari 2.0.4
        var url = replace(element.getAttribute("url"), /&#38;/g, "&");
        info(logger, 'Redirecting to ' + url);
        window.location.href = url;
    };

    SetCookie = function(message) {
        document.cookie = message.firstChild.data;
    };

    ParsingError = function(message) {
        logger.error('Parsing error');
        var errorNode = message.firstChild;
        logger.error(errorNode.data);
        var sourceNode = errorNode.firstChild;
        logger.error(sourceNode.data);
    };
})();
