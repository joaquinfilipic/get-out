
	/**
	* Configuración runtime para RequireJS.
	*/

(function (require) {

	"use strict";

	// https://hash.online-convert.com/sha256-generator
	const sri = {
			/*
			"lib/angular" : "sha256-fLrCdLpHxkcLn9X9QN4J21jxzh2JF7aqVgn0P49myhc=",
			"lib/angular-resource" : "sha256-KVJQeqkZ1oUiw4V3/bEUDWQVgPecNlMDdz1ovhxm3Ko=",
			"lib/angular-route" : "sha256-B+4jOLVKTr/pWpkp+6jNpj98eppg9sEubLxHW9kTOGQ=",
			"lib/bootstrap" : "sha256-63ld7aiYP6UxBifJWEzz87ldJyVnETUABZAYs5Qcsmc=",
			"lib/domReady" : "sha256-fDwbDWUsnjte9PjQ0WvJG63FjAWbUfTdy06oJS4/+34=",
			"lib/jquery" : "sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=",
			"lib/lodash" : "sha256-7/yoZS3548fXSRXqc/xYzjsmuW3sFKzuvOCHd06Pmps=",
			"lib/require" : "sha256-Hv/khlPWrFV11wU1SxNalBP/k+mcw1cOWxpfADVjjxs=",
			"lib/toastr" : "sha256-yNbKY1y6h2rbVcQtf0b8lq4a+xpktyFc3pSYoGAY1qQ="
			*/
		};

	require.config({
		baseUrl : ".",
		nodeIdCompat : true,
		waitSeconds : 0,
		onNodeCreated : (node, config, module) => {
			if (sri[module] !== undefined) {
				node.setAttribute("integrity", sri[module]);
				node.setAttribute("crossorigin", "anonymous");
			}
		},
		paths : {
			"ext/maps.google.com" : "https://maps.google.com/maps/api/js?key=AIzaSyAst8ZMvRVbM614bWNCRi267HqDqxnTEL8"
		},
		shim : {
			"lib/angular" : {
				exports : "angular"
			},
			"lib/angular-route" : {
				deps : ["lib/angular"]
			},
			"lib/jquery" : {
				exports : "$"
			},
			"lib/popper" : {
				exports : "Popper"
			},
			"lib/bootstrap" : {
				deps : [
					"lib/jquery",
					"lib/popper"
				]
			},
			"lib/ngStorage" : {
				deps : ["lib/angular"]
			},
			"lib/angular-toastr.tpls" :{
				deps : ["lib/angular"]
			},
			"lib/angular-animate" : {
				deps : ["lib/angular"]
			},
			"lib/bootstrap-datepicker" : {
				deps : ["lib/bootstrap"]
			},
			"lib/angular-base64-upload" : {
				deps : ["lib/angular"]
			},
			"lib/ng-map" : {
				deps : [
					"lib/angular",
					"ext/maps.google.com"
				]
			},
			"lib/angular-translate" : {
				deps : ["lib/angular"]
			},
			"lib/angular-translate-loader-partial" : {
				deps : [
					"lib/angular",
					"lib/angular-translate"
				]
			}
		}
	});
}) (require);
