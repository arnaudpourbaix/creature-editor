/* global jQuery */
(function(window, $) {
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
					console.log('rendering', value);
					if (value) {
						element.jqxDropDownList('val', value);
					} else {
						element.jqxDropDownList('clearSelection');
					}
				}

//				ngModel.$render = function() {
//					render(ngModel.$viewValue);
//				};

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
						console.log('select item', value);
						if (!$scope.$$phase) {
							$scope.$apply(function() {
								$scope.$parent[attributes.ngModel] = value;
							});
						} else {
							$scope.$parent[attributes.ngModel] = value;
						}
					});
				};

				var params = getParams();
				bindEvents(params);
				$jqDropdownlist.create(element, $scope, params);

				var selectedId = $scope.$eval(attributes.jqSelectedItem);
				if (selectedId) {
					$scope.$parent[attributes.ngModel] = selectedId;
					// TODO add selectedIndex property to init grid with selected value
				}
				
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
					console.log('watch model change', newValue, oldValue);
					ngModel.$setViewValue(newValue);
					render(newValue);
				});
				
			}
		};
	} ]);

}(window, jQuery));