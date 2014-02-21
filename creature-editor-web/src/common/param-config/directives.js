(function() {
	'use strict';

	var module = angular.module('param-config.directives', [ 'param-config.services' ]);

	module.directive('checkFolder', [ 'ParameterService', function CheckFolderDirective(ParameterService) {
		return {
			require : 'ngModel',
			restrict : 'A',
			link : function(scope, element, attrs, ctrl) {
				// using push() here to run it as the last parser, after we are sure that other validators were run
				ctrl.$parsers.push(function(viewValue) {
					if (viewValue) {
						ParameterService.checkFolder(viewValue).then(function(result) {
							ctrl.$setValidity('folder', result.data === 'true');
						});
						return viewValue;
					}
				});
			}
		};
	} ]);

})();
