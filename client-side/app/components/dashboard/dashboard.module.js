define(function (require) {

	"use strict";

	const angular = require("lib/angular");
	require("lib/ngStorage");
	require("services/userServices.service");
	require("services/communicationServices.service");
	require("services/attendanceServices.service");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");

	// Define the `dashboard` module
	return angular.
		module('dashboard', [
			'ngStorage',
			'userServices',
			'attendanceServices',
			'toastr',
			'communicationServices'
		]);
});
