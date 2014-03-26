/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.tabs', [ 'jqwidgets.common' ]);

	module.provider('$jqTabs', function JqTabsProvider() {
		var options = {};

		this.$get = [ '$jqCommon', '$compile', '$translate', '$timeout', function jqTabsService($jqCommon, $compile, $translate, $timeout) {
			var service = {};

			service.options = function() {
				return options;
			};

			return service;
		} ];
	});

	module.directive('jqTabs', [ '$compile', '$timeout', '$jqCommon', '$jqTabs', function JqTabsDirective($compile, $timeout, $jqCommon, $jqTabs) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var params = scope.$eval(attributes.jqTabs) || {};
				var options = angular.extend({}, $jqCommon.options(), $jqTabs.options(), params);
				$timeout(function() {
					element.jqxTabs(options);
				});
			}
		};
	} ]);

}(window, jQuery, _));