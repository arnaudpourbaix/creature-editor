(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.directives' ]);

	module.controller('CategoryListController', ['$scope', '$translate', '$q', 'categories', function CategoryListController($scope, $translate, $q, categories) {
		$scope.categories = categories;

		var setCategoryTree = function() {
			$q.all([ $translate('CATEGORY.FIELDS.NAME'), $translate('CATEGORY.COLUMNS.ACTION'), $translate('CATEGORY.FIELDS.COLUMNS.DELETE') ]).then(function(labels) {
				$scope.categoryTree = {
					data : 'categories',
					columns : [ {
						text : labels[0],
						dataField : 'resource',
						type : 'string',
						align : 'center',
						width : 80
					}, {
						text : labels[1],
						dataField : 'name',
						type : 'string',
						align : 'center',
						width : 200
					}, {
						text : labels[2],
						dataField : 'level',
						type : 'number',
						cellsalign : 'center',
						align : 'center',
						width : 55
					} ],
					options : {
						allowDrag : true,
						allowDrop : true
					},
					events : {
					}
				};
			});
		};
		
		setCategoryTree();
		$scope.$onRootScope('$translateChangeSuccess', function() {
			setCategoryTree();
		});
		

		// var source = {
		// datatype : "json",
		// datafields : [ {
		// name : 'id'
		// }, {
		// name : 'name'
		// }, {
		// name : 'parentId'
		// } ],
		// id : 'id',
		// localdata : categories
		// }, dataAdapter = new $.jqx.dataAdapter(source, {
		// autoBind : true
		// });
		//
		// $scope.categories = dataAdapter.getRecordsHierarchy('id', 'parentId', null, [ {
		// name : 'name',
		// map : 'label'
		// } ]);
		//
		// $scope.dragEnd = function(dragItem, dropItem, args, dropPosition, tree) {
		// console.debug('dragEnd', dragItem, dropItem);
		// };

	}]);

})();
