(function() {
	'use strict';

	var module = angular.module('creatureEditor.main.controllers', [ 'pascalprecht.translate' ]);

	module.controller('MainController', [ '$scope', '$translate', '$rootScope', function MainController($scope, $translate, $rootScope) {
		
		$scope.$on('$translateChangeSuccess', function() {
			$scope.langKey = $translate.uses();
		});
		
		$scope.changeLanguage = function(langKey) {
			$translate.uses(langKey);
		};
		
	} ]);

})();
