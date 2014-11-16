angular.module('editor.mod.controllers', [])

.controller('ModListController', function($scope, $translate, $state, toaster, Mod, ModService, mods) {
	$scope.mods = mods;

	$scope.splitter = {
			width : 800,
			height : 400,
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
			settings : {
				width : 260,
				height : 300
			},
			buttons : {
				add : 'add'
			},
			events : {
				rowclick : 'edit',
				contextMenu : {
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
		$state.go('mod.edit', {
			id : mod.id
		});
	};
	
	$scope.remove = function(mod) {
		mod.$delete().then(function(response) {
			var message = $translate.instant('CRUD.REMOVE_SUCCESS', {
				entity: $translate.instant('MOD.LABEL'),
				name: mod.name
			});
			toaster.pop('success', null, message);
			$scope.mods.splice($scope.mods.indexOf(mod), 1);
			$state.go('mod'); // item could have been selected, opening edit state
		}, function() {
			var message = $translate.instant('CRUD.REMOVE_ERROR', {
				entity: $translate.instant('MOD.LABEL'),
				name: mod.name
			});
			toaster.pop('danger', null, message);
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
