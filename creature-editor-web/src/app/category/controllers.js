angular.module('editor.category.controllers', [])

.controller('CategoryListController', function($scope, $translate, $state, toaster, Category, CategoryService, categories) {
	$scope.categories = categories;
	
	$scope.splitter = {
		width : 1000,
		height : 600,
		panels : [ {
			size : 600,
			min : 200
		}, {
			size : 400,
			min : 100
		} ]
	};
	
	$scope.categoryTree = {
		datasource : 'categories',
		datafields : [ {
			name : 'id',
			type : 'number'
		}, {
			name : 'name',
			type : 'string'
		}, {
			name : 'parentId',
			map : 'parent.id',
			type : 'number'
		} ],
		id : 'id',
		parent : 'parentId',
		display : 'name',
		settings : {
			width : 580,
			allowDrag : true,
			allowDrop : true,
		},
		filter : true,
		buttons : {
			add : 'addCategory',
			expandCollapse : true
		},
		events : {
			dragEnd : 'moveCategory',
			selectItem : 'editCategory',
			contextMenu : {
				options : {
					width : '200px',
					height : '90px'
				},
				items : [ {
					label : $translate.instant('CATEGORY.CONTEXTUAL.ADD'),
					action : 'addCategory'
				}, {
					label : $translate.instant('CATEGORY.CONTEXTUAL.EDIT'),
					action : 'editCategory'
				}, {
					label : $translate.instant('CATEGORY.CONTEXTUAL.DELETE'),
					action : 'removeCategory'
				} ]
			}
		}
	};
	
	$scope.selectedCategory = function(params) {
		var result;
		if (params.categoryId && params.categoryId !== 'new') {
			result = CategoryService.getById($scope.categories, params.categoryId);
		} else if (params.categoryParentId && params.categoryParentId !== 'root') {
			result = CategoryService.getById($scope.categories, params.categoryParentId);
		}
		return result;
	};

	$scope.addCategory = function(category) {
		$state.go('category.edit', {
			categoryParentId : category ? category.id : 'root',
			categoryId : 'new'
		});
	};

	$scope.editCategory = function(category) {
		$state.go('category.edit', {
			categoryParentId : category.parent ? category.parent.id : 'root',
			categoryId : category.id
		});
	};

	$scope.moveCategory = function(category, parent) {
		category.parent = parent;
		category.$save();
	};

	$scope.removeCategory = function(category) {
		category.$delete().then(function(response) {
//			$translateWrapper('CATEGORY.LABEL').then(function(label) {
//				$alertMessage.push('CRUD.REMOVE_SUCCESS', 'info', {
//					entity : label,
//					name : category.name
//				});
//			});
//			Category.query().$promise.then(function(result) {
//				$scope.categories = result;
//			});
		}, function() {
//			$alertMessage.push('CRUD.REMOVE_ERROR', 'danger', {
//				name : category.name
//			});
		});
	};

})

.controller('CategoryEditController', function($scope, $state, $stateParams, CategoryService, category) {
	$scope.category = category;
	
	$scope.edit = { model: 'category', entity: 'CATEGORY.LABEL', name: 'name' };
	
	$scope.onCancel = function() {
		//$scope.$dismiss();
		$state.go('^');
	};

	$scope.onSave = function() {
		$scope.$close();
	};

	$scope.onSaveError = function() {
		$scope.$dismiss();
	};
	
	$scope.onRemove = function() {
		$scope.$close();
	};

	$scope.onRemoveError = function() {
		$scope.$dismiss();
	};

//	$scope.onSave = function() {
//		if ($scope.create) {
//			$scope.categories.push($scope.category);
//		} else {
//			var item = CategoryService.getById($scope.categories, $scope.category.$id());
//			angular.extend(item, $scope.category);
//		}
//		$state.go('^');
//	};
//
//	$scope.onSaveError = function() {
//		$state.go('^');
//	};
//
//	$scope.onRemove = function() {
//		var item = CategoryService.getById($scope.categories, $scope.category.$id());
//		$scope.categories.splice($scope.categories.indexOf(item), 1);
//		$state.go('^');
//	};
//
//	$scope.onRemoveError = function() {
//		$state.go('^');
//	};
})

;
