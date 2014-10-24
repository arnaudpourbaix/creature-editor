/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.splitter', [ 'jqwidgets.common' ]);

	module.provider('$jqSplitter', function JqSplitterProvider() {
		var options = {};

		this.$get = [ '$jqCommon', function jqSplitterService($jqCommon) {
			var service = {
				options : function() {
					return options;
				}
			};
			return service;
		} ];
	});

	module.directive('jqSplitter', [ '$compile', '$timeout', '$jqCommon', '$jqSplitter', function JqSplitterDirective($compile, $timeout, $jqCommon, $jqSplitter) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var params = scope.$eval(attributes.jqSplitter);
				var options = angular.extend({}, $jqCommon.options(), $jqSplitter.options(), params);
				element.jqxSplitter(options);
			}
		};
	} ]);

}(window, jQuery, _));