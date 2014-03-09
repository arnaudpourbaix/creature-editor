(function() {
	'use strict';

	var module = angular.module('jqwidgets.controllers', []);

	module.controller('JqContextualMenuController', [ '$scope', 'items', function JqContextualMenuController($scope, items) {
		$scope.items = items;
	} ]);

})();
