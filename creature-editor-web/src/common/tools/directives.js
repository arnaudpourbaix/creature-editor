angular.module('apx-tools.directives', [])

.directive('focusMe', function($timeout) {
	return function(scope, element, attrs) {
		$timeout(function() {
			element.focus();
		}, 700);
	};
})

.directive("apxScopeElement", function() {
	return {
		restrict : "A",
		compile : function(tElement, tAttrs, transclude) {
			return {
				pre : function preLink(scope, iElement, iAttrs) {
					scope[iAttrs.apxScopeElement] = iElement;
				}
			};
		}
	};
})

;
