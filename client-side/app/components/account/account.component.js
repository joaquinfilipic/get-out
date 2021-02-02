define(function (require) {

	"use strict";

	const accountModule = require("components/account/account.module");

	// Register `account` component, along with its associated controller and template
	accountModule.
	  component('account', {
	    templateUrl:'components/account/account.template.html',
	    controller: ['$scope', '$http', '$location','$localStorage', 'userServices', 'toastr', 'communicationServices', '$filter',
            function accountController($scope,$http,$location,$localStorage, userServices, toastr, communicationServices, $filter) {

                $scope.updatePassword = function(password) {

                    userServices.updatePassword($localStorage.currentUser.id, password)
                    .then(
                        //HTTP Post Success
                        function () {
                        toastr.success($filter('translate')('TOAST_PASS_UPDATED'), $filter('translate')('TOAST_SUCCESS'));
                        //HTTP Post Error
                    }, function () {
                        toastr.error($filter('translate')('TOAST_PASS_UPDATE_ERROR'), $filter('translate')('TOAST_ERROR'));
                    }
                    );
                };

                $scope.deleteAccount = function() {
                    userServices.deleteAccount($localStorage.currentUser.id)
                    .then(
                        //HTTP Delete Success
                        function () {
                            // remove user from local storage and clear http auth header
                            $localStorage.$reset();
                            $http.defaults.headers.common.Authorization = ''; 
                            $scope.isAuthenticated = false;
                            toastr.warning($filter('translate')('TOAST_DELETE_ACC'), $filter('translate')('TOAST_WARNING'));
                            communicationServices.deleteAccount();
                            $location.$$search = {};
                            $location.path('/');
                    }, function() {
                        toastr.error($filter('translate')('TOAST_DELETE_ACC_ERROR'), $filter('translate')('TOAST_ERROR'));
                    });
                            
                };

	        }]
	  });

});
