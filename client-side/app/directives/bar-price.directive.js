define(function (require) {

    "use strict";

    const barPriceModule = require("directives/bar-price.module");

    barPriceModule.directive('barPriceValidate', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    var pricePatt1 = new RegExp('^[0-9]*$');
                    scope.pricePattern = (pricePatt1.test(viewValue)) ? 'valid' : undefined;

                    scope.priceLength = (viewValue.length <= 4 ? 'valid' : undefined);

                    if(scope.pricePattern && scope.priceLength) {
                        ctrl.$setValidity('price', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('price', false);                    
                        return undefined;
                    }

                });
            }
        };
    });

});
