angular.module('apx-jqwidgets.tree', [])

/**
 * @ngdoc service
 * @name apx-jqwidgets.jqTreeServiceProvider
 * @description
 * Use `jqTreeServiceProvider` to change the default behavior of the {@link  apx-jqwidgets.jqTreeService jqTreeService} service.
 */
.provider('jqTreeService', function() {
	/**
	 * @ngdoc property
	 * @name apx-jqwidgets.jqTreeServiceProvider#defaults
	 * @propertyOf apx-jqwidgets.jqTreeServiceProvider
	 * @description Object containing default values for {@link apx-jqwidgets.jqTreeService jqTreeService}. The object has following properties:
	 * 
	 * - **toggleMode** - `{string}` - User interaction used for expanding or collapsing any item ('click' or 'dblclick').<br>
	 * 
	 * @returns {Object} Default values object.
	 */		
	var defaults = this.defaults = {
		toggleMode : 'click'
	};

	/**
	 * @ngdoc service
	 * @name apx-jqwidgets.jqTreeService
     * @requires apx-jqwidgets.jqCommon
     * @requires apx-jqwidgets.jqDataAdapter
     * @requires $compile
     * @requires $timeout
     * @requires $translate
	 * @description
	 * # jqTreeService
	 * This service is a wrapper for tree widget.<br>
	 */
	this.$get = function(jqCommon, jqDataAdapter, $compile, $timeout, $translate) {
		/**
		 * @description Returns a new data-adapter.
		 * @param {Object} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @param {Object} scope Menu's scope.
		 * @param {Object} getEntity Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		var getParent = function(item, items, params) {
			if (!item) {
				return null;
			}
			var parentId = item[params.parent];
			if (angular.isUndefined(parentId)) {
				var field = _.find(params.datafields, function(datafield) {
					return datafield.name === params.parent;
				});
				var obj = item;
				angular.forEach(field.map.split("."), function(f) {
					if (obj) {
						obj = obj[f];
					}
				});
				parentId = obj;
			}
			var parent = _.find(items, function(item) {
				return item[params.id] === parentId;
			});
			return parent;
		};

		/**
		 * @description Returns a new data-adapter.
		 * @param {Object} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @param {Object} scope Menu's scope.
		 * @param {Object} getEntity Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		var retrieveParents = function(item, items, params, parents) {
			var parent = getParent(item, items, params);
			if (parent) {
				parents.push(parent);
				retrieveParents(parent, items, params, parents);
			}
		};

		var service = {};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTreeService#create
		 * @methodOf apx-jqwidgets.jqTreeService
		 * @description Returns a new data-adapter.
		 * @param {Object} element DOM element.
		 * @param {Object} scope Tree's scope.
		 * @param {Object} params Tree parameters. See {@link apx-jqwidgets.directive:jqTree jqTree}
		 * @param {Object=} selectedItem Select a node in tree and expand parents. It must be an item present in datasource.  
		 * @param {string=} filter Tree nodes will be filtered by this parameter.
		 */
		service.create = function(element, scope, params, selectedItem, filter) {
			if (!scope[params.datasource]) {
				throw new Error("undefined data in scope: " + params.datasource);
			}
			var items = service.getFilteredItems(filter, params, scope[params.datasource]);
			var source = angular.extend({
				datafields : params.datafields,
				datatype : "json",
				localdata : items,
				id : params.id
			});
			var dataAdapter = jqDataAdapter.getRecordsHierarchy(source, params.id, params.parent, params.display);
			var settings = angular.extend({}, jqCommon.defaults, defaults, params.settings, {
				source : dataAdapter
			});
			if (settings.allowDrop && !settings.dragEnd && angular.isString(params.events.dragEnd)) {
				angular.extend(settings, {
					dragEnd : function(item, dropItem) {
						var dragEntity = service.getEntity(items, item, params.id);
						var dropEntity = service.getEntity(items, dropItem, params.id);
						$timeout(function() {
							scope.$eval(params.events.dragEnd)(dragEntity, dropEntity);
							element.jqxTree('expandItem', dropItem);
						});
					}
				});
			}
			element.jqxTree(settings);
			if (selectedItem) {
				$timeout(function() {
					var item = service.getItem(element, selectedItem[params.display]);
					if (item) {
						element.jqxTree('selectItem', item);
						element.jqxTree('expandItem', item.parentElement);
					}
				});
			}
			if (filter) {
				element.jqxTree('expandAll');
			}
		};
		
		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTreeService#getFilteredItems
		 * @methodOf apx-jqwidgets.jqTreeService
		 * @description Returns a new data-adapter.
		 * @param {Object} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @param {Object} scope Menu's scope.
		 * @param {Object} getEntity Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		service.getFilteredItems = function(filter, params, items) {
			if (!filter) {
				return items;
			}
			var result = _.filter(items, function(item) {
				return _.contains(item[params.display].toUpperCase(), filter.toUpperCase());
			});
			var parents = [];
			angular.forEach(result, function(item) {
				retrieveParents(item, items, params, parents);
			});
			result = _.uniq(result.concat(parents));
			return result;
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTreeService#getEntity
		 * @methodOf apx-jqwidgets.jqTreeService
		 * @description Returns a new data-adapter.
		 * @param {Object} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @param {Object} scope Menu's scope.
		 * @param {Object} getEntity Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		service.getEntity = function(entities, item, idField) {
			var entity = _.find(entities, function(entity) { /* jshint -W116 */
				return entity[idField] == item.id;
			});
			return entity;
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTreeService#getItem
		 * @methodOf apx-jqwidgets.jqTreeService
		 * @description Returns a new data-adapter.
		 * @param {Object} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @param {Object} scope Menu's scope.
		 * @param {Object} getEntity Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		service.getItem = function(element, label) {
			var items = element.jqxTree('getItems');
			return _.find(items, function(item) { /* jshint -W116 */
				return item.label == label;
			});
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTreeService#addButtons
		 * @methodOf apx-jqwidgets.jqTreeService
		 * @description Returns a new data-adapter.
		 * @param {Object} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @param {Object} scope Menu's scope.
		 * @param {Object} getEntity Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		service.addButtons = function(element, scope, settings) {
			var template = '';
			if (settings.add) {
				template += '<button type="button" data-ng-click="add()" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-plus" />&nbsp;{{ \'JQWIDGETS.TREE.ADD\' | translate }}</button>';
			}
			if (settings.expandCollapse) {
				template += '<button type="button" data-ng-click="collapse()" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-collapse-down" />&nbsp;{{ \'JQWIDGETS.TREE.COLLAPSE\' | translate }}</button>';
				template += '<button type="button" data-ng-click="expand()" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-expand" />&nbsp;{{ \'JQWIDGETS.TREE.EXPAND\' | translate }}</button>';
			}
			var html = $compile(template)(scope);
			element.html(html);
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTreeService#addFilter
		 * @methodOf apx-jqwidgets.jqTreeService
		 * @description Returns a new data-adapter.
		 * @param {Object} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @param {Object} scope Menu's scope.
		 * @param {Object} getEntity Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		service.addFilter = function(element, scope, settings) {
			var template = '';
			template += '<input class="form-control" type="text" placeholder="{{ \'JQWIDGETS.FILTER.SEARCH\' | translate }}" data-ng-model="searchFilter" data-ng-trim="true" />';
			var html = $compile(template)(scope);
			element.html(html);
		};

		return service;
	};
})


/**
 * @ngdoc directive
 * @name apx-jqwidgets.directive:jqTree
 * @restrict A
 * @priority 0
 * @description Apply tree widget on element.
 * The object has following properties:
 * 
 * - **datasource** - `{array|Object}` - Datasource, it can be an object or an array.<br>
 * - **datafields** - `{array}` - Datafields description.<br>
 * - **id**	- `{string}` - Datafield id property.<br>
 * - **parent**	- `{string}` - Datafield parent id property.<br>
 * - **display** - `{string}` - Datafield display property.<br>
 * - **settings** - `{Object=}` - Set of key/value pairs that configure the jqxTree plug-in. All settings are optional.<br>
 * - **events** - `{Object=}` - Bind events between widget and Angular's scope. See below.<br>
 * - **buttons** - `{Object=}` - Configure external buttons tied to widget. See below.<br>
 * - **filter** - `{boolean=}` - Add a filter text field.<br>
 * 
 * events property has following properties:
 * 
 * - **dragEnd** - `{string=}` - Must be a method within scope. It will be called with 2 parameters: drag entity, drop entity<br>
 * - **contextMenu** - `{Object=}` - Use this to set a contextual menu on items. See below.<br>
 * 
 * contextMenu property has following properties:
 * 
 * - **settings** - `{Object=}` - Set of key/value pairs that configure the jqxMenu plug-in. All settings are optional.<br>
 * - **items** - `{array}` - Contains a list of menu items. Each item has 2 properties: label {string} and action {string} (must be a method within scope).<br>
 * 
 * buttons property has following properties:
 * 
 * - **add** - `{string=}` - Add a button to add a new element in tree. Must be a method within scope.<br>
 * - **expandCollapse** - `{boolean=}` - Add expand All and Collapse All buttons.<br>
 */
.directive('jqTree', function($compile, $timeout, jqCommon, jqTreeService, jqMenu) {

	var getParams = function($scope, attrs) {
		return jqCommon.getParams($scope.$eval(attrs.jqTree), [ 'datasource', 'datafields', 'id', 'parent', 'display' ], 
				[ 'settings', 'events', 'buttons', 'filter' ]);
	};
	var getSelectedEntity = function($scope, params) {
		var selectedItem = $scope.tree.jqxTree('getSelectedItem');
		if (!selectedItem) {
			return null;
		}
		return jqTreeService.getEntity($scope[params.data], selectedItem, params.id);
	};
	var bindEvents = function($scope, params) {
		$scope.tree.off();
		if (angular.isString(params.events.itemClick)) {
			$scope.tree.on('select', function(event) {
				var entity = getSelectedEntity();
				$scope.$eval(params.events.itemClick)(entity);
			});
		}
		if (params.events.contextMenu) {
			$scope.tree.on('contextmenu', 'li', function(event) {
				// disable the default browser's context menu
				event.preventDefault();
				var target = angular.element(event.target).parents('li:first')[0];
				if (target == null) {
					throw new Error("Menu should have 'li' elements");
				}
				$scope.tree.jqxTree('selectItem', target);
				var posX = angular.element(window).scrollLeft() + parseInt(event.clientX) + 5;
				var posY = angular.element(window).scrollTop() + parseInt(event.clientY) + 5;
				$scope.contextualMenu.jqxMenu('open', posX, posY);
			});
			jqMenu.getContextual($scope.contextualMenu, params.events.contextMenu, $scope, getSelectedEntity).then(function(result) {
				$scope.contextualMenu = result;
			});
		}
	};
	
	return {
		restrict : 'A',
		template : '<div class="jq-tree"><div jq-scope-element="contextualMenu" class="jq-contextual-menu"></div>' + 
					'<div class="jq-tree-header"><div jq-scope-element="buttons" class="jq-tree-buttons"></div>' + 
					'<div jq-scope-element="filter" class="jq-tree-filter"></div></div><div jq-scope-element="tree" class="jq-tree-body"></div></div>',
		replace : true,
		scope : true,
		compile : function() {
			return {
				post : function($scope, element, attrs) {
					var params = getParams($scope, attrs);

					//bindEvents(params);
					jqTreeService.create($scope.tree, $scope, params, $scope.$eval(attrs.jqSelectedItem));
//					if (params.buttons) {
//						jqTreeService.addButtons($scope.buttons, $scope, params.buttons);
//					}
//					if (params.filter) {
//						jqTreeService.addFilter($scope.filter, $scope, params.filter);
//					}
//
//					$scope.$parent.$watch(attrs.jqTree, function(newValue, oldValue) {
//						if (angular.equals(newValue, oldValue)) {
//							return;
//						}
//						params = getParams();
//						bindEvents(params);
//						var selectedItem = $scope.$eval(attrs.jqSelectedItem) || getSelectedEntity();
//						jqTreeService.create($scope.tree, $scope, params, selectedItem);
//					});
//
//					$scope.$parent.$watch(params.data, function(newValue, oldValue) {
//						if (angular.equals(newValue, oldValue)) {
//							return;
//						}
//						var selectedItem = $scope.$eval(attrs.jqSelectedItem) || getSelectedEntity();
//						jqTreeService.create($scope.tree, $scope, params, selectedItem);
//					}, true);
//
//					$scope.add = function() {
//						$scope.$eval(params.buttons.add)();
//					};
//
//					$scope.expand = function() {
//						$scope.tree.jqxTree('expandAll');
//					};
//
//					$scope.collapse = function() {
//						$scope.tree.jqxTree('collapseAll');
//					};
//
//					$scope.$watch('searchFilter', function(newValue, oldValue) {
//						if (newValue === oldValue) {
//							return;
//						}
//						var selectedItem = $scope.$eval(attrs.jqSelectedItem) || getSelectedEntity();
//						$scope.tree.jqxTree('clear');
//						jqTreeService.create($scope.tree, $scope, params, selectedItem, newValue);
//					});

				}
			};
		}
	};
})

;