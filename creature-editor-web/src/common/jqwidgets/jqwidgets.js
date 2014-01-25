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
			selectionMode : 'singleRow',
			source : dataAdapter,
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
										event.stopPropagation();
										params.events.rowClick($scope, event.args.row);
									});
								}
								$scope.$watchCollection('data', function() {
									// createDataTable($element, params.columns, $scope.data, params.options);
								});
							}
						};
					}
				};
			} ]);

}(window, jQuery));