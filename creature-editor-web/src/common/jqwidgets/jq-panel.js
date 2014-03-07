/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.panel', [ 'jqwidgets.services' ]);

	module.directive('jqPanel', [ '$compile', '$jqwidgets', function JqPanelDirective($compile, $jqwidgets) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var params = scope.$eval(attributes.jqPanel);
				var options = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.panelOptions(), params.options);
				element.jqxPanel(options);
				if (params.content) {
					var content = $compile(params.content)(scope);
					element.jqxPanel('append', content);
				}
			}
		};
	} ]);

}(window, jQuery));