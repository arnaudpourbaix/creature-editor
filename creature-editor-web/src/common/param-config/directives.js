(function() {
	'use strict';

	var module = angular.module('param-config.directives', []);
	
	module.directive("checkFolder", function () {
		return {
			link: function (scope, element, attributes) {
				element.bind("change", function (changeEvent) {
					scope.$apply(function () {
						scope.fileread = changeEvent.target.files[0];
					});
				});
			}
		};
	});

})();
