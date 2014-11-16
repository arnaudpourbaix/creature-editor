angular.module('editor.creature.controllers', [])

.controller('CreatureListController', function($scope, $translate, $state, toaster, $alertify, apxPanel, CreatureImportService, creatures, mods) {
	$scope.mods = mods;
	
	
	angular.forEach(creatures, function(creature) {
		creature.attributeValues = _.indexBy(creature.attributeValues, function(attributeValue) {
			return attributeValue.attribute.id;
		});
	});
	$scope.creatures = creatures;
		
	$scope.creatureGrid = {
			datasource : 'creatures',
			columns : [ {
				text : $translate.instant('CREATURE.FIELDS.GAME'),
				datafield : 'game',
				map: 'game.id',
				type : 'string',
				align : 'center',
				width : 100
			},
			{
				text : $translate.instant('CREATURE.FIELDS.MOD'),
				datafield : 'mod',
				map: 'mod.name',
				type : 'string',
				align : 'center',
				width : 100
			},{
				text : $translate.instant('CREATURE.FIELDS.RESOURCE'),
				datafield : 'resource',
				type : 'string',
				align : 'center',
				width : 200
			},{
				text : $translate.instant('CREATURE.FIELDS.NAME'),
				datafield : 'name',
				map: 'attributeValues.NAME.stringValue',
				type : 'string',
				align : 'center',
				width : 200
			} ],
			settings : {
				width : 900,
				height : 590
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
	
	$scope.docking = {
		settings : {
			width : 1200,
			height : 800
		},
		windows : [ {
			id : 'creature-list',
			height : 700
		} ]
	};

	$scope.edit = function(creature) {
		$state.go('creature.edit', {
			id : creature.id
		});
	};
	
	$scope.remove = function(creature) {
		$alertify.confirm($translate.instant("CREATURE.DELETE_CONFIRM")).then(function() {
			return creature.$delete().then(function(response) {
				var message = $translate.instant('CRUD.REMOVE_SUCCESS', {
					entity: $translate.instant('CREATURE.LABEL'),
					name: creature.resource
				});
				toaster.pop('success', null, message);
				$scope.creatures.splice($scope.creatures.indexOf(creature), 1);
			}, function() {
				var message = $translate.instant('CRUD.REMOVE_ERROR', {
					entity: $translate.instant('CREATURE.LABEL'),
					name: creature.resource
				});
				toaster.pop('danger', null, message);
			});
		});
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

.controller('CreatureEditController', function($scope, $translate, creature) {
	$scope.creature = creature;
	
})

;

