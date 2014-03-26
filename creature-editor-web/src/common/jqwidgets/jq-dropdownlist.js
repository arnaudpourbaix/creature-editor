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

	module.directive('jqDropDownList', [ '$compile', '$timeout', '$jqCommon', '$jqDropdownlist',
			function JqDropDownListDirective($compile, $timeout, $jqCommon, $jqDropdownlist) {
				return {
					restrict : 'AE',
					require : 'ngModel',
					scope : true,
					link : function($scope, iElement, iAttrs, controller) {
						var getParams = function() {
							return $jqCommon.getParams($scope.$eval(iAttrs.jqDropDownList), [ 'data', 'displayMember', 'valueMember' ], [ 'options', 'events' ]);
						};
						var bindEvents = function(params) {
							iElement.off();
							iElement.on('select', function(event) {
								var value = event.args.item.value;
								$scope.$apply(function() {
									console.log('select value', value);
									// $scope.$parent[iAttrs.ngModel] = value;
									controller.$setViewValue(value);
								});
							});
						};
						
						controller.$render = function () {
							console.log('rendering value', controller.$viewValue);
							$timeout(function() {
								iElement.jqxDropDownList('val', controller.$viewValue);
							});
						};
						
						var params = getParams();
						bindEvents(params);
						$jqDropdownlist.create(iElement, $scope, params);

						$scope.$parent.$watch(iAttrs.jqDropDownList, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							params = getParams();
							bindEvents(params);
							$jqDropdownlist.create(iElement, $scope, params);
						});

//						$scope.$watch(iAttrs.ngModel, function(newValue, oldValue) {
//							if (newValue == null && newValue !== oldValue) {
//								console.log('clearSelection');
//								iElement.jqxDropDownList('clearSelection');
//							} else if (newValue !== oldValue) {
//								console.log('set value', newValue);
//								iElement.jqxDropDownList('val', newValue);
//							}
//						}, true);

						var selectedId = $scope.$eval(iAttrs.jqSelectedItem);
						if (selectedId) {
							console.log('set value from url', selectedId);
							// $scope.$parent[iAttrs.ngModel] = selectedId;
							$timeout(function() {
								iElement.jqxDropDownList('val', parseInt(selectedId, 10));
							});
						}
//						else if (angular.isDefined($scope.$parent[iAttrs.ngModel])) {
//							$timeout(function() {
//								console.log('set initial value', $scope.$parent[iAttrs.ngModel]);
//								iElement.jqxDropDownList('val', $scope.$parent[iAttrs.ngModel]);
//							});
//						}
					}
				};
			} ]);

}(window, jQuery));