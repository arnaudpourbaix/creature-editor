(function() {
	'use strict';

	angular.module('parameter.config', [])

	.config(function ParameterTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/parameter');
	})

	.config(function ConfigurationStateConfig($stateProvider) {

		$stateProvider.state('parameter', {
			abstract: true,
			url : '/parameter',
			controller : 'ParameterController',
			templateUrl : 'parameter/parameter.tpl.html',
			resolve : {
				types : function(ParameterType) {
					return ParameterType.query().$promise;
				}
			}
		});

		$stateProvider.state('parameter.list', {
			url : '/:typeId',
			views : {
				'parameter-list' : {
					controller : 'ParameterListController',
					templateUrl : 'parameter/parameter-list.tpl.html',
				}
			},
			resolve : {
				parameters : function(Parameter, $stateParams) {
					return Parameter.query({
						typeId : $stateParams.typeId
					}).$promise;
				}
			}
		});

		$stateProvider.state('parameter.list.edit', {
			url : '/:id',
			onEnter : function($state, $stateParams, $modal, $timeout) {
				var modal = $modal.open({
					controller : 'ParameterEditController',
					templateUrl : "parameter/parameter-edit.tpl.html",
					resolve : {
//						mod : function(Mod) {
//							if ($stateParams.modId !== 'new') {
//								return Mod.get({
//									id : $stateParams.modId
//								}).$promise;
//							} else {
//								return new Mod({
//									id : null,
//									name : ''
//								});
//							}
//						}
					}
				});
//				modal.result.then(function(result) {
//					$state.go('^', {}, {
//						reload : true
//					});
//				}, function() {
//					$state.go('^');
//				});
			}
		});

	})
	
	;

})();
