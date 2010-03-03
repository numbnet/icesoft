var at = operator();
var putAt = operator();
var removeAt = operator();

var Hashtable;

(function() {
    var removeInArray = Array.prototype.splice ? function(array, index) {
        array.splice(index, 1);
    } : function(array, index) {
        if (index == array.length - 1) {
            array.length = index;
        } else {
            var rightSlice = array.slice(index + 1);
            array.length = index;
            for (var i = 0, l = rightSlice.length; i < l; ++i) {
                array[index + i] = rightSlice[i];
            }
        }
    };


    Hashtable = function() {
        var buckets = [];

        return object(function(method) {
            method(at, function at(self, k, notFoundThunk) {
                var index = hash(k);
                var bucket = buckets[index];
                if (bucket) {
                    for (var i = 0, l = bucket.length; i < l; i++) {
                        var entry = bucket[i];
                        if (equal(entry.key, k)) {
                            return entry.value;
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            });

            method(putAt, function putAt(self, k, v) {
                var index = hash(k);
                var bucket = buckets[index];
                if (bucket) {
                    for (var i = 0, l = bucket.length; i < l; i++) {
                        var entry = bucket[i];
                        if (equal(entry.key, k)) {
                            var oldValue = entry.value;
                            entry.value = v;
                            return oldValue;
                        }
                    }
                    bucket.push({ key:k, value: v });
                    return null;
                } else {
                    bucket = [
                        {
                            key:k,
                            value: v
                        }
                    ];
                    buckets[index] = bucket;
                    return null;
                }
            });

            method(removeAt, function(self, k) {
                var index = hash(k);
                var bucket = buckets[index];
                if (bucket) {
                    for (var i = 0, l = bucket.length; i < l; i++) {
                        var entry = bucket[i];
                        if (equal(entry.key, k)) {
                            removeInArray(bucket, i);
                            return entry.value;
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            });
        });
    };
})();