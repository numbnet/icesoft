function replaceDiv(divID, content) {
/*     var tmp = document.createElement('div');
     try {
       tmp.style.display = 'none';
       document.body.appendChild(tmp);
       tmp.innerHTML = content;

       var newDiv = tmp.firstChild;
       var oldDiv = document.getElementById(divID);
       oldDiv.parentNode.replaceChild(newDiv, oldDiv);
     } finally {
       document.body.removeChild(tmp);
     } */
	var container = document.getElementById(divID);
	container.innerHTML = content;
}
