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
        failure(message + (exception && ' -- ' + String(exception) || ''));
    };

    checkEqual = function(tested, ref, message) {
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
        var cursor = document.body;
        cursor.appendChild(document.createElement('h3')).appendChild(document.createTextNode(suiteName));

        var interruptTest = new Object;
        Tester(function(message) {
            cursor.appendChild(document.createTextNode(message));
            cursor.style.color = 'red';
            throw interruptTest;
        });

        var previousCursor = cursor;
        tests(function(testName, thunk) {
            try {
                cursor = cursor.appendChild(document.createElement('li'));
                cursor.appendChild(document.createTextNode(testName + ' :: '));
                thunk();
                cursor.appendChild(document.createTextNode('ok'));
                cursor.style.color = 'green';
            } catch (e) {
                if (e != interruptTest) throw e;
            } finally {
                cursor = previousCursor;
            }
        });
    };
}

window.onload = InPageRunner('Less is more', function(test) {
    test('Check greatness', function() {
        checkTrue(4 < 5, '4 is not greater than 5');
        checkTrue(3 < 5, '3 is not greater than 5');
        checkException(function() {
            throw 'dfgdf';
        }, 'Failed to detect exception');

        try {
            throw 'bla';
        } catch (e) {
            fail('failed to run without exceptions', e);
        }
    });

    test('Check lesser', function() {
        checkFalse(4 > 5, '5 is not greater than 4');
        checkEqual('a', 'aaa');
        checkNotEqual('b', 'b');
        try {
            throw 'bla';
        } catch (e) {
            fail('failed to run without exceptions');
        }
    });

    test('All true!', function() {
        checkFalse(4 > 5, '5 is not greater than 4');
    });
});
