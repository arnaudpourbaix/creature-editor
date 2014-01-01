var mod = angular.module('creatureEditor.mod', [ 'ui.router', 'ngRoute', 'ngResource' ]);

mod.config(function config($stateProvider) {
	'use strict';
	$stateProvider.state('mod', {
		abstact : true,
		url : '/mod',
		template : '<ui-view/>'
	}).state('mod.list', {
		url : '/list',
		controller : 'ModListController',
		templateUrl : 'mod/mod-list.tpl.html'
	}).state('mod.new', {
		url : "/new",
		onEnter : function($stateParams, $state, $modal, $resource) {
			$modal.open({
				templateUrl : "mod/mod-new.tpl.html",
				controller : 'ModNewController'
			}).result.then(function(result) {
				return $state.transitionTo("mod.list");
			});
		}
	});
});

mod.factory('Mod', function($resource) {
	'use strict';
	return $resource('rest/mod/:id', {}, {
		'save' : {
			method : 'PUT'
		}
	});
});

mod.controller('ModListController', function ModListController($scope, $state, $timeout, Mod) {
	'use strict';

	$scope.save = {
		promise : null,
		pending : false,
		row : null
	};

	$scope.mods = Mod.query();

	$scope.gridOptions = {
		data : 'mods',
		enableCellSelection : false,
		enableRowSelection : false,
		enableCellEditOnFocus : true,
		columnDefs : [ {
			field : 'name',
			displayName : 'Name',
			enableCellEdit : true,
			editableCellTemplate : '<input ng-class="\'colt\' + col.index" ng-input="COL_FIELD" ng-model="COL_FIELD" ng-blur="updateEntity(row)" />'
		}, {
			field : '',
			sortable : false,
			enableCellEdit : false,
			cellClass : 'deleteColumn',
			width : '80px',
			cellTemplate : '<button ng-click="deleteEntity(row)">Delete</button>'
		} ]
	};

	$scope.updateEntity = function(row) {
		if ($scope.save.pending) {
			return;
		}
		if (row.entity.id == null) {
			if (row.entity.name.trim().length === 0) {
				$scope.mods.splice(row.rowIndex, 1);
			}
		}
		console.debug(row);
		// var mod = $scope.mods[$scope.save.row];
		// $scope.save.pending = true;
		// $scope.save.row = row.rowIndex;
		// $scope.save.promise = $timeout(function() {
		// $scope.mods[$scope.save.row].$save();
		// // console.log("Here you'd save your record to the server, we're updating row: " + $scope.save.row + " to be: " +
		// // $scope.mods[$scope.save.row].name);
		// $scope.save.pending = false;
		// }, 500);
	};

	$scope.deleteEntity = function(row) {
		row.entity.$delete({
			id : row.entity.id
		}).then(function(response) {
			// if (response.status === 'OK') {
			remove($scope.mods, 'id', row.entity.id);
			// }
		});
	};

	function remove(array, property, value) {
		$.each(array, function(index, result) {
			if (result[property] === value) {
				array.splice(index, 1);
			}
		});
	}

});

mod.controller('ModNewController', function ModNewController($scope, $modalInstance, Mod) {
	'use strict';

	$scope.mod = new Mod({
		name : ''
	});

	$scope.create = function() {
		if ($scope.mod.name.trim() === '') {
			// TODO empty name message
			return;
		}
		$scope.mod.$save(function() {
			$modalInstance.close($scope.mod);
		});
	};
});
