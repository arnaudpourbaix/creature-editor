(function() {
	'use strict';

	angular.module('editor.creature.controllers', [])

	.controller('CreatureListController', function($scope, creatures, CreatureImportService) {
				$scope.creatures = creatures;
				
				$scope.import = function() {
					CreatureImportService.startImport({ id: 1 });
				};

				$scope.stopImport = function() {
					CreatureImportService.cancelImport();
				};
				
	})
	
	;

})();
