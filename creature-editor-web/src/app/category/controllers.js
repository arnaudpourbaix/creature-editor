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

	module.controller('CategoryListController', [ '$scope', '$translate', '$state', 'crudListMethods', '$q', '$alertMessage', 'Category',
			function CategoryListController($scope, $translate, $state, crudListMethods, $q, $alertMessage, Category) {
				angular.extend($scope, crudListMethods('/categories'));

				$scope.addCategory = function(category) {
					$scope.category = category;
					$state.go('categories.list.edit', {
						categoryId : 'new'
					});
				};

				$scope.editCategory = function(category) {
					$state.go('categories.list.edit', {
						categoryId : category.id
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
								allowDrop : true
							},
							buttons : {
								selector : '#treeButtons',
								add : $scope.addCategory,
								expandCollapse : true
							},
							events : {
								//itemClick : $scope.editCategory,
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

	module.controller('CategoryEditController', [ '$scope', '$state', 'category', function CategoryEditController($scope, $state, category) {
		$scope.category = category;

		$scope.onCancel = function() {
			$state.go('^');
		};
		
		$scope.onSave = function(category) {
		};

		$scope.onSaveError = function(category) {
		};

		$scope.onRemove = function(category) {
		};

		$scope.onRemoveError = function(category) {
		};
	} ]);

})();
