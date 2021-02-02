define(function (require) {

    "use strict";

    const descriptionModule = require("directives/description.module");

    descriptionModule.directive('descriptionValidate', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    scope.descriptionLength = (viewValue.length <= 256 ? 'valid' : undefined);

                    if(scope.descriptionLength) {
                        ctrl.$setValidity('descU', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('descU', false);                    
                        return undefined;
                    }

                });
            }
        };
    });

});