define(function (require) {

    "use strict";

    const barDescriptionModule = require("directives/bar-description.module");

    barDescriptionModule.directive('barDescriptionValidate', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    scope.descriptionLength = (viewValue.length <= 256 ? 'valid' : undefined);

                    if(scope.descriptionLength) {
                        ctrl.$setValidity('descriptionB', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('descriptionB', false);                    
                        return undefined;
                    }

                });
            }
        };
    });

});
