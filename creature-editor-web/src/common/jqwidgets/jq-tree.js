/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.tree', [ 'jqwidgets.services' ]);

	var createTree = function($jqwidgets, element, columns, data, options, events) {
		//$jqwidgets.dataAdapter().get();
		var dataFields = [];
		angular.forEach(columns, function(column, index) {
			if (column.dataField) {
				dataFields.push({
					name : column.dataField,
					type : column.type
				});
			}
		});
		var dataAdapter = new $.jqx.dataAdapter({
			localData : data,
			dataType : "array",
			dataFields : dataFields
		});
		var params = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.treeOptions(), options, {
			source : dataAdapter
		});
		element.jqxTree(params);
	};

	module.directive('jqTree', [ '$compile', '$jqwidgets', function JqTreeDirective($compile, $jqwidgets) {
		return {
			restrict : 'AE',
			replace : true,
			scope : true,
			compile : function() {
				return {
					pre : function($scope, iElement, iAttrs) {
						var params = $scope.$eval(iAttrs.jqTree);
						createTree($jqwidgets, iElement, params.columns, $scope[params.data], params.options, params.events);
						$scope.$parent.$watch(iAttrs.jqGrid, function() {
							params = $scope.$eval(iAttrs.jqTree);
							createTree($jqwidgets, iElement, params.columns, $scope[params.data], params.options, params.events);
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery));