angular.module('editor.category.config', [])

.config(function($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('app/category');
})

.config(function($stateProvider) {

	$stateProvider.state('category', {
		url : '/category',
		controller : 'CategoryListController',
		templateUrl : 'category/category-list.tpl.html',
		resolve : {
			categories : function(Category) {
				return Category.query().$promise;
			}
		}
	});

	$stateProvider.state('category.edit', {
		url : '/:categoryParentId/:categoryId',
		views : {
			'category-edit' : {
				controller : 'CategoryEditController',
				templateUrl : 'category/category-edit.tpl.html'
			}
		},
		resolve : {
			category : function($stateParams, CategoryService) {
				return CategoryService.get($stateParams.categoryParentId, $stateParams.categoryId);
			}
		}
	});

})

;
