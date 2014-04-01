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
					if (!scope[params.data]) {
						throw new Error("undefined data in scope: " + params.data);
					}
					var settings = angular.extend({}, $jqCommon.options(), options, params.options, {
						source : scope[params.data],
						displayMember : params.displayMember,
						valueMember : params.valueMember
					});
					element.jqxDropDownList(settings);
				}
			};
			return service;
		} ];
	});

	module.directive('jqDropDownList', [ '$compile', '$timeout', '$jqCommon', '$jqDropdownlist', function JqDropDownListDirective($compile, $timeout, $jqCommon, $jqDropdownlist) {
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
					return _.findIndex($scope[params.data], function(item) {
						return item[params.valueMember] == id;
					});
				}
				function selectItem(id) {
					$scope.$parent[attributes.ngModel] = id;
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

				var params = getParams();
				bindEvents(params);
				
				var selectedId = $scope.$eval(attributes.jqSelectedItem);
				if (selectedId != null) {
					$scope.$parent[attributes.ngModel] = selectedId;
					params.options.selectedIndex = getItemIndex(selectedId);
				} else if ($scope.$parent[attributes.ngModel] != null) {
					params.options.selectedIndex = $scope.$parent[attributes.ngModel];
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