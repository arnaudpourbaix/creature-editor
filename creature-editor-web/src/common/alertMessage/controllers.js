(function() {
	'use strict';

	var module = angular.module('alertMessage.controllers', [ 'alertMessage.services' ]);

	module.controller('AlertMessageController', function AlertMessageController($scope, $timeout, alertMessageService) {
		$scope.messages = alertMessageService.messages;

		$scope.$watchCollection('messages', function() {
			if (alertMessageService.empty() || $scope.message) {
				return;
			}
			$scope.message = alertMessageService.pop();
			console.log('show message', $scope.message);
			$timeout($scope.remove, 5000);
		});

		$scope.remove = function() {
			var msg = $scope.message;
			$scope.message = null;
			alertMessageService.remove(msg);
		};

	});

})();
