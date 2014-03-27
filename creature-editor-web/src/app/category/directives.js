(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.directives', [ 'creatureEditor.category.services' ]);

	/**
	 * ensure that category name is unique
	 */
	module.directive('apUniqueCategoryName', [ 'Category', function ApUniqueCategoryNameDirective(Category) {
		return {
			require : 'ngModel',
			restrict : 'A',
			link : function(scope, element, attrs, ctrl) {
				// using push() here to run it as the last parser, after we are sure that other validators were run
				ctrl.$parsers.push(function(viewValue) {
					if (viewValue) {
						var params = scope.$eval(attrs.apUniqueCategoryName);
						Category.getByName({
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
