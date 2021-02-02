define(function (require) {

	"use strict";

	const angular = require("lib/angular");

	require("lib/angular-route");
	require("components/navbar/navbar.component");
	require("components/footer/footer.component");



	// Define the `index` module
	return angular
		.module('index', [
	  		'ngRoute',
	  		'navbar',
	  		'footer'
		]);
});
