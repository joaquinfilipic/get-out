define(function (require) {

	"use strict";

	const angular = require("lib/angular");
	require("lib/ngStorage");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");
	require("services/userServices.service");
	require("services/communicationServices.service");
	require("directives/username.directive");
	require("directives/name.directive");
	require("directives/password.directive");
	require("directives/email.directive");

	// Define the `navbar` module
	return angular.
		module('navbar', [
			'ngStorage',
			'ngAnimate',
			'toastr',
			'userServices',
			'communicationServices',
			'usernameDirective',
			'nameDirective',
			'passwordDirective',
			'emailDirective'
		]);
});
