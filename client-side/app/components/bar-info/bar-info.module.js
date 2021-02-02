define(function (require) {

	"use strict";

	const angular = require("lib/angular");
	require("services/pubServices.service");
	require("services/pubInfoServices.service");
	require("directives/bar-name.directive");
	require("directives/bar-description.directive");
	require("directives/bar-time.directive");
	require("directives/bar-address.directive");
	require("directives/bar-price.directive");

	require("lib/angular-route");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");
	require("components/navbar/navbar.component");
	require("components/footer/footer.component");

	// Provee la dependencia ngMap:
	require("lib/ng-map");

	// Define the `barInfo` module
	return angular.
		module('barInfo', [
			'ngRoute',
	  		'navbar',
	  		'footer',
	  		'pubInfoServices',
	  		'pubServices',
	  		'barNameDirective',
			'barDescriptionDirective',
			'barTimeDirective',
			'barAddressDirective',
			'barPriceDirective',
			'ngMap',
			'toastr'
		]);
});
