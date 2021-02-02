define(function (require) {

    "use strict";

    const nameModule = require("directives/name.module");

    nameModule.directive('nameValidate', [function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    scope.nameLength = (viewValue.length <= 64 ? 'valid' : undefined);

                    if(scope.nameLength) {
                        ctrl.$setValidity('name', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('name', false);                    
                        return undefined;
                    }

                });
            }
        };
    }
    ]);

});