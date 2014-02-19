/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.dropdownlist', []);

	var createDropDownList = function(element, data, displayMember, valueMember, options, events) {
		var params = {
			source : data,
			displayMember : displayMember,
			valueMember : valueMember,
			searchMode : 'containsignorecase',
			theme : 'bootstrap'
		};
		element.jqxDropDownList(angular.extend(params, options));
	};

	module.directive('jqDropDownList', [ '$compile', '$timeout', function JqDropDownListDirective($compile, $timeout) {
		return {
			restrict : 'AE',
			replace : true,
			require: 'ngModel',
			compile : function(tElm, tAttrs) {
				return function($scope, iElement, iAttrs, controller) {
						var params = $scope.$eval(iAttrs.jqDropDownList);
						createDropDownList(iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						$scope.$parent.$watchCollection(params.data + '.length', function() {
							createDropDownList(iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						});
						$scope.$parent.$watch(iAttrs.jqDropDownList, function() {
							params = $scope.$eval(iAttrs.jqDropDownList);
							createDropDownList(iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						});
						$scope.$watch(tAttrs.ngModel, function(current, old) {
							if (current == null) {
								iElement.jqxDropDownList('clearSelection');
							} else if (current !== old) {
								iElement.val(current);
							}
						}, true);
						iElement.on('select', function(event) {
							var value = event.args.item.value;
							if (value !== controller.$viewValue) {
								controller.$setViewValue(value);
								$scope.$apply();
							}
						});
						$timeout(function () {
							iElement.val(controller.$viewValue);
						});
				};
			}
		};
	} ]);

}(window, jQuery));