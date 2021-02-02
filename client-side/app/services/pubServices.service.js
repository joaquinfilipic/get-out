define(function (require) {

    "use strict";

    const pubServicesModule = require("services/pubServices.module");

    // Register `pubServices` service
    pubServicesModule
        .service('pubServices', [ '$http',
            function($http) {

                var basepath = "api/v1.0";

                this.pubsByDate = function(date) {
                    var getPubsByDateUrl =  basepath + '/pubs/date/' + date;
                    return $http.get(getPubsByDateUrl);
                };

                this.pubById = function(id) {
                    var getPubsByDateUrl =  basepath + '/pub/' + id;
                    return $http.get(getPubsByDateUrl);
                };                

                this.createPub = function(userId, nameB, descriptionB, timeB, imageB, typeB) {
                    var createPubUrl = basepath + "/pubs";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(createPubUrl, { name: nameB, description: descriptionB, openTime: timeB, image: imageB, content: typeB});
                };

                this.updatePub = function(pubId, name, description, openTime) {
                    var updatePubUrl = basepath + "/pub/" + pubId;
                    $http.defaults.headers.put["Content-Type"] = "application/json";
                    return $http.put(updatePubUrl, { name: name, description: description, openTime: openTime});
                };

                this.updatePubImage = function(pubId, image, content) {
                    var updatePubImageUrl = basepath + "/pub/" + pubId + "/image";
                    $http.defaults.headers.put["Content-Type"] = "application/json";
                    return $http.put(updatePubImageUrl, { image: image, content: content});
                };

                this.deletePub = function(pubId) {
                    var deleteUrl = basepath + "/pub/" + pubId;
                    return $http.delete(deleteUrl);
                };

                this.validatePubOwner = function(pubId, userId) {
                    var validatePubOwnerUrl = basepath + "/pubs/validate/pub/" + pubId + "/user/" + userId;
                    return $http.get(validatePubOwnerUrl);
                };

            }
        ]);

});
