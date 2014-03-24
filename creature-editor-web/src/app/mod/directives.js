(function() {
	'use strict';

	var module = angular.module('creatureEditor.mod.directives', [ 'creatureEditor.mod.services' ]);

	/**
	 * ensure that mod name is unique
	 */
	module.directive('uniqueModName', [ 'Mod', function UniqueModNameDirective(Mod) {
		return {
			require : 'ngModel',
			restrict : 'A',
			link : function(scope, element, attrs, ctrl) {
				// using push() here to run it as the last parser, after we are sure that other validators were run
				ctrl.$parsers.push(function(viewValue) {
					if (viewValue) {
						var params = scope.$eval(attrs.uniqueModName);
						Mod.getByName({
							name : viewValue
						}, function(result) {
							ctrl.$setValidity('unique', angular.isUndefined(result.id) || result.id === params.id);
						});
						return viewValue;
					}
				});
			}
		};
	} ]);

})();
