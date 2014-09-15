(function() {
	'use strict';

	angular.module('parameter.controllers', [])

	.controller('ParameterController', function($scope, types) {
		$scope.types = types;
	})

	.controller('ParameterListController', function($scope, $location, $translate, parameters, crudListMethods, $alertMessage, $interpolate, $stateParams) {
		angular.extend($scope, crudListMethods($location.url()));
		
		$scope.typeId = $stateParams.typeId;

		$scope.parameters = parameters;
		
		$scope.columns = [
		    {
					displayName: $translate.instant('PARAMETER.FIELDS.NAME'),
					field: 'name',
					sortable: true,
					width: 'auto',
					minWidth: 300
		    },
		    {
					displayName: $translate.instant('PARAMETER.FIELDS.LABEL'),
					field: 'label',
					sortable: true,
					width: 'auto',
					minWidth: 200
		    },
		    {
					displayName: $translate.instant('PARAMETER.FIELDS.VALUE'),
					field: 'value',
					sortable: false,
					width: 'auto',
					minWidth: 300
		    },
		    {
					displayName: $translate.instant('PARAMETER.FIELDS.ACTION'),
					sortable: false,
					width: 'auto',
					minWidth: 60,
					cellTemplate: 'parameter/parameter-grid-buttons.tpl.html'
		    }
		];
		
		$scope.gridOptions = {
				data: 'parameters',
				columnDefs: 'columns',
				i18n: 'fr',
				enableColumnResize: true,
				rowHeight : 35,
				multiSelect: false
		};
		
		$scope.edit = function() {
			console.log('edit');
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
