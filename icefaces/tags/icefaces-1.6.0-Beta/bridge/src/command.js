[ Ice.Command = new Object ].as(function(This) {

    This.Dispatcher = Object.subclass({
        initialize: function() {
            this.commands = new Object;
        },

        register: function(messageName, command) {
            this.commands[messageName] = command;
        },

        deserializeAndExecute: function(message) {
            var messageName = message.tagName;
            for (name in this.commands) {
                if (name == messageName) {
                    this.commands[messageName](message);
                    return;
                }
            }

            throw 'Unknown message received: ' + messageName;
        }
    });

    This.Redirect = function(element) {
        var url = element.getAttribute("url");
        /* the following replaces ampersand entities incorrectly decoded
           by Safari 2.0.4.  It appears to be fixed in nightly Safari builds 
        */
        url = url.replace(/&#38;/g, "&");
        logger.info('Redirecting to ' + url);
        //avoid view disposal on navigation rules
        if (url.contains('rvn=')) {
            connection.cancelDisposeViews();
        }
        window.location.href = url;
    };

    This.SetCookie = function(message) {
        document.cookie = message.firstChild.data;
    };

    This.ServerError = function(message) {
        logger.error('Server side error');
        logger.error(message.firstChild.data);
        statusManager.serverError.on();
        application.dispose();
    };

    This.ParsingError = function(message) {
        logger.error('Parsing error');
        var errorNode = message.firstChild;
        logger.error(errorNode.data);
        var sourceNode = errorNode.firstChild;
        logger.error(sourceNode.data);
    };
});
