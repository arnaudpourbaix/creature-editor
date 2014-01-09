(function() {
	'use strict';

	var category = angular.module('creatureEditor.category', [ 'creatureEditor.category.services', 'creatureEditor.category.directives',
			'creatureEditor.category.controllers', 'ui.router', 'ngRoute' ]);

	category.config(function config($stateProvider) {
		$stateProvider.state('categories', {
			url : '/categories',
			resolve : {
				categories : [ 'Category', function(Category) {
					return Category.query().$promise;
				} ]
			},
			controller : 'CategoryListController',
			templateUrl : 'category/category-list.tpl.html'
		});

	});

})();
