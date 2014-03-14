(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.controllers', [ 'creatureEditor.category.directives', 'creatureEditor.category.services' ]);

	module.controller('CategoryListController', [ '$scope', '$translate', 'crudListMethods', '$q', '$alertMessage', 'categories', function CategoryListController($scope, $translate, crudListMethods, $q, $alertMessage, categories) {
		angular.extend($scope, crudListMethods('/categories'));
		
		//$scope.categories = categories;
		
		$scope.categories = [{"id":3,"parent":{"id":1,"parent":null,"name":"Groups","creatures":null,"children":null},"name":"Amazons","creatures":null,"children":null},{"id":6,"parent":{"id":5,"parent":null,"name":"Monster","creatures":null,"children":null},"name":"Bear","creatures":null,"children":null},{"id":7,"parent":{"id":6,"parent":{"id":5,"parent":null,"name":"Monster","creatures":null,"children":null},"name":"Bear","creatures":null,"children":null},"name":"Black bear","creatures":null,"children":null},{"id":8,"parent":{"id":6,"parent":{"id":5,"parent":null,"name":"Monster","creatures":null,"children":null},"name":"Bear","creatures":null,"children":null},"name":"Brown bear","creatures":null,"children":null},{"id":10,"parent":{"id":5,"parent":null,"name":"Monster","creatures":null,"children":null},"name":"Cat","creatures":null,"children":null},{"id":1,"parent":null,"name":"Groups","creatures":null,"children":null},{"id":11,"parent":{"id":10,"parent":{"id":5,"parent":null,"name":"Monster","creatures":null,"children":null},"name":"Cat","creatures":null,"children":null},"name":"Jaguar","creatures":null,"children":null},{"id":12,"parent":{"id":4,"parent":{"id":1,"parent":null,"name":"Groups","creatures":null,"children":null},"name":"Mines","creatures":null,"children":null},"name":"Mercenary 1","creatures":null,"children":null},{"id":4,"parent":{"id":1,"parent":null,"name":"Groups","creatures":null,"children":null},"name":"Mines","creatures":null,"children":null},{"id":5,"parent":null,"name":"Monster","creatures":null,"children":null},{"id":9,"parent":{"id":6,"parent":{"id":5,"parent":null,"name":"Monster","creatures":null,"children":null},"name":"Bear","creatures":null,"children":null},"name":"Polar bear","creatures":null,"children":null}];
		var dataAdapter = new $.jqx.dataAdapter({
			localdata : $scope.categories,
			datafields : [ {
				name : 'id',
				type : 'number'
			}, {
				name : 'name',
				type : 'string'
			}, {
				name : 'parentId',
				map : 'parent>id',
				type : 'number'
			} ],
			id : 'id',
		}, { /*autoBind: true*/ });
		
		var data = dataAdapter.getRecordsHierarchy('id', 'parentId', null, [ {
			name : 'name',
			map : 'label'
		} ]);

		$scope.addCategory = function(category) {
			console.log('add category', category);
		};

		$scope.removeCategory = function(category) {
			console.log('remove category', category);
			category.$delete().then(function(response) {
				
				$translate('CATEGORY.LABEL').then(function (label) {
					$alertMessage.push('CRUD.REMOVE_SUCCESS', 'info', {
						entity : label,
						name : category.name
					});
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
					map : 'parent>id',
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
