define(function (require) {

    "use strict";

    const matchServicesModule = require("services/matchServices.module");

    // Register `matchServices` service
    matchServicesModule
        .service('matchServices', [ '$http',
            function($http) {

                var basepath = "api/v1.0";

                this.createLike = function(senderId, receiverId, pubId, date) {
                    var createLikeUrl = basepath + "/like";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(createLikeUrl, { userId: receiverId, pubId: pubId, date: date });
                };

                this.createReject = function(senderId, receiverId, pubId, date) {
                    var createRejectUrl = basepath + "/reject";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(createRejectUrl, { userId: receiverId, pubId: pubId, date: date });
                };

                this.getPotentialMatches = function(userId, pubId, date) {
                    var getPotentialMatchesUrl = basepath + "/potential/matches/pub/" + pubId + "/date/" + date;
                    return $http.get(getPotentialMatchesUrl);
                };

                this.getPendings = function(userId, pubId, date) {
                    var getPendingsUrl = basepath + "/pendings/pub/" + pubId + "/date/" + date;
                    return $http.get(getPendingsUrl);
                };

                this.getMatches = function(userId, pubId, date) {
                    var getMatchesUrl = basepath + "/matches/pub/" + pubId + "/date/" + date;
                    return $http.get(getMatchesUrl);
                };


            }
        ]);

});
