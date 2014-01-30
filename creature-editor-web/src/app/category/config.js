(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.config', [ 'creatureEditor.category.services', 'ui.router' ]);

	module.run(function run(alertMessageService) {
		alertMessageService.addConfig({});
	});

	module.config(function config($stateProvider) {
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
