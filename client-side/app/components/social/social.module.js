define(function (require) {

	"use strict";

	const angular = require("lib/angular");

	require("lib/angular-route");
	require("components/navbar/navbar.component");
	require("components/footer/footer.component");
	require("services/matchServices.service");
	require("services/messageServices.service");

	// Define the `social` module
	return angular.
		module('social', [
			'ngRoute',
			'navbar',
			'footer',
			'matchServices',
			'messageServices'
		]);
});
