define(function (require) {

	"use strict";

	const angular = require("lib/angular");
	require("lib/angular-base64-upload");
	require("directives/username.directive");
	require("directives/name.directive");
	require("directives/description.directive");
	require("directives/email.directive");
	require("services/userServices.service");
	require("services/communicationServices.service");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");

	// Define the `user` module
	return angular.module('user', [
		'naif.base64',
		'usernameDirective',
		'nameDirective',
		'descriptionDirective',
		'emailDirective',
		'userServices',
		'toastr',
		'communicationServices'
	]);
});
