(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.directives', 'creatureEditor.category.services' ]);

	module.controller('CategoryController', [ '$scope', 'categories', function CategoryController($scope, categories) {
		$scope.categories = categories;

		$scope.splitter = {
			width : 1000,
			height : 600,
			panels : [ {
				size : 600,
				min : 200
			}, {
				size : 400,
				min : 100
			} ]
		};
	} ]);

	module.controller('CategoryListController', [ '$scope', '$translate', '$state', 'crudListMethods', '$q', '$alertMessage', 'Category', 'CategoryService','categories',
			function CategoryListController($scope, $translate, $state, crudListMethods, $q, $alertMessage, Category, CategoryService, categories) {
				angular.extend($scope, crudListMethods('/categories'));

				$scope.categories = categories;

				$scope.splitter = {
					width : 1000,
					height : 600,
					panels : [ {
						size : 600,
						min : 200
					}, {
						size : 400,
						min : 100
					} ]
				};
				
				$scope.addCategory = function(category) {
					$scope.category = CategoryService.new(category);
					$state.go('categories.edit', {
						categoryParentId : category ? category.id : 'root',
						categoryId : 'new'
					});
				};
				
				$scope.editCategory = function(category) {
					$scope.category = category;
					$state.go('categories.edit', {
						categoryParentId : category.parent ? category.parent.id : 'root',
						categoryId : category.id
					});
				};

				$scope.moveCategory = function(category, parent) {
					category.parent = parent;
					category.$save().then(function(response) {
//						$translate('CATEGORY.LABEL').then(function(label) {
//							$alertMessage.push('CRUD.SAVE_SUCCESS', 'success', {
//								entity : label,
//								name : category.name
//							});
//						});
					});
				};
				
				$scope.removeCategory = function(category) {
					category.$delete().then(function(response) {
						$translate('CATEGORY.LABEL').then(function(label) {
							$alertMessage.push('CRUD.REMOVE_SUCCESS', 'info', {
								entity : label,
								name : category.name
							});
						});
						Category.query().$promise.then(function(result) {
							$scope.categories = result;
						});
					}, function() {
						$alertMessage.push('CRUD.REMOVE_ERROR', 'danger', {
							name : category.name
						});
					});
				};

				var setCategoryTree = function() {
					$q.all([ $translate('CATEGORY.CONTEXTUAL.ADD'), $translate('CATEGORY.CONTEXTUAL.EDIT'), $translate('CATEGORY.CONTEXTUAL.DELETE') ]).then(function(labels) {
						$scope.categoryTree = {
							data : 'categories',
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
							options : {
								width : 580,
								allowDrag : true,
								allowDrop : true,
							},
							buttons : {
								selector : '#treeButtons',
								add : $scope.addCategory,
								expandCollapse : true
							},
							events : {
								itemClick : function(category) {
									console.log('itemClick', category);
									$scope.selectedCategory = category;
								},
								dragEnd: $scope.moveCategory,
								contextMenu : {
									domSelector : '#contextualMenu',
									options : {
										width : '200px',
										height : '90px'
									},
									items : [ {
										label : labels[0],
										action : $scope.addCategory
									}, {
										label : labels[1],
										action : $scope.editCategory
									}, {
										label : labels[2],
										action : $scope.removeCategory
									} ]
								}
							}
						};
					});
				};

				setCategoryTree();
				$scope.$onRootScope('$translateChangeSuccess', function() {
					setCategoryTree();
				});

			} ]);

	module.controller('CategoryEditController', [ '$scope', '$state', '$stateParams', function CategoryEditController($scope, $state, $stateParams) {
		if (!$scope.category) { // need to select in tree
			console.log('$stateParams', $stateParams);
		}
		console.log('CategoryEditController', $scope.category);

		$scope.onCancel = function() {
			$state.go('^');
		};
		
		$scope.onSave = function(category) {
			$state.go('^');
//			$state.go('^', {}, {
//				reload : true
//			});
		};

		$scope.onSaveError = function(category) {
			$state.go('^');
		};

		$scope.onRemove = function(category) {
			$state.go('^');
//			$state.go('^', {}, {
//				reload : true
//			});
		};

		$scope.onRemoveError = function(category) {
			$state.go('^');
		};
	} ]);

})();
