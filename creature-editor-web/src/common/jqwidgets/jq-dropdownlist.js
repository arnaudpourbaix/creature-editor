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
					//scope : true,
					link : function($scope, element, attributes, ngModel) {
						function read() {
							var html = element.jqxDropDownList('val');
							console.log('read', 'value', html);
							ngModel.$setViewValue(html);
						}
						function render(value) {
							console.log('render', value);
							if (value) {
								element.jqxDropDownList('val', value);
							} else {
								element.jqxDropDownList('clearSelection');
							}
						}

						ngModel.$render = function() {
							render(ngModel.$viewValue);
						};
						
						var getParams = function() {
							return $jqCommon.getParams($scope.$eval(attributes.jqDropDownList), [ 'data', 'displayMember', 'valueMember' ], [ 'options', 'events' ]);
						};
						
						var bindEvents = function(params) {
							element.off();
							element.on('select', function(event) {
								var value = event.args.item.value;
								if (!$scope.$$phase) {
									console.log('select event $apply', value);
									//$scope.$apply(read);
									read();
									$scope.$apply();
								} else {
									console.log('select event', value);
									read();
								}
							});
						};

						var params = getParams();
						bindEvents(params);
						$jqDropdownlist.create(element, $scope, params);

						$scope.$parent.$watch(attributes.jqDropDownList, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							params = getParams();
							bindEvents(params);
							$jqDropdownlist.create(element, $scope, params);
						});

						$scope.$parent.$watch(attributes.ngModel, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							console.log('ngModel change', newValue, oldValue);
							render(newValue);
						});
						
						console.log('init dropdown');
						//read();

						var selectedId = $scope.$eval(attributes.jqSelectedItem);
						if (selectedId) {
							console.log('set value from url', selectedId);
							// $scope.$parent[attributes.ngModel] = selectedId;
//							$timeout(function() {
//								element.jqxDropDownList('val', parseInt(selectedId, 10));
//							});
						} else {
							// console.log('set value from model', element.jqxDropDownList('val'));
							// ngModel.$setViewValue(element.jqxDropDownList('val'));
						}
						// else if (angular.isDefined($scope.$parent[attributes.ngModel])) {
						// $timeout(function() {
						// console.log('set initial value', $scope.$parent[attributes.ngModel]);
						// element.jqxDropDownList('val', $scope.$parent[attributes.ngModel]);
						// });
						// }
					}
				};
			} ]);

	module.directive('apTest', function() {
		return {
			restrict : 'A',
			require : 'ngModel',
			link : function(scope, element, attrs, ngModel) {
				// Write data to the model
				function read() {
					var html = element.html();
					// When we clear the content editable the browser leaves a <br> behind
					// If strip-br attribute is provided then we strip this out
					if (attrs.stripBr && html === '<br>') {
						html = '';
					}
					console.log('read', 'value=', html);
					ngModel.$setViewValue(html);
				}
				
				// Specify how UI should be updated
				ngModel.$render = function() {
					console.log('$render', 'grml=', ngModel.$viewValue || '');
					element.html(ngModel.$viewValue || '');
				};

				// Listen for change events to enable binding
				element.on('blur keyup change', function() {
					console.log('apply change');
					scope.$apply(read);
				});
				
				console.log('initialize');
				read(); // initialize
			}
		};
	});

}(window, jQuery));