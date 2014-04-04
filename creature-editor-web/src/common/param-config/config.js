(function() {
	'use strict';

	var module = angular.module('param-config.config', [ 'param-config.services', 'pascalprecht.translate', 'ui.router' ]);

	module.config([ '$translatePartialLoaderProvider', function ParameterTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/param-config');
	} ]);

	module.config([ '$stateProvider', function ConfigurationStateConfig($stateProvider) {

		$stateProvider.state('param-config', {
			url : '/configuration',
			controller : 'ParamConfigController',
			templateUrl : 'param-config/param-config.tpl.html',
			resolve : {
				types : [ 'ParameterType', function(ParameterType) {
					return ParameterType.query().$promise;
				} ]
			}
		});

		$stateProvider.state('param-config.list', {
			url : '/:typeId',
			views : {
				'param-config-list' : {
					controller : 'ParamConfigListController',
					templateUrl : 'param-config/parameter-list.tpl.html',
				}
			},
			resolve : {
				parameters : [ 'Parameter', '$stateParams', function(Parameter, $stateParams) {
					return Parameter.query({
						typeId : $stateParams.typeId
					}).$promise;
				} ]
			}
		});

		$stateProvider.state('param-config.list.edit', {
			url : '/:id',
			views : {
				'param-config-edit' : {
					controller : 'ParamConfigEditController',
					templateUrl : "param-config/parameter-edit.tpl.html"
				}
			}
		});

	} ]);

})();
