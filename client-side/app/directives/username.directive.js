define(function (require) {

    "use strict";

    const usernameModule = require("directives/username.module");

    usernameModule.directive('usernameValidate', [function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    var patt1 = new RegExp('[a-zA-Z]([-.]?[a-zA-Z0-9]+)*');
                    scope.pattern = (viewValue && patt1.test(viewValue)) ? 'valid' : undefined;

                    var patt2 = new RegExp('^.{6,32}$');
                    scope.length = (viewValue && patt2.test(viewValue)) ? 'valid' : undefined;

                    if(scope.pattern && scope.length) {
                        ctrl.$setValidity('usr', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('usr', false);                    
                        return undefined;
                    }

                });
            }
        };
    }
    ]);

});