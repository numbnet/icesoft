var checkTrue;
var checkFalse;
var checkException;
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
}

function InPageRunner(tests) {
    var container = document.createElement('ul');
    document.body.appendChild(container);
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

    tests(function(testName, thunk) {
        container.appendChild(document.createElement('h2').appendChild(document.createTextNode(testName)));
        thunk();
    });
}

window.onload = function() {
    InPageRunner(function(test) {
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
            try {
                throw 'bla';
            } catch (e) {
                fail('failed to run without exceptions');
            }
        });
    });
};