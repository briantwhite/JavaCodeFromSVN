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
     	genexSetDefaultDNASequence("GGTATAACATGTAACCGGGGGT");
    	genexSetProblemNumber(0);
    	genexSetDNASequence("GGTATAACATGTAACCGGGGGT");

    	//Now load mouse and keyboard handlers
        genexSetClickEvent();
        genexSetKeyEvent();
    };
    
}).call(this);