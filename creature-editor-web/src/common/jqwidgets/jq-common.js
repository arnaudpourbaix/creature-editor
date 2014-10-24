angular.module('apx-jqwidgets.common', [])

.provider('jqCommon', function() {
	var options = this.options = {
		theme : 'classic'
	};

	this.$get = function($q, $http, $templateCache, $rootScope, $controller, $compile, $injector, $parse) {
		var service = {};

		service.options = function() {
			return options;
		};

		/**
		 * @ngdoc function
		 * @name jqCommon.getParams
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description Returns params object after controlling required properties and adding missing optional properties.
		 * @param {Object}
		 *           params object to control.
		 * @param {array}
		 *           requiredProps required properties array.
		 * @param {array}
		 *           optionalProps optional properties array.
		 * @returns {Object} params object including missing optional properties.
		 */
		service.getParams = function(params, requiredProps, optionalProps) {
			var result = angular.copy(params);
			service.checkRequiredParams(result, requiredProps);
			service.checkOptionnalParams(result, optionalProps);
			return result;
		};

		/**
		 * @ngdoc function
		 * @name jqCommon.checkRequiredParams
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description controls required properties.
		 * 
		 * @param {Object}
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
		 * @name jqCommon.getParams
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description controls optional properties.
		 * @param {Object}
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
		 * @name jqCommon.checkTemplateParams
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description controls that template is specified (must have template or templateUrl property).
		 * @param {Object}
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
		 * @name jqCommon.getTemplatePromise
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description Get template promise from a template string or url.
		 * @param {Object}
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
		 * @name jqCommon.resolveDependencies
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description it will return a deferred with all dependencies. Each promise will be resolved before resolution.
		 * @param {Object} dependencies object.
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
		 * @name jqCommon.instanciateController
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description Instanciate a controller and returns a deferred to track when it's done.
		 * @param {string}
		 *           controller Controller's name.
		 * @param {array}
		 *           dependencies Dependencies to inject. Promises within dependencies will be resolved.
		 * @param {Object}
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
		 * @name jqCommon.getView
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description Returns a view, also instanciate a controller related to the view.
		 * @param {Object}
		 *           templateOptions Must contains template or templateUrl property.
		 * @param {string}
		 *           controller Controller's name.
		 * @param {array}
		 *           dependencies Dependencies to inject.
		 * @param {Object}
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
		 * @name jqCommon.getScopeData
		 * @module apx-jqwidgets.common
		 * @function
		 * 
		 * @description Get scope data value. Throw an exception if undefined.
		 * @param {string}
		 *           name data name within a scope.
		 * @param {Object}
		 *           scope scope containing the data.
		 * @returns {Object} Value.
		 */
		service.getScopeData = function(name, scope) {
			var value = $parse(name)(scope);
			if (angular.isUndefined(value)) {
				throw new Error('Undefined data in scope :' + name);
			}
			return value;
		};
		
		return service;
	};
})

	
/**
 * @ngdoc directive
 * @name jqScopeElement
 * @module apx-jqwidgets.common
 * @directive
 * 
 * @description Assign a DOM element to a scope.
 * 
 */
.directive("jqScopeElement", function() {
	return {
		restrict : "A",
		compile : function(tElement, tAttrs, transclude) {
			return {
				pre : function(scope, iElement, iAttrs) {
					scope[iAttrs.jqScopeElement] = iElement;
				}
			};
		}
	};
})

;