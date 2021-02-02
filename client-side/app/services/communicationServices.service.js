define(function (require) {

    "use strict";

    const communicationServicesModule = require("services/communicationServices.module");

    // Register `communicationServices` service
    communicationServicesModule
        .service('communicationServices', [ '$rootScope',
            function($rootScope) {

                this.loginEvent = function() {
                    $rootScope.$broadcast('loginEvent');                    
                };

                this.attendanceEvent = function() {
                    $rootScope.$broadcast('attendanceEvent');                    
                };

                this.dropEvent = function(pubId, date) {
                    $rootScope.$broadcast('dropEvent', { pubId:pubId, date:date });                    
                };

                this.dropEventFromBarsList = function() {

                };

                this.deleteAccount = function() {
                    $rootScope.$broadcast('deleteAccount');
                };

                this.pendingsCount = function() {
                    $rootScope.$broadcast('pendingsCount');
                };

                this.updateProfile = function() {
                    $rootScope.$broadcast('updateProfile');
                };

                this.updateAvatar = function() {
                    $rootScope.$broadcast('updateAvatar');
                };

            }
        ]);

});
