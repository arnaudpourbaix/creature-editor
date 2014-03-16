(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.config', [ 'creatureEditor.category.services', 'ui.router' ]);

	module.config([ '$translatePartialLoaderProvider', function run($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('app/category');
	} ]);

	module.config(function config($stateProvider) {

		$stateProvider.state('categories', {
			abstract : true,
			url : '/categories',
			controller : 'CategoryController',
			templateUrl : 'category/category.tpl.html',
			resolve : {
				categories : [ 'Category', function(Category) {
					return Category.query().$promise;
				} ]
			}
		});

		$stateProvider.state('categories.list', {
			views : {
				'category-list' : {
					controller : 'CategoryListController',
					templateUrl : 'category/category-list.tpl.html'
				}
			},
			url : ''
		});

		$stateProvider.state('categories.edit', {
			url : '/:categoryId',
			views : {
				'category-list' : {
					controller : 'CategoryListController',
					templateUrl : 'category/category-list.tpl.html'
				},
				'category-edit' : {
					controller : 'CategoryEditController',
					templateUrl : 'category/category-edit.tpl.html',
					resolve : {
						category : [ 'Category', '$stateParams', function(Category, $stateParams) {
							if ($stateParams.categoryId !== 'new') {
								return Category.get({
									id : $stateParams.categoryId
								}).$promise;
							} else {
								return new Category({
									id : null,
									name : ''
								});
							}
						} ]
					}
				}
			}
		});
	});

})();
