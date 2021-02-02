define(function (require) {

	"use strict";

	const angular = require("lib/angular");
	require("services/userServices.service");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");

	// Define the `pending` module
	return angular.module('pending', [
		'userServices',
		'toastr'
	]);
});
