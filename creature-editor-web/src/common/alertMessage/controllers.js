(function() {
	'use strict';

	var module = angular.module('alertMessage.controllers', [ 'alertMessage.services', 'ui.bootstrap', 'ngAnimate' ]);

	module.controller('AlertMessageController', function AlertMessageController($scope, $timeout, alertMessageService) {
		$scope.messages = alertMessageService.messages;

		$scope.$watchCollection('messages', function() {
			if (alertMessageService.empty() || $scope.message) {
				return;
			}
			$scope.message = alertMessageService.pop();
			$timeout($scope.clear, 4000);
		});

		$scope.clear = function() {
			$scope.message = null;
			$timeout(alertMessageService.remove, 500);
		};

	});

})();
