(function() {
	'use strict';

	angular.module('editor.creature.directives', [])

	/**
	 * ensure that category name is unique
	 */
//	.directive('apUniqueCategoryName', function(Category) {
//		return {
//			require : 'ngModel',
//			restrict : 'A',
//			link : function(scope, element, attrs, ctrl) {
//				// using push() here to run it as the last parser, after we are sure that other validators were run
//				ctrl.$parsers.push(function(viewValue) {
//					if (viewValue) {
//						var params = scope.$eval(attrs.apUniqueCategoryName);
//						Category.getByName({
//							name : viewValue
//						}, function(result) {
//							ctrl.$setValidity('unique', angular.isUndefined(result.id) || result.id === params.id);
//						});
//						return viewValue;
//					}
//				});
//			}
//		};
//	})

	;
	
})();
