// functions to work with FileSaver.js from https://github.com/eligrey/FileSaver.js

function loadWorkDialog() {
	document.getElementById("loadFileChooserDialog").showModal();
}

function loadWork(inputFile) {
	var reader = new FileReader();
	reader.readAsText(inputFile);
	reader.onload = fileLoaded;
}

function fileLoaded(evt) {
	var contents = evt.target.result;
	window.setGreenhouse(contents);
	document.getElementById('loadFileChooserDialog').close();
}
