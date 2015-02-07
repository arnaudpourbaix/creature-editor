angular.module('editor.creature.edit.controllers', [])

.controller('CreatureEditController', function($scope, $translate, $state, creature, mods, categories) {
	if (creature.mod) {
		creature.mod = _.find(mods, function(mod) {
			return mod.id === creature.mod.id;
		});
	}
	$scope.creature = creature;
	console.log(creature);
	
	$scope.mods = mods;
	$scope.categories = categories;
	$scope.categoryTree = {
			datasource : 'categories',
			datafields : [ {
				name : 'id',
				type : 'number'
			}, {
				name : 'name',
				type : 'string'
			}, {
				name : 'parentId',
				map : 'parent.id',
				type : 'number'
			} ],
			id : 'id',
			parent : 'parentId',
			display : 'name',
			settings : {
				width : 580,
				allowDrag : false,
				allowDrop : false,
				checkboxes: true
			},
			filter : true,
			buttons : {
				expandCollapse : true
			}
	};
	
	$scope.onCancel = function() {
		$state.go('creatureList');
	};
	
})

;

