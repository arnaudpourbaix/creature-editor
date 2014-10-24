angular.module('editor.mod.controllers', [])

.controller('ModListController', function($scope, $location, crudListMethods, $translate, $state, ModService, mods, toaster) {
	angular.extend($scope, crudListMethods($location.url()));

	var setTable = function() {
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
	};
	
	setTable();
	
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
			setTable();
		}, function() {
			var message = $translate.instant('CRUD.REMOVE_ERROR', {
				entity: $translate.instant('MOD.LABEL'),
				name: mod.name
			});		
			toaster.pop('error', null, message);
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
	
	$scope.onRemove = function() {
		$scope.$close();
	};

	$scope.onRemoveError = function() {
		$scope.$dismiss();
	};
})

;
