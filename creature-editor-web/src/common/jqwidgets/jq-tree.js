angular.module('apx-jqwidgets.tree', [])

.provider('$jqTree', function() {
	var options = {
		toggleMode : 'click'
	};

	this.$get = function jqTreeService($jqCommon, $jqDataAdapter, $compile, $translate, $timeout) {
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

		var retrieveParents = function(item, items, params, parents) {
			var parent = getParent(item, items, params);
			if (parent) {
				parents.push(parent);
				retrieveParents(parent, items, params, parents);
			}
		};

		var service = {};

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

		service.create = function(element, scope, params, selectedItem, filter) {
			if (!scope[params.data]) {
				throw new Error("undefined data in scope: " + params.data);
			}
			var items = service.getFilteredItems(filter, params, scope[params.data]);
			var source = angular.extend({
				datafields : params.datafields,
				datatype : "json",
				localdata : items,
				id : params.id
			});
			var dataAdapter = $jqDataAdapter.getRecordsHierarchy(source, params.id, params.parent, params.display);
			var settings = angular.extend({}, $jqCommon.options(), options, params.options, {
				source : dataAdapter
			});
			if (settings.allowDrop && !settings.dragEnd && angular.isString(params.events.dragEnd)) {
				angular.extend(settings, {
					dragEnd : function(item, dropItem) {
						var dragEntity = service.getEntity(items, item, params.id);
						var dropEntity = service.getEntity(items, dropItem, params.id);
						$timeout(function() {
							scope.$eval(params.events.dragEnd)(dragEntity, dropEntity, item, dropItem);
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

		service.getEntity = function(entities, item, idField) {
			var entity = _.find(entities, function(entity) { /* jshint -W116 */
				return entity[idField] == item.id;
			});
			return entity;
		};

		service.getItem = function(element, label) {
			var items = element.jqxTree('getItems');
			return _.find(items, function(item) { /* jshint -W116 */
				return item.label == label;
			});
		};

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

		service.addFilter = function(element, scope, settings) {
			var template = '';
			template += '<input class="form-control" type="text" placeholder="{{ \'JQWIDGETS.FILTER.SEARCH\' | translate }}" data-ng-model="searchFilter" data-ng-trim="true" />';
			var html = $compile(template)(scope);
			element.html(html);
		};

		return service;
	};
})

.directive('jqTree', function($compile, $timeout, $jqCommon, $jqTree, $jqMenu) {
	return {
		restrict : 'A',
		template : '<div class="jq-tree"><div data-ng-scope-element="contextualMenu" class="jq-contextual-menu"></div><div class="jq-tree-header"><div data-ng-scope-element="buttons" class="jq-tree-buttons"></div><div data-ng-scope-element="filter" class="jq-tree-filter"></div></div><div data-ng-scope-element="tree" class="jq-tree-body"></div></div>',
		replace : true,
		scope : true,
		compile : function() {
			return {
				post : function($scope, iElement, iAttrs) {
					var getParams = function() {
						return $jqCommon.getParams($scope.$eval(iAttrs.jqTree), [ 'data', 'datafields', 'id', 'parent', 'display' ], [ 'options', 'events',
								'contextMenu', 'buttons', 'filter' ]);
					};
					var getSelectedEntity = function() {
						var selectedItem = $scope.tree.jqxTree('getSelectedItem');
						if (!selectedItem) {
							return null;
						}
						return $jqTree.getEntity($scope[params.data], selectedItem, params.id);
					};
					var bindEvents = function(params) {
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
							$jqMenu.getContextual($scope.contextualMenu, params.events.contextMenu, $scope, getSelectedEntity).then(function(result) {
								$scope.contextualMenu = result;
							});
						}
					};

					var params = getParams();

					bindEvents(params);
					$jqTree.create($scope.tree, $scope, params, $scope.$eval(iAttrs.jqSelectedItem));
					if (params.buttons) {
						$jqTree.addButtons($scope.buttons, $scope, params.buttons);
					}
					if (params.filter) {
						$jqTree.addFilter($scope.filter, $scope, params.filter);
					}

					$scope.$parent.$watch(iAttrs.jqTree, function(newValue, oldValue) {
						if (angular.equals(newValue, oldValue)) {
							return;
						}
						params = getParams();
						bindEvents(params);
						var selectedItem = $scope.$eval(iAttrs.jqSelectedItem) || getSelectedEntity();
						$jqTree.create($scope.tree, $scope, params, selectedItem);
					});

					$scope.$parent.$watch(params.data, function(newValue, oldValue) {
						if (angular.equals(newValue, oldValue)) {
							return;
						}
						var selectedItem = $scope.$eval(iAttrs.jqSelectedItem) || getSelectedEntity();
						$jqTree.create($scope.tree, $scope, params, selectedItem);
					}, true);

					$scope.add = function() {
						$scope.$eval(params.buttons.add)();
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
						var selectedItem = $scope.$eval(iAttrs.jqSelectedItem) || getSelectedEntity();
						$scope.tree.jqxTree('clear');
						$jqTree.create($scope.tree, $scope, params, selectedItem, newValue);
					});

				}
			};
		}
	};
})

;