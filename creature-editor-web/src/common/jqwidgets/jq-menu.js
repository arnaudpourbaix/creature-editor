/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.menu', [ 'jqwidgets.services' ]);

	module.directive('jqMenu', [ '$compile', '$jqwidgets', '$timeout', function JqMenuDirective($compile, $jqwidgets, $timeout) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var settings = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.menuOptions(), scope.$eval(attributes.jqMenu));
				element.jqxMenu(settings);
//				$timeout(function() {
//					element.jqxMenu(settings);
//				});
			}
//			compile : function() {
//				return {
//					pre : function($scope, iElement, iAttrs) {
//						var settings = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.menuOptions(), $scope.$eval(iAttrs.jqMenu));
//						iElement.jqxMenu(settings);
//					}
//				};
//			}
		};
	} ]);

}(window, jQuery));