/* global jQuery */
(function(window, $) {
	'use strict';

	var jq = angular.module('jqwidgets', []);

	jq.directive('jqDataTable', [ '$compile', '$filter', '$templateCache', '$sortService', '$domUtilityService', '$utilityService', '$timeout', '$parse',
			'$http', '$q', function($compile, $filter, $templateCache, sortService, domUtilityService, $utils, $timeout, $parse, $http, $q) {
				return {
					scope : true,
					compile : function() {
						return {
							pre : function($scope, iElement, iAttrs) {
								var $element = $(iElement);
								var options = $scope.$eval(iAttrs.jqDataTable);

							}
						};
					}
				};
			} ]);

}(window, jQuery));