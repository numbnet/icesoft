/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

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

    function atPrimitive(buckets, bucketCount, k, notFoundThunk) {
        var index = hash(k) % bucketCount;
        var bucket = buckets[index];
        if (bucket) {
            for (var i = 0, l = bucket.length; i < l; i++) {
                var entry = bucket[i];
                if (equal(entry.key, k)) {
                    return entry.value;
                }
            }
            if (notFoundThunk) notFoundThunk();
            return null;
        } else {
            if (notFoundThunk) notFoundThunk();
            return null;
        }
    }

    function putAtPrimitive(buckets, bucketCount, k, v) {
        var index = hash(k) % bucketCount;
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

    function removeAtPrimitive(buckets, bucketCount, k) {
        var index = hash(k) % bucketCount;
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
    var internalBucketCount = operator();

    HashTable = function() {
        var buckets = [];
        var bucketCount = 5000;

        return object(function(method) {
            method(at, function(self, k, notFoundThunk) {
                return atPrimitive(buckets, bucketCount, k, notFoundThunk);
            });

            method(putAt, function(self, k, v) {
                return putAtPrimitive(buckets, bucketCount, k, v);
            });

            method(removeAt, function(self, k) {
                return removeAtPrimitive(buckets, bucketCount, k);
            });

            method(each, function(iterator) {
                injectPrimitive(buckets, null, function(tally, k, v) {
                    iterator(k, v);
                });
            });
        });
    };

    HashSet = function(list) {
        var buckets = [];
        var bucketCount = 5000;
        var present = new Object;

        if (list) {
            each(list, function(k) {
                putAtPrimitive(buckets, bucketCount, k, present);
            });
        }

        return object(function(method) {
            method(append, function(self, k) {
                putAtPrimitive(buckets, bucketCount, k, present);
            });

            method(each, function(self, iterator) {
                injectPrimitive(buckets, null, function(t, k, v) {
                    iterator(k);
                });
            });

            method(contains, function(self, k) {
                return !!atPrimitive(buckets, bucketCount, k);
            });

            method(complement, function(self, other) {
                var result = [];
                var c;
                try {
                    var othersInternalBuckets = internalBuckets(other);
                    var othersInternalBucketCount = internalBucketCount(other);
                    c = function(items, k) {
                        return !!atPrimitive(othersInternalBuckets, othersInternalBucketCount, k);
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

            method(asString, function(self) {
                return 'HashSet[' + join(injectPrimitive(buckets, [], function(tally, k, v) {
                    tally.push(k);
                    return tally;
                }), ',') + ']';
            });

            method(internalBuckets, function(self) {
                return buckets;
            });

            method(internalBucketCount, function(self) {
                return bucketCount;
            });
        });
    };
})();