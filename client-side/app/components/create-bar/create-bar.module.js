define(function (require) {

	"use strict";

	const angular = require("lib/angular");

	require("lib/angular-route");
	require("lib/angular-base64-upload");
	require("components/navbar/navbar.component");
	require("components/footer/footer.component");
	require("directives/bar-name.directive");
	require("directives/bar-description.directive");
	require("directives/bar-time.directive");
	require("services/pubServices.service");
	require("services/pubInfoServices.service");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");

	// Define the `createBar` module
	return angular.
		module('createBar', [
			'ngRoute',
			'navbar',
			'footer',
			'naif.base64',
			'barNameDirective',
			'barDescriptionDirective',
			'barTimeDirective',
			'pubServices',
			'pubInfoServices',
			'toastr'
		]);
});