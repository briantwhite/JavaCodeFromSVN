(function () {
    var timeout = 1000;

    waitForGenex();

    function waitForGenex() {
        if (typeof(jsgenex) !== "undefined" && jsgenex) {
            jsgenex.onInjectionDone("jsgenex");
        }
        else {
            setTimeout(function() { waitForGenex(); }, timeout);
        }
    }

    genexIsReady = function() {
    	// setup params - usually this function is defined in JsGenex.html for custom problems
     	setupGenex();

    	//Now load mouse and keyboard handlers
        genexSetClickEvent();
        genexSetKeyEvent();
    };
    
}).call(this);