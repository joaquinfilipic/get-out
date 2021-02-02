define(function (require) {

    "use strict";

    const passwordModule = require("directives/password.module");

    passwordModule.directive('passwordValidate', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    scope.pwdValidLength = (viewValue && viewValue.length >= 10 ? 'valid' : undefined);

                    if(scope.pwdValidLength) {
                        ctrl.$setValidity('pwd', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('pwd', false);                    
                        return undefined;
                    }

                });
            }
        };
    });

    passwordModule.directive('compareTo', function() {
        return {
            require: 'ngModel',
            scope: {
                confirmPassword: "=compareTo"
            },
            link: function(scope, elm, attrs, ctrl) {

                ctrl.$validators.compareTo = function(val) {
                    
                    scope.confirmPwd = (val === scope.confirmPassword ? 'valid' : undefined);
                    return val === scope.confirmPassword;
                };

                scope.$watch("confirmPassword", function() {
                    ctrl.$validate();
                });

            }
        };
    });


});