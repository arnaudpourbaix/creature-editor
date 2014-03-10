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
					var service = {
						options : function() {
							return options;
						},
						/**
						 * @ngdoc function
						 * @name angular.getParams
						 * @module jqwidgets.common
						 * @function
						 *
						 * @description return params object after controlling required properties and adding missing optional properties.
						 * @param {object} params object to control.
						 * @param {array} requiredProps required properties array.
						 * @param {array} optionalProps optional properties array.
						 * @returns {object} params object including missing optional properties.
						 */
						getParams : function(params, requiredProps, optionalProps) {
							var paramKeys = _.keys(params);
							if (angular.isArray(requiredProps)) {
								var difference = _.difference(requiredProps, paramKeys);
								if (difference.length) {
									throw new Error("missing required parameters: " + difference.toString());
								}
							}
							if (angular.isArray(optionalProps)) {
								angular.forEach(optionalProps, function(prop, index) {
									params[prop] = params[prop] || {};
								});
							}
							return params;
						},

						/**
						 * @ngdoc function
						 * @name angular.getTemplatePromise
						 * @module jqwidgets.common
						 * @function
						 *
						 * @description Get template promise from a template string or url.
						 * @param {array} options Must contains template or templateUrl property.
						 * @returns {object} Template promise.
						 */
						getTemplatePromise : function(options) {
							return options.template ? $q.when(options.template) : $http.get(options.templateUrl, {
								cache : $templateCache
							}).then(function(result) {
								return result.data;
							});
						},

						/**
						 * @ngdoc function
						 * @name angular.instanciateController
						 * @module jqwidgets.common
						 * @function
						 *
						 * @description Instanciate a controller.
						 * @param {string} controller Controller's name.
						 * @param {object} scope Controller's scope, if not provided, a new scope is created from root scope.
						 * @param {array} dependencies Dependencies to inject.
						 * @returns
						 */
						instanciateController : function(controller, scope, dependencies) {
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
						}

					};
					return service;
				} ];
	});

}(window, _));