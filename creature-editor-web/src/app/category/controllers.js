(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.directives' ]);

	module.controller('CategoryListController', [ '$scope', '$translate', '$q', 'categories', function CategoryListController($scope, $translate, $q, categories) {
		$scope.categories = categories;

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
						domSelector: '#testMenu',
						options : {
							width : '200px',
							height : '60px'
						},
						items : [ {
							label : 'Add category',
							action : function($scope, category) {
								console.log('add category', category);
							}
						}, {
							label : 'Remove category',
							action : function($scope, category) {
								console.log('remove category', category);
							}
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
