(function() {
	'use strict';

	angular.module('parameter.controllers', [])

	.controller('ParameterController', function($scope, types) {
		$scope.types = types;
	})

	.controller('ParameterListController', function($scope, $location, crudListMethods, $translate, parameters, $stateParams) {
		angular.extend($scope, crudListMethods($location.url()));

		$scope.parameters = parameters;

		$scope.columns = [ {
			text : $translate.instant('PARAMETER.FIELDS.NAME'),
			dataField : 'name',
			type : 'string',
			width : 300
		}, {
			text : $translate.instant('PARAMETER.FIELDS.LABEL'),
			dataField : 'label',
			type : 'string',
			width : 200
		}, {
			text : $translate.instant('PARAMETER.FIELDS.VALUE'),
			dataField : 'value',
			type : 'string',
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

	.controller('ParameterEditController', function($scope, $state, parameter, $translate, ParameterService) {
		$scope.parameter = parameter;
		
		$scope.edit = { model: 'parameter', entity: 'PARAMETER.LABEL', name: 'name' };

		$scope.valuesSelect = {
				width: 300,
				height: 30,
				autoDropDownHeight: true,
				displayMember: "description",
				valueMember: "value",
				//placeHolder : $translate.instant('PARAMETER.VALUE_SELECT'),
				source: $scope.parameter.parameterValues
		};

		$scope.onCancel = function() {
			$scope.$dismiss();
		};

		$scope.onSave = function() {
			$scope.$close();
		};

		$scope.onSaveError = function() {
			$scope.$dismiss();
		};

	})

	;

})();
