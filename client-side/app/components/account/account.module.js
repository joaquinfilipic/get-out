define(function (require) {

	"use strict";

	const angular = require("lib/angular");
	require("directives/password.directive");
	require("services/userServices.service");
	require("services/communicationServices.service");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");

	// Define the `account` module
	return angular.module('account', [
		'passwordDirective',
		'userServices',
		'toastr',
		'communicationServices'
	]);
});
