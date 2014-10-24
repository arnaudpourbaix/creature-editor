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


.directive("panelWindow", function($panelStack, $timeout) {
			return {
				restrict : 'EA',
				scope : {
					index : '@',
					animate : '='
				},
				replace : true,
				transclude : true,
				template : '<div tabindex="-1" class="left-panel" ng-class="{in: animate}" ng-style="{\'z-index\': 1050 + index*10, display: \'block\'}"><div class="left-panel-inner" ng-click="close($event)" ng-transclude></div></div>',
				link : function(scope, element, attrs) {
					element.addClass(attrs.windowClass || '');
					scope.size = attrs.size;

					$timeout(function() {
						// trigger CSS transitions
						scope.animate = true;
						// focus a freshly-opened modal
						element[0].focus();
					});

					scope.close = function(evt) {
						var panel = $panelStack.getTop();
						if (panel && panel.value.backdrop && panel.value.backdrop !== 'static' && (evt.target === evt.currentTarget)) {
							evt.preventDefault();
							evt.stopPropagation();
							$panelStack.dismiss(panel.key, 'backdrop click');
						}
					};
				}
			};
		})

.directive("panelBackdrop", function($timeout, $panelStack) {
	return {
		restrict : 'EA',
		replace : true,
		template : '<div class="modal-backdrop fade" ng-click="close($event)" ng-class="{in: animate}" ng-style="{\'z-index\': 1040 + (index && 1 || 0) + index*10}"></div>',
		link : function(scope) {
			scope.animate = false;
			// trigger CSS transitions
			$timeout(function() {
				scope.animate = true;
			});

			scope.close = function(evt) {
				var panel = $panelStack.getTop();
				if (panel && panel.value.backdrop && panel.value.backdrop !== 'static' && (evt.target === evt.currentTarget)) {
					evt.preventDefault();
					evt.stopPropagation();
					$panelStack.dismiss(panel.key, 'backdrop click');
				}
			};
		}
	};
})

;
