define(function (require) {

    "use strict";

    const userServicesModule = require("services/userServices.module");

    // Register `userServices` service
    userServicesModule
        .service('userServices', [ '$http',
            function($http) {

                // Relativo al context-path:
                const basepath = "api/v1.0";

                this.login = function(user,pass){
                    var loginUrl = basepath + "/login";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(loginUrl, { username: user, password: pass });
                };

                this.userInfo = function() {
                    var userUrl = basepath + "/user";
                    return $http.get(userUrl);
                };

                this.register = function(nameR, emailR, usernameR, passwordR) {
                    var registerUrl = basepath + "/users";
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(registerUrl, { username: usernameR, name: nameR, mail: emailR, password: passwordR, repeatPassword: passwordR });

                };

                this.getProfileInfo = function() {
                    var userProfileUrl = basepath + "/profile";
                    return $http.get(userProfileUrl);
                };

                this.updateProfile = function(id, nameU, usernameU, selectedGender, descriptionU, emailU) {
                    var updateProfileUrl = basepath + "/profile/";
                    $http.defaults.headers.put["Content-Type"] = "application/json";
                    return $http.put(updateProfileUrl, { name: nameU, username: usernameU, gender: selectedGender, bio: descriptionU, mail: emailU });
                };

                this.updateAvatar = function(id, file, type) {
                    var updateAvatarUrl = basepath + "/avatar";
                    $http.defaults.headers.put["Content-Type"] = "application/json";
                    return $http.put(updateAvatarUrl, { image: file, content: type });
                };

                this.updatePassword = function(id, password) {
                    var updatePasswordUrl = basepath + "/password";
                    $http.defaults.headers.put["Content-Type"] = "application/json";
                    return $http.put(updatePasswordUrl, { password: password });
                };

                this.deleteAccount = function() {
                    var deleteUrl = basepath + '/user';
                    return $http.delete(deleteUrl);
                };

                this.userAttendance = function() {
                    var getAttendancesUrl =  basepath + '/attendance';
                    return $http.get(getAttendancesUrl);
                };

                this.userPendings = function() {
                    var userPendingsUrl =  basepath + '/pendings';
                    return $http.get(userPendingsUrl);
                };

                this.userPendingsCount = function() {
                    var userPendingsCountUrl =  basepath + '/pendings/count';
                    return $http.get(userPendingsCountUrl);
                };

            }
        ]);

});
