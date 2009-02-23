(function(This) {
    //todo: should this code be part of Element.replaceHtml method?    
    This.Loader = function(logger) {
        var logger = childLogger(logger, 'script-loader');
        //the scripts present on the page are already evaluated
        var referencedScripts = asArray(select(document.documentElement.getElementsByTagName('script'), function(script) {
            return script.src;
        }));
        var client = Client(this.logger);

        function evaluateScript(script) {
            var uri = script.src;
            if (uri) {
                if (!contains(referencedScripts, script.src)) {
                    debug(logger, 'loading : ' + uri);
                    getSynchronously(client, uri, noop, noop, $witch(function(condition) {
                        condition(OK, function(response) {
                            append(referencedScripts, uri);
                            debug(logger, 'evaluating script at : ' + uri);
                            try {
                                eval(contentAsText(response));
                            } catch (e) {
                                warn(logger, 'Failed to evaluate script located at: ' + uri, e);
                            }
                        });
                    }));
                }
            } else {
                var code = script.innerHTML;
                debug(logger, 'evaluating script : ' + code);
                try {
                    eval(code);
                } catch (e) {
                    warn(logger, 'Failed to evaluate script: \n' + code, e);
                }
            }
        }

        return function(element) {
            each(element.getElementsByTagName('script'), evaluateScript);
        };
    };
})(Ice.Script = new Object);