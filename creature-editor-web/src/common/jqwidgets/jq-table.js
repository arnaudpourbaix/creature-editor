/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.table', []);

	var createDataTable = function(element, columns, data, options, events) {
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
		var params = {
			altRows : true,
			columnsResize : true,
			sortable : true,
			filterable : true,
			selectionMode : 'none',
			source : dataAdapter,
			theme : 'bootstrap',
			columns : columns
		};
		element.jqxDataTable(angular.extend(params, options));
	};

	module.directive('jqDataTable', [ '$compile', function JqDataTableDirective($compile) {
		return {
			restrict : 'AE',
			replace : true,
			scope : true,
			compile : function() {
				return {
					pre : function($scope, iElement, iAttrs) {
						var params = $scope.$eval(iAttrs.jqDataTable);
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
						createDataTable(iElement, params.columns, $scope[params.data], params.options, params.events);
						$scope.$parent.$on('jqDataTable-new-data', function() {
							createDataTable(iElement, params.columns, $scope[params.data], params.options, params.events);
						});
						$scope.$parent.$watchCollection(params.data + '.length', function() {
							iElement.jqxGrid('updatebounddata');
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery));