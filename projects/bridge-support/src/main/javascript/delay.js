var run = operator();
var runOnce = operator();
var stop = operator();
function Delay(f, milliseconds) {
    return object(function(method) {
        var id = null;

        method(run, function(self, times) {
            //avoid starting a new process
            if (id) return;

            var call = times ? function() {
                try {
                    f();
                } finally {
                    if (--times < 1) stop(self);
                }
            } : f;

            id = setInterval(call, milliseconds);

            return self;
        });

        method(runOnce, function(self) {
            return run(self, 1);
        });

        method(stop, function(self) {
            //stop only an active process
            if (!id) return;

            clearInterval(id);
            id = null;
        });
    });
}