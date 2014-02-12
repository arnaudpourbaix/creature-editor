(function(window, $) {
	'use strict';

	var module = angular.module('effeckt', []);

	module.factory('effecktService', function() {
		var effecktService = {};

		return effecktService;

	});
	
	module.directive('effecktButton', [ 'effecktService', '$parse', function(effecktService, $parse) {
		return {
			restrict : 'A',
			//transclude : true,
			//replace : true,
			//template : '<div data-ng-transclude><span class="label">Expand Right</span> <span class="spinner"></span></div>',
			link : function(scope, element, attributes) {
				element.addClass('effeckt-button');
				element.attr('data-effeckt-type', attributes.effecktButton);
			}
		};
	} ]);

	module.directive('uiButton', [ 'effecktService', '$parse', function(effecktService, $parse) {
		return {
			restrict : 'E',
			scope : true,
			transclude : true,
			replace : true,
			template : '<button ng-transclude><span class="label">Expand Right</span> <span class="spinner"></span></button>',
			link : function(scope, element, attributes) {
				scope.type = attributes.effecktButton;
				element.addClass('btn effeckt-button');
				element.attr('data-effeckt-type', attributes.effecktButton);
			}
		};
	} ]);
	
}(window));
