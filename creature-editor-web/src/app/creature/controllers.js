(function() {
	'use strict';

	angular.module('editor.creature.controllers', [])
	
	.controller('CreatureListController', function($scope, $translate, CreatureImportService, apxPanel, creatures, mods) {
			angular.forEach(creatures, function(creature) {
				creature.attributeValues = _.indexBy(creature.attributeValues, function(attributeValue) {
					return attributeValue.attribute.id;
				});
			});
			$scope.creatures = creatures;
			
			var source = {
					localdata: $scope.creatures,
					datafields: [
					   { name: 'game', type: 'string', map: 'game>id', mapChar : '>' },
					   { name: 'mod', type: 'string', map: 'mod>name', mapChar : '>' },
					   { name: 'resource', type: 'string' },
					   { name: 'name', type: 'string', map: 'attributeValues>NAME>stringValue', mapChar : '>' }
					],
					datatype: "array"
			};
			
			var dataAdapter = new $.jqx.dataAdapter(source);

			var columns = [ {
				text : $translate.instant('CREATURE.FIELDS.GAME'),
				dataField : 'game',
				width : 100
			}, {
				text : $translate.instant('CREATURE.FIELDS.MOD'),
				dataField : 'mod',
				width : 100
			}, {
				text : $translate.instant('CREATURE.FIELDS.RESOURCE'),
				dataField : 'resource',
				width : 200
			}, {
				text : $translate.instant('CREATURE.FIELDS.NAME'),
				dataField : 'name',
				width : 200
			}
			];
			
			$scope.settings = {
					altrows : true,
					sortable: true,
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
			
	})

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

})();
