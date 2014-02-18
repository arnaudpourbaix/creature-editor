(function() {
	'use strict';

	var module = angular.module('creatureEditor.main.controllers', [ 'pascalprecht.translate' ]);

	module.controller('MainController', [ '$scope', '$translate', '$rootScope', function MainController($scope, $translate, $rootScope) {
		
		$scope.$onRootScope('$translateChangeSuccess', function() {
			$scope.langKey = $translate.use();
		});
		
		$scope.changeLanguage = function(langKey) {
			$translate.use(langKey);
		};
		
	} ]);

})();
