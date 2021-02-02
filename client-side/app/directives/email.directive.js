define(function (require) {

    "use strict";

    const emailModule = require("directives/email.module");

    emailModule.directive('emailValidate', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    var patt1 = new RegExp('[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}');
                    scope.emailPattern = (viewValue && patt1.test(viewValue)) ? 'valid' : undefined;

                    if(scope.emailPattern) {
                        ctrl.$setValidity('email', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('email', false);                    
                        return undefined;
                    }

                });
            }
        };
    });

});