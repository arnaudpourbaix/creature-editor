/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('toolbox.directives', [ 'ui.bootstrap', 'toolbox.services' ]);

	module.directive('focusMe', [ '$timeout', function FocusMeDirective($timeout) {
		return function(scope, element, attrs) {
			scope.$watch(attrs.focusMe, function(value) {
				if (value) {
					$timeout(function() {
						element.focus();
					}, 700);
				}
			});
		};
	} ]);
	
	module.directive('draggable', [ 'uiService', function(uiService) {
		return {
			restrict : 'A',
			link : function(scope, element, attributes) {
				uiService.draggable(element);
			}
		};
	} ]);

	module.directive('resizable', [ 'uiService', function(uiService) {
		return {
			restrict : 'A',
			link : function(scope, element, attributes) {
				uiService.resizable(element);
			}
		};
	} ]);

	module.directive('widgetPanel', [ 'uiService', function(uiService) {
		return {
			restrict : 'E',
			transclude : true,
			replace : true,
			template : '<div class="panel panel-primary" data-ng-transclude></div>',
			link : function(scope, element, attributes) {
				uiService.draggable(element, {
					handle : $('.panel-heading', element),
					containment : "document"
				});
				uiService.resizable(element);
			}
		};
	} ]);

	module.directive('widgetPanelHeader', function() {
		return {
			restrict : 'AE',
			transclude : true,
			replace : true,
			template : '<div class="panel-heading" data-ng-transclude></div>'
		};
	});

	module.directive('widgetPanelBody', function() {
		return {
			restrict : 'AE',
			transclude : true,
			replace : true,
			template : '<div class="panel-body" data-ng-transclude></div>'
		};
	});

	module.directive('widgetPanelFooter', function() {
		return {
			restrict : 'AE',
			transclude : true,
			replace : true,
			template : '<div class="panel-footer" data-ng-transclude></div>'
		};
	});

	angular
			.module("template/modal/window.html", [])
			.run(
					[
							"$templateCache",
							function($templateCache) {
								$templateCache
										.put(
												"template/modal/window.html",
												"<div widget-modal tabindex=\"-1\" class=\"modal fade {{ windowClass }}\" ng-class=\"{in: animate}\" ng-style=\"{'z-index': 1050 + index*10, display: 'block'}\" ng-click=\"close($event)\">\n"
														+ "    <div class=\"modal-dialog\"><div class=\"modal-content\" ng-transclude></div></div>\n" + "</div>");
							} ]);

	module.directive('widgetModal', [ 'uiService', function(uiService) {
		return {
			restrict : 'A',
			link : function(scope, element, attributes) {
				uiService.draggable(element, {
					handle : $('.modal-header', element)
				});
				// uiService.resizable(element);
			}
		};
	} ]);

	module.directive('widgetModalHeader', [ '$compile', function($compile) {
		return {
			restrict : 'AE',
			transclude : true,
			replace : true,
			priority : 2,
			template : '<div class="modal-header" data-ng-transclude></div>',
			compile : function compile(tElement, tAttrs, transclude) {
				return {
					post : function postLink(scope, iElement, iAttrs, controller) {
						if (iAttrs.closeButton === 'true') {
							$(iElement).prepend($compile('<button type="button" class="close" data-ng-click="$dismiss()" aria-hidden="true">x</button>')(scope));
						}
					}
				};
			}
		};
	} ]);

	module.directive('widgetModalBody', function() {
		return {
			restrict : 'AE',
			transclude : true,
			replace : true,
			template : '<div class="modal-body" data-ng-transclude></div>'
		};
	});

	module.directive('widgetModalFooter', function() {
		return {
			restrict : 'AE',
			transclude : true,
			replace : true,
			template : '<div class="modal-footer" data-ng-transclude></div>'
		};
	});

}(window, jQuery));
