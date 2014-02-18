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

	module.directive('jqDropDownList', [ '$compile', function JqDropDownListDirective($compile) {
		return {
			restrict : 'AE',
			replace : true,
			scope : true,
			compile : function() {
				return {
					pre : function($scope, iElement, iAttrs) {
						var params = $scope.$eval(iAttrs.jqDropDownList);
						createDropDownList(iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						$scope.$parent.$watchCollection(params.data + '.length', function() {
							createDropDownList(iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						});
						$scope.$parent.$watch(iAttrs.jqDropDownList, function() {
							params = $scope.$eval(iAttrs.jqDropDownList);
							createDropDownList(iElement, $scope[params.data], params.displayMember, params.valueMember, params.options, params.events);
						});
						iElement.off();
						console.log('model', $scope[params.model]);
						iElement.on('select', function(event) {
							var args = event.args;
							if (args) {
								// index represents the item's index.
								var index = args.index;
								var item = args.item;
								// get item's label and value.
								var label = item.label;
								var value = item.value;
								console.log(value, label);
								$scope[params.model] = value;
								console.log('model', $scope[params.model]);
							}
							event.stopPropagation();
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery));