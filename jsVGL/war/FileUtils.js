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
//	saveAs(blob, "test.txt");
}