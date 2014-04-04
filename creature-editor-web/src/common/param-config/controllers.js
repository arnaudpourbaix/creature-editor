(function() {
	'use strict';

	var module = angular.module('param-config.controllers', [ 'param-config.services', 'crud', 'alert-message', 'translate-wrapper' ]);

	module.controller('ParamConfigController', [ '$scope', 'types', function ParamConfigController($scope, types) {
		$scope.types = types;
		
		$scope.layout = {
			options : {
				width : 1200,
				height : 800
			},
			windows : [ {
				id : 'param-config-list',
				height : 800
			} ]
		};
	} ]);

	module.controller('ParamConfigListController', [ '$scope', '$location', '$translateWrapper', 'parameters', 'crudListMethods', '$alertMessage', '$interpolate', '$stateParams',
			function ParamConfigListController($scope, $location, $translateWrapper, parameters, crudListMethods, $alertMessage, $interpolate, $stateParams) {
				angular.extend($scope, crudListMethods($location.url()));
				
				$scope.splitter = {
					width : 1180,
					height : 700,
					panels : [ {
						size : 720,
						min : 200
					}, {
						size : 420,
						min : 100
					} ]
				};

				$scope.typeId = $stateParams.typeId;

				$scope.parameters = parameters;
				
				var setParameterGrid = function() {
					$translateWrapper([ 'PARAMETER.FIELDS.NAME', 'PARAMETER.FIELDS.LABEL', 'PARAMETER.FIELDS.VALUE' ]).then(function(labels) {
						$scope.parameterGrid = {
							data : 'parameters',
							columns : [ {
								text : labels['PARAMETER.FIELDS.NAME'],
								datafield : 'name',
								type : 'string',
								align : 'center',
								hidden : true,
								width : 250
							}, {
								text : labels['PARAMETER.FIELDS.LABEL'],
								datafield : 'label',
								type : 'string',
								align : 'center',
								width : 300
							}, {
								text : labels['PARAMETER.FIELDS.VALUE'],
								datafield : 'value',
								type : 'string',
								align : 'center',
								width : 380
							} ],
							options : {
								width : 700,
								height : 590,
								pageable : true
							},
							events : {
								cellClick : function($scope, parameter, column) {
									$scope.edit(parameter.name);
								}
							}
						};
					});
				};

				setParameterGrid();
				$scope.$onRootScope('$translateChangeSuccess', function() {
					setParameterGrid();
				});

			} ]);

	module.controller('ParamConfigEditController', [ '$scope', '$state', '$stateParams', '$translateWrapper', 'ParameterService', function ParamConfigEditController($scope, $state, $stateParams, $translateWrapper, ParameterService) {
		$scope.parameter = angular.copy(ParameterService.getById($scope.parameters, $stateParams.id));
		
		$translateWrapper('PARAMETER.VALUE_SELECT').then(function(translation) {
			$scope.valuesSelect = {
				data : 'parameter.parameterValues',
				displayMember : 'description',
				valueMember : 'value',
				options : {
					placeHolder : translation,
					autoOpen : true,
					width : '300px'
				}
			};
		});
		
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

	} ]);

})();
