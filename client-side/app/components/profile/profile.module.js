define(function (require) {

	"use strict";

	const angular = require("lib/angular");

	require("lib/angular-route");
	require("components/navbar/navbar.component");
	require("components/footer/footer.component");
	require("components/user/user.component");
	require("components/account/account.component");
	require("components/pending/pending.component");

	// Define the `profile` module
	return angular.
		module('profile', [
			'ngRoute',
			'navbar',
			'footer',
			'user',
			'account',
			'pending'		
		]);
});
