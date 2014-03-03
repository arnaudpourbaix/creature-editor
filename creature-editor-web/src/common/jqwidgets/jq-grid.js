/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.grid', [ 'jqwidgets.services' ]);

	var createGrid = function($jqwidgets, element, columns, data, options, events) {
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
		$jqwidgets.dataAdapter().get({
			localData : data,
			dataType : "array",
			dataFields : dataFields
		});
		var params = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.gridOptions(), options, {
			columns : columns,
			source : dataAdapter
		});
		element.jqxGrid(params);
	};

	module.directive('jqGrid', [ '$compile', '$jqwidgets', function JqGridDirective($compile, $jqwidgets) {
		return {
			restrict : 'AE',
			replace : true,
			scope : true,
			compile : function() {
				return {
					pre : function($scope, iElement, iAttrs) {
						var params = $scope.$eval(iAttrs.jqGrid);
						if (params.events.rowClick) {
							iElement.on('rowClick', function(event) {
								event.stopPropagation();
								$scope.$apply(function() {
									var item = $scope[params.data][event.args.rowindex];
									params.events.rowClick($scope, item);
								});
							});
						}
						if (params.events.cellClick) {
							iElement.on("cellClick", function(event) {
								event.stopPropagation();
								$scope.$apply(function() {
									var item = $scope[params.data][event.args.rowindex];
									params.events.cellClick($scope.$parent, item, event.args.columnindex);
								});
							});
						}
						createGrid($jqwidgets, iElement, params.columns, $scope[params.data], params.options, params.events);
						$scope.$on('jqGrid-new-data', function() {
							createGrid($jqwidgets, iElement, params.columns, $scope[params.data], params.options, params.events);
						});
						$scope.$parent.$watchCollection(params.data + '.length', function() {
							iElement.jqxGrid('updatebounddata');
						});
						$scope.$parent.$watch(iAttrs.jqGrid, function() {
							params = $scope.$eval(iAttrs.jqGrid);
							createGrid($jqwidgets, iElement, params.columns, $scope[params.data], params.options, params.events);
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery));