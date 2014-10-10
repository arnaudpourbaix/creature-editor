(function() {
	'use strict';

	angular.module('editor.creature.controllers', [])
	
	.controller('CreatureListController', function($scope, $translate, CreatureImportService, apxPanel, creatures, mods) {
			$scope.creatures = creatures;
			
			var source = {
					localdata: $scope.creatures,
					datafields: [
					   { name: 'resource', type: 'string' },
					   { name: 'game', type: 'string' }
					],
					datatype: "array"
			};
			
			var dataAdapter = new $.jqx.dataAdapter(source);

			var columns = [ {
				text : $translate.instant('CREATURE.FIELDS.RESOURCE'),
				dataField : 'resource',
				width : 100
			}, {
				text : $translate.instant('CREATURE.FIELDS.NAME'),
				dataField : 'game',
				width : 200
			}
			];
			
			$scope.settings = {
					altrows : true,
					width : 900,
					height : 400,
					source : dataAdapter,
					columns : columns,
					rowselect: function(event) {
						$scope.selectedRow = event.args.row;
					}
			};

			$scope.import = function() {
				var panel = apxPanel.open({
					templateUrl : 'creature/import-panel.tpl.html',
					controller : 'CreatureImportController',
					resolve : {
						mods : function() {
							return $scope.mods.$promise;
						}
					}
				});
			};
			
			$scope.mods = mods;
			$scope.import();
			
	})

	.controller('CreatureImportController', function($scope, $translate, CreatureImportService, $panelInstance, mods) {
		
		$scope.mods = mods;
		
		$scope.options = {
				resource: null,
				override: false,
				full: false
		};
		
		$scope.import = function() {
			CreatureImportService.startImport($scope.options);
		};

		$scope.stopImport = function() {
			CreatureImportService.cancelImport();
		};
		
		
	})
	
	;

})();
