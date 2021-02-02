define(function (require) {

  "use strict";

  const barsListModule = require("components/bars-list/bars-list.module");

  require("lib/ngStorage");


  // Register `barsList` component, along with its associated controller and template
  barsListModule.
    component('barsList', {
      templateUrl:'components/bars-list/bars-list.template.html',
      controller: ['$http', '$scope', '$localStorage', '$route', '$interval', 
        'pubServices', 'attendanceServices', 'userServices', 'messageServices', 'toastr', 'communicationServices', '$filter', '$location',

        function barsListController($http,$scope,$localStorage,$route,$interval,
          pubServices,attendanceServices,userServices, messageServices, toastr, communicationServices, $filter, $location) {

          $scope.isAuthenticated = ($localStorage.currentUser)? true : false;
          $scope.selectedPubId = undefined;
          $scope.bars = [];
          $scope.loadingPubs = false;
          
          var lastMessageId = 0;
          var timer;
          $scope.attendancesByPubAndDate = {};

          var retrievePubs = function() {
            $scope.loadingPubs = true;
            pubServices.pubsByDate($scope.selectedDate)
              .then(function(response) {
                  if(response.data) {
                    $scope.bars = response.data;
                    if($scope.isAuthenticated) {
                      for(var i = 0; i < $scope.bars.length; i++) {
                        validateAttendance($scope.bars[i].id);
                      }
                    }   
                  }
                  $scope.loadingPubs = false;
              }, function () {
                //Error 
                toastr.error($filter('translate')('TOAST_GET_PUBS_ERROR'), $filter('translate')('TOAST_ERROR'));
              });
          };

          var validateAttendance = function(pubId) {
            attendanceServices.validateAttendance(pubId, $localStorage.currentUser.id, $scope.selectedDate)
              .then(function(response) {
                  $scope.attendancesByPubAndDate["p" + pubId + "d" + $scope.selectedDate] = response.data.response;
                }, function() {
              });
          };

          $(document).ready(function() {
            $('#datepickerInput').datepicker({
              format: 'yyyy-mm-dd'
            }).on('changeDate', function(date) {
                $scope.selectedDate = date.format();
                retrievePubs();
            });
            $("#datepickerInput").datepicker('option', 'minDate', new Date());
            
            //On load, actual date
            $("#datepickerInput").datepicker("setDate", new Date());
            $scope.selectedDate = $("#datepickerInput").val();
            
            //Show on load
            retrievePubs();
          });


          var intervalFunction = function() {
            //console.log("intervalFunction called");
            if($scope.selectedPubId !== undefined){
              messageServices.getGlobalMessagesWithId($scope.selectedPubId, $scope.selectedDate,lastMessageId)
                .then( function(response){
                  //OK
                  if(response.data.length > 0) {
                    $scope.messages = $scope.messages.concat(response.data);
                    lastMessageId = response.data[response.data.length - 1].id;
                    checkTextareaHeight();
                  }
                });
            }
          };

          var checkTextareaHeight = function() {
             var textarea = document.getElementById("textarea");
             if(textarea.scrollHeight - textarea.scrollTop < 50) {
                textarea.scrollTop = textarea.scrollHeight;
             }
          };

          $scope.$on('loginEvent', function() {
            $scope.isAuthenticated = true;    
            for(var i = 0; i < $scope.bars.length; i++) {
              validateAttendance($scope.bars[i].id);
            }       
          });

          $scope.sendGlobalMessage = function(msg){
            $interval.cancel(timer);
            timer = undefined;
            if(msg !== undefined && msg !== '') {
              if(msg.length < 1024) {
                messageServices.sendGlobalMessage($localStorage.currentUser.id,$scope.selectedPubId,msg,$scope.selectedDate)
                .then(function() {
                  //OK
                  $scope.globalMessage = undefined;
                  var textarea = document.getElementById("textarea");
                  textarea.scrollTop = textarea.scrollHeight;
                  timer = $interval(function() {
                      intervalFunction();
                    },500);
                }, function() {
                  //Error
                    toastr.error($filter('translate')('TOAST_SEND_MSG_ERROR'), $filter('translate')('TOAST_ERROR'));
                });
              } else {
                  toastr.error($filter('translate')('TOAST_SEND_MSG_LENGTH_ERROR'), $filter('translate')('TOAST_ERROR'));
              }


            }
          };

          $scope.openChatModal = function(pubId) {
            $("#chatModal").modal("show");
            $scope.selectedPubId = pubId;
            messageServices.getGlobalMessages(pubId, $scope.selectedDate)
              .then( function(response){
                $scope.messages = response.data;
                if(response.data.length > 0) {
                  lastMessageId = response.data[response.data.length - 1].id;
                }
                if(timer === undefined) {
                  timer = $interval(function() {
                    intervalFunction();
                  },500);
                }
                var textarea = document.getElementById("textarea");
                textarea.scrollTop = textarea.scrollHeight;
              }, function(){
                //Error
                  toastr.error($filter('translate')('TOAST_GET_MSG_ERROR'), $filter('translate')('TOAST_ERROR'));
              });
          };

          $("#chatModal").on("hide.bs.modal", function () {
            //console.log("close modal");
            $scope.selectedPubId = undefined;
            $scope.globalMessage = undefined;
            $interval.cancel(timer);
            timer = undefined;
          });

          $scope.$on('$routeChangeStart', function() {
              $interval.cancel(timer);
              timer = undefined;
          });

          $scope.$on('dropEvent', function(event, arg) {
            if(arg.date === $scope.selectedDate) {
              $scope.attendancesByPubAndDate["p" + arg.pubId + "d" + $scope.selectedDate] = false;
              for(var i = 0; i < $scope.bars.length; i++) {
                if($scope.bars[i].id === arg.pubId) {
                  $scope.bars[i].attendance--;
                  break;
                }
              }
            }
          });

          /*$scope.unattend = function(pubId) {
            communicationServices.dropEventFromBarsList();
            $scope.attendancesByPubAndDate["p" + pubId + "d" + $scope.selectedDate] = false;
              for(var i = 0; i < $scope.bars.length; i++) {
                if($scope.bars[i].id === pubId) {
                  $scope.bars[i].attendance--;
                  break;
                }
              }
          };*/

          $scope.attend =function(pubId) {
            if($scope.isAuthenticated) {
              attendanceServices.attend($localStorage.currentUser.id,pubId,$scope.selectedDate)
              .then(
                //HTTP Post Success
                function () {
                  $scope.attendancesByPubAndDate["p" + pubId + "d" + $scope.selectedDate] = true;
                  for(var i = 0; i < $scope.bars.length; i++) {
                    if($scope.bars[i].id === pubId) {
                      $scope.bars[i].attendance++;
                      break;
                    }
                  }
                  communicationServices.attendanceEvent();
                }, function() {
                  //Error
                  toastr.error($filter('translate')('TOAST_ATTEND_ERROR'), $filter('translate')('TOAST_ERROR'));
                }
              );
            } else {
              $location.path('/');
              toastr.warning($filter('translate')('TOAST_UNAUTH_PATH'), $filter('translate')('TOAST_WARNING'));
            }
            
          };

          $scope.availableBars = function() {
            return $scope.bars.length !== 0;
          };      

        }]
    });
});
