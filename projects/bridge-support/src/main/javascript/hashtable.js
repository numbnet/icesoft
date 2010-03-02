var at = operator();
var putAt = operator();
var Hashtable;

(function() {
    function jenkinsOneAtATimeHash(k) {
        var key = String(k);
        var hash = 0;
        for (var i = 0, length = key.length; i < length; i++) {
            hash += parseInt(key[i], 36);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return hash;
    }

    Hashtable = function(hashFunction, equalFunction) {
        var h = hashFunction || jenkinsOneAtATimeHash;
        var eq = equalFunction || equal;

        var buckets = [];

        return object(function(method) {
            method(at, function at(self, k, notFoundThunk) {
                var index = h(k);
                var bucket = buckets[index];
                if (bucket) {
                    for (var i = 0, l = bucket.length; i < l; i++) {
                        var entry = bucket[i];
                        if (eq(entry.key, k)) {
                            return entry.value;
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            });

            method(putAt, function putAt(self, k, v) {
                var index = h(k);
                var bucket = buckets[index];
                if (bucket) {
                    for (var i = 0, l = bucket.length; i < l; i++) {
                        var entry = bucket[i];
                        if (eq(entry.key, k)) {
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
        });
    };
})();