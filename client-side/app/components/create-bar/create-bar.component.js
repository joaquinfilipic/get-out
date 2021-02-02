define(function (require) {

	"use strict";

	const createBarModule = require("components/create-bar/create-bar.module");

	// Register `createBar` component, along with its associated controller and template
	createBarModule.
 		component('createBar', {
			templateUrl:'components/create-bar/create-bar.template.html', 
    			controller: ['$scope', '$location', '$localStorage', 'pubServices', 'pubInfoServices', 'toastr', '$filter',
    				function createBarController($scope, $location, $localStorage, pubServices, pubInfoServices, toastr, $filter) {

    					$scope.createBar = function(nameB, descriptionB, timeB, imageB, typeB) {
                            if(typeB !== 'image/jpeg' && typeB !== 'image/jpg' && typeB !== 'image/png' && typeB !== 'image/bmp' && typeB !== 'image/gif') {
                                toastr.error($filter('translate')('TOAST_AV_UPD_TYPE_ERROR'), $filter('translate')('TOAST_ERROR'));
                            } else {
                                pubServices.createPub($localStorage.currentUser.id, nameB, descriptionB, timeB, imageB, typeB)    
                                .then( function (response) {
                                    //successful creation
                                    var latitude = -34.60368;
                                    var longitude = -58.38156;
                                    pubInfoServices.setCoordinates(response.data.id, latitude, longitude)
                                    .then(function() {
                                      //OK
                                    }, function(){
                                        //Error
                                        toastr.error($filter('translate')('TOAST_SET_DEF_COORD_ERROR'), $filter('translate')('TOAST_ERROR'));
                                    });

                                    $location.path('/catalogue');
                                    toastr.success($filter('translate')('TOAST_CREATE_PUB'), $filter('translate')('TOAST_SUCCESS'));
                                }, function() {
                                    toastr.error($filter('translate')('TOAST_CREATE_PUB_ERROR'), $filter('translate')('TOAST_ERROR'));
                                });
                            }
                            
    					};

                        var uploadField = document.getElementById("inputFile");

                        uploadField.onchange = function() {
                            if(this.files[0].size > 1048576){
                               toastr.error($filter('translate')('TOAST_IMAGE_SIZE_ERROR'), $filter('translate')('TOAST_ERROR'));
                               this.value = "";
                            }
                        };

    				}
    			]
		});

});