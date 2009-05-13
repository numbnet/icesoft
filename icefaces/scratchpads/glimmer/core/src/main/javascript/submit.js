function submit(event, element) {
    var query = Query();
    serializeOn($event(event, element), query);
    var queryString = join(collect(queryParameters(query), asURIEncodedString), ',');
    jsf.ajax.request(element, event, {execute: '@all', render: '@all', params: queryString});
}