/* global _ */
(function(window, _) {
	'use strict';

	var module = angular.module('jqwidgets.common', []);

	module.provider('$jqCommon', function JqCommonProvider() {
		var options = {
			theme : 'classic'
		};
		this.theme = function(value) {
			options.theme = value;
		};

		this.$get = [ '$q', '$http', '$templateCache', '$rootScope', '$controller', '$compile',
				function jqCommonService($q, $http, $templateCache, $rootScope, $controller, $compile) {
					var service = {};

					service.options = function() {
						return options;
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.getParams
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description return params object after controlling required properties and adding missing optional properties.
					 * @param {object}
					 *           params object to control.
					 * @param {array}
					 *           requiredProps required properties array.
					 * @param {array}
					 *           optionalProps optional properties array.
					 * @returns {object} params object including missing optional properties.
					 */
					service.getParams = function(params, requiredProps, optionalProps) {
						var paramKeys = _.keys(params);
						if (angular.isArray(requiredProps)) {
							var difference = _.difference(requiredProps, paramKeys);
							if (difference.length) {
								throw new Error("missing required parameters: " + difference.toString());
							}
						}
						if (angular.isArray(optionalProps)) {
							angular.forEach(optionalProps, function(prop, index) {
								params[prop] = params[prop] || null;
							});
						}
						return params;
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.getTemplatePromise
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description Get template promise from a template string or url.
					 * @param {object}
					 *           options Must contains template or templateUrl property.
					 * @returns {object} Template promise.
					 */
					service.getTemplatePromise = function(options) {
						return options.template ? $q.when(options.template) : $http.get(options.templateUrl, {
							cache : $templateCache
						}).then(function(result) {
							return result.data;
						});
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.instanciateController
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description Instanciate a controller.
					 * @param {string}
					 *           controller Controller's name.
					 * @param {array}
					 *           dependencies Dependencies to inject.
					 * @param {object}
					 *           scope Controller's scope, if not provided, a new scope is created from root scope.
					 * @returns
					 */
					service.instanciateController = function(controller, dependencies, scope) {
						if (!controller) {
							throw new Error("missing controller!");
						}
						var ctrlLocals = {
							$scope : scope || $rootScope.$new()
						};
						angular.forEach(dependencies, function(value, key) {
							ctrlLocals[key] = value;
						});
						$controller(controller, ctrlLocals);
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.getView
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description Create and return a view, also instanciate a controller related to the view.
					 * @param {object}
					 *           templateOptions Must contains template or templateUrl property.
					 * @param {string}
					 *           controller Controller's name.
					 * @param {array}
					 *           dependencies Dependencies to inject.
					 * @param {object}
					 *           scope Controller's scope, if not provided, a new scope is created from root scope.
					 * @returns {element} dom element compiled with angular's scope.
					 */
					service.getView = function(templateOptions, controller, dependencies, scope) {
						return service.getTemplatePromise(templateOptions).then(function(template) {
							scope = scope || $rootScope.$new();
							if (controller) {
								service.instanciateController(controller, dependencies, scope);
							}
							return $compile(template)(scope);
						});
					};

					return service;
				} ];
	});

	module.directive("ngScopeElement", function() {
		return {
			restrict : "A",
			compile : function compile() {
				return {
					pre : function preLink(scope, iElement, iAttrs, controller) {
						scope[iAttrs.ngScopeElement] = iElement;
					}
				};
			}
		};

	});

}(window, _));