/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.grid', [ 'jqwidgets.services' ]);

	var createGrid = function($jqwidgets, element, scope, params) {
		if (!scope[params.data]) {
			throw new Error("undefined data in scope: " + params.data);
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
						var getParams = function() {
							return $jqwidgets.common().getParams($scope.$eval(iAttrs.jqGrid), ['data', 'columns'], ['options', 'events']);
						};
						var bindEvents = function(params) {
							iElement.off();
							if (params.events.rowClick) {
								iElement.on('rowclick', function(event) {
									event.stopPropagation();
									$scope.$apply(function() {
										var item = $scope[params.data][event.args.rowindex];
										params.events.rowClick($scope, item);
									});
								});
							}
							if (params.events.cellClick) {
								iElement.on("cellclick", function(event) {
									event.stopPropagation();
									$scope.$apply(function() {
										var item = $scope[params.data][event.args.rowindex];
										params.events.cellClick($scope.$parent, item, event.args.columnindex);
									});
								});
							}
						};
						
						
						var params = getParams();
						bindEvents(params);
						createGrid($jqwidgets, iElement, $scope, params);
						
						$scope.$parent.$watch(iAttrs.jqGrid, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							params = getParams();
							bindEvents(params);
							createGrid($jqwidgets, iElement, $scope, params);
						});
						
						$scope.$on('jqGrid-new-data', function() {
							createGrid($jqwidgets, iElement, $scope, params);
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