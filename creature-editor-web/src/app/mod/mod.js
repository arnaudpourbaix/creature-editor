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

mod.factory('modService', [ '$http', function($http) {
	'use strict';
	var serviceBase = 'rest/mod/', modFactory = {};

	modFactory.checkUniqueValue = function(name) {
		return $http.get(serviceBase + 'checkUnique/' + name).then(function(results) {
			console.debug('server return:', results.data);
			return results.data === 'true';
		});
	};

	return modFactory;

} ]);

mod.directive('ensureUnique', [ 'modService', function(modService) {
	'use strict';
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel) {
			element.bind('blur', function(e) {
				if (!ngModel || !element.val()) {
					return;
				}
				var currentValue = element.val();
				modService.checkUniqueValue(currentValue).then(function(unique) {
					// Ensure value that being checked hasn't changed since the Ajax call was made
					if (currentValue === element.val()) {
						ngModel.$setValidity('unique', unique);
					}
				}, function() {
					ngModel.$setValidity('unique', false);
				});
			});
		}
	};
} ]);

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
		var mod = $scope.mods[$scope.save.row];
		$scope.save.pending = true;
		$scope.save.row = row.rowIndex;
		$scope.save.promise = $timeout(function() {
			$scope.mods[$scope.save.row].$save();
			$scope.save.pending = false;
		}, 500);
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
