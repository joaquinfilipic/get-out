define(function (require) {

	"use strict";

	const footerModule = require("components/footer/footer.module");

	// Register `footer` component, along with its associated controller and template
	footerModule
	  .component('footer', {
	    templateUrl:'components/footer/footer.template.html',
	    controller: function footerController() {
	      
	    }
	  });
});
