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
							text : $translate('SPELL.RESOURCE_FIELD'),
							dataField : 'name',
							type : 'string',
							align : 'center',
							width : 80
						}, {
							text : $translate('SPELL.NAME_FIELD'),
							dataField : 'value',
							type : 'string',
							align : 'center',
							width : 200
						} ],
						options : {
							width : 670,
							height : 400,
							pageable : true,
							pagerButtonsCount : 10,
							pageSize : 15
						},
						events : {
							cellClick : function($scope, parameter, column) {
								$scope.edit(parameter.id);
							}
						}
					};
				};

				$scope.parameterGrid = getParameterGrid();
				$scope.$on('$translateChangeSuccess', function() {
					$scope.parameterGrid = getParameterGrid();
				});

			} ]);

	module.controller('ParamConfigEditController', [ '$scope', '$modalInstance', 'spell', function ParamConfigEditController($scope, $modalInstance, spell) {
		$scope.spell = spell;

		$scope.onSave = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onSaveError = function() {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onRemove = function(spell) {
			$modalInstance.close({
				spell : spell
			});
		};

		$scope.onRemoveError = function() {
			$modalInstance.close({
				spell : spell
			});
		};
	} ]);

})();
