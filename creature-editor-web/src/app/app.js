(function() {
	'use strict';

	var module = angular.module('creatureEditor', [ 'templates-app', 'templates-common', 'pascalprecht.translate', 'alertMessage', 'restangular', 'creatureEditor.mod',
			'creatureEditor.spell', 'creatureEditor.category' ]);

	module.config([ '$urlRouterProvider', '$locationProvider', '$translateProvider', 'RestangularProvider',
			function($urlRouterProvider, $locationProvider, $translateProvider, RestangularProvider) {
				RestangularProvider.setBaseUrl('/rest');
				// $locationProvider.html5Mode(true);
				$urlRouterProvider.otherwise('/');
				var translations = {
					'APP_TITLE' : 'Editor',
					'APP_NAME' : 'Creature Editor',
					'LANGUAGE' : {
						'EN' : 'English',
						'FR' : 'French'
					},
					'MENU' : {
						'MODS' : 'Mods',
						'SPELLS' : 'Spells',
						'CATEGORIES' : 'Categories',
						'CONFIGURATION' : 'Configuration',
					},
					'CONTROLS' : {
						'REQUIRED' : 'Required',
						'EXISTS' : 'Already exists'
					},
					'CRUD' : {
						'ADD_BUTTON' : 'Add',
						'SAVE_BUTTON' : 'Save',
						'REMOVE_BUTTON' : 'Remove',
						'REVERT_BUTTON' : 'Revert changes',
						'SAVE_SUCCESS' : "{{entity}} '{{name}}' was saved successfully.",
						'SAVE_ERROR' : "{{entity}} '{{name}}' was not saved, an error occured.",
						'REMOVE_SUCCESS' : "{{entity}} '{{name}}' was removed successfully.",
						'REMOVE_ERROR' : "{{entity}} '{{name}}' was not removed, an error occured."
					},
					'MOD' : {
						'LABEL' : 'Mod',
						'SELECT_MOD' : 'Select a mod',
						'LIST_TITLE' : 'Mods',
						'EDIT_TITLE' : 'Mod details',
						'NAME' : 'Name'
					}
				};
				var translationsFr = {
					'MENU' : {
						'MODS' : 'Mods',
						'SPELLS' : 'Sorts',
						'CATEGORIES' : 'Catégories',
						'CONFIGURATION' : 'Configuration',
					},
					'CONTROLS' : {
						'REQUIRED' : 'Requis',
						'EXISTS' : 'Existe déjà'
					}
				};
				$translateProvider.preferredLanguage('fr');
				$translateProvider.fallbackLanguage('en');
				$translateProvider.translations('en', translations);
				$translateProvider.translations('fr', translationsFr);
			} ]);

	module.constant('appSettings', {
		restBaseUrl : 'rest/'
	});

	module.controller('AppCtrl', [ '$scope', '$translate', function($scope, $translate) {
		$scope.changeLanguage = function(langKey) {
			$translate.uses(langKey);
		};
	} ]);

	module.directive('focusMe', [ '$timeout', function($timeout) {
		return function(scope, element, attrs) {
			scope.$watch(attrs.focusMe, function(value) {
				if (value) {
					$timeout(function() {
						element.focus();
					}, 700);
				}
			});
		};
	} ]);

	module.filter('range', function() {
		return function(input, total) {
			total = parseInt(total, 10);
			for (var i = 0; i < total; i++) {
				input.push(i);
			}
			return input;
		};
	});

	module.run([ '$rootScope', '$state', '$stateParams', function run($rootScope, $state, $stateParams) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	} ]);

})();
