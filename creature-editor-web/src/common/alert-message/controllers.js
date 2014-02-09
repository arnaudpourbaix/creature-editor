(function() {
	'use strict';

	var module = angular.module('alertMessage.controllers', [ 'alertMessage.services', 'ui.bootstrap', 'ngAnimate' ]);

	module.controller('AlertMessageController', [ '$scope', '$timeout', 'alertMessageService', function AlertMessageController($scope, $timeout, alertMessageService) {
		$scope.show = false;
		$scope.messages = alertMessageService.messages;

		$scope.$watchCollection('messages', function() {
			if (alertMessageService.empty() || $scope.show) {
				return;
			}
			$scope.message = alertMessageService.pop();
			$scope.show = true;
			$timeout($scope.clear, 4000);
		});

		$scope.clear = function() {
			$scope.show = false;
			$timeout(alertMessageService.remove, 100);
		};

	} ]);

})();
