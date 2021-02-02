define(function (require) {

	"use strict";

	require("lib/ngStorage");

	const catalogueModule = require("components/catalogue/catalogue.module");

	// Register `catalogue` component, along with its associated controller and template
	catalogueModule
	  .component('catalogue', {
	    templateUrl:'components/catalogue/catalogue.template.html',
	    controller: ['$scope', '$localStorage',
	    	function catalogueController($scope, $localStorage) {
	     		
	     		$scope.isAuthenticated = ($localStorage.currentUser)? true : false;

	     		$scope.$on('loginEvent', function() {
					$scope.isAuthenticated = true;	     			
                });

	    	}
	    ]
	  });
});
