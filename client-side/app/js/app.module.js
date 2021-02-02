
	/**
	* Módulo principal de la aplicación.
	*/

define(function (require) {

	"use strict";

	/*
	* Así se trae todo. Sino, se puede pedir uno por uno (ver 'shim' en 'require-runtime.js').
	* Puede ir en cualquier lado obviamente, no necesariamente en este lugar.
	*/
	require("lib/bootstrap-datepicker");

	const SPA = "ar.getout-spa";
	const Â = require("lib/angular");

	require("lib/angular-route");
	require("lib/ngStorage");
	require("lib/angular-translate");

	// Se incluyen los 'component', y éstos a su vez incluyen sus 'module'.
	require("components/index/index.component");
	require("components/catalogue/catalogue.component");
	require("components/bar-info/bar-info.component");
	require("components/social/social.component");
	require("components/profile/profile.component");
	require("components/create-bar/create-bar.component");

	Â.module(SPA, [
			"ngRoute",
			"index",
			"catalogue",
			"barInfo",
			"social",
			"profile",
			"createBar",
			"ngStorage",
			"pascalprecht.translate"
		])
	.config([
		"$locationProvider", "$routeProvider", "toastrConfig", "$translateProvider",
		function($locationProvider, $routeProvider, toastrConfig, $translateProvider) {
			$locationProvider.hashPrefix("!");
			$routeProvider
				.when("/", {
					template : "<index></index>"
				})
				.when("/catalogue", {
					template : "<catalogue></catalogue>"
				})
				.when("/info/:barId", {
					template : "<bar-info></bar-info>"
				})
				.when("/social/:barId/:date", {
					template : "<social></social>"
				})
				.when("/profile", {
					template : "<profile></profile>"
				})
				.when("/create-bar", {
					template : "<create-bar></create-bar>"
				})
				.otherwise({
					redirectTo : "/"
				});

			angular.extend(toastrConfig, {
			    positionClass: 'toast-bottom-right',
			    closeButton: false,
			    timeOut: 3000,
			    progressBar: true
			});


			// Add languages here
			var userLang; 
			var listOfSupportedLanguages = ['en','es'];
			var translations;

			// to avoid being called in non browser environments
			if (typeof navigator === 'object') {
				userLang = navigator.language || navigator.userLanguage;
				userLang = userLang.split('-')[0];
			}

			// Set English as default language
			if (userLang === undefined || listOfSupportedLanguages.indexOf(userLang) < 0) {
				userLang = 'en';
			}

			switch(userLang) {
				case 'en':
					translations = require('i18n/translations.en');
					break;
				case 'es':
					translations = require('i18n/translations.es');
					break;
				default:
					translations = require('i18n/translations.es');
			}
			
			$translateProvider.useSanitizeValueStrategy('escape');
			$translateProvider.translations('preferredLanguage', translations);
            $translateProvider.preferredLanguage('preferredLanguage');

		}
	])
	.run(["$rootScope", "$http", "$location", "$localStorage", "toastr", "$filter", 
		function($rootScope,$http, $location, $localStorage, toastr, $filter) {
        // keep user logged in after page refresh
        if ($localStorage.currentUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
        }

        // redirect to login page if not logged in and trying to access a restricted page
        $rootScope.$on('$locationChangeStart', function () {
            //var restrictedPages = ['/social/:barId/:date', '/profile', '/create-bar'];
            //var restrictedPage = restrictedPages.indexOf($location.path()) >= 0;
            var restrictedPage = $location.path().includes("social") || $location.path().includes("profile") || $location.path().includes("create-bar");

            if (restrictedPage && !$localStorage.currentUser) {
                $location.path('/');
                toastr.warning($filter('translate')('TOAST_UNAUTH_PATH'), $filter('translate')('TOAST_WARNING'));
            }

            //Check JWT expiration
            var d = new Date();
            var n = d.getTime();
            if($localStorage.currentUser && (n - $localStorage.currentUser.tokenTime > 3000000)) {
            	// remove user from local storage and clear http auth header
                delete $localStorage.currentUser;
                $http.defaults.headers.common.Authorization = ''; 
                $location.path('/');
                $rootScope.$broadcast('notAuthenticated');
                toastr.warning($filter('translate')('TOAST_EXPIRED'), $filter('translate')('TOAST_WARNING'));
            }
            if($localStorage.currentUser) {
            	$rootScope.$broadcast('pendingsCount');
            }

        });
    }]);

	Â.element(() => Â.bootstrap(document.getElementById("ng-app"), [SPA], [/* Config */]));
});
