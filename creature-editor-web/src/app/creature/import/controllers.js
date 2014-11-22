angular.module('editor.creature.import.controllers', [])

.controller('CreatureImportController', function($scope, $translate, CreatureImportService, $panelInstance, mods) {
	
	$scope.mods = mods;
	
	$scope.options = {
			mod: _.find(mods, function(mod) {
				return mod.id === 1;
			}),
			resource: 'AL.*',
			override: false,
			onlyName: true
	};
	
	$scope.validate = function() {
		CreatureImportService.startImport($scope.options);
	};

	$scope.stopImport = function() {
		CreatureImportService.cancelImport();
	};
	
	
})

;

