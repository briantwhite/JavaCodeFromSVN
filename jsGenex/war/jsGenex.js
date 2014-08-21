//The following three functions have to be global, they are used by jsInput
function get_grade_jsGenex() {
	return JSON.stringify(document.getElementById("grade_container").childNodes[0].value);
}

function get_state_jsGenex() {
	var state = {"defaultDNA": defaultDNASequence, "currentDNA": genexGetDNASequence(), "problemNumber": problemNumber};
	return JSON.stringify(state);
}

function set_state_jsGenex(stateJSON) {
	state = JSON.parse(stateJSON);
	genexSetDefaultDNASequence(state["defaultDNA"]);
	genexSetProblemNumber(state["problemNumber"]);
	genexSetDNASequence(state["currentDNA"]);
}