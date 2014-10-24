angular.module('apx-jqwidgets.grid', [])

.provider('$jqGrid', function JqGridProvider() {
	var options = {
		altRows : true,
		columnsResize : true,
		sortable : true,
		showfilterrow : true,
		filterable : true,
		selectionMode : 'singlerow',
		pagermode : 'simple',
		pagerButtonsCount : 10,
		pageSize : 20,
		enabletooltips : true
	};

	this.$get = function jqGridService($jqCommon, $jqDataAdapter, $compile) {
		var service = {};

		service.create = function(element, scope, params) {
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
		};

		service.addButtons = function(element, scope, settings) {
			var template = '';
			if (settings.add) {
				template += '<button type="button" data-ng-click="add()" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-plus" />&nbsp;{{ \'JQWIDGETS.GRID.ADD\' | translate }}</button>';
			}
			var html = $compile(template)(scope);
			element.html(html);
		};

		return service;
	};
})

.directive('jqGrid', function JqGridDirective($compile, $jqCommon, $jqGrid) {
	return {
		restrict : 'AE',
		template : '<div class="jq-grid"><div data-ng-scope-element="contextualMenu" class="jq-contextual-menu"></div><div data-ng-show="showHeader()" class="jq-grid-header"><div data-ng-scope-element="buttons" class="jq-grid-buttons"></div></div><div data-ng-scope-element="grid" class="jq-grid-body"></div></div>',
		replace : true,
		scope : true,
		compile : function() {
			return {
				post : function($scope, iElement, iAttrs) {
					var getParams = function() {
						return $jqCommon.getParams($scope.$eval(iAttrs.jqGrid), [ 'data', 'columns' ], [ 'options', 'events', 'contextMenu', 'buttons', 'filter' ]);
					};
					var bindEvents = function(params) {
						$scope.grid.off();
						if (params.events.rowClick) {
							$scope.grid.on('rowclick', function(event) {
								event.stopPropagation();
								$scope.$apply(function() {
									var item = $scope[params.data][event.args.rowindex];
									params.events.rowClick($scope, item);
								});
							});
						}
						if (params.events.cellClick) {
							$scope.grid.on("cellclick", function(event) {
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
					$jqGrid.create($scope.grid, $scope, params);
					if (params.buttons) {
						$jqGrid.addButtons($scope.buttons, $scope, params.buttons);
					}
					
					$scope.showHeader = function() {
						return params.buttons;
					};

					$scope.add = function() {
						$scope.$eval(params.buttons.add)();
					};

					$scope.$parent.$watch(iAttrs.jqGrid, function(newValue, oldValue) {
						if (newValue === oldValue) {
							return;
						}
						params = getParams();
						bindEvents(params);
						$jqGrid.create($scope.grid, $scope, params);
					});

					$scope.$on('jqGrid-new-data', function() {
						$jqGrid.create($scope.grid, $scope, params);
					});

					$scope.$parent.$watch(params.data, function(newValue, oldValue) {
						if (angular.equals(newValue, oldValue)) {
							return;
						}
						$scope.grid.jqxGrid('updatebounddata');
					}, true);
				}
			};
		}
	};
})

;