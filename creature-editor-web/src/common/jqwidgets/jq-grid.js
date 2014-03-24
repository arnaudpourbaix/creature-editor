/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.grid', [ 'jqwidgets.common', 'jqwidgets.data-adapter' ]);

	module.provider('$jqGrid', function JqGridProvider() {
		var options = {
			altRows : true,
			columnsResize : true,
			sortable : true,
			showfilterrow : true,
			filterable : true,
			selectionMode : 'singlerow',
			pagermode : 'simple',
			enabletooltips : true
		};

		this.$get = [ '$jqCommon', '$jqDataAdapter', function jqGridService($jqCommon, $jqDataAdapter) {
			var service = {
				create : function(element, scope, params) {
					if (!scope[params.data]) {
						throw new Error("undefined data in scope: " + params.data);
					}
					var source = {
						localdata : scope[params.data],
						datatype : "json",
						datafields : params.columns
					};
					var dataAdapter = $jqDataAdapter.get(source);
					var settings = angular.extend({}, $jqCommon.options(), options, params.options, {
						columns : params.columns,
						source : dataAdapter
					});
					element.jqxGrid(settings);
				}
			};
			return service;
		} ];
	});

	module.directive('jqGrid', [ '$compile', '$jqCommon', '$jqGrid', function JqGridDirective($compile, $jqCommon, $jqGrid) {
		return {
			restrict : 'AE',
			replace : true,
			scope : true,
			compile : function() {
				return {
					pre : function($scope, iElement, iAttrs) {
						var getParams = function() {
							return $jqCommon.getParams($scope.$eval(iAttrs.jqGrid), [ 'data', 'columns' ], [ 'options', 'events' ]);
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
						$jqGrid.create(iElement, $scope, params);

						$scope.$parent.$watch(iAttrs.jqGrid, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							params = getParams();
							bindEvents(params);
							$jqGrid.create(iElement, $scope, params);
						});

						$scope.$on('jqGrid-new-data', function() {
							$jqGrid.create(iElement, $scope, params);
						});

						$scope.$parent.$watch(params.data, function(newValue, oldValue) {
							if (angular.equals(newValue, oldValue)) {
								return;
							}
							iElement.jqxGrid('updatebounddata');
						}, true);
					}
				};
			}
		};
	} ]);

}(window, jQuery));