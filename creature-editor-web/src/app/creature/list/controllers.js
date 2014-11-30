angular.module('editor.creature.list.controllers', [])

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
				height : 390
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

	$scope.edit = function(creature) {
		$state.go('creatureEdit', {
			id : creature.id
		});
	};
	
	$scope.remove = function(creature) {
		$alertify.confirm($translate.instant("CREATURE.DELETE_CONFIRM", { resource: creature.resource })).then(function() {
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
	
	$scope.importPanel = function() {
		CreatureImportService.getPanel();
	};

	$scope.stopImport = function() {
		CreatureImportService.cancelImport();
	};
		
})

;

