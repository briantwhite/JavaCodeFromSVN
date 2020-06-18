// functions to work with FileSaver.js from https://github.com/eligrey/FileSaver.js

function saveWorkDialog(stateXML) {
	if (stateXML.startsWith("ERROR: No Problem")) {
		alert("There is no work to save; please start a problem first.");
		return;
	}
	if (stateXML.startsWith("ERROR: Practice")) {
		alert("Sorry, but it is not possible to save a problem created in Practice Mode.");
		return;
	}

	var fileName = prompt("Enter the name of the file to save and click OK:", "work file.jsvgl");
	if (fileName != null) {
		saveWork(stateXML, fileName);
	}
}

function saveWork(stateXML, workFileName) {
	var x2js = new X2JS();
	var blob = new Blob([JSON.stringify(x2js.xml_str2json(stateXML))], {type: "text/plain;charset=utf-8"});
	if (workFileName == "") {
		alert("No file name entered; please try again and enter a file name in the space provided.");
		return;
	}
	if (!workFileName.endsWith(".jsvgl")) {
		workFileName = workFileName + ".jsvgl";
	}
	alert("A file named " + workFileName + " will be saved to your Desktop.\n Your browser may warn you about the file; it is safe.");
	saveAs(blob, workFileName);
}

function exportWorkDialog(stateHTML) {
	var fileName = prompt("Enter the name of the file to save and click OK:", "work file.jsvgl");
	if (fileName != null) {
		exportWork(stateHTML, fileName);
	}
}

function exportWork(stateHTML, exportFileName) {
	var blob = new Blob([stateHTML], {type: "text/html;charset=utf-8"});
	if (exportFileName == "") {
		alert("No file name entered; please try again and enter a file name in the space provided.");
		return;
	}
	if (!exportFileName.endsWith(".html")) {
		exportFileName = exportFileName + ".html";
	}
	alert("A file named " + exportFileName + " will be saved to your Desktop.\n Your browser may warn you about the file; it is safe.");
	saveAs(blob, exportFileName);	
}

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
	var x2js = new X2JS();
	try {
		var xml = x2js.json2xml_str(JSON.parse(contents));
	}
	catch (err) {
		alert("Sorry, that doesn't seem to be a jsVGL file.\nPlease try a different file.");
		return;
	}
	window.setStateXML(xml);
	document.getElementById('loadFileChooserDialog').close();
}