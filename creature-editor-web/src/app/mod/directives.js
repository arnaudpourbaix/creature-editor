(function() {
	'use strict';

	var mod = angular.module('creatureEditor.mod.directives', [ 'creatureEditor.mod.services' ]);

	/**
	 * A validation directive to ensure that the model contains a unique name mod
	 */
	mod.directive('uniqueName', [ "Mod", function(Mod) {
		return {
			require : 'ngModel',
			restrict : 'A',
			link : function(scope, el, attrs, ctrl) {
				// using push() here to run it as the last parser, after we are sure that other validators were run
				ctrl.$parsers.push(function(viewValue) {
					if (viewValue) {
						var params = scope.$eval(attrs.uniqueName);
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
