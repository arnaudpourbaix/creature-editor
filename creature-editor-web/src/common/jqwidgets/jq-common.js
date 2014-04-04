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

		this.$get = [ '$q', '$http', '$templateCache', '$rootScope', '$controller', '$compile', '$injector', '$parse',
				function jqCommonService($q, $http, $templateCache, $rootScope, $controller, $compile, $injector, $parse) {
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
						var result = angular.copy(params);
						service.checkRequiredParams(result, requiredProps);
						service.checkOptionnalParams(result, optionalProps);
						return result;
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.checkRequiredParams
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description controls required properties.
					 * 
					 * @param {object}
					 *           params object to control.
					 * @param {array}
					 *           or {string} requiredProps required properties.
					 */
					service.checkRequiredParams = function(params, requiredProps) {
						if (!requiredProps) {
							return;
						}
						var props = angular.isArray(requiredProps) ? requiredProps : [].concat(requiredProps);
						var difference = _.difference(props, _.keys(params));
						if (difference.length) {
							throw new Error("missing required parameters: " + difference.toString());
						}
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.getParams
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description controls optional properties.
					 * @param {object}
					 *           params object to control.
					 * @param {array}
					 *           or {string} optionalProps optional properties array.
					 */
					service.checkOptionnalParams = function(params, requiredProps, optionalProps) {
						if (!optionalProps) {
							return;
						}
						var props = angular.isArray(optionalProps) ? optionalProps : [].concat(optionalProps);
						angular.forEach(props, function(prop, index) {
							params[prop] = params[prop] || null;
						});
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.checkTemplateParams
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description controls that template is specified (must have template or templateUrl property).
					 * @param {object}
					 *           params object to control.
					 */
					service.checkTemplateParams = function(params) {
						if (!params.template && !params.templateUrl) {
							throw new Error('Missing template ! Add template or templateUrl option.');
						}
						if (params.template && params.templateUrl) {
							throw new Error('Too many template options ! Choose between template and templateUrl.');
						}
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
					 * @returns {promise} Template promise.
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
					 * @name $jqCommon.resolveDependencies
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description it will return a deferred with all dependencies. Each promise will be resolved before resolution.
					 * @param {object} dependencies object.
					 * @returns {promise} resolved dependencies object.
					 */
					service.resolveDependencies = function(dependencies) {
						var promises = [];
						angular.forEach(dependencies, function(value) {
							if (angular.isFunction(value) || (angular.isArray(value) && angular.isFunction(value[value.length - 1]))) {
								promises.push($q.when($injector.invoke(value)));
							} else {
								promises.push($q.when(value));
							}
						});
						return $q.all(promises).then(function(data) {
							var result = {}, i = 0;
							angular.forEach(dependencies, function(value, key) {
								result[key] = data[i++];
							});
							return result;
						});
					};

					/**
					 * @ngdoc function
					 * @name $jqCommon.instanciateController
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description Instanciate a controller and returns a deferred to track when it's done.
					 * @param {string}
					 *           controller Controller's name.
					 * @param {array}
					 *           dependencies Dependencies to inject. Promises within dependencies will be resolved.
					 * @param {object}
					 *           scope Controller's scope, if not provided, a new scope is created from root scope.
					 * @returns {promise} resolved when controller is instanciated
					 */
					service.instanciateController = function(controller, dependencies, scope) {
						var deferred = $q.defer();
						if (!controller) {
							deferred.resolve();
							return deferred;
						}
						service.resolveDependencies(dependencies).then(function(result) {
							var ctrlLocals = {
								$scope : scope || $rootScope.$new()
							};
							angular.forEach(result, function(value, key) {
								ctrlLocals[key] = value;
							});
							$controller(controller, ctrlLocals);
							deferred.resolve();
						});
						return deferred.promise;
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
					 * @returns {promise} dom element compiled with angular's scope.
					 */
					service.getView = function(templateOptions, controller, dependencies, scope) {
						var $scope = scope || $rootScope.$new();
						return $q.all([ service.getTemplatePromise(templateOptions), service.instanciateController(controller, dependencies, $scope)]).then(function(result) {
							return $compile(result[0])($scope);
						});
					};
					
					/**
					 * @ngdoc function
					 * @name $jqCommon.getScopeData
					 * @module jqwidgets.common
					 * @function
					 * 
					 * @description Get scope data value. Throw an exception if undefined.
					 * @param {string}
					 *           name data name within a scope.
					 * @param {object}
					 *           scope scope containing the data.
					 * @returns {promise} dom element compiled with angular's scope.
					 */
					service.getScopeData = function(name, scope) {
						var data = $parse(name);
						var value = data(scope);
						if (angular.isUndefined(value)) {
							throw new Error('Undefined data in scope :' + name);
						}
						return value;
					};
					
					return service;
				} ];
	});

	
	/**
	 * @ngdoc directive
	 * @name ngScopeElement
	 * @module jqwidgets.common
	 * @directive
	 * 
	 * @description Assign a DOM element to a scope.
	 * 
	 */
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