/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.docking', [ 'jqwidgets.common' ]);

	module.provider('$jqDocking', function JqDockingProvider() {
		var options = {};

		this.$get = [ '$jqCommon', function jqDockingService($jqCommon) {
			var service = {
				options : function() {
					return options;
				}
			};
			return service;
		} ];
	});

	module.directive('jqDocking', [ '$compile', '$timeout', '$jqCommon', '$jqDocking', function JqDockingDirective($compile, $timeout, $jqCommon, $jqDocking) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var params = scope.$eval(attributes.jqDocking);
				var options = angular.extend({}, $jqCommon.options(), $jqDocking.options(), params);
				element.jqxDocking(options);
			}
		};
	} ]);

}(window, jQuery, _));