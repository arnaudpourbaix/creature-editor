(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.directives' ]);

	module.controller('CategoryListController', function CategoryListController($scope, $location, categories) {
		var source = {
			datatype : "json",
			datafields : [ {
				name : 'id'
			}, {
				name : 'name'
			}, {
				name : 'parentId'
			} ],
			id : 'id',
			localdata : categories
		}, dataAdapter = new $.jqx.dataAdapter(source, {
			autoBind : true
		});

		$scope.categories = dataAdapter.getRecordsHierarchy('id', 'parentId', null, [ {
			name : 'name',
			map : 'label'
		} ]);

		$scope.dragEnd = function(dragItem, dropItem, args, dropPosition, tree) {
			console.debug('dragEnd', dragItem, dropItem);
		};

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

})();
