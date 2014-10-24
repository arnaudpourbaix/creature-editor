angular.module('apx-jqwidgets.splitter', [])

.provider('$jqSplitter', function() {
	var options = {};

	this.$get = function jqSplitterService($jqCommon) {
		var service = {
			options : function() {
				return options;
			}
		};
		return service;
	};
})

.directive('jqSplitter', function JqSplitterDirective($compile, $timeout, $jqCommon, $jqSplitter) {
	return {
		restrict : 'AE',
		link : function(scope, element, attributes) {
			var params = scope.$eval(attributes.jqSplitter);
			var options = angular.extend({}, $jqCommon.options(), $jqSplitter.options(), params);
			element.jqxSplitter(options);
		}
	};
})

;