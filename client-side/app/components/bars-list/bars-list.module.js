define(function (require) {

	"use strict";

	const angular = require("lib/angular");

	require("lib/angular-route");
	require("components/bar-info/bar-info.component");
	require("lib/ngStorage");
	require("services/pubServices.service");
	require("services/attendanceServices.service");
	require("services/userServices.service");
	require("services/messageServices.service");
	require("services/communicationServices.service");
	require("lib/angular-toastr.tpls");
	require("lib/angular-animate");
	
	// Define the `barsList` module
	return angular.
		module('barsList', [
			'barInfo',
			'ngStorage',
			'pubServices',
			'attendanceServices',
			'userServices',
			'messageServices',
			'toastr',
			'communicationServices'
		]);
});
