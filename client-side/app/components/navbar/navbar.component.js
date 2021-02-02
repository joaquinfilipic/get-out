define(function (require) {

    "use strict";

    const navbarModule = require("components/navbar/navbar.module");

    // Register `navbar` component, along with its associated controller and template
    navbarModule
     		.component('navbar', {
    			templateUrl:'components/navbar/navbar.template.html',
        			controller: ['$scope', '$http', '$location','$localStorage', '$route', 'toastr', 'userServices', 'communicationServices', '$filter',
        				function navbarController($scope,$http,$location,$localStorage, $route, toastr, userServices, communicationServices, $filter) {

                            $scope.isAuthenticated = ($localStorage.currentUser)? true : false;
                            $scope.pendingsCount = undefined;

                            var userData = function() {
                                $scope.usernameLabel = $localStorage.currentUser.username;  
                                $scope.nameLabel = $localStorage.currentUser.name;
                                if($scope.nameLabel.length > 20) {
                                    $scope.nameLabel = $scope.nameLabel.substring(0,20) + '...';
                                }
                                if($localStorage.currentUser.avatar) {
                                    $scope.avatarLabel = $localStorage.currentUser.avatar;
                                    $scope.contentTypeLabel = $localStorage.currentUser.contentType;    
                                } else {
                                    $scope.avatarLabel = undefined;
                                    $scope.contentTypeLabel = undefined;
                                }
                                //Pendings count
                                userServices.userPendingsCount()
                                .then(function(response) {
                                    $scope.pendingsCount = response.data.count;
                                }, function() {
                                    toastr.error($filter('translate')('TOAST_PENDINGS_COUNT_ERROR'), $filter('translate')('TOAST_ERROR'));
                                });
                            };


                            if($localStorage.currentUser) { 
                                userData();
                            } else {
                                $scope.usernameLabel = '';
                                $scope.nameLabel = '';  
                            }
                            
                            $scope.login = function(user, pass) {

                                userServices.login(user,pass)
                                .then(
                                    //HTTP Post Success
                                    function (response) {
                                    //successful login if there's a token in the response
                                    if (response.data.token) {
                                        // add jwt token to auth header for all requests made by the $http service
                                        var token = response.data.token;
                                        $http.defaults.headers.common.Authorization = 'Bearer ' + token;
                                        toastr.success($filter('translate')('TOAST_LOGIN'), $filter('translate')('TOAST_SUCCESS'));

                                        userServices.getProfileInfo()
                                            .then(function(profileResponse) {
                                                if(profileResponse.data) {
                                                    var d = new Date();
                                                    var n = d.getTime();
                                                    $localStorage.currentUser = {   id: profileResponse.data.id, 
                                                                                    name: profileResponse.data.user.name, 
                                                                                    username: profileResponse.data.user.username,
                                                                                    mail: profileResponse.data.user.mail,
                                                                                    bio: profileResponse.data.bio,
                                                                                    gender: profileResponse.data.gender,
                                                                                    avatar: profileResponse.data.avatar,
                                                                                    contentType: profileResponse.data.contentType,
                                                                                    token: token,
                                                                                    tokenTime: n};
                                                    
                                                    /*if(document.getElementById("rememberCheckbox").checked) {    
                                                    }*/
                                                    $scope.isAuthenticated = true;
                                                    userData();
                                                    communicationServices.loginEvent();
                                                }
                                            }, function(){
                                                
                                            });
                                    } else {
                                            //failed login
                                    }

                                    //HTTP Post Error
                                }, function () {
                                    toastr.warning($filter('translate')('TOAST_WRONG_CRED'), $filter('translate')('TOAST_WARNING'));
                                }
                                );
                                

                            };

                            $scope.register = function(nameR, emailR, usernameR, passwordR) {

                                $http.defaults.headers.post["Content-Type"] = "application/json";
                                userServices.register(nameR, emailR, usernameR, passwordR)
                                .then(
                                    //HTTP Post Success
                                    function (response) {
                                        if (response.data.token) {
                                            // add jwt token to auth header for all requests made by the $http service
                                            var token = response.data.token;
                                            $http.defaults.headers.common.Authorization = 'Bearer ' + token;
                                            toastr.success($filter('translate')('TOAST_LOGIN'), $filter('translate')('TOAST_SUCCESS'));
                                            var d = new Date();
                                            var n = d.getTime();
                                            $localStorage.currentUser = {   id: response.data.id, 
                                                                            name: nameR, 
                                                                            username: usernameR, 
                                                                            mail: emailR,
                                                                            token: token,
                                                                            tokenTime: n };

                                            $scope.isAuthenticated = true;
                                            userData();
                                            communicationServices.loginEvent();
                                        
                                        } else {
                                            //failed login
                                        }

                                    //HTTP Post Error
                                    }, function () {
                                        toastr.error($filter('translate')('TOAST_REGISTER_ERROR'), $filter('translate')('TOAST_ERROR'));
                                    }
                                );

                            };

                            $scope.logout = function() {
                                // remove user from local storage and clear http auth header
                                //delete $localStorage.currentUser;
                                $localStorage.$reset();
                                $http.defaults.headers.common.Authorization = ''; 
                                $scope.isAuthenticated = false;
                                $location.path('/');
                                toastr.warning($filter('translate')('TOAST_LOGOUT'), $filter('translate')('TOAST_WARNING'));
                            };

                            $scope.$on('deleteAccount', function() {
                                $scope.isAuthenticated = false;
                            });

                            $scope.$on('notAuthenticated', function() {
                                $scope.isAuthenticated = false;
                            });                            

                            $scope.$on('pendingsCount', function() {
                                userServices.userPendingsCount()
                                .then(function(response) {
                                    $scope.pendingsCount = response.data.count;
                                }, function() {
                                    toastr.error($filter('translate')('TOAST_PENDINGS_COUNT_ERROR'), $filter('translate')('TOAST_ERROR'));
                                });
                            });                            

                            $scope.$on('updateProfile', function() {
                                $scope.usernameLabel = $localStorage.currentUser.username;  
                                $scope.nameLabel = $localStorage.currentUser.name;
                                if($scope.nameLabel.length > 20) {
                                    $scope.nameLabel = $scope.nameLabel.substring(0,20) + '...';
                                }
                            });

                            $scope.$on('updateAvatar', function() {
                                $scope.avatarLabel = $localStorage.currentUser.avatar;
                                $scope.contentTypeLabel = $localStorage.currentUser.contentType;
                            });

        				}
        			]
    		});

});
