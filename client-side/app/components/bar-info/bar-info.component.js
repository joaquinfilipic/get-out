define(function (require) {

  "use strict";

  const barInfoModule = require("components/bar-info/bar-info.module");

  // Register `barInfo` component, along with its associated controller and template
  barInfoModule.
    component('barInfo', {
      templateUrl:'components/bar-info/bar-info.template.html',
      controller: ['$http', '$route', '$routeParams', '$scope', '$localStorage', '$location', 'pubInfoServices', 'pubServices', 'toastr', '$filter',
      	function barInfoController($http, $route, $routeParams, $scope, $localStorage, $location, pubInfoServices, pubServices, toastr, $filter) {

          $scope.pubOwner = false;
          $scope.pub = {};
          $scope.pubInfo = {};
          $scope.foods = [];
          $scope.drinks = [];
          $scope.promos = [];

          $scope.coordinatesId = undefined;
          $scope.latitude = -34.60368;
          $scope.longitude = -58.38156;
          $scope.mapZoom = 14;

          var obtainFood = function() {
            pubInfoServices.getFood($routeParams.barId)
              .then(function(response) {
                $scope.foods = response.data;
              }, function() {
                //Error
                toastr.error($filter('translate')('TOAST_GET_FOOD_ERROR'), $filter('translate')('TOAST_ERROR'));
              });
          };

          var obtainDrink = function() {
            pubInfoServices.getDrink($routeParams.barId)
              .then(function(response) {
                $scope.drinks = response.data;
              }, function() {
                //Error
                toastr.error($filter('translate')('TOAST_GET_DRINK_ERROR'), $filter('translate')('TOAST_ERROR'));
              });
          };

          var obtainPromos = function() {
            pubInfoServices.getPromos($routeParams.barId)
              .then(function(response) {
                $scope.promos = response.data;
              }, function() {
                //Error
                toastr.error($filter('translate')('TOAST_GET_PROMO_ERROR'), $filter('translate')('TOAST_ERROR'));
              });
          };

          if($localStorage.currentUser) {
            pubServices.validatePubOwner($routeParams.barId, $localStorage.currentUser.id)
              .then(function(response) {
                $scope.pubOwner = response.data.response;
              }, function() {
                
              });
          }

          $scope.getpos = function(event){
             $scope.latitude = Math.round((event.latLng.lat()) * 100000) / 100000;
             $scope.longitude = Math.round((event.latLng.lng()) * 100000) / 100000;
          };

          pubServices.pubById($routeParams.barId)
            .then(function(response) {
                if(response.data) {
                  $scope.pub = response.data;
                }
              }, function () {
              //Error 
              toastr.error($filter('translate')('TOAST_GET_PUB_INFO_ERROR'), $filter('translate')('TOAST_ERROR'));
            });

          pubInfoServices.pubInfo($routeParams.barId)
            .then(function(response) {
                if(response.data) {
                  $scope.pubInfo = response.data;
                }
              }, function () {
              //Error 
              //toastr.error('Error al obtener la información del pub. Por favor, intente más tarde', 'Error');
            });

          pubInfoServices.getCoordinates($routeParams.barId)
            .then(function(response) {
                if(response.data) {
                  $scope.coordinatesId = response.data.id;
                  $scope.latitude = response.data.latitude;
                  $scope.longitude = response.data.longitude;
                }
              }, function () {
              //Error 
              pubInfoServices.setCoordinates($routeParams.barId, $scope.latitude, $scope.longitude)
                .then(function() {
                  //OK
                  pubInfoServices.getCoordinates($routeParams.barId)
                    .then(function(response2) {
                      //OK
                      $scope.coordinatesId = response2.data.id;
                    }, function() {
                      //Error
                      toastr.error($filter('translate')('TOAST_GET_COORD_ERROR'), $filter('translate')('TOAST_ERROR'));
                    });
                }, function() {
                  //Error
                  toastr.error($filter('translate')('TOAST_SET_DEF_COORD_ERROR'), $filter('translate')('TOAST_ERROR'));
                });
            });

            obtainFood();
            obtainDrink();
            obtainPromos();

          //------------------

          $scope.$on('loginEvent', function() {
            pubServices.validatePubOwner($routeParams.barId, $localStorage.currentUser.id)
              .then(function(response) {
                $scope.pubOwner = response.data.response;
              }, function() {
                
              });            
          });

          $scope.editPubImage = function(img, type) {
            pubServices.updatePubImage($routeParams.barId, img, type, $localStorage.currentUser.id)    
              .then(
                  //HTTP Post Success
                  function () {
                      //successful creation
                      $scope.pub.image = img;
                      $scope.pub.content = type;
                      
                      toastr.success($filter('translate')('TOAST_IMAGE_UPD'), $filter('translate')('TOAST_SUCCESS'));
                  }, function() {
                    toastr.error($filter('translate')('TOAST_IMAGE_UPD_ERROR'), $filter('translate')('TOAST_ERROR'));
                  }
              );
          };

          $scope.editPubPrimaryInfo = function(name, description, openTime) {
            pubServices.updatePub($routeParams.barId, name, description, openTime, $localStorage.currentUser.id)
              .then(function() {
                //OK
                $scope.pub.name = name;
                $scope.pub.description = description;
                $scope.pub.openTime = openTime;

                toastr.success($filter('translate')('TOAST_EV_INFO_UPD'), $filter('translate')('TOAST_SUCCESS'));
                }, function () {
                //Error 
                toastr.error($filter('translate')('TOAST_EV_INFO_UPD_ERROR'), $filter('translate')('TOAST_ERROR'));
              });
          };

          $scope.editPubSecondaryInfo = function(address, price) {
            pubInfoServices.pubInfo($routeParams.barId)
            .then(function(response) {
                if(response.data) { 
                  pubInfoServices.updatePubInfo(response.data.id,$routeParams.barId, address, price, $localStorage.currentUser.id)
                    .then(function() {
                      //OK
                      $scope.pubInfo.address = address;
                      $scope.pubInfo.price = price;

                      toastr.success($filter('translate')('TOAST_PUB_INFO_UPD'), $filter('translate')('TOAST_SUCCESS'));
                      }, function () {
                      //Error 
                      toastr.error($filter('translate')('TOAST_PUB_INFO_UPD_ERROR'), $filter('translate')('TOAST_ERROR'));
                    });
                }
              }, function () {
                pubInfoServices.createPubInfo($routeParams.barId, address, price, $localStorage.currentUser.id)
                    .then(function() {
                      //OK
                      $scope.pubInfo.address = address;
                      $scope.pubInfo.price = price;

                      toastr.success($filter('translate')('TOAST_PUB_INFO_UPD'), $filter('translate')('TOAST_SUCCESS'));
                      }, function () {
                      //Error 
                      toastr.error($filter('translate')('TOAST_PUB_INFO_UPD_ERROR'), $filter('translate')('TOAST_ERROR'));
                    });
            });
          };

          $scope.updateCoordinates = function() {
            pubInfoServices.updateCoordinates($scope.coordinatesId,$routeParams.barId,$scope.latitude, $scope.longitude)
              .then(function() {
                //OK
                toastr.success($filter('translate')('TOAST_COORD_UPD'), $filter('translate')('TOAST_SUCCESS'));
              }, function() {
                toastr.error($filter('translate')('TOAST_COORD_UPD_ERROR'), $filter('translate')('TOAST_ERROR'));
              });
          };

          $scope.createFood = function(name, price) {
            $scope.foodName = undefined;
            $scope.foodPrice = undefined;
            pubInfoServices.createFood($routeParams.barId, name, price, $localStorage.currentUser.id)
            .then( function(response) {
              $scope.foods[$scope.foods.length] = {"id": response.data.id,"food": name,"price": price,"pubId": $routeParams.barId};
            }, function(){
              //Error 
              toastr.error($filter('translate')('TOAST_ADD_FOOD_ERROR'), $filter('translate')('TOAST_ERROR'));
            });
            $("#foodModal").modal("hide");
          };

          $scope.createDrink = function(name, price) {
            $scope.drinkName = undefined;
            $scope.drinkPrice = undefined;
            pubInfoServices.createDrink($routeParams.barId, name, price, $localStorage.currentUser.id)
            .then( function(response) {
              $scope.drinks[$scope.drinks.length] = {"id": response.data.id,"drink": name,"price": price,"pubId": $routeParams.barId};
            }, function(){
              //Error 
              toastr.error($filter('translate')('TOAST_ADD_DRINK_ERROR'), $filter('translate')('TOAST_ERROR'));
            });
            $("#drinkModal").modal("hide");
          };

          $scope.createPromo = function(name, description) {
            $scope.promoName = undefined;
            $scope.promoDescription = undefined;
            pubInfoServices.createPromo($routeParams.barId, name, description, $localStorage.currentUser.id)
            .then( function(response) {
              $scope.promos[$scope.promos.length] = {"id": response.data.id,"name": name,"description": description,"pubId": $routeParams.barId};
            }, function(){
              //Error 
              toastr.error($filter('translate')('TOAST_ADD_PROMO_ERROR'), $filter('translate')('TOAST_ERROR'));
            });
            $("#promoModal").modal("hide");
          };

          $scope.deletePub = function() {
            pubServices.deletePub($routeParams.barId,$localStorage.currentUser.id)
            .then(function() {
              //OK
              $location.path('/catalogue');
              toastr.warning($filter('translate')('TOAST_DELETE_PUB'), $filter('translate')('TOAST_WARNING'));
            }, function() {
              toastr.error($filter('translate')('TOAST_DELETE_PUB_ERROR'), $filter('translate')('TOAST_ERROR'));
            });
          };

          $scope.deleteFood = function(id) {
            pubInfoServices.deleteFood(id)
            .then(function() {
              obtainFood();
            }, function() {
              toastr.error($filter('translate')('TOAST_DELETE_FOOD_ERROR'), $filter('translate')('TOAST_ERROR'));
            });
          };

          $scope.deleteDrink = function(id) {
            pubInfoServices.deleteDrink(id)
            .then(function() {
              obtainDrink();
            }, function() {
              toastr.error($filter('translate')('TOAST_DELETE_DRINK_ERROR'), $filter('translate')('TOAST_ERROR'));
            });
          };

          $scope.deletePromo = function(id) {
            pubInfoServices.deletePromo(id)
            .then(function() {
              obtainPromos();
            }, function() {
              toastr.error($filter('translate')('TOAST_DELETE_PROMO_ERROR'), $filter('translate')('TOAST_ERROR'));
            });
          };


      	}
      ]
    });
});
