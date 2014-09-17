(function() {
	'use strict';

	angular.module('parameter.controllers', [])

	.controller('ParameterController', function($scope, types) {
		$scope.types = types;
	})

	.controller('ParameterListController', function($scope, $location, crudListMethods, $translate, parameters, $stateParams) {
		angular.extend($scope, crudListMethods($location.url()));

		//$scope.typeId = $stateParams.typeId;

		$scope.parameters = parameters;

		$scope.columns = [ {
			text : $translate.instant('PARAMETER.FIELDS.NAME'),
			dataField : 'name',
			width : 300
		}, {
			text : $translate.instant('PARAMETER.FIELDS.LABEL'),
			dataField : 'label',
			width : 200
		}, {
			text : $translate.instant('PARAMETER.FIELDS.VALUE'),
			dataField : 'value',
			width : 300
		}
		];

		$scope.settings = {
				altrows : true,
				width : 900,
				height : 400,
				source : $scope.parameters,
				columns : $scope.columns,
				rowselect: function(event) {
					$scope.selectedRow = event.args.row;
				}
		};

	})

	.controller('ParameterEditController', function($scope, $state, $stateParams, $translate, ParameterService) {
		$scope.parameter = angular.copy(ParameterService.getById($scope.parameters, $stateParams.id));

		$scope.valuesSelect = {
			data : 'parameter.parameterValues',
			displayMember : 'description',
			valueMember : 'value',
			options : {
				placeHolder : $translate.instant('PARAMETER.VALUE_SELECT'),
				autoOpen : true,
				width : '300px'
			}
		};

		$scope.onCancel = function() {
			$state.go('^');
		};

		$scope.onSave = function() {
			var item = ParameterService.getById($scope.parameters, $scope.parameter.$id());
			angular.extend(item, $scope.parameter);
			$state.go('^');
		};

		$scope.onSaveError = function() {
			$state.go('^');
		};

		$scope.onRemove = function() {
			var item = ParameterService.getById($scope.parameters, $scope.parameter.$id());
			$scope.parameters.splice($scope.parameters.indexOf(item), 1);
			$state.go('^');
		};

		$scope.onRemoveError = function() {
			$state.go('^');
		};

	})

	;

})();
