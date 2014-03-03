/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.dropdownlist', [ 'jqwidgets.services' ]);

	var createDropDownList = function($jqwidgets, element, data, displayMember, valueMember, options, events) {
		var params = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.dropDownListOptions(), options, {
			source : data,
			displayMember : displayMember,
			valueMember : valueMember
		});
		element.jqxDropDownList(params);
	};

	module.directive('jqDropDownList', [ '$compile', '$timeout', '$jqwidgets', function JqDropDownListDirective($compile, $timeout, $jqwidgets) {
		return {
			restrict : 'AE',
			replace : true,
			require: 'ngModel',
			compile : function(tElm, tAttrs) {
				return function($scope, iElement, iAttrs, controller) {
						var params = $scope.$eval(iAttrs.jqDropDownList);
						createDropDownList($jqwidgets, iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						$scope.$parent.$watchCollection(params.data + '.length', function() {
							createDropDownList($jqwidgets, iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						});
						$scope.$parent.$watch(iAttrs.jqDropDownList, function() {
							params = $scope.$eval(iAttrs.jqDropDownList);
							createDropDownList($jqwidgets, iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
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