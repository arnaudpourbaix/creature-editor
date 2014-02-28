/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.window', [ 'ui.router' ]);

	function JqWindowService(globalOptions, $injector, $rootScope, $q, $http, $templateCache, $controller, $compile, $interpolate, $state) {
		var windowInstances = [], windowSequence = 0;

		function log(message) {
			// console.debug(message);
		}

		function WindowWrapper(windowOptions, sequence) {
			var windowResultDeferred = $q.defer();
			var windowOpenedDeferred = $q.defer();
			var containerId, jqSelector, resultData;

			function getTemplatePromise(options) {
				return options.template ? $q.when(options.template) : $http.get(options.templateUrl, {
					cache : $templateCache
				}).then(function(result) {
					return result.data;
				});
			}

			function getResolvePromises(resolves) {
				var promisesArr = [];
				angular.forEach(resolves, function(value, key) {
					if (angular.isFunction(value) || angular.isArray(value)) {
						promisesArr.push($q.when($injector.invoke(value)));
					}
				});
				return promisesArr;
			}
			
			function instanciateController(windowOptions, windowScope, tplAndVars) {
				if (!windowOptions.controller) {
					return;
				}
				var resolveIter = 1; // 0 is the view template
				var ctrlLocals = {
					$scope : windowScope,
					$windowInstance : self
				};
				angular.forEach(windowOptions.resolve, function(value, key) {
					ctrlLocals[key] = tplAndVars[resolveIter++];
				});
				angular.forEach(windowOptions.inject, function(value, key) {
					ctrlLocals[key] = value;
				});
				$controller(windowOptions.controller, ctrlLocals);
			}
			
			function close() {
				jqSelector.remove();
				if (resultData) {
					windowResultDeferred.resolve(resultData);
				} else {
					windowResultDeferred.reject(resultData);
				}
			}
			
			function open(tplAndVars) {
				var windowScope = (windowOptions.scope || $rootScope).$new();
				instanciateController(windowOptions, windowScope, tplAndVars);

				var options = angular.extend({}, globalOptions, windowOptions.options, {
					title : windowOptions.title ? $interpolate(windowOptions.title)(windowScope) : '&nbsp;',
					content : $compile(tplAndVars[0])(windowScope)
				});
				containerId = 'jqWindow' + sequence;
				$(document.body).append('<div class="jq-window" id="' + containerId + '"><div></div><div></div></div>');
				jqSelector = $('#' + containerId);
				jqSelector.jqxWindow(options);
				jqSelector.on('close', function(event) {
					// windowScope.$apply(function() {
						close();
					// });
					return false;
				});
			}
			
			var self = {};

			self.result = windowResultDeferred.promise;
			self.opened = windowOpenedDeferred.promise;

			self.close = function(result) {
				resultData = result;
				jqSelector.jqxWindow('close');
			};

			self.dismiss = function(reason) {
				jqSelector.jqxWindow('close');
			};
			
			self.containerId = function() {
				return containerId;
			};
			
			(function constructor() {
				var templateAndResolvePromise = $q.all([ getTemplatePromise(windowOptions) ].concat(getResolvePromises(windowOptions.resolve)));
				templateAndResolvePromise.then(function resolveSuccess(tplAndVars) {
					open(tplAndVars);
					windowOpenedDeferred.resolve(true);
				}, function resolveError(reason) {
					windowOpenedDeferred.reject(false);
					windowResultDeferred.reject(reason);
				});
			})();
			
			return self;
		}

		var service = {
			open : function(windowOptions) {
				// verify options
				if (!windowOptions.template && !windowOptions.templateUrl) {
					throw new Error('One of template or templateUrl options is required.');
				}
				windowOptions.resolve = windowOptions.resolve || {};

				var sequence = windowSequence++;
				var windowInstance = new WindowWrapper(windowOptions, sequence);
				windowInstance.result.finally(function() {
					delete windowInstances[sequence];
				});
				windowInstances[sequence] = windowInstance;
				return windowInstance;
			},
			
			viewInstances: function() {
				console.log('current instances:');
				angular.forEach(windowInstances, function(value, key) {
					console.log(key, value.containerId());
				});
			}
		};

		return service;
	}

	module.provider('$jqWindow', function JqWindowProvider() {
		var options = {
			theme : 'bootstrap',
			showCollapseButton : true,
			isModal : true
		};

		this.theme = function(value) {
			options.theme = value;
		};

		this.showCollapseButton = function(value) {
			options.showCollapseButton = value;
		};

		this.isModal = function(value) {
			options.isModal = value;
		};

		this.$get = [ '$injector', '$rootScope', '$q', '$http', '$templateCache', '$controller', '$compile', '$interpolate', '$state',
				function jqWindowService($injector, $rootScope, $q, $http, $templateCache, $controller, $compile, $interpolate, $state) {
					return new JqWindowService(options, $injector, $rootScope, $q, $http, $templateCache, $controller, $compile, $interpolate, $state);
				} ];
	});
	
}(window, jQuery));