angular.module('apx-jqwidgets.splitter', [])

/**
 * @ngdoc directive
 * @name apx-jqwidgets.directive:jqSplitter
 * @restrict A
 * @priority 0
 * @description Apply splitter widget on element.
 * Directive attribute's value is an object that contains widget properties:
 */
.directive('jqSplitter', function(jqCommon) {
	return {
		restrict : 'A',
		link : function(scope, element, attributes) {
			var params = scope.$eval(attributes.jqSplitter);
			var settings = angular.extend({}, jqCommon.defaults, params);
			element.jqxSplitter(settings);
		}
	};
})

;