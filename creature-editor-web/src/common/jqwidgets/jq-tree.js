angular.module('apx-jqwidgets.tree', [])

/**
 * @ngdoc service
 * @name apx-jqwidgets.jqTreeProvider
 * @description
 * Use `jqTreeProvider` to change the default behavior of the {@link  apx-jqwidgets.jqTree jqTree} service.
 */
.provider('jqTree', function() {
	/**
	 * @ngdoc property	
	 * @name apx-jqwidgets.jqTreeProvider#defaults
	 * @propertyOf apx-jqwidgets.jqTreeProvider
	 * @description Object containing default values for {@link apx-jqwidgets.jqTree jqTree}. The object has following properties:
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
	 * @name apx-jqwidgets.jqTree
     * @requires apx-jqwidgets.jqCommon
     * @requires apx-jqwidgets.jqDataAdapter
     * @requires $compile
     * @requires $timeout
     * @requires $translate
	 * @description
	 * # jqTree
	 * This service is a wrapper for tree widget.<br>
	 */
	this.$get = function(jqCommon, jqDataAdapter, $compile, $timeout, $translate) {
		/**
		 * @description Returns entity's parent entity.
		 * @param {Object} entity Entity to get parents from.
		 * @param {Object|array} entities Datasource entities.
		 * @param {Object} params Tree parameters. See {@link apx-jqwidgets.directive:jqTree jqTree}
		 * @returns {Object} Parent's entity.
		 */
		var getParent = function(entity, entities, params) {
			if (!entity) {
				return null;
			}
			var parentId = entity[params.parent];
			if (angular.isUndefined(parentId)) { // should be undefined if parent is a property of a child
				parentId = jqDataAdapter.getEntityParentId(entity, params.datafields, params.parent);
			}
			var parent = _.find(entities, function(entity) {
				return entity[params.id] === parentId;
			});
			return parent;
		};

		/**
		 * @description Returns all parents up to root from an entity.
		 * @param {Object} entity Entity to get parents from.
		 * @param {Object|array} entities Datasource entities.
		 * @param {Object} params Tree parameters. See {@link apx-jqwidgets.directive:jqTree jqTree}
		 * @param {Object=} parents Accumulated parents (will be concatenated with entity parents).
		 * @returns {array} Parents.
		 */
		var getParents = function(entity, entities, params, parents) {
			var parent = getParent(entity, entities, params);
			if (!parent) {
				return [];
			}
			var result = [ parent ].concat(getParents(parent, entities, params, parents));
			return result;
		};

		var service = {};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTree#create
		 * @methodOf apx-jqwidgets.jqTree
		 * @description Returns a new data-adapter.
		 * @param {Object} element Tree DOM element.
		 * @param {Object} $scope Controller's scope.
		 * @param {Object} params Tree parameters. See {@link apx-jqwidgets.directive:jqTree jqTree}
		 * @param {Object=} selectedEntity Must be an entity present in datasource. Select it in tree and expand its parents.  
		 * @param {string=} filterText Tree nodes will be filtered by this parameter.
		 */
		service.create = function(element, $scope, params, selectedEntity, filterText) {
			if (!$scope[params.datasource]) {
				throw new Error("undefined data in scope: " + params.datasource);
			}
			var items = service.filterEntities($scope[params.datasource], filterText, params);
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
							$scope.$eval(params.events.dragEnd)(dragEntity, dropEntity);
							element.jqxTree('expandItem', dropItem);
						});
					}
				});
			}
			element.jqxTree(settings);
			if (selectedEntity) {
				$timeout(function() {
					var item = service.getItem(element, selectedEntity[params.display]);
					if (item) {
						element.jqxTree('selectItem', item);
						element.jqxTree('expandItem', item.parentElement);
					}
				});
			}
			if (filterText) {
				element.jqxTree('expandAll');
			}
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTree#addButtons
		 * @methodOf apx-jqwidgets.jqTree
		 * @description Add defined buttons within tree template.
		 * @param {Object} element Buttons DOM element.
		 * @param {Object} $scope Controller's scope.
		 * @param {Object} settings Buttons settings. See {@link apx-jqwidgets.directive:jqTree jqTree, buttons property}.
		 */
		service.addButtons = function(element, $scope, settings) {
			$scope.buttons = settings;
			jqCommon.getView({ templateUrl: 'jqwidgets/tree/jqtree-buttons.tpl.html'}, null, null, $scope).then(function(view) {
				element.html(view);
			});
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTree#addFilter
		 * @methodOf apx-jqwidgets.jqTree
		 * @description Add filter text field within tree template.
		 * @param {Object} element Filter DOM element.
		 * @param {Object} $scope Controller's scope.
		 */
		service.addFilter = function(element, $scope) {
			jqCommon.getView({ templateUrl: 'jqwidgets/tree/jqtree-filter.tpl.html'}, null, null, $scope).then(function(view) {
				element.html(view);
			});
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTree#getEntity
		 * @methodOf apx-jqwidgets.jqTree
		 * @description Returns an entity from a jqxTree item.
		 * @param {Object|array} entities Datasource entities.
		 * @param {Object} item jqxTree item, can be obtained with getSelectedItem method: <pre>element.jqxTree('getSelectedItem')</pre>.
		 * @param {string} propertyId Entity id property.
		 * @returns {Object} Entity.
		 */
		service.getEntity = function(entities, item, propertyId) {
			var entity = _.find(entities, function(entity) { /* jshint -W116 */
				return entity[propertyId] == item.id;
			});
			return entity;
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTree#getItem
		 * @methodOf apx-jqwidgets.jqTree
		 * @description Returns a jqxTree item from a tree item label.
		 * @param {Object} element Tree DOM element.
		 * @param {string} label Tree item label.
		 * @returns {Object} jqxTree item.
		 */
		service.getItem = function(element, label) {
			var items = element.jqxTree('getItems');
			return _.find(items, function(item) { /* jshint -W116 */
				return item.label == label;
			});
		};
		
		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqTree#filterEntities
		 * @methodOf apx-jqwidgets.jqTree
		 * @description Filter and return entities. Filtering is case insensitive and will match any label containing filter text.
		 * @param {Object|array} entities Datasource entities.
		 * @param {string} filterText Filter text.
		 * @param {Object} params Tree parameters. See {@link apx-jqwidgets.directive:jqTree jqTree}
		 * @returns {array} Filtered entities.
		 */
		service.filterEntities = function(entities, filterText, params) {
			if (!filterText) {
				return entities;
			}
			var result = _.filter(entities, function(entity) {
				return _.contains(entity[params.display].toUpperCase(), filterText.toUpperCase());
			});
			var parents = [];
			angular.forEach(result, function(entity) {
				parents = parents.concat(getParents(entity, entities, params));
			});
			result = _.uniq(result.concat(parents));
			return result;
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
 * Directive attribute's value is an object that has following properties:
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
 * - **selectItem** - `{string=}` - Must be a method within scope. It will be called with 1 parameter: selected entity<br>
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
 * 
 * HTML attributes:
 * 
 * - **jq-selected-item** - `{string=}` - Object or function that will be evaluated to get a datasource item. This item will be selected after tree creation.<br>
 * 
 * # Example:
 * 
 * <pre>
 * <div data-jq-tree="categoryTree"></div>
 * </pre>
 * 
 * <pre>
 * 	$scope.categoryTree = {
		datasource : 'categories',
		datafields : [ {
			name : 'id',
			type : 'number'
		}, {
			name : 'name',
			type : 'string'
		}, {
			name : 'parentId',
			map : 'parent.id',
			type : 'number'
		} ],
		id : 'id',
		parent : 'parentId',
		display : 'name',
		settings : {
			width : 580,
			allowDrag : true,
			allowDrop : true,
		},
		filter : true,
		buttons : {
			add : 'addCategory',
			expandCollapse : true
		},
		events : {
			dragEnd : 'moveCategory',
			contextMenu : {
				options : {
					width : '200px',
					height : '90px'
				},
				items : [ {
					label : $translate.instant('CATEGORY.CONTEXTUAL.ADD'),
					action : 'addCategory'
				}, {
					label : $translate.instant('CATEGORY.CONTEXTUAL.EDIT'),
					action : 'editCategory'
				}, {
					label : $translate.instant('CATEGORY.CONTEXTUAL.DELETE'),
					action : 'removeCategory'
				} ]
			}
		}
	};
 * </pre>
 */
.directive('jqTree', function($compile, $timeout, jqCommon, jqTree, jqMenu) {
	return {
		restrict : 'A',
		templateUrl: 'jqwidgets/tree/jqtree.tpl.html',
		replace : true,
		scope : true,
		compile : function() {
			return {
				post : function($scope, element, attrs) {
					var getParams = function() {
						return jqCommon.getParams($scope.$eval(attrs.jqTree), [ 'datasource', 'datafields', 'id', 'parent', 'display' ], 
								[ 'settings', 'events', 'buttons', 'filter' ]);
					};
					var getSelectedEntity = function() {
						var selectedItem = $scope.tree.jqxTree('getSelectedItem');
						if (!selectedItem) {
							return null;
						}
						return jqTree.getEntity($scope[params.datasource], selectedItem, params.id);
					};
					var bindEvents = function() {
						$scope.tree.off();
						if (angular.isString(params.events.selectItem)) {
							$scope.tree.on('select', function(event) {
								var entity = getSelectedEntity();
								$scope.$eval(params.events.selectItem)(entity);
							});
						}
						if (angular.isObject(params.events.contextMenu)) {
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
							jqMenu.getContextual(params.events.contextMenu, $scope, getSelectedEntity).then(function(result) {
								$scope.contextualMenu = result;
							});
						}
					};
					var params = getParams($scope, attrs);
					bindEvents($scope, params);
					jqTree.create($scope.tree, $scope, params, $scope.$eval(attrs.jqSelectedItem));
					if (params.buttons) {
						jqTree.addButtons($scope.buttons, $scope, params.buttons);
					}
					if (params.filter) {
						jqTree.addFilter($scope.filter, $scope, params.filter);
					}
					
					$scope.$on('$destroy', function() {
						$scope.tree.jqxTree('destroy');
					});

					$scope.$parent.$watch(params.datasource, function(newValue, oldValue) {
						if (angular.equals(newValue, oldValue)) {
							return;
						}
						var selectedEntity = getSelectedEntity();
						jqTree.create($scope.tree, $scope, params, selectedEntity);
					}, true);

					$scope.add = function() {
						$scope.$eval(params.buttons.add)(getSelectedEntity());
					};

					$scope.expand = function() {
						$scope.tree.jqxTree('expandAll');
					};

					$scope.collapse = function() {
						$scope.tree.jqxTree('collapseAll');
					};

					$scope.$watch('searchFilter', function(newValue, oldValue) {
						if (newValue === oldValue) {
							return;
						}
						var selectedItem = $scope.$eval(attrs.jqSelectedItem) || getSelectedEntity();
						$scope.tree.jqxTree('clear');
						jqTree.create($scope.tree, $scope, params, selectedItem, newValue);
					});

				}
			};
		}
	};
})

;