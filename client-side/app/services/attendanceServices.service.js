define(function (require) {

    "use strict";

    const attendanceServicesModule = require("services/attendanceServices.module");

    // Register `attendanceServices` service
    attendanceServicesModule
        .service('attendanceServices', [ '$http',
            function($http) {

                var basepath = "api/v1.0";

                this.attend = function(userId,pubId, date) {
                    var attendanceUrl =  basepath + '/attendance';
                    $http.defaults.headers.post["Content-Type"] = "application/json";
                    return $http.post(attendanceUrl, { pubId: pubId, date: date });
                };

                this.deleteAttendance = function(userId,attendanceId) {
                    var deleteUrl = basepath + '/attendance/' + attendanceId;
                    return $http.delete(deleteUrl);
                };

                this.validateAttendance = function(pubId, userId, date) {
                    var validateAttendanceUrl = basepath + "/attendance/validate/pub/" + pubId + "/user/" + userId + "/date/" + date;
                    return $http.get(validateAttendanceUrl);
                };

            }
        ]);

});
