/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.dropdownlist', [ 'jqwidgets.common' ]);

	module.provider('$jqDropdownlist', function JqDropdownlistProvider() {
		var options = {
			searchMode : 'containsignorecase'
		};

		this.$get = [ '$jqCommon', function jqDropdownlistService($jqCommon) {
			var service = {
				create : function(element, scope, params) {
					var data = $jqCommon.getScopeData(params.data, scope);
					var settings = angular.extend({}, $jqCommon.options(), options, params.options, {
						source : data,
						displayMember : params.displayMember,
						valueMember : params.valueMember
					});
					element.jqxDropDownList(settings);
				}
			};
			return service;
		} ];
	});

	module.directive('jqDropDownList', [ '$compile', '$timeout', '$parse', '$jqCommon', '$jqDropdownlist', function JqDropDownListDirective($compile, $timeout, $parse, $jqCommon, $jqDropdownlist) {
		return {
			restrict : 'AE',
			require : 'ngModel',
			link : function($scope, element, attributes, ngModel) {
				function render(value) {
					if (value) {
						element.jqxDropDownList('val', value);
					} else {
						element.jqxDropDownList('clearSelection');
					}
				}
				function getItemIndex(id) { /* jshint -W116 */
					var data = $jqCommon.getScopeData(params.data, $scope);
					return _.findIndex(data, function(item) {
						return item[params.valueMember] == id;
					});
				}
				function selectItem(id) {
					modelValue.assign($scope.$parent, id);
					if (angular.isString(params.select)) {
						$scope[params.select](id);
					}
				}

				var getParams = function() {
					return $jqCommon.getParams($scope.$eval(attributes.jqDropDownList), [ 'data', 'displayMember', 'valueMember' ], [ 'options', 'select' ]);
				};

				var bindEvents = function(params) {
					element.off();
					element.on('select', function(event) {
						var value = event.args.item.value;
						if (value === ngModel.$viewValue) { // do nothing if value is the same
							return;
						}
						if (!$scope.$$phase) {
							$scope.$apply(function() {
								selectItem(value);
							});
						} else {
							selectItem(value);
						}
					});
				};

				var modelValue = $parse(attributes.ngModel);
				var params = getParams();
				bindEvents(params);
				
				var selectedId = $scope.$eval(attributes.jqSelectedItem);
				if (selectedId != null) {
					modelValue.assign($scope.$parent, selectedId);
					params.options.selectedIndex = getItemIndex(selectedId);
				} else if (modelValue($scope) != null) {
					params.options.selectedIndex = getItemIndex(modelValue($scope));
				}
				
				$jqDropdownlist.create(element, $scope, params);

				
				$scope.$watch(attributes.jqDropDownList, function(newValue, oldValue) { /* jshint -W116 */
					if (newValue == oldValue) {
						return;
					}
					params = getParams();
					bindEvents(params);
					$jqDropdownlist.create(element, $scope, params);
				});
				
				$scope.$parent.$watch(attributes.ngModel, function(newValue, oldValue) { /* jshint -W116 */
					if (newValue == oldValue) {
						return;
					}
					ngModel.$setViewValue(newValue);
					render(newValue);
				});
				
			}
		};
	} ]);

}(window, jQuery, _));