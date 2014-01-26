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

	jq.directive('jqDataTable', [ '$compile', '$filter', '$templateCache', '$sortService', '$domUtilityService', '$utilityService', '$timeout', '$parse', '$http', '$q',
			function($compile, $filter, $templateCache, sortService, domUtilityService, $utils, $timeout, $parse, $http, $q) {
				return {
					scope : true,
					compile : function() {
						return {
							pre : function($scope, iElement, iAttrs) {
								var $element = $(iElement);
								var params = $scope.$eval(iAttrs.jqDataTable);
								$scope.data = $scope[params.data];
								createDataTable($element, params.columns, $scope.data, params.options, params.events);
								if (params.events.rowClick) {
									$element.on('rowClick', function(event) {
										console.log('click');
										event.stopPropagation();
										$scope.$apply(function() {
											params.events.rowClick($scope, event.args.row);
										});
									});
								}
								$scope.$watchCollection('data', function() {
									createDataTable($element, params.columns, $scope.data, params.options);
								});
							},
							post : function postLink($scope, iElement, iAttrs) {
								var $element = $(iElement);
								$compile($element)($scope);
							}
						};
					}
				};
			} ]);

	var createGrid = function(element, columns, data, options, events) {
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

	jq.directive('jqGrid', [ '$compile', '$filter', '$templateCache', '$sortService', '$domUtilityService', '$utilityService', '$timeout', '$parse', '$http', '$q',
			function($compile, $filter, $templateCache, sortService, domUtilityService, $utils, $timeout, $parse, $http, $q) {
				return {
					scope : true,
					compile : function() {
						return {
							pre : function($scope, iElement, iAttrs) {
								var $element = $(iElement);
								var params = $scope.$eval(iAttrs.jqGrid);
								$scope.data = $scope[params.data];
								$element.on('initialized', function() {
									$compile(iElement)($scope);
								});
								if (params.events.rowClick) {
									$element.on('rowClick', function(event) {
										event.stopPropagation();
										$scope.$apply(function() {
											var spell = $scope.data[event.args.rowindex];
											params.events.rowClick($scope, spell);
										});
									});
								}
								if (params.events.cellClick) {
									$element.on("cellClick", function(event) {
										var column = event.args.column;
										var rowindex = event.args.rowindex;
										var columnindex = event.args.columnindex;
										console.log(column, rowindex, columnindex);
									});
								}
								createGrid($element, params.columns, $scope.data, params.options, params.events);
								$scope.$watchCollection('data', function() {
									createGrid($element, params.columns, $scope.data, params.options);
								});
							}
						};
					}
				};
			} ]);

}(window, jQuery));