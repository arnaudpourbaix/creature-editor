(function() {
	'use strict';

	angular.module('editor.creature.controllers', [])

	.controller('CreatureListController', function($scope, $translate, creatures, CreatureImportService) {
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
			
		    $scope.showPanel = false;
		    $scope.openPanel = function(panelId){
		        $scope.showPanel = panelId;
		    };
		    $scope.closePanel = function(){
		        $scope.showPanel = false;
		    };
			
			$scope.import = function() {
				$scope.openPanel('creature-import');
				//CreatureImportService.startImport({ id: 1 });
			};

			$scope.stopImport = function() {
				CreatureImportService.cancelImport();
			};
				
	})

	.controller('CreatureImportController', function($scope, $translate, CreatureImportService) {
	})
	
	;

})();
