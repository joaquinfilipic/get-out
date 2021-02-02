define(function (require) {

	"use strict";

	const angular = require("lib/angular");

	require("lib/angular-route");
	require("lib/ngStorage");
	require("components/navbar/navbar.component");
	require("components/footer/footer.component");
	require("components/bars-list/bars-list.component");
	require("components/dashboard/dashboard.component");

	// Define the `catalogue` module
	return angular.
		module('catalogue', [
	  		'ngRoute',
	  		'ngStorage',
	  		'navbar',
	  		'footer',
	  		'barsList',
	  		'dashboard'
		]);
});
