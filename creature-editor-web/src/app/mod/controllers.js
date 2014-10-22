angular.module('editor.mod.controllers', [])

.controller('ModListController', function($scope, $location, crudListMethods, $translate, $state, ModService, mods) {
	angular.extend($scope, crudListMethods($location.url()));

	var source = {
			localdata: mods,
			datafields: [
			   { name: 'id', type: 'number' },
			   { name: 'name', type: 'string' }
			],
			datatype: "array"
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source);

	var columns = [{
		text : $translate.instant('MOD.FIELDS.NAME'),
		dataField : 'name',
		width : 200
	}];
	
	$scope.settings = {
			altrows : true,
			width : 260,
			height : 400,
			source : dataAdapter,
			columns : columns,
			rowselect: function(event) {
				$scope.selectedRow = event.args.row;
			}
	};
	
	$scope.remove = function(id) {
		var mod = ModService.getById(mods, id);
		mod.$delete().then(function(response) {
			$scope.removeFromList(mods, 'id', mod.$id());
			$state.refresh();
//			$translateWrapper('MOD.LABEL').then(function(label) {
//				$alertMessage.push('CRUD.REMOVE_SUCCESS', 'info', {
//					entity : label,
//					name : mod.name
//				});
//			});
		}, function() {
//			$alertMessage.push('CRUD.REMOVE_ERROR', 'danger', {
//				name : mod.name
//			});
		});
	};

})


.controller('ModEditController', function($scope, ModService, mod) {
	$scope.mod = mod;
	
	$scope.edit = { model: 'mod', entity: 'MOD.LABEL', name: 'name' };
	
	$scope.onCancel = function() {
		$scope.$dismiss();
	};

	$scope.onSave = function() {
		$scope.$close();
	};

	$scope.onSaveError = function() {
		$scope.$dismiss();
	};
	
//	$scope.create = $stateParams.modId === 'new';
//	if ($scope.create) {
//		$scope.mod = ModService.getNew();
//	} else {
//		$scope.mod = angular.copy(ModService.getById($scope.mods, $stateParams.modId));
//	}
//
//	$scope.onCancel = function() {
//		$state.go('^');
//	};
//
//	$scope.onSave = function() {
//		if ($scope.create) {
//			$scope.mods.push($scope.mod);
//		} else {
//			var item = ModService.getById($scope.mods, $scope.mod.$id());
//			angular.extend(item, $scope.mod);
//		}
//		$state.go('^');
//	};
//
//	$scope.onSaveError = function() {
//		$state.go('^');
//	};
//
//	$scope.onRemove = function() {
//		var item = ModService.getById($scope.mods, $scope.mod.$id());
//		$scope.mods.splice($scope.mods.indexOf(item), 1);
//		$state.go('^');
//	};
//
//	$scope.onRemoveError = function() {
//		$state.go('^');
//	};
})

;
