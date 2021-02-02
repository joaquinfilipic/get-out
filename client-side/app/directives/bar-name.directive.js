define(function (require) {

    "use strict";

    const barNameModule = require("directives/bar-name.module");

    barNameModule.directive('barNameValidate', [function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    var patt1 = new RegExp('^[a-zA-Z0-9_ ]*');
                    scope.pattern = (viewValue && patt1.test(viewValue)) ? 'valid' : undefined;

                    var patt2 = new RegExp('^.{2,20}$');
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
