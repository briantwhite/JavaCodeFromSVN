// functions to control which menu items are active
//  depending on whether or not there is a problem in the window
//  NOTE: requires specific menu items to be present
    
function updateMenuStatus(state) {
	if (state) {
		// there is work in progress
		document.getElementById("saveWorkButton").disabled = false;
		document.getElementById("exportWorkButton").disabled = false;
		document.getElementById("loadWorkButton").disabled = true;
	} else {
		// no work in progress
		document.getElementById("saveWorkButton").disabled = true;
		document.getElementById("exportWorkButton").disabled = true;
		document.getElementById("loadWorkButton").disabled = false;
	}
}

