var edXMolCalc = (function() {

    var width = 400, height = 400;
    var container;
    var channel;

    // Establish a channel only if this application is embedded in an iframe.
    // This will let the parent window communicate with this application using
    // RPC and bypass SOP restrictions.
    if (window.parent !== window) {
        channel = Channel.build({
            window: window.parent,
            origin: "*",
            scope: "JSInput"
        });

        channel.bind("getGrade", getGrade);
        channel.bind("getState", getState);
        channel.bind("setState", setState);
    }

    function init() {
        container = document.getElementById('container');
        
       }

 
    init();

    function getGrade() {
        // The following return value may or may not be used to grade
        // server-side.
        // If getState and setState are used, then the Python grader also gets
        // access to the return value of getState and can choose it instead to
        // grade.
//        return JSON.stringify(state['selectedObjects']);
    }

    function getState() {
 //       return JSON.stringify(state);
    }

    // This function will be called with 1 argument when JSChannel is not used,
    // 2 otherwise. In the latter case, the first argument is a transaction
    // object that will not be used here
    // (see http://mozilla.github.io/jschannel/docs/)
    function setState() {
 //       stateStr = arguments.length === 1 ? arguments[0] : arguments[1];
 //       state = JSON.parse(stateStr);
 //       updateMaterials();
    }

    return {
        getState: getState,
        setState: setState,
        getGrade: getGrade
    };
}());