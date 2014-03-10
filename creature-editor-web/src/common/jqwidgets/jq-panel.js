/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.panel', [ 'jqwidgets.common' ]);

	module.provider('$jqPanel', function JqPanelProvider() {
		var options = {};

		this.$get = [ '$jqCommon', '$compile',
				function jqPanelService($jqCommon, $compile) {
					var service = {
						options : function() {
							return options;
						}
					};
					return service;
				} ];
	});

	module.directive('jqPanel', [ '$compile', '$jqCommon', '$jqPanel', function JqPanelDirective($compile, $jqCommon, $jqPanel) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var params = scope.$eval(attributes.jqPanel);
				var options = angular.extend({}, $jqCommon.options(), $jqPanel.options(), params.options);
				element.jqxPanel(options);
				if (params.content) {
					var content = $compile(params.content)(scope);
					element.jqxPanel('append', content);
				}
			}
		};
	} ]);

}(window, jQuery));