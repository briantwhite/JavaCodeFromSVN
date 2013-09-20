//The following three functions have to be global, they are used by jsInput
function get_grade_jsVGL() {
  if (window.getStateXML().indexOf("ERROR:") != -1) return '{"Grade": {"PracticeMode":"true"}}';
  var x2js = new X2JS();
  return JSON.stringify(x2js.xml_str2json(window.getGradeXML()));
}

function get_state_jsVGL() {
  if (window.getStateXML().indexOf("ERROR:") != -1) return '{"Grade": {"PracticeMode":"true"}}';
  var x2js = new X2JS();
  return JSON.stringify(x2js.xml_str2json(window.getStateXML()));
}

function set_state_jsVGL(state) {
  var x2js = new X2JS();
    stateJSON = JSON.parse(state),
    stateXML = x2js.json2xml_str(stateJSON);
  window.setStateXML(stateXML);
}