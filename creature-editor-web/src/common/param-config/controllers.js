(function() {
	'use strict';

	var module = angular.module('param-config.controllers', [ 'param-config.services', 'alert-message', 'crud', 'ui.bootstrap' ]);

	module.controller('ParamConfigController', [ '$scope', 'types', function ParamConfigController($scope, types) {
		$scope.types = types;
	} ]);

	module.controller('ParamConfigListController', [ '$scope', '$stateParams', '$location', '$translate', 'parameters', 'crudListMethods', '$alertMessage', '$q', '$interpolate',
			function ParamConfigListController($scope, $stateParams, $location, $translate, parameters, crudListMethods, $alertMessage, $q, $interpolate) {
				angular.extend($scope, crudListMethods($location.url()));

				$scope.typeId = $stateParams.typeId;

				$scope.parameters = parameters;

				var setParameterGrid = function() {
					$q.all([ $translate('PARAMETER.FIELDS.NAME'), $translate('PARAMETER.FIELDS.LABEL'), $translate('PARAMETER.FIELDS.VALUE') ]).then(function(labels) {
						$scope.parameterGrid = {
							data : 'parameters',
							columns : [ {
								text : labels[0],
								dataField : 'name',
								type : 'string',
								align : 'center',
								width : 250
							}, {
								text : labels[1],
								dataField : 'label',
								type : 'string',
								align : 'center',
								width : 400
							}, {
								text : labels[2],
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
					});
				};

				setParameterGrid();
				$scope.$onRootScope('$translateChangeSuccess', function() {
					setParameterGrid();
				});

			} ]);

	module.controller('ParamConfigEditController', [ '$scope', '$windowInstance', '$translate', 'parameter',
			function ParamConfigEditController($scope, $windowInstance, $translate, parameter) {
				$scope.parameter = parameter;
				$scope.parameterValues = parameter.parameterValues;

				$translate('PARAMETER.VALUE_SELECT').then(function(translation) {
					$scope.valuesSelect = {
						data : 'parameterValues',
						displayMember : 'description',
						valueMember : 'value',
						options : {
							placeHolder : translation,
							autoOpen : true,
							width : '300px'
						}
					};
				});

				$scope.onSave = function(parameter) {
					$windowInstance.close({
						parameter : parameter
					});
				};

				$scope.onSaveError = function(parameter) {
					$windowInstance.close({
						parameter : parameter
					});
				};
			} ]);

})();
