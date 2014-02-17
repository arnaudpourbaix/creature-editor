(function(window) {
	'use strict';

	var module = angular.module('toolbox.filters', []);

	module.filter('range', function RangeFilter() {
		return function(input, total) {
			total = parseInt(total, 10);
			for (var i = 0; i < total; i++) {
				input.push(i);
			}
			return input;
		};
	});

}(window));
