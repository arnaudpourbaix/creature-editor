(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.directives', 'creatureEditor.category.services' ]);

	module.controller('CategoryListController', [ '$scope', '$translate', 'crudListMethods', '$q', '$alertMessage', 'categories', 'Category',
			function CategoryListController($scope, $translate, crudListMethods, $q, $alertMessage, categories, Category) {
				angular.extend($scope, crudListMethods('/categories'));

				$scope.categories = categories;

				$scope.addCategory = function(category) {
					console.log('add category', category);
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
							console.log(result);
							$scope.categories = result;
						});
					}, function() {
						$alertMessage.push('CRUD.REMOVE_ERROR', 'danger', {
							name : category.name
						});
					});
				};

				var setCategoryTree = function() {
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
							allowDrag : true,
							allowDrop : true
						},
						events : {
							itemClick : function($scope, category) {
								console.log(category);
							},
							contextMenu : {
								domSelector : '#contextualMenu',
								options : {
									width : '200px',
									height : '60px'
								},
								items : [ {
									label : 'Add category',
									action : $scope.addCategory
								}, {
									label : 'Remove category',
									action : $scope.removeCategory
								} ]
							}
						}
					};
				};

				setCategoryTree();
				$scope.$onRootScope('$translateChangeSuccess', function() {
					setCategoryTree();
				});

			} ]);

})();
