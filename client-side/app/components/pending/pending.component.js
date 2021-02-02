define(function (require) {

	"use strict";

	const pendingModule = require("components/pending/pending.module");

	// Register `pending` component, along with its associated controller and template
	pendingModule.
	  component('pending', {
	    templateUrl:'components/pending/pending.template.html',
	    controller: ['$scope', '$http', '$localStorage', 'userServices', 'toastr', '$filter',
	    	function pendingController($scope, $http, $localStorage, userServices, toastr, $filter) {

	    		$scope.pendings = [];

    			userServices.userPendings()
				.then(function(response) {
			    	$scope.pendings = response.data;
				}, function () {
					//Error
					toastr.error($filter('translate')('TOAST_ATT_LIST_ERROR'), $filter('translate')('TOAST_ERROR'));
                });

	    		$scope.hasBarsToAttend = function() {
	    			return $scope.pendings.length !== 0;
	    		};

	    	}]
	  });
});
