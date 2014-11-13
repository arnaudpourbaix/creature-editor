angular.module('editor.mod.controllers', [])

.controller('ModListController', function($scope, $translate, $state, $interpolate, toaster, Mod, ModService, mods) {
	$scope.mods = mods;

	$scope.splitter = {
			width : 800,
			height : 600,
			panels : [ {
				size : 400,
				min : 200
			}, {
				size : 400,
				min : 100
			} ]
	};
	
	$scope.modGrid = {
			datasource : 'mods',
			columns : [ {
				text : $translate.instant('MOD.FIELDS.NAME'),
				datafield : 'name',
				type : 'string',
				align : 'center',
				width : 200
			} ],
			options : {
				width : 260,
				height : 400
			},
			buttons : {
				add : 'add'
			},
			events : {
				contextMenu : {
					options : {
						width : '200px',
						height : '90px'
					},
					items : [ {
						label : $translate.instant('CONTEXTUAL.EDIT'),
						action : 'edit'
					}, {
						label : $translate.instant('CONTEXTUAL.DELETE'),
						action : 'remove'
					} ]
				}
			}
	};
	
	$scope.add = function() {
		$state.go('mod.edit', {	id : 'new' });
	};
	
	$scope.edit = function(mod) {
		console.log('edit', mod);
//		$state.go('category.edit', {
//			categoryParentId : category.parent ? category.parent.id : 'root',
//			categoryId : category.id
//		});
	};
	
	$scope.remove = function(id) {
		var mod = ModService.getById(mods, id);
		$scope.removeFromList(mods, 'id', mod.$id());
		mod.$delete().then(function(response) {
			var message = $translate.instant('CRUD.REMOVE_SUCCESS', {
				entity: $translate.instant('MOD.LABEL'),
				name: mod.name
			});
			toaster.pop('info', null, message);
			$scope.removeFromList(mods, 'id', mod.$id());
		}, function() {
			var message = $translate.instant('CRUD.REMOVE_ERROR', {
				entity: $translate.instant('MOD.LABEL'),
				name: mod.name
			});		
			toaster.pop('error', null, message);
		});
	};

})


.controller('ModEditController', function($scope, $state, ModService, mod) {
	$scope.mod = mod;
	$scope.isNew = mod.id == null; 
	
	$scope.edit = { model: 'mod', entity: 'MOD.LABEL', name: 'name' };

	$scope.onCancel = function() {
		$state.go('^');
	};
	
	$scope.onSave = function() {
		if ($scope.isNew) {
			$scope.mods.push($scope.mod);
		} else {
			var item = ModService.getById($scope.mods, $scope.mod.$id());
			angular.extend(item, $scope.mod);
		}
		$state.go('^');
	};

	$scope.onSaveError = function() {
		$state.go('^');
	};

	$scope.onRemove = function() {
		var item = ModService.getById($scope.mods, $scope.mod.$id());
		$scope.mods.splice($scope.mods.indexOf(item), 1);
		$state.go('^');
	};

	$scope.onRemoveError = function() {
		$state.go('^');
	};
	
})

;
