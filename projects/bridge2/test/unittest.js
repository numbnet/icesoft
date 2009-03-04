var checkTrue;
var checkFalse;
var checkException;
var checkEqual;
var checkNotEqual;
var fail;

function Tester(success, failure) {
    checkTrue = function(value, failMessage, successMessage) {
        if (!value) {
            failure(failMessage);
        } else {
            success(successMessage);
        }
    };

    checkFalse = function(value, failMessage, successMesage) {
        if (value) {
            failure(failMessage);
        } else {
            success(successMesage);
        }
    };

    checkException = function(thunk, failMessage, successMessage) {
        try {
            thunk();
        } catch (e) {
            return success(successMessage);
        }

        failure(failMessage);
    };

    fail = function(message, exception) {
        failure(message + (exception && ' > ' + String(exception) || ''));
    };

    checkEqual = function(ref, tested, failMessage, sucessMessage) {
        if (ref == tested) {
            success(sucessMessage);
        } else {
            failure(failMessage || ('expected value is (' + ref + ') but it was (' + tested + ')'));
        }
    };

    checkNotEqual = function(ref, tested, failMessage, sucessMessage) {
        if (ref != tested) {
            success(sucessMessage);
        } else {
            failure(failMessage || ('compared values are not different as expected'));
        }
    };
}

function InPageRunner(tests) {
    return function() {
        var container = document.body;

        function fail(message) {
            var entry = document.createElement('li');
            container.appendChild(entry);
            entry.appendChild(document.createTextNode(message));
        }

        function success(message) {
            var entry = document.createElement('li');
            container.appendChild(entry);
            entry.appendChild(document.createTextNode(message || 'ok'));
        }

        Tester(success, fail);

        var previousContainer = container;
        tests(function(testName, thunk) {
            container.appendChild(document.createElement('h4').appendChild(document.createTextNode(testName)));
            container = previousContainer.appendChild(document.createElement('ol'));
            thunk();
            container = previousContainer;
        });
    };
}

window.onload = InPageRunner(function(test) {
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
