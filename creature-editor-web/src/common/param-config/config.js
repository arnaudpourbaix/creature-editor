(function() {
	'use strict';

	var module = angular.module('param-config.config', [ 'param-config.services', 'jqwidgets', 'pascalprecht.translate', 'ui.router', 'ui.bootstrap' ]);

	module.config([ '$translatePartialLoaderProvider', function ParameterTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/param-config');
	} ]);

	module.config([ '$stateProvider', function ConfigurationStateConfig($stateProvider) {

		$stateProvider.state('param-config', {
			abstract : true,
			url : '/configuration',
			controller : 'ParamConfigController',
			templateUrl : 'param-config/param-config.tpl.html',
			resolve : {
				types : [ 'ParameterType', function(ParameterType) {
					return ParameterType.query().$promise;
				} ]
			}
		});

		$stateProvider.state('param-config.typeSelect', {
			url : ''
		});

		$stateProvider.state('param-config.paramList', {
			url : '/:typeId',
			controller : 'ParamConfigListController',
			templateUrl : 'param-config/parameter-list.tpl.html',
			resolve : {
				parameters : [ 'Parameter', '$stateParams', function(Parameter, $stateParams) {
					return Parameter.query({
						typeId : $stateParams.typeId
					}).$promise;
				} ]
			}
		});

		$stateProvider.state('param-config.paramList.edit', {
			url : '/:id',
			resolve : {
				parameter : [ 'Parameter', '$stateParams', function(Parameter, $stateParams) {
					return Parameter.get({
						id : $stateParams.id
					}).$promise;
				} ]
			},
			onEnter : [ '$state', '$stateParams', '$jqWindow', 'parameter', function($state, $stateParams, $jqWindow, parameter) {
				var modal = $jqWindow.open({
					templateUrl : "param-config/parameter-edit.tpl.html",
					controller : 'ParamConfigEditController',
					options : {
						width : 500,
						height : 350
					},
					title : parameter.name,
					inject : {
						parameter : parameter
					}
				});
				modal.result.then(function(result) {
					$state.go('^', {}, {
						reload : true
					});
				}, function() {
					$state.go('^');
				});
			} ]
		});

	} ]);

})();
