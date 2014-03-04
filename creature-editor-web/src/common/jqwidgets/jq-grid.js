/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.grid', [ 'jqwidgets.services' ]);

	var createGrid = function($jqwidgets, element, scope, params) {
		if (!params.data || !scope[params.data]) {
			throw new Error("undefined param 'data'!");
		}
		if (!params.columns) {
			throw new Error("undefined param 'columns'!");
		}
		var source = {
			localdata : scope[params.data],
			datatype : "json",
			datafields : params.columns
		};
		var dataAdapter = $jqwidgets.dataAdapter().get(source);
		var settings = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.gridOptions(), params.options, {
			columns : params.columns,
			source : dataAdapter
		});
		element.jqxGrid(settings);
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
						createGrid($jqwidgets, iElement, $scope, params);
						$scope.$on('jqGrid-new-data', function() {
							createGrid($jqwidgets, iElement, $scope, params);
						});
						$scope.$parent.$watchCollection(params.data + '.length', function() {
							iElement.jqxGrid('updatebounddata');
						});
						$scope.$parent.$watch(iAttrs.jqGrid, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							params = $scope.$eval(iAttrs.jqGrid);
							createGrid($jqwidgets, iElement, $scope, params);
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery));