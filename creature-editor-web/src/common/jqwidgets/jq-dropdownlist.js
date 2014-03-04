/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.dropdownlist', [ 'jqwidgets.services' ]);

	var createDropDownList = function($jqwidgets, element, scope, params) {
		if (!params.data || !scope[params.data]) {
			throw new Error("undefined param 'data'!");
		}
		if (!params.displayMember) {
			throw new Error("undefined param 'displayMember'!");
		}
		if (!params.valueMember) {
			throw new Error("undefined param 'valueMember'!");
		}
		var settings = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.dropDownListOptions(), params.options, {
			source : scope[params.data],
			displayMember : params.displayMember,
			valueMember : params.valueMember
		});
		element.jqxDropDownList(settings);
	};

	module.directive('jqDropDownList', [ '$compile', '$timeout', '$jqwidgets', function JqDropDownListDirective($compile, $timeout, $jqwidgets) {
		return {
			restrict : 'AE',
			replace : true,
			require: 'ngModel',
			compile : function(tElm, tAttrs) {
				return function($scope, iElement, iAttrs, controller) {
						var params = $scope.$eval(iAttrs.jqDropDownList);
						createDropDownList($jqwidgets, iElement, $scope, params);
						$scope.$parent.$watch(params.data, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							createDropDownList($jqwidgets, iElement, $scope, params);
						});
						$scope.$parent.$watch(iAttrs.jqDropDownList, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							params = $scope.$eval(iAttrs.jqDropDownList);
							createDropDownList($jqwidgets, iElement, $scope, params);
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