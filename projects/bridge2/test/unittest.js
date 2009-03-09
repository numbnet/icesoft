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
    function styleNormalCell(cell) {
        cell.style.padding = '6px';
        cell.style.borderColor = 'green';
        cell.style.borderBottomWidth = '1px';
        cell.style.borderBottomStyle = 'dotted';
    }

    function styleHeaderCell(cell) {
        cell.style.padding = '6px';
        cell.style.backgroundColor = '#dddddd';
        cell.style.borderColor = 'green';
        cell.style.borderBottomWidth = '1px';
        cell.style.borderBottomStyle = 'solid';
        return cell;
    }

    function timeThunk(thunk) {
        var start = new Date;
        thunk();
        var end = new Date;
        return end.getMilliseconds() - start.getMilliseconds();
    }


    return function() {
        var cursor = document.body;
        cursor.appendChild(document.createElement('h3')).appendChild(document.createTextNode(suiteName));
        cursor = cursor.appendChild(document.createElement('table'));
        var titles = cursor.appendChild(document.createElement('tr'));
        styleHeaderCell(titles.appendChild(document.createElement('td'))).appendChild(document.createTextNode('Test Case'));
        styleHeaderCell(titles.appendChild(document.createElement('td'))).appendChild(document.createTextNode('Result'));
        styleHeaderCell(titles.appendChild(document.createElement('td'))).appendChild(document.createTextNode('Time (ms)'));
        cursor.style.borderCollapse = 'collapse';

        document.title = suiteName;

        var interruptTest = new Object;
        Tester(function(message) {
            var error = cursor.appendChild(document.createElement('td'));
            var empty = cursor.appendChild(document.createElement('td'));
            error.appendChild(document.createTextNode(message));
            error.style.letterSpacing = '0.12em';
            cursor.style.backgroundColor = '#ee4444';
            styleNormalCell(error);
            styleNormalCell(empty);
            throw interruptTest;
        });

        var previousCursor = cursor;
        tests(function(testName, thunk) {
            try {
                cursor = cursor.appendChild(document.createElement('tr'));
                cursor.style.backgroundColor = '#ddffaa';

                var testTitle = cursor.appendChild(document.createElement('td'));
                testTitle.appendChild(document.createTextNode(testName));
                styleNormalCell(testTitle);

                var elapsedTime = timeThunk(thunk);

                var result = cursor.appendChild(document.createElement('td'));
                result.appendChild(document.createTextNode('okay'));
                styleNormalCell(result);

                var time = cursor.appendChild(document.createElement('td'));
                time.appendChild(document.createTextNode(elapsedTime));
                styleNormalCell(time);
            } catch (e) {
                if (e != interruptTest) throw e;
            } finally {
                cursor = previousCursor;
            }
        });
    };
}