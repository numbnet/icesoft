var at = operator();
var putAt = operator();
var removeAt = operator();

var HashTable;
var HashSet;

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

    function atPrimitive(buckets, k, notFoundThunk) {
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
    }

    function putAtPrimitive(buckets, k, v) {
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
    }

    function removeAtPrimitive(buckets, k) {
        var index = hash(k);
        var bucket = buckets[index];
        if (bucket) {
            for (var i = 0, l = bucket.length; i < l; i++) {
                var entry = bucket[i];
                if (equal(entry.key, k)) {
                    removeInArray(bucket, i);
                    if (bucket.length == 0) {
                        removeInArray(buckets, index);
                    }
                    return entry.value;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    function injectPrimitive(buckets, initialValue, iterator) {
        var tally = initialValue;
        for (var i = 0, lbs = buckets.length; i < lbs; i++) {
            var bucket = buckets[i];
            if (bucket) {
                for (var j = 0, lb = bucket.length; j < lb; j++) {
                    var entry = bucket[j];
                    if (entry) {
                        tally = iterator(tally, entry.key, entry.value);
                    }
                }
            }
        }

        return tally;
    }

    var internalBuckets = operator();

    HashTable = function() {
        var buckets = [];

        return object(function(method) {
            method(at, function(self, k, notFoundThunk) {
                return atPrimitive(buckets, k, notFoundThunk);
            });

            method(putAt, function putAt(self, k, v) {
                return putAtPrimitive(buckets, k, v);
            });

            method(removeAt, function(self, k) {
                return removeAtPrimitive(buckets, k);
            });

            method(each, function(iterator) {
                injectPrimitive(buckets, null, function(tally, k, v) {
                    iterator(k, v);
                });
            });
        });
    };

    HashSet = function() {
        var buckets = [];
        var present = new Object;
        return object(function(method) {
            method(append, function(self, k) {
                putAtPrimitive(buckets, k, present);
            });

            method(each, function(self, iterator) {
                injectPrimitive(buckets, null, function(t, k, v) {
                    iterator(k);
                });
            });

            method(contains, function(self, k) {
                return !!atPrimitive(buckets, k);
            });

            method(complement, function(self, other) {
                var result = [];
                var c;
                try {
                    var othersInternalBuckets = internalBuckets(other);
                    c = function(items, k) {
                        return !!atPrimitive(othersInternalBuckets, k);
                    };
                } catch (e) {
                    c = contains;
                }

                return injectPrimitive(buckets, result, function(tally, k, v) {
                    if (!c(other, k)) {
                        result.push(k);
                    }
                    return tally;
                });
            });

            method(internalBuckets, function(self) {
                return buckets;
            });
        });
    };
})();