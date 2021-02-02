define(function (require) {

	"use strict";

	const dashboardModule = require("components/dashboard/dashboard.module");

	// Register `dashboard` component, along with its associated controller and template
	dashboardModule.
	  component('dashboard', {
	    templateUrl:'components/dashboard/dashboard.template.html',
	    controller: ['$scope', '$localStorage', '$location', '$route', 'userServices', 'attendanceServices', 'toastr', 'communicationServices', '$filter',
	    	function dashboardController($scope, $localStorage, $location, $route, userServices, attendanceServices, toastr, communicationServices, $filter) {

	    		$scope.attendances = [];

                var loadAttendances = function() {
                	userServices.userAttendance($localStorage.currentUser.id)
					.then(function(response) {
					    if(response.data) {
					    	$scope.attendances = response.data;
					    }
					}, function () {
						//Error
						toastr.error($filter('translate')('TOAST_ATT_LIST_ERROR'), $filter('translate')('TOAST_ERROR'));
	                });
                };

                if($localStorage.currentUser) { 
	                $scope.usernameLabel = $localStorage.currentUser.username;  
	                $scope.nameLabel = $localStorage.currentUser.name;
	                $scope.descriptionLabel = ($localStorage.currentUser.bio) ? $localStorage.currentUser.bio : '';
	                if($localStorage.currentUser.avatar) {
	                    $scope.avatarLabel = $localStorage.currentUser.avatar;
	                    $scope.contentTypeLabel = $localStorage.currentUser.contentType;    
	                } else {
	                    $scope.avatarLabel = undefined;
	                    $scope.contentTypeLabel = undefined;
	                }
	                loadAttendances();
	            } else {
	                $scope.usernameLabel = '';
	                $scope.nameLabel = '';  
	            }

                $scope.$on('attendanceEvent', function() {
		            loadAttendances();            
		        });

                $scope.dropOut = function(attendanceId, pubId, date) {
                	attendanceServices.deleteAttendance($localStorage.currentUser.id,attendanceId)
                	.then(function() {
                		loadAttendances();
                		communicationServices.dropEvent(pubId, date);

                	}, function () {
						//Error	
						toastr.error($filter('translate')('TOAST_DROP_ATT_ERROR'), $filter('translate')('TOAST_ERROR'));
                    });
                };

                $scope.hasBarsToAttend = function() {
	    			return $scope.attendances.length !== 0;
	    		};

	    	}]
	  	});
});
