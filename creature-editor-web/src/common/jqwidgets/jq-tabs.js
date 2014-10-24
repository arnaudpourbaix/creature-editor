angular.module('jqwidgets.tabs', [])

.provider('$jqTabs', function() {
	var options = {};

	this.$get = function($jqCommon, $compile, $translate, $timeout) {
		var service = {};

		service.options = function() {
			return options;
		};

		return service;
	};
})

.directive('jqTabs', function($compile, $timeout, $jqCommon, $jqTabs) {
	return {
		restrict : 'AE',
		link : function(scope, element, attributes) {
			var params = scope.$eval(attributes.jqTabs) || {};
			var options = angular.extend({}, $jqCommon.options(), $jqTabs.options(), params);
			element.jqxTabs(options);
		}
	};
})

;