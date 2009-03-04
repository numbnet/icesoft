var checkTrue;
var checkFalse;
var checkException;
var checkEqual;
var checkNotEqual;
var fail;

function Tester(failure) {
    checkTrue = function(value, message) {
        if (!value) {
            failure(message);
        }
    };

    checkFalse = function(value, message) {
        if (value) {
            failure(message);
        }
    };

    checkException = function(thunk, message) {
        try {
            thunk();
        } catch (e) {
            return;
        }

        failure(message);
    };

    fail = function(message, exception) {
        failure(message + (exception && ' > ' + String(exception) || ''));
    };

    checkEqual = function(ref, tested, message) {
        if (ref != tested) {
            failure(message || ('expected value is (' + ref + ') but it was (' + tested + ')'));
        }
    };

    checkNotEqual = function(ref, tested, message) {
        if (ref == tested) {
            failure(message || ('compared values are not different as expected'));
        }
    };
}

function InPageRunner(suiteName, tests) {
    return function() {
        var container = document.body;
        container.appendChild(document.createElement('h3')).appendChild(document.createTextNode(suiteName));

        Tester(function(message) {
            var entry = document.createElement('li');
            container.appendChild(entry);
            entry.appendChild(document.createTextNode(message));
        });

        var previousContainer = container;
        tests(function(testName, thunk) {
            container.appendChild(document.createElement('h4')).appendChild(document.createTextNode(testName));
            container = previousContainer.appendChild(document.createElement('ol'));
            thunk();
            container = previousContainer;
        });
    };
}

window.onload = InPageRunner('Less is more', function(test) {
    test('Check greatness', function() {
        checkTrue(4 > 5, '4 is not greater than 5');
        checkTrue(3 > 5, '3 is not greater than 5');
        checkTrue(3 < 5, '3 is greater than 5');
        checkException(function() {
            throw '';
        }, 'Failed to detect exception');

        try {
            throw 'bla';
        } catch (e) {
            fail('failed to run without exceptions', e);
        }
    });

    test('Check lesser', function() {
        checkFalse(4 < 5, '5 is not greater than 4');
        checkEqual('a', 'aaa');
        checkNotEqual('b', 'b');
        try {
            throw 'bla';
        } catch (e) {
            fail('failed to run without exceptions');
        }
    });
});
