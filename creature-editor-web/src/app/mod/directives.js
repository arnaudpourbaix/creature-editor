/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.mod.directives', [ 'creatureEditor.mod.services' ]);

	/**
	 * A validation directive to ensure that the model contains a unique name mod
	 */
	module.directive('uniqueName', [ 'Mod', function UniqueNameDirective(Mod) {
		return {
			require : 'ngModel',
			restrict : 'A',
			link : function(scope, element, attrs, ctrl) {
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

	/**
	 * Create a select2 component for a mod<br>
	 * Emit 'selectedMod' event on change with mod parameter.
	 */
	module.directive('selectMod', [ 'Mod', function SelectModDirective(Mod) {
		return {
			restrict : 'E',
			transclude : true,
			replace : true,
			scope : true,
			templateUrl : 'mod/mod-select.tpl.html',
			controller : function($scope, $element, $attrs, $transclude) {
				$scope.mods = Mod.query();

				$scope.modId = null;

				$scope.select2Options = {
					allowClear : $attrs.allowClear === 'true'
				};

				$scope.$watch('modId', function(newValue, oldValue) {
					if (newValue === oldValue) {
						return;
					}
					var mod = _.find($scope.mods, function(mod) {
						return mod.id === parseInt($scope.modId, 10);
					});
					$scope.$emit('selectedMod', mod);
				});
			}
		};
	} ]);

})(_);
