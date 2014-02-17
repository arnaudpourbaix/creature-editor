(function() {
	'use strict';

	var module = angular.module('param-config.controllers', [ 'param-config.services', 'alert-message', 'crud', 'ui.bootstrap' ]);

	module.controller('ParamConfigController', [ '$scope', 'types', function ParamConfigController($scope, types) {
		$scope.types = types;
	} ]);

	module.controller('ParamConfigListController', [ '$scope', '$stateParams', '$location', '$translate', 'parameters', 'crudListMethods', 'alertMessageService',
			function ParamConfigListController($scope, $stateParams, $location, $translate, parameters, crudListMethods, alertMessageService) {
				angular.extend($scope, crudListMethods($location.url()));

				$scope.typeId = $stateParams.typeId;

				$scope.parameters = parameters;

				var getParameterGrid = function() {
					return {
						data : 'parameters',
						columns : [ {
							text : $translate('PARAMETER.NAME_FIELD'),
							dataField : 'name',
							type : 'string',
							align : 'center',
							width : 250
						}, {
							text : $translate('PARAMETER.DESCRIPTION_FIELD'),
							dataField : 'description',
							type : 'string',
							align : 'center',
							width : 400
						}, {
							text : $translate('PARAMETER.VALUE_FIELD'),
							dataField : 'value',
							type : 'string',
							align : 'center',
							width : 150
						} ],
						options : {
							width : 800,
							height : 400,
							pageable : true,
							pagerButtonsCount : 10,
							pageSize : 15
						},
						events : {
							cellClick : function($scope, parameter, column) {
								$scope.edit(parameter.name);
							}
						}
					};
				};

				$scope.parameterGrid = getParameterGrid();
				$scope.$on('$translateChangeSuccess', function() {
					$scope.parameterGrid = getParameterGrid();
				});

			} ]);

	module.controller('ParamConfigEditController', [ '$scope', '$modalInstance', 'parameter', function ParamConfigEditController($scope, $modalInstance, parameter) {
		$scope.parameter = parameter;

		$scope.onSave = function(parameter) {
			$modalInstance.close({
				parameter : parameter
			});
		};

		$scope.onSaveError = function(parameter) {
			$modalInstance.close({
				parameter : parameter
			});
		};
	} ]);

})();
