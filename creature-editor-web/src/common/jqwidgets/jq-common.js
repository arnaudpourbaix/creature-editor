angular.module('apx-jqwidgets.common', [])

/**
 * @ngdoc service
 * @name apx-jqwidgets.jqCommonProvider
 * @description
 * Use `jqCommonProvider` to change the default behavior of the {@link  apx-jqwidgets.jqCommon jqCommon} service.
 * # Example
 * <pre>
 * angular.module('main.config').config(function(jqCommonProvider) {
 *    jqCommon.defaults.theme = 'bootstrap';
 * });
 * </pre>
 */
.provider('jqCommon', function() {
	/**
	 * @ngdoc property
	 * @name apx-jqwidgets.jqCommonProvider#defaults
	 * @propertyOf apx-jqwidgets.jqCommonProvider
	 * @description Object containing default values for {@link apx-jqwidgets.jqCommon jqCommon}. The object has following properties:
	 * 
	 * - **theme** - `{string}` - Default theme used by all widgets.<br>
	 * 
	 * @returns {Object} Default values object.
	 */		
	var defaults = this.defaults = {
		theme : 'classic'
	};

	/**
	 * @ngdoc service
	 * @name apx-jqwidgets.jqCommon
     * @requires $q
     * @requires $http
     * @requires $templateCache
     * @requires $rootScope
     * @requires $controller
     * @requires $compile
     * @requires $injector
     * @requires $parse
	 * @description
	 * # jqCommon
	 * This service contains common functions used by all widgets.<br>
	 */
	this.$get = function($q, $http, $templateCache, $rootScope, $controller, $compile, $injector, $parse) {
		var service = {};

		service.defaults = function() {
			return defaults;
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqCommon#getParams
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Returns params object after controlling required properties and adding missing optional properties.
		 * Throws an exception if any required param is missing.
		 * @param {Object} params Object to control.
		 * @param {array=} requiredProps Required properties.
		 * @param {array=} optionalProps Optional properties.
		 * @returns {Object} Object including missing optional properties.
		 */
		service.getParams = function(params, requiredProps, optionalProps) {
			var result = angular.copy(params);
			service.checkRequiredParams(result, requiredProps);
			service.checkOptionnalParams(result, optionalProps);
			return result;
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqCommon#checkRequiredParams
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Controls required properties. Throws an exception if any required param is missing.
		 * @param {Object} params Object to control.
		 * @param {array|string=} requiredProps Required properties.
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
		 * @name apx-jqwidgets.jqCommon#checkOptionnalParams
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Add optional properties missing in params.
		 * @param {Object} params Object to control.
		 * @param {array|string=} optionalProps Optional properties array.
		 */
		service.checkOptionnalParams = function(params, optionalProps) {
			if (!optionalProps) {
				return;
			}
			var props = angular.isArray(optionalProps) ? optionalProps : [].concat(optionalProps);
			angular.forEach(props, function(prop) {
				params[prop] = params[prop] || null;
			});
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqCommon#checkTemplateParams
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Controls that template is properly specified (must have template or templateUrl property). Throws an exception if none or both are defined.
		 * @param {Object} params Object to control.
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
		 * @name apx-jqwidgets.jqCommon#getTemplatePromise
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Get template promise from a template string or url. Throws an exception if template options are not valid.
		 * @param {Object} options Must contains template or templateUrl property.
		 * @returns {Object} Template's promise.
		 */
		service.getTemplatePromise = function(options) {
			service.checkTemplateParams();
			return options.template ? $q.when(options.template) : $http.get(options.templateUrl, {
				cache : $templateCache
			}).then(function(result) {
				return result.data;
			});
		};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqCommon#resolveDependencies
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Resolve all dependencies and returns a promise containing a resolved object.
		 * @param {Object=} dependencies Dependencies object. Each dependency can be a value or a promise.
		 * @returns {Object} Resolved dependencies object.
		 */
		service.resolveDependencies = function(dependencies) {
			if (!dependencies) {
				return $q.when();
			}
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
		 * @name apx-jqwidgets.jqCommon#instanciateController
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Instanciate a controller and returns a promise to track when it's done.
		 * @param {string=} controller Controller's name.
		 * @param {array=} dependencies Dependencies to inject in controller. Promises within dependencies will be resolved before injection.
		 * @param {Object=} scope Controller's scope. If not provided, a new scope is created from root scope.
		 * @returns {Object} Promise resolved when controller is instanciated
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
		 * @name apx-jqwidgets.jqCommon#getView
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Compile and returns a view. It will also instanciate a controller if provided.
		 * Throws an exception if template options are not valid.
		 * @param {Object} templateOptions Must contains template or templateUrl property.
		 * @param {string=} controller Controller's name.
		 * @param {array=} dependencies Dependencies to inject.
		 * @param {Object=} scope Controller's scope, if not provided, a new scope is created from root scope.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		service.getView = function(templateOptions, controller, dependencies, scope) {
			var $scope = scope || $rootScope.$new();
			return $q.all([ service.getTemplatePromise(templateOptions), service.instanciateController(controller, dependencies, $scope)]).then(function(result) {
				return $compile(result[0])($scope);
			});
		};
		
		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqCommon#getScopeData
		 * @methodOf apx-jqwidgets.jqCommon
		 * @description Get scope data value. Throw an exception if undefined.
		 * @param {string} name data name within a scope.
		 * @param {Object} scope scope containing the data.
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
 * @name apx-jqwidgets.directive:jqScopeElement
 * @restrict A
 * @priority 0
 * @description Assign a DOM element to a scope.
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