(function () {
    var timeout = 1000;

     genexIsReady = function() {
    	console.log("hi");
    	genexSetDefaultDNASequence("GACT");
    	genexSetProblemNumber(0);
    	genexSetDNASequence("GACT");

    	//Now load mouse and keyboard handlers
        genexSetClickEvent();
        genexSetKeyEvent();
    };
    
}).call(this);