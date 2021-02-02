define(function (require) {

	"use strict";

	const socialModule = require("components/social/social.module");

	// Register `social` component, along with its associated controller and template
	socialModule.
	  component('social', {
	    templateUrl:'components/social/social.template.html',
	    controller: ['$scope', '$routeParams', '$http', '$location', '$localStorage', '$interval', 'matchServices', 'messageServices', 'toastr', '$filter',
	    	function socialController($scope,$routeParams,$http,$location,$localStorage,$interval,matchServices, messageServices, toastr, $filter) {

	    		$scope.potentialMatches = [];
	    		$scope.pendings = [];
	    		$scope.matches = [];
	    		$scope.showChatWindow = false;

	    		$scope.potentialMatch = {};
	    		$scope.potentialMatchesLength = 0;
	    		$scope.potentialMatchesIndex = 0;
	    		$scope.pendingUser = {};
	    		$scope.selectedMatch = {};
	    		$scope.internMessage = { msg: undefined };

	    		$scope.loadingPotentialMatches = false;

	    		var lastMessageId = 0;
	        	var timer;

	    		var getPendings = function() {
	    			matchServices.getPendings($localStorage.currentUser.id, $routeParams.barId, $routeParams.date)
		    			.then(function(response) {
		    				$scope.pendings = response.data;
		    			}, function() {
		    				//Error
			                  toastr.error($filter('translate')('TOAST_PEND_LIST_ERROR'), $filter('translate')('TOAST_ERROR'));
		    			});
	    		};

	    		var getMatches = function() {
		    		matchServices.getMatches($localStorage.currentUser.id, $routeParams.barId, $routeParams.date)
		    			.then(function(response) {
		    				$scope.matches = response.data;
		    			}, function() {
		    				//Error
			                  toastr.error($filter('translate')('TOAST_MATCH_LIST_ERROR'), $filter('translate')('TOAST_ERROR'));
		    			});
	    		};

	    		var getPotentialMatches = function() {
	    			$scope.loadingPotentialMatches = true;
	    			matchServices.getPotentialMatches($localStorage.currentUser.id, $routeParams.barId, $routeParams.date)
		    			.then(function(response) {
		    				$scope.loadingPotentialMatches = false;
		    				$scope.potentialMatches = response.data;
		    				$scope.potentialMatchesLength = $scope.potentialMatches.length;
		    				if(!$scope.potentialMatchesIndex || $scope.potentialMatchesIndex >= $scope.potentialMatchesLength) {
		    					$scope.potentialMatchesIndex = 0;
		    				}
		    				$scope.potentialMatch = $scope.potentialMatches[$scope.potentialMatchesIndex];

		    			}, function() {
		    				//Error
			                  toastr.error($filter('translate')('TOAST_POT_MATCH_LIST_ERROR'), $filter('translate')('TOAST_ERROR'));
		    			});
	    		};

	    		var intervalFunction = function() {
		            if($scope.selectedMatch !== undefined){
		              messageServices.getInternMessagesWithId($scope.selectedMatch.id,lastMessageId)
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
		            if(textarea.scrollHeight - (textarea.scrollTop + textarea.clientHeight) < 50) {
		                textarea.scrollTop = textarea.scrollHeight;
		             }
		        };

	    		//On load
	    		getPendings();
	    		getMatches();

	    		//-------------------------------------------------------

	    		$scope.openMatchModal = function() {
	    			getPotentialMatches();
	    		};

	    		$scope.openPendingModal = function(pendingUser) {
	    			$scope.pendingUser = pendingUser;
	    		};

	    		$scope.like = function(receiverId) {
	    			matchServices.createLike($localStorage.currentUser.id, receiverId, Number($routeParams.barId), $routeParams.date)
	    			.then(function() {
						getPotentialMatches();
						getMatches();
						getPendings();
	    			}, function() {
	    				//Error
			              toastr.error($filter('translate')('TOAST_LIKE_ERROR'), $filter('translate')('TOAST_ERROR'));
	    			});
	    		};

	    		$scope.reject = function(receiverId) {
	    			matchServices.createReject($localStorage.currentUser.id, receiverId, Number($routeParams.barId), $routeParams.date)
	    			.then(function() {
						getPotentialMatches();
	    			}, function() {
	    				//Error
		                  toastr.error($filter('translate')('TOAST_REJECT_ERROR'), $filter('translate')('TOAST_ERROR'));
	    			});
	    		};

	    		$scope.likePending = function() {
	    			matchServices.createLike($localStorage.currentUser.id, $scope.pendingUser.id, Number($routeParams.barId), $routeParams.date)
	    			.then(function() {
						$scope.pendingUser = {};
						getPendings();
						getMatches();
						$("#pendingModal").modal("hide");
	    			}, function() {
	    				//Error
	    				toastr.error($filter('translate')('TOAST_LIKE_ERROR'), $filter('translate')('TOAST_ERROR'));
	    			});
	    		};

	    		$scope.rejectPending = function() {
	    			matchServices.createReject($localStorage.currentUser.id, $scope.pendingUser.id, Number($routeParams.barId), $routeParams.date)
	    			.then(function() {
						$scope.pendingUser = {};
						getPendings();
						$("#pendingModal").modal("hide");
	    			}, function() {
	    				//Error
	    				toastr.error($filter('translate')('TOAST_REJECT_ERROR'), $filter('translate')('TOAST_ERROR'));
	    			});
	    		};

	    		$scope.selectMatchToChat = function(selectedMatch) {
	    			$scope.showChatWindow = true;
	    			$scope.selectedMatch = selectedMatch;
		            messageServices.getInternMessages($scope.selectedMatch.id)
		              .then( function(response){
		                $scope.messages = response.data;
		                if(response.data.length > 0) {
		                  lastMessageId = response.data[response.data.length - 1].id;
		                }
		                $interval.cancel(timer);
		                timer = $interval(function() {
		                	intervalFunction();
		                },500);
		                var textarea = document.getElementById("textarea");
		                textarea.scrollTop = textarea.scrollHeight;
		              }, function() {
		              	//Error
		              	toastr.error($filter('translate')('TOAST_GET_MSG_ERROR'), $filter('translate')('TOAST_ERROR'));
		              });
	    		};

	    		$scope.sendInternMessage = function(msg){
	    			$interval.cancel(timer);
		            timer = undefined;
	    			if(msg !== undefined && msg !== '') {
	    				if(msg.length < 1024) {
							messageServices.sendInternMessage($scope.selectedMatch.id,msg)
				              .then(function() {
				                //OK
				                $scope.internMessage.msg = undefined;
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

		        $scope.$on('$routeChangeStart', function() {
			        $interval.cancel(timer);
		            timer = undefined;
			    });

	    		$scope.hasPotentialMatches = function() {
	    			return $scope.potentialMatches.length !== 0;
	    		};

	    		$scope.hasPendings = function() {
	    			return $scope.pendings.length !== 0;
	    		};

	    		$scope.hasMatches = function() {
	    			return $scope.matches.length !== 0;
	    		};


	    	}
		]
	  });
});
