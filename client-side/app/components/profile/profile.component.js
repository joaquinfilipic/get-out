define(function (require) {

	"use strict";

	const profileModule = require("components/profile/profile.module");

	// Register `profile` component, along with its associated controller and template
	profileModule.
	 		component('profile', {
				templateUrl:'components/profile/profile.template.html',
	    			controller: ['$scope', '$routeParams',
	    				function profileController($scope, $routeParams) {
	    					
	    					if($routeParams.component) {
	    						$scope.profileComponent = $routeParams.component;	
	    					}
	    					else {
	    						$scope.profileComponent = 'user';	
	    					}

	  						$scope.showUser = function(){
	  							$scope.profileComponent = 'user';  
							};

							$scope.showAccount = function(){
								$scope.profileComponent = 'account'; 
							};

							$scope.showPending = function(){
								$scope.profileComponent = 'pending';
							};
	    				}
	    			]
			});
});
