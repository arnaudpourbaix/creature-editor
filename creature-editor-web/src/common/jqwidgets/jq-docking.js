/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.docking', [ 'jqwidgets.common' ]);

	module.provider('$jqDocking', function JqDockingProvider() {
		var options = {};

		this.$get = [ '$jqCommon', function jqDockingService($jqCommon) {
			var service = {};

			service.options = function() {
				return options;
			};

			service.setWindowProperties = function(element, properties) {
				var props = {};
				if (properties.height && properties.height > 600) {
					props.maxHeight = properties.height;
				}
				angular.extend(props, _.omit(properties, [ 'id', 'template', 'templateUrl' ]));
				angular.forEach(props, function(value, key) {
					element.jqxDocking('setWindowProperty', properties.id, key, value);
				});
			};

			return service;
		} ];
	});

	module.directive('jqDocking', [ '$compile', '$timeout', '$jqCommon', '$jqDocking', function JqDockingDirective($compile, $timeout, $jqCommon, $jqDocking) {
		return {
			restrict : 'AE',
			link : function($scope, iElement, iAttrs) {
				var params = $jqCommon.getParams($scope.$eval(iAttrs.jqDocking), [ 'windows' ], [ 'options' ]);
				var options = angular.extend({}, $jqCommon.options(), $jqDocking.options(), params.options);
				iElement.jqxDocking(options);
				angular.forEach(params.windows, function(w) {
					$jqDocking.setWindowProperties(iElement, w);
				});
			}
		};
	} ]);

}(window, jQuery, _));