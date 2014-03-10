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
					replace : true,
					require : 'ngModel',
					compile : function(tElm, tAttrs) {
						return function($scope, iElement, iAttrs, controller) {
							var getParams = function() {
								return $jqCommon.getParams($scope.$eval(iAttrs.jqDropDownList), [ 'data', 'displayMember', 'valueMember' ], [ 'options', 'events' ]);
							};
							var bindEvents = function(params) {
								iElement.off();
								iElement.on('select', function(event) {
									var value = event.args.item.value;
									if (value !== controller.$viewValue) {
										controller.$setViewValue(value);
										$scope.$apply();
									}
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

							$scope.$watch(tAttrs.ngModel, function(current, old) {
								if (current == null) {
									iElement.jqxDropDownList('clearSelection');
								} else if (current !== old) {
									iElement.val(current);
								}
							}, true);

							$scope.$parent.$watch(params.data, function(newValue, oldValue) {
								if (newValue === oldValue) {
									return;
								}
								$jqDropdownlist.create(iElement, $scope, params);
							});

							$timeout(function() {
								iElement.val(controller.$viewValue);
							});
						};
					}
				};
			} ]);

}(window, jQuery));