angular.module('apx-jqwidgets.grid', [])

/**
 * @ngdoc service
 * @name apx-jqwidgets.jqGridProvider
 * @description
 * Use `jqGridProvider` to change the default behavior of the {@link  apx-jqwidgets.jqGrid jqGrid} service.
 */
.provider('jqGrid', function() {
	/**
	 * @ngdoc property	
	 * @name apx-jqwidgets.jqGridProvider#defaults
	 * @propertyOf apx-jqwidgets.jqGridProvider
	 * @description Object containing default values for {@link apx-jqwidgets.jqGrid jqGrid}. The object has following properties:
	 * 
	 * - **altRows** - `{boolean}` - Enables or disables the alternating rows.<br>
	 * - **columnsResize** - `{boolean}` - Enables or disables the columns resizing.<br>
	 * - **sortable** - `{boolean}` - The sortable property enables or disables the sorting feature.<br>
	 * - **showfilterrow** - `{boolean}` - Shows or hides the filter row.<br>
	 * - **filterable** - `{boolean}` - Enables or disables the Grid Filtering feature. When the value of this property is true, the Grid displays a filtering panel in the columns popup menus.<br>
	 * - **selectionMode** - `{string}` - Sets or gets the selection mode: none, singlerow, multiplerows, multiplerowsextended, singlecell, multiplecells, multiplecellsextended, multiplecellsadvanced, checkbox.<br>
	 * - **pagermode** - `{string}` - Sets or gets the rendering mode of the pager. Available values - "simple" and "default".<br>
	 * - **pagerButtonsCount** - `{number}` - Sets or gets the buttons displayed in the pager when the "pagermode" is set to "simple".<br>
	 * - **pageSize** - `{number}` - Sets or gets the number of visible rows per page when the Grid paging is enabled.<br>
	 * - **enabletooltips** - `{boolean}` - Enables or disables the grid tooltips.<br>
	 * 
	 * @returns {Object} Default values object.
	 */		
	var defaults = this.defaults = {
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

	/**
	 * @ngdoc service
	 * @name apx-jqwidgets.jqGrid
     * @requires $compile
     * @requires apx-jqwidgets.jqCommon
     * @requires apx-jqwidgets.jqDataAdapter
	 * @description
	 * # jqGrid
	 * This service is a wrapper for grid widget.<br>
	 */
	this.$get = function($compile, jqCommon, jqDataAdapter) {
		var service = {};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqGrid#create
		 * @methodOf apx-jqwidgets.jqGrid
		 * @description Create jqxGrid widget.
		 * @param {Object} element Grid DOM element.
		 * @param {Object} $scope Controller's scope.
		 * @param {Object} params Grid parameters. See {@link apx-jqwidgets.directive:jqGrid jqGrid}
		 */
		service.create = function(element, $scope, params) {
			if (!$scope[params.datasource]) {
				throw new Error("undefined data in scope: " + params.datasource);
			}
			var source = {
				datafields : params.columns,
				datatype : "json",
				localdata : $scope[params.datasource]
			};
			var dataAdapter = jqDataAdapter.get(source);
			var settings = angular.extend({}, jqCommon.defaults, defaults, params.settings, {
				columns : params.columns,
				source : dataAdapter
			});
			element.jqxGrid(settings);
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqGrid#addButtons
		 * @methodOf apx-jqwidgets.jqGrid
		 * @description Add defined buttons within grid template.
		 * @param {Object} element Buttons DOM element.
		 * @param {Object} $scope Controller's scope.
		 * @param {Object} settings Buttons settings. See {@link apx-jqwidgets.directive:jqGrid jqGrid, buttons property}.
		 */
		service.addButtons = function(element, $scope, settings) {
			$scope.buttons = settings;
			jqCommon.getView({ templateUrl: 'jqwidgets/grid/jqgrid-buttons.tpl.html'}, null, null, $scope).then(function(view) {
				element.html(view);
			});
		};

		return service;
	};
})


/**
 * @ngdoc directive
 * @name apx-jqwidgets.directive:jqGrid
 * @restrict A
 * @priority 0
 * @description Apply grid widget on element.
 * Directive attribute's value is an object that has following properties:
 * 
 * - **datasource** - `{array|Object}` - Datasource, it can be an object or an array.<br>
 * - **columns** - `{array}` - Columns description.<br>
 * - **settings** - `{Object=}` - Set of key/value pairs that configure the jqxGrid plug-in. All settings are optional.<br>
 * - **events** - `{Object=}` - Bind events between widget and Angular's scope. See below.<br>
 * - **buttons** - `{Object=}` - Configure external buttons tied to widget. See below.<br>
 * - **filter** - `{boolean=}` - Add a filter text field.<br>
 * 
 * events property has following properties:
 * 
 * - **rowclick** - `{string=}` - This event is triggered when a row is clicked. Must be a method within scope. It will be called with following parameters: entity, rowindex, visibleIndex, rightclick<br>
 * - **cellclick** - `{string=}` - This event is triggered when a cell is clicked. Must be a method within scope. It will be called with following parameters: entity, rowindex, visibleIndex, rightclick, columnindex, datafield, value<br>
 * - **contextMenu** - `{Object=}` - Use this to set a contextual menu on items. See below.<br>
 * 
 * contextMenu property has following properties:
 * 
 * - **settings** - `{Object=}` - Set of key/value pairs that configure the jqxMenu plug-in. All settings are optional.<br>
 * - **items** - `{array}` - Contains a list of menu items. Each item has 2 properties: label {string} and action {string} (must be a method within scope).<br>
 * 
 * buttons property has following properties:
 * 
 * - **add** - `{string=}` - Add a button to add a new element in grid. Must be a method within scope.<br>
 * 
 * HTML attributes:
 * 
 * - **jq-selected-item** - `{string=}` - Object or function that will be evaluated to get a datasource item. This item will be selected after grid creation.<br>
 * 
 * # Example:
 * 
 * <pre>
 * <div data-jq-grid="modGrid"></div>
 * </pre>
 * 
 */
.directive('jqGrid', function($compile, jqCommon, jqGrid, jqMenu) {
	return {
		restrict : 'AE',
		templateUrl: 'jqwidgets/grid/jqgrid.tpl.html',
		replace : true,
		scope : true,
		compile : function() {
			return {
				post : function($scope, element, attrs) {
					var getParams = function() {
						return jqCommon.getParams($scope.$eval(attrs.jqGrid), [ 'datasource', 'columns' ], [ 'settings', 'events', 'buttons', 'filter' ]);
					};
					var getSelectedEntity = function() {
						var rowIndex = $scope.grid.jqxGrid('getselectedrowindex');
						return $scope[params.datasource][rowIndex];
					};
					var bindEvents = function(params) {
						$scope.grid.off();
						if (angular.isString(params.events.rowclick)) {
							$scope.grid.on('rowclick', function(event) {
								event.stopPropagation();
								if (!event.args.rightclick) {
									$scope.$apply(function() {
										var item = $scope[params.datasource][event.args.rowindex];
										$scope.$eval(params.events.rowclick)(item);
									});
								}
							});
						}
						if (angular.isString(params.events.cellclick)) {
							$scope.grid.on("cellclick", function(event) {
								event.stopPropagation();
								if (!event.args.rightclick) {
									$scope.$apply(function() {
										var item = $scope[params.datasource][event.args.rowindex];
										$scope.$eval(params.events.cellclick)(item, event.args.columnindex);
									});
								}
							});
						}
						if (angular.isObject(params.events.contextMenu)) {
							$scope.grid.on('contextmenu', function(event) {
								return false;
							});
							$scope.grid.on('rowclick', function(event) {
								if (event.args.rightclick) {
									$scope.grid.jqxGrid('selectrow', event.args.rowindex);
									var posX = angular.element(window).scrollLeft() + parseInt(event.args.originalEvent.clientX) + 5;
									var posY = angular.element(window).scrollTop() + parseInt(event.args.originalEvent.clientY) + 5;
									$scope.contextualMenu.jqxMenu('open', posX, posY);
									return false;
								}
							});
							jqMenu.getContextual(params.events.contextMenu, $scope, getSelectedEntity).then(function(result) {
								$scope.contextualMenu = result;
							});
						}
					};

					var params = getParams();
					bindEvents(params);
					jqGrid.create($scope.grid, $scope, params);
					if (params.buttons) {
						jqGrid.addButtons($scope.buttons, $scope, params.buttons);
					}

					$scope.$on('$destroy', function() {
						$scope.grid.jqxGrid('destroy');
					});
					
					$scope.showHeader = function() {
						return params.buttons;
					};

					$scope.add = function() {
						$scope.$parent.$eval(params.buttons.add)();
					};

					$scope.$parent.$watch(params.datasource, function(newValue, oldValue) {
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