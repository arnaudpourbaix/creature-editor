var category = angular.module('creatureEditor.category', [ 'ui.router', 'ngRoute', 'ngResource' ]);

category.config(function config($stateProvider) {
	'use strict';
	$stateProvider.state('category.list', {
		url : '/category',
		views : {
			"main" : {
				controller : 'CategoryListController',
				templateUrl : 'category/category-list.tpl.html'
			}
		}
	});
});

category.factory('Category', function($resource) {
	'use strict';
	return $resource('rest/category/:id', {}, {
		'save' : {
			method : 'PUT'
		}
	});
});

category.directive('jqxtree', function() {
	'use strict';
	return {
		restrict : 'A',
		scope : {
			data : '=jqxtree'
		},
		link : function(scope, element, attrs) {
			scope.$watch('data', function() {
				$(element).jqxTree({
					source : scope.data,
					allowDrag : true,
					allowDrop : true,
					dragEnd : function() {
					}
				});
			}, true);
		}

	};
});

category.controller('CategoryListController', function CategoryListController($scope, $location, Category) {
	'use strict';
	$scope.categories = Category.query();
	/*
	 * Category.query().$promise.then(function(res) { var list = res, source = { datatype : "json", datafields : [ { name : 'id' }, { name : 'name' }, { name :
	 * 'parentId' } ], id : 'id', localdata : list }, dataAdapter = new $.jqx.dataAdapter(source, { autoBind : true }); $scope.categories =
	 * dataAdapter.getRecordsHierarchy('id', 'parentId', null, [ { name : 'name', map : 'label' } ]); }); $scope.dragEnd = function(dragItem, dropItem, args,
	 * dropPosition, tree) { console.debug('dragEnd', dragItem, dropItem); };
	 */

	$scope.gotoCategoryNewPage = function() {
		$location.path("/category/new");
	};
	$scope.deleteCategory = function(category) {
		category.$delete({
			'id' : category.id
		}, function() {
			$location.path('/');
		});
	};
});
