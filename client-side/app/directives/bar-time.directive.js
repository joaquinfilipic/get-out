define(function (require) {

    "use strict";

    const barTimeModule = require("directives/bar-time.module");

    barTimeModule.directive('barTimeValidate', function() {
            return {
                require: 'ngModel',
                link: function(scope, elm, attrs, ctrl) {
                    ctrl.$parsers.unshift(function(viewValue) {

                        var patt1 = new RegExp('^(0[0-9]|1[0-9]|2[0-3]|[0-9]):[0-5][0-9]$');
                        scope.timePattern = (viewValue && patt1.test(viewValue)) ? 'valid' : undefined;

                        if(scope.timePattern) {
                            ctrl.$setValidity('timeB', true);
                            return viewValue;
                        } else {
                            ctrl.$setValidity('timeB', false);                    
                            return undefined;
                        }

                    });
                }
            };
        });

});