/* global jQuery */
(function(window, $) {
	'use strict';

	var jq = angular.module('jqwidgets', []);

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

	jq.directive('jqDataTable', [ '$compile', function($compile) {
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
									var spell = $scope[params.data][event.args.rowindex];
									params.events.rowClick($scope, spell);
								});
							});
						}
						if (params.events.cellClick) {
							iElement.on("cellClick", function(event) {
								event.stopPropagation();
								$scope.$apply(function() {
									var spell = $scope[params.data][event.args.rowindex];
									params.events.cellClick($scope.$parent, spell, event.args.columnindex);
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

	var createGrid = function(element, columns, data, options, events) {
		console.log('create grid');
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
			selectionMode : 'singlerow',
			source : dataAdapter,
			filtermode : 'excel',
			pagermode : 'simple',
			columnsreorder : false,
			enabletooltips : true,
			theme : 'bootstrap',
			columns : columns
		};
		element.jqxGrid(angular.extend(params, options));
	};

	jq.directive('jqGrid', [ '$compile', function($compile) {
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
									var spell = $scope[params.data][event.args.rowindex];
									params.events.rowClick($scope, spell);
								});
							});
						}
						if (params.events.cellClick) {
							iElement.on("cellClick", function(event) {
								event.stopPropagation();
								$scope.$apply(function() {
									var spell = $scope[params.data][event.args.rowindex];
									params.events.cellClick($scope.$parent, spell, event.args.columnindex);
								});
							});
						}
						createGrid(iElement, params.columns, $scope[params.data], params.options, params.events);
						$scope.$parent.$on('jqGrid-new-data', function() {
							createGrid(iElement, params.columns, $scope[params.data], params.options, params.events);
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