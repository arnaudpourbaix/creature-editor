(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.config', [ 'creatureEditor.category.services', 'ui.router' ]);

	module.config([ '$translatePartialLoaderProvider', function run($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('app/category');
	} ]);

	module.config(function config($stateProvider) {
		
	$stateProvider.state('categories', {
		url : '/categories',
		controller : 'CategoryListController',
		templateUrl : 'category/category-list.tpl.html',
		resolve : {
			categories : [ 'Category', function(Category) {
				return Category.query().$promise;
			} ]
		}
	});

	$stateProvider.state('categories.edit', {
		url : '/:categoryParentId/:categoryId',
		views : {
			'category-edit' : {
				controller : 'CategoryEditController',
				templateUrl : 'category/category-edit.tpl.html'
			}
		}
	});
		
	});

})();
