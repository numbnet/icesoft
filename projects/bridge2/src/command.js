var register = operator();
var deserializeAndExecute = operator();
var CommandDispatcher;
var SetCookie;
var ParsingError;

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
