define(function (require) {

	"use strict";

	const indexModule = require("components/index/index.module");

	// Register `index` component, along with its associated controller and template
	indexModule
	  .component('index', {
	    templateUrl:'components/index/index.template.html',
	    controller: [ 
	    	function indexController() {
	      		
	    	}
	    ]
	  });
});
