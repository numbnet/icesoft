function replaceDiv(divID, content) {
    var container = document.getElementById(divID);
    container.innerHTML = content;
}

function getRegion(url, divID, group) {
    ice.push.get(url, function(parameter) {
        parameter('group', group);
        },
        function(statusCode, responseText) {replaceDiv(divID, responseText);});
}
