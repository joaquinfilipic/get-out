define(function (require) {

    "use strict";

    const messageServicesModule = require("services/messageServices.module");

    // Register `messaegServices` service
    messageServicesModule
        .service('messageServices', [ '$http',
            function($http) {

                var basepath = "api/v1.0";

                this.getGlobalMessages = function(pubId, date) {
                    var getGlobalMessagesUrl =  basepath + '/messages/pub/' + pubId + '/date/' + date;
                    return $http.get(getGlobalMessagesUrl);
                };


                this.getGlobalMessagesWithTimestamp = function(pubId, date, timestamp) {
                    var getGlobalMessagesUrl =  basepath + '/messages/pub/' + pubId + '/date/' + date + '?timestamp=' + timestamp;
                    return $http.get(getGlobalMessagesUrl);
                };

                this.getGlobalMessagesWithId = function(pubId, date, msgid) {
                    var getGlobalMessagesUrl =  basepath + '/messages/pub/' + pubId + '/date/' + date + '?fromId=' + msgid;
                    return $http.get(getGlobalMessagesUrl);
                };

                this.sendGlobalMessage = function(userId, pubId, message, date) {
                    var sendGlobalMessageUrl = basepath + "/messages/global";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(sendGlobalMessageUrl, { pubId: pubId, message: message, date: date });
                };

                this.getInternMessages = function(userId) {
                    var getInternMessagesUrl =  basepath + '/messages/user/' + userId;
                    return $http.get(getInternMessagesUrl);
                };

                this.getInternMessagesWithTimestamp = function(userId, timestamp) {
                    var getInternMessagesUrl =  basepath + '/messages/user/' + userId + '?timestamp=' + timestamp;
                    return $http.get(getInternMessagesUrl);
                };

                this.getInternMessagesWithId = function(userId, msgid) {
                    var getInternMessagesUrl =  basepath + '/messages/user/' + userId + '?fromId=' + msgid;
                    return $http.get(getInternMessagesUrl);
                };

                this.sendInternMessage = function(userId, message) {
                    var sendInternMessageUrl = basepath + "/messages/private";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(sendInternMessageUrl, { userId: userId, message: message });
                };

            }
        ]);

});
