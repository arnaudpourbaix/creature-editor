(function() {
	'use strict';

	var mod = angular.module('creatureEditor.mod.directives', [ 'creatureEditor.mod.services' ]);

	mod.directive('uniqueName', [ 'Mod', function(Mod) {
		var toId = null;

		function checkName(id, name, ctrl) {
			console.log('calling Mod.getByName');
			Mod.getByName({
				name : name
			}, function(result) {
				console.log('return id', result.id);
				ctrl.$setValidity('unique', angular.isUndefined(result.id) || result.id === id);
			});
		}

		return {
			restrict : 'A',
			require : 'ngModel',
			link : function(scope, elem, attr, ctrl) {
				scope.$watch(attr.ngModel, function(value) { // when the scope changes, check the mod.
					if (!value) {
						return;
					}
					console.log('checking unique name', value);
					var params = scope.$eval(attr.uniqueName);
					if (toId) { // if there was a previous attempt, stop it.
						clearTimeout(toId);
					} else {

					}
					// toId = setTimeout(function() {
					console.log('calling Mod.getByName');
					Mod.getByName({
						name : value
					}, function(result) {
						console.log('return id / current id', result.id, params.id);
						ctrl.$setValidity('unique', angular.isUndefined(result.id) || result.id === params.id);
					});
					// checkName(params.id, value, ctrl);
					// }, 300);
				});
			}
		};
	} ]);

})();
