define(function (require) {

    "use strict";

    const pubInfoServicesModule = require("services/pubInfoServices.module");

    // Register `pubInfoServices` service
    pubInfoServicesModule
        .service('pubInfoServices', [ '$http',
            function($http) {

                var basepath = "api/v1.0";

                this.pubInfo = function(id) {
                    var getPubInfoUrl =  basepath + '/pub/information/pub/' + id;
                    return $http.get(getPubInfoUrl);
                };

                this.createPubInfo = function(pubId, address, price) {
                    var createPubInfoUrl = basepath + "/pub/information";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(createPubInfoUrl, { pubId: pubId, address: address, price: price});
                };

                this.updatePubInfo = function(id, pubId, address, price) {
                    var updatePubInfoUrl = basepath + "/pub/information/" + id;
                    $http.defaults.headers.put["Content-Type"] = "application/json";
                    return $http.put(updatePubInfoUrl, { pubId: pubId, address: address, price: price});
                };  

                //Coordinates     

                this.setCoordinates = function(pubId, lat, longit) {
                    var setCoordinatesUrl = basepath + "/coordinates";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(setCoordinatesUrl, { pubId: pubId, latitude: lat, longitude: longit });
                };

                this.updateCoordinates = function(coordinatesId, pubId, lat, longit) {
                    var updateCoordinatesUrl = basepath + "/coordinates/" + coordinatesId;
                    $http.defaults.headers.put["Content-Type"] = "application/json";
                    return $http.put(updateCoordinatesUrl, { pubId: pubId, latitude: lat, longitude: longit });
                };         

                this.getCoordinates = function(pubId) {
                    var getCoordinatesUrl =  basepath + '/coordinates/pub/' + pubId;
                    return $http.get(getCoordinatesUrl);
                };

                //Food

                this.getFood = function(id) {
                    var getFoodUrl =  basepath + '/food/pub/' + id;
                    return $http.get(getFoodUrl);
                };

                this.createFood = function(pubId, food, price) {
                    var createFoodUrl = basepath + "/food";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(createFoodUrl, { pubId: pubId, food: food, price: price});
                };

                this.deleteFood = function(id) {
                    var deleteFoodUrl = basepath + "/food/" + id;
                    return $http.delete(deleteFoodUrl);
                };

                //Drink

                this.getDrink = function(id) {
                    var getDrinkUrl =  basepath + '/drinks/pub/' + id;
                    return $http.get(getDrinkUrl);
                };

                this.createDrink = function(pubId, drink, price) {
                    var createDrinkUrl = basepath + "/drinks";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(createDrinkUrl, { pubId: pubId, drink: drink, price: price});
                };

                this.deleteDrink = function(id) {
                    var deleteDrinkUrl = basepath + "/drinks/" + id;
                    return $http.delete(deleteDrinkUrl);
                };

                //Promos

                this.getPromos = function(id) {
                    var getPromosUrl =  basepath + '/promos/pub/' + id;
                    return $http.get(getPromosUrl);
                };

                this.createPromo = function(pubId, name, description) {
                    var createPromoUrl = basepath + "/promos";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(createPromoUrl, { pubId: pubId, name: name, description: description});
                };

                this.deletePromo = function(id) {
                    var deletePromoUrl = basepath + "/promos/" + id;
                    return $http.delete(deletePromoUrl);
                };

            }
        ]);

});
