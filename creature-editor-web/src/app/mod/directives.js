var mod = angular.module('creatureEditor.mod.directives', [ 'creatureEditor.mod.services' ]);

mod.directive('uniqueName', [ 'modService', function(modService) {
	'use strict';
	var toId = null;
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, elem, attr, ctrl) {
			scope.$watch(attr.ngModel, function(value) { // when the scope changes, check the mod.
				if (angular.isString(value) && value.length === 0) {
					return;
				}
				if (toId) { // if there was a previous attempt, stop it.
					clearTimeout(toId);
				}
				var params = scope.$eval(attr.uniqueName);
				toId = setTimeout(function() {
					modService.checkUniqueValue(params.id, value).then(function(unique) {
						ctrl.$setValidity('unique', unique);
					});
				}, 300);
			});
		}
	};
} ]);
