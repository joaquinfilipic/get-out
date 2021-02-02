define(function (require) {

    "use strict";

    const barAddressModule = require("directives/bar-address.module");

    barAddressModule.directive('barAddressValidate', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    scope.addressLength = (viewValue.length <= 32 ? 'valid' : undefined);

                    if(scope.addressLength) {
                        ctrl.$setValidity('address', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('address', false);                    
                        return undefined;
                    }

                });
            }
        };
    });

});