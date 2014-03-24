/* global _ */
(function(_) {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.services', 'crud', 'alert-message', 'translate-wrapper' ]);

	module.controller('CategoryListController', [ '$scope', '$translateWrapper', '$state', '$alertMessage', 'Category', 'CategoryService', 'categories',
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

				$scope.selectedCategory = function(params) {
					var result;
					if (params.categoryId && params.categoryId !== 'new') {
						result = CategoryService.getById($scope.categories, params.categoryId);
					} else if (params.categoryParentId && params.categoryParentId !== 'root') {
						result = CategoryService.getById($scope.categories, params.categoryParentId);
					}
					return result;
				};

				$scope.addCategory = function(category) {
					$state.go('categories.edit', {
						categoryParentId : category ? category.id : 'root',
						categoryId : 'new'
					});
				};

				$scope.editCategory = function(category) {
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
					$translateWrapper([ 'CATEGORY.CONTEXTUAL.ADD', 'CATEGORY.CONTEXTUAL.EDIT', 'CATEGORY.CONTEXTUAL.DELETE' ]).then(function(labels) {
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
										label : labels['CATEGORY.CONTEXTUAL.ADD'],
										action : 'addCategory'
									}, {
										label : labels['CATEGORY.CONTEXTUAL.EDIT'],
										action : 'editCategory'
									}, {
										label : labels['CATEGORY.CONTEXTUAL.DELETE'],
										action : 'removeCategory'
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
				$scope.create = $stateParams.categoryId === 'new';
				if ($scope.create) {
					var parent = CategoryService.getById($scope.categories, $stateParams.categoryParentId);
					$scope.category = CategoryService.getNew(parent);
				} else {
					$scope.category = angular.copy(CategoryService.getById($scope.categories, $stateParams.categoryId));
				}

				$scope.onCancel = function() {
					$state.go('^');
				};

				$scope.onSave = function() {
					if ($scope.create) {
						$scope.categories.push($scope.category);
					} else {
						var item = CategoryService.getById($scope.categories, $scope.category.id);
						angular.extend(item, $scope.category);
					}
					$state.go('^');
				};

				$scope.onSaveError = function(category) {
					$state.go('^');
				};

				$scope.onRemove = function() {
					var item = CategoryService.getById($scope.categories, $scope.category.id);
					$scope.categories.splice($scope.categories.indexOf(item), 1);
					$state.go('^');
				};

				$scope.onRemoveError = function(category) {
					$state.go('^');
				};
			} ]);

})(_);
