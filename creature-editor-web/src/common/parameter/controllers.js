angular.module('parameter.controllers', [])

.controller('ParameterController', function($scope, types) {
	$scope.types = types;
})

.controller('ParameterListController', function($scope, $translate, $state, parameters) {
	$scope.parameters = parameters;

	$scope.parameterGrid = {
			datasource : 'parameters',
			columns : [ {
				text : $translate.instant('PARAMETER.FIELDS.NAME'),
				datafield : 'name',
				type : 'string',
				align : 'center',
				width : 200
			},
			{
				text : $translate.instant('PARAMETER.FIELDS.LABEL'),
				datafield : 'label',
				type : 'string',
				align : 'center',
				width : 300
			},{
				text : $translate.instant('PARAMETER.FIELDS.VALUE'),
				datafield : 'value',
				type : 'string',
				align : 'center',
				width : 380
			} ],
			settings : {
				width : 900,
				height : 590
			},
			events : {
				rowclick : 'edit'
			}
	};

	$scope.edit = function(parameter) {
		$state.go('parameter.list.edit', {
			id : parameter.name
		});
	};
	
})

.controller('ParameterEditController', function($scope, $state, parameter, $translate) {
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
