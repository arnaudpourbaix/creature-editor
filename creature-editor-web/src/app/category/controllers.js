/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.directives', 'creatureEditor.category.services', 'translate-wrapper' ]);

	module.controller('CategoryListController', [
			'$scope',
			'$translateWrapper',
			'$state',
			'$alertMessage',
			'Category',
			'CategoryService',
			'categories',
			function CategoryListController($scope, $translateWrapper, $state, $alertMessage, Category, CategoryService, categories) {
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
				
				$scope.selectedItem = function(params) {
					if (params.categoryId && params.categoryId !== 'new') {
						//return params.categoryId;
						return CategoryService.getById($scope.categories, params.categoryId);
					} else if (params.categoryParentId && params.categoryParentId !== 'root') {
						//return params.categoryParentId;
						return CategoryService.getById($scope.categories, params.categoryParentId);
					}
					return null;
				};

				$scope.addCategory = function(category) {
					$scope.category = CategoryService.getNew(category);
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
					category.$save();
				};

				$scope.removeCategory = function(category) {
					category.$delete().then(function(response) {
						$translateWrapper('CATEGORY.LABEL').then(function(label) {
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
					$translateWrapper(['CATEGORY.CONTEXTUAL.ADD', 'CATEGORY.CONTEXTUAL.EDIT', 'CATEGORY.CONTEXTUAL.DELETE']).then(
							function(labels) {
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
										dragEnd : $scope.moveCategory,
										contextMenu : {
											domSelector : '#contextualMenu',
											options : {
												width : '200px',
												height : '90px'
											},
											items : [ {
												label : labels['CATEGORY.CONTEXTUAL.ADD'],
												action : $scope.addCategory
											}, {
												label : labels['CATEGORY.CONTEXTUAL.EDIT'],
												action : $scope.editCategory
											}, {
												label : labels['CATEGORY.CONTEXTUAL.DELETE'],
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

	module.controller('CategoryEditController', [ '$scope', '$state', '$stateParams', 'CategoryService',
			function CategoryEditController($scope, $state, $stateParams, CategoryService) {
				if (!$scope.category) {
					if ($stateParams.categoryId !== 'new') {
						$scope.$parent.category = CategoryService.getById($scope.categories, $stateParams.categoryId);
					} else {
						var parent = CategoryService.getById($scope.categories, $stateParams.categoryParentId);
						$scope.$parent.category = CategoryService.getNew(parent);
					}
				}

				$scope.onCancel = function() {
					$state.go('^');
				};

				$scope.onSave = function(category) {
					$state.go('^');
					// $state.go('^', {}, {
					// reload : true
					// });
				};

				$scope.onSaveError = function(category) {
					$state.go('^');
				};

				$scope.onRemove = function(category) {
					$state.go('^');
					// $state.go('^', {}, {
					// reload : true
					// });
				};

				$scope.onRemoveError = function(category) {
					$state.go('^');
				};
			} ]);

})(_);
