angular.module('editor.creature.import.controllers', [])

.controller('CreatureImportController', function($scope, $translate, toaster, $alertify, CreatureImportService, $panelInstance, mods) {
	
	$scope.mods = mods;
	
	$scope.options = {
			mod: _.find(mods, function(mod) {
				return mod.id === 1;
			}),
			resource: 'AL.*',
			override: false,
			onlyName: true
	};
	
	$scope.running = false;
	
	$scope.validate = function() {
		CreatureImportService.import($scope.options).then(function(response) {
			console.info(response);
			$scope.running = true;
			$scope.data = response;
			//$panelInstance.close();
		}, function(response) {
			toaster.pop('danger', null, $translate.instant(response.data.code));
		});
	};

	$scope.cancel = function() {
		$alertify.confirm($translate.instant("CREATURE.IMPORT.CANCEL_CONFIRM")).then(function() {
			$scope.running = false;
			toaster.pop('warning', null, $translate.instant("CREATURE.IMPORT.CANCELLED"));
			//CreatureImportService.cancelImport();
		});
	};
	
	
})

;

