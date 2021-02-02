define(function (require) {

	"use strict";

	const userModule = require("components/user/user.module");

	// Register `user` component, along with its associated controller and template
	userModule.
	  component('user', {
	    templateUrl:'components/user/user.template.html',
	    controller: ['$scope', '$http', '$location', '$localStorage', '$route', 'userServices', 'toastr', '$filter', 'communicationServices',
	    	function userController($scope,$http,$location,$localStorage,$route,userServices, toastr, $filter, communicationServices) {
	      
	    	$scope.selectedGender = "Otro";
            $scope.genders = [ $filter('translate')('USER_MASC_LABEL'), $filter('translate')('USER_FEM_LABEL'), $filter('translate')('USER_OTHER_LABEL')];

            if($localStorage.currentUser) {
                $scope.usernameU = $localStorage.currentUser.username;  
                $scope.nameU = $localStorage.currentUser.name;
                if($localStorage.currentUser.avatar) {
                    $scope.avatarLabel = $localStorage.currentUser.avatar;
                    $scope.contentTypeLabel = $localStorage.currentUser.contentType;    
                } else {
                    $scope.avatarLabel = undefined;
                    $scope.contentTypeLabel = undefined;
                }
                $scope.descriptionU = ($localStorage.currentUser.bio) ? $localStorage.currentUser.bio : '';
                $scope.emailU = ($localStorage.currentUser.mail) ? $localStorage.currentUser.mail : ''; 
                if($localStorage.currentUser.gender !== undefined && $localStorage.currentUser.gender === 'MALE') {
                    $scope.selectedGender = $filter('translate')('USER_MASC_LABEL');
                } else if($localStorage.currentUser.gender !== undefined && $localStorage.currentUser.gender === 'FEMALE') {
                    $scope.selectedGender = $filter('translate')('USER_FEM_LABEL');    
                }
            } else {
                $scope.usernameU = '';
                $scope.nameU = '';   
            }

            $scope.updateProfile = function(nameU, usernameU, selectedGender, descriptionU, emailU) {
                var updateGender;

                switch(selectedGender) {
                    case $filter('translate')('USER_MASC_LABEL'):   
                        updateGender = 'MALE';
                        break;
                    case $filter('translate')('USER_FEM_LABEL'):   
                        updateGender = 'FEMALE';
                        break;
                    case $filter('translate')('USER_OTHER_LABEL'):   
                        updateGender = 'OTHER';
                }

                if(updateGender !== undefined) {
                    userServices.updateProfile($localStorage.currentUser.id, nameU, usernameU, updateGender, descriptionU, emailU)
                    .then(
                        //HTTP Post Success
                        function (response) {
                            //successful update 
                            if(response.data.token) {
                                $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                                $localStorage.currentUser.token = response.data.token;    
                            }
                            $localStorage.currentUser.name = nameU;
                            $localStorage.currentUser.username = usernameU;
                            $localStorage.currentUser.gender = updateGender;
                            $localStorage.currentUser.bio = descriptionU;
                            $localStorage.currentUser.mail = emailU;
                            communicationServices.updateProfile();
                            toastr.success($filter('translate')('TOAST_PROFILE_UPD'), $filter('translate')('TOAST_SUCCESS'));

                        //HTTP Post Error
                        }, function () {
                            toastr.error($filter('translate')('TOAST_PROFILE_UPD_ERROR'), $filter('translate')('TOAST_ERROR'));
                        }
                    );
                } else {
                    toastr.error($filter('translate')('TOAST_GENDER_ERROR'), $filter('translate')('TOAST_ERROR'));
                }
                
            };

            $scope.updateAvatar = function(file, type) {
                if(type !== 'image/jpeg' && type !== 'image/jpg' && type !== 'image/png' && type !== 'image/bmp' && type !== 'image/gif') {
                    toastr.error($filter('translate')('TOAST_AV_UPD_TYPE_ERROR'), $filter('translate')('TOAST_ERROR'));
                } else {
                    userServices.updateAvatar($localStorage.currentUser.id, file, type)    
                    .then(
                        //HTTP Post Success
                        function () {
                            //successful creation
                            $localStorage.currentUser.avatar = file;
                            $localStorage.currentUser.contentType = type;
                            $scope.avatarLabel = $localStorage.currentUser.avatar;
                            $scope.contentTypeLabel = $localStorage.currentUser.contentType;
                            communicationServices.updateAvatar();
                            toastr.success($filter('translate')('TOAST_AV_UPD'), $filter('translate')('TOAST_SUCCESS'));
                        }, function() {
                            toastr.error($filter('translate')('TOAST_AV_UPD_ERROR'), $filter('translate')('TOAST_ERROR'));
                        }
                    );
                }
            };

            var uploadField = document.getElementById("inputFile");

            uploadField.onchange = function() {
                if(this.files[0].size > 1048576){
                   toastr.error($filter('translate')('TOAST_IMAGE_SIZE_ERROR'), $filter('translate')('TOAST_ERROR'));
                   this.value = "";
                }
            };

	    }]
	  });

});
