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
			add : 'add',
			expandCollapse : true
		},
		events : {
			dragEnd : 'move',
			select : 'edit',
			contextMenu : {
				items : [ {
					label : $translate.instant('CONTEXTUAL.ADD'),
					action : 'add'
				}, {
					label : $translate.instant('CONTEXTUAL.EDIT'),
					action : 'edit'
				}, {
					label : $translate.instant('CONTEXTUAL.DELETE'),
					action : 'remove'
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

	$scope.add = function(category) {
		$state.go('category.edit', {
			categoryParentId : category ? category.id : 'root',
			categoryId : 'new'
		});
	};

	$scope.edit = function(category) {
		$state.go('category.edit', {
			categoryParentId : category.parent ? category.parent.id : 'root',
			categoryId : category.id
		});
	};

	$scope.move = function(category, parent) {
		category.parent = parent;
		category.$save();
	};

	$scope.remove = function(category) {
		category.$delete().then(function(response) {
			var message = $translate.instant('CRUD.REMOVE_SUCCESS', {
				entity: $translate.instant('CATEGORY.LABEL'),
				name: category.name
			});
			toaster.pop('success', null, message);
			Category.query().$promise.then(function(categories) {
				$scope.categories = categories;
				$state.go('category'); // item could have been selected, opening edit state
			});
		}, function() {
			var message = $translate.instant('CRUD.REMOVE_ERROR', {
				entity: $translate.instant('CATEGORY.LABEL'),
				name: category.name
			});
			toaster.pop('danger', null, message);
		});
	};

})

.controller('CategoryEditController', function($scope, $state, $stateParams, CategoryService, category) {
	$scope.category = category;
	$scope.isNew = category.id == null; 
	
	$scope.edit = { model: 'category', entity: 'CATEGORY.LABEL', name: 'name' };
	
	$scope.onCancel = function() {
		$state.go('^');
	};
	
	$scope.onSave = function() {
		if ($scope.isNew) {
			$scope.categories.push($scope.category);
		} else {
			var item = CategoryService.getById($scope.categories, $scope.category.id);
			angular.extend(item, $scope.category);
		}
		$state.go('^');
	};

	$scope.onSaveError = function() {
		$state.go('^');
	};

	$scope.onRemove = function() {
		var item = CategoryService.getById($scope.categories, $scope.category.id);
		$scope.categories.splice($scope.categories.indexOf(item), 1);
		$state.go('^');
	};

	$scope.onRemoveError = function() {
		$state.go('^');
	};
})

;
