var app = angular.module('ui-components', [ 'ngGrid', 'ui.bootstrap' ]);

app.factory('uiService', function() {
	'use strict';
	var uiService = {};

	uiService.draggable = function(element, options) {
		var defaultOpts = {
			cursor : 'move',
			refreshPositions : true,
			opacity : 0.5,
			drag : function(e, ui) {
				ui.position.left = ui.position.left * 1.4;
				ui.position.top = ui.position.top * 1.0;
			}
		}, opts = angular.extend(defaultOpts, options || {});
		$(element).draggable(opts);
	};

	uiService.resizable = function(element, options) {
		var defaultOpts = {}, opts = angular.extend(defaultOpts, options || {});
		$(element).resizable(opts);
	};

	return uiService;

});

app.directive('draggable', [ 'uiService', function(uiService) {
	'use strict';
	return {
		restrict : 'A',
		link : function(scope, element, attributes) {
			uiService.draggable(element);
		}
	};
} ]);

app.directive('resizable', [ 'uiService', function(uiService) {
	'use strict';
	return {
		restrict : 'A',
		link : function(scope, element, attributes) {
			uiService.resizable(element);
		}
	};
} ]);

app.directive('widgetPanel', [ 'uiService', function(uiService) {
	'use strict';
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

app.directive('widgetPanelHeader', function() {
	'use strict';
	return {
		restrict : 'AE',
		transclude : true,
		replace : true,
		template : '<div class="panel-heading" data-ng-transclude></div>'
	};
});

app.directive('widgetPanelBody', function() {
	'use strict';
	return {
		restrict : 'AE',
		transclude : true,
		replace : true,
		template : '<div class="panel-body" data-ng-transclude></div>'
	};
});

app.directive('widgetPanelFooter', function() {
	'use strict';
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
							'use strict';
							$templateCache
									.put(
											"template/modal/window.html",
											"<div widget-modal tabindex=\"-1\" class=\"modal fade {{ windowClass }}\" ng-class=\"{in: animate}\" ng-style=\"{'z-index': 1050 + index*10, display: 'block'}\" ng-click=\"close($event)\">\n"
													+ "    <div class=\"modal-dialog\"><div class=\"modal-content\" ng-transclude></div></div>\n" + "</div>");
						} ]);

app.directive('widgetModal', [ 'uiService', function(uiService) {
	'use strict';
	return {
		restrict : 'A',
		link : function(scope, element, attributes) {
			uiService.draggable(element, {
				handle : $('.modal-header', element)
			});
			uiService.resizable(element);
		}
	};
} ]);

app.directive('widgetModalHeader', function($compile) {
	'use strict';
	return {
		restrict : 'AE',
		transclude : true,
		replace : true,
		priority : 2,
		template : '<div class="modal-header" data-ng-transclude></div>',
		compile : function compile(tElement, tAttrs, transclude) {
			return {
				post : function postLink(scope, iElement, iAttrs, controller) {
					if (iAttrs.closeButton) {
						$(iElement).prepend($compile('<button type="button" class="close" data-ng-click="$dismiss()" aria-hidden="true">x</button>')(scope));
					}
				}
			};
		}
	};
});

app.directive('widgetModalBody', function() {
	'use strict';
	return {
		restrict : 'AE',
		transclude : true,
		replace : true,
		template : '<div class="modal-body" data-ng-transclude></div>'
	};
});

app.directive('widgetModalFooter', function() {
	'use strict';
	return {
		restrict : 'AE',
		transclude : true,
		replace : true,
		template : '<div class="modal-footer" data-ng-transclude></div>'
	};
});
