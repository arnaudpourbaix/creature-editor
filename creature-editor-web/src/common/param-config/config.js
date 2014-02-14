(function() {
	'use strict';

	var module = angular.module('param-config.config', [ 'param-config.services', 'pascalprecht.translate', 'ui.router', 'ui.bootstrap' ]);

	module.config([ '$translatePartialLoaderProvider', function SpellTranslateConfig($translatePartialLoaderProvider) {
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

		$stateProvider.state('param-config.list.edit', {
			url : '/:id',
			onEnter : [ '$state', '$stateParams', '$modal', 'Spell', function($state, $stateParams, $modal, Spell) {
				var modal = $modal.open({
					templateUrl : "param-config/spell-edit.tpl.html",
					controller : 'ParamConfigEditController',
					resolve : {
						spell : [ 'Spell', function(Spell) {
							if ($stateParams.spellId !== 'new') {
								return Spell.get({
									id : $stateParams.id
								}).$promise;
							} else {
								return new Spell({
									id : null,
									name : ''
								});
							}
						} ]
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
