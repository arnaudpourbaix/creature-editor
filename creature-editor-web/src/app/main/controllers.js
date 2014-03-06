(function() {
	'use strict';

	var module = angular.module('creatureEditor.main.controllers', [ 'pascalprecht.translate' ]);

	module.controller('MainController', [ '$scope', '$translate', '$rootScope', '$interval', '$alertMessage', function MainController($scope, $translate, $rootScope, $interval, $alertMessage) {
		
		$scope.$onRootScope('$translateChangeSuccess', function() {
			$scope.langKey = $translate.use();
		});
		
		$scope.changeLanguage = function(langKey) {
			$translate.use(langKey);
		};
		
		$interval(function() {
			$alertMessage.push('SPELL.IMPORT.WARNING_MESSAGE', 'info', {
				name : 'Dummy Dummy Dummy'
			});
		}, 2000);
		
	} ]);

})();
