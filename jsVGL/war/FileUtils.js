// functions to work with FileSaver.js from https://github.com/eligrey/FileSaver.js

function saveWork(stateXML) {
	if (stateXML.startsWith("ERROR: No Problem")) {
		alert("There is no work to save; please start a problem first.");
		return;
	}
	if (stateXML.startsWith("ERROR: Practice")) {
		alert("Sorry, but it is not possible to save a problem created in Practice Mode.");
		return;
	}
	var x2js = new X2JS();
	var blob = new Blob([JSON.stringify(x2js.xml_str2json(stateXML))], {type: "text/plain;charset=utf-8"});
	var workFileName = document.getElementById("workFileName").value;
	if (workFileName == "") {
		alert("Please enter a name for the file in the blank to the right of the Save button.");
		return;
	}
	if (!workFileName.endsWith(".jsvgl")) {
		workFileName = workFileName + ".jsvgl";
	}
	alert("A file named " + workFileName + " will be saved to your Desktop.\n Your browser may warn you about the file; it is safe.");
	saveAs(blob, workFileName);
}

function loadWork(inputFile) {
	var reader = new FileReader();
	reader.readAsText(inputFile);
	reader.onload = fileLoaded;
}

function fileLoaded(evt) {
	var contents = evt.target.result;
	var x2js = new X2JS();
	try {
		var xml = x2js.json2xml_str(JSON.parse(contents));
	}
	catch (err) {
		alert("Sorry, that doesn't seem to be a jsVGL file.\nPlease try a different file.");
		return;
	}
	window.setStateXML(xml);
}