/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.window', [ 'jqwidgets.common', 'ui.router' ]);
	
	module.service('$jqWindow', [ '$jqCommon', '$injector', '$rootScope', '$q', '$http', '$templateCache', '$controller', '$compile', '$interpolate', '$state', function jqWindowService($jqCommon, $injector, $rootScope, $q, $http, $templateCache, $controller, $compile, $interpolate, $state) {
		var options = {
			showCollapseButton : true,
			isModal : true
		};
		var windowInstances = [], windowSequence = 0;

		function WindowWrapper(windowOptions) {
			var windowResultDeferred = $q.defer();
			var windowOpenedDeferred = $q.defer();
			var jqSelector, windowScope, resultData;

			function getResolveTitle(title) {
				// can resolve function that return a text or a promise, but it doesn't support dependencies injection yet
				if (angular.isFunction(title)) {
					return $q.when(title());
				} else {
					return $q.when(title);
				}
			}
			
			function close() {
				jqSelector.remove();
				if (resultData) {
					console.log('close window');
					windowResultDeferred.resolve(resultData);
				} else {
					console.log('dismiss window');
					windowResultDeferred.reject(resultData);
				}
			}
			
			function open() {
				var settings = angular.extend({}, $jqCommon.options(), options, windowOptions.options);
				if (settings.height > 600) {
					settings.maxHeight = settings.height;
				}
				if (settings.width > 600) {
					settings.maxWidth = settings.width;
				}
				jqSelector = windowOptions.element;
				jqSelector.jqxWindow(settings);
				jqSelector.on('close', function(event) {
					if (!windowScope.$$phase) {
						windowScope.$apply(function() {
							close();
						});
					} else {
						close();
					}
					return false;
				});
				windowOpenedDeferred.resolve(true);
			}
			
			var self = {};

			self.result = windowResultDeferred.promise;
			self.opened = windowOpenedDeferred.promise;

			self.close = function(result) {
				resultData = result || true;
				jqSelector.jqxWindow('close');
			};

			self.dismiss = function(reason) {
				jqSelector.jqxWindow('close');
			};
			
			(function constructor() {
				windowScope = windowOptions.scope || $rootScope.$new();
				if (windowOptions.element) { // window opened by directive
					windowOptions.instance = self;
					open();
				} else {
					var deps = angular.extend({}, windowOptions.resolve, { $windowInstance: self });
					$jqCommon.getView(windowOptions, windowOptions.controller, deps, windowScope).then(function(view) {
						angular.extend(windowOptions.options, {
							content : view
						});
						var containerId = 'jqWindow' + windowOptions.sequence;
						$(document.body).append('<div class="jq-window" id="' + containerId + '"><div></div><div></div></div>');
						windowOptions.element = $('#' + containerId);
						open();
					}, function(error) {
						windowOpenedDeferred.reject(error);
						windowResultDeferred.reject(error);
					});
				}
			})();
			
			return self;
		}

		var service = {};
		
		service.options = function() {
			return options;
		};
		
		service.open = function(windowOptions) {
			var settings = $jqCommon.getParams(windowOptions, 'options');
			$jqCommon.checkTemplateParams(settings);
			angular.extend(settings, {
				sequence: windowSequence++
			});
			var windowInstance = new WindowWrapper(settings);
			windowInstance.result.finally(function() {
				delete windowInstances[settings.sequence];
			});
			windowInstances[settings.sequence] = windowInstance;
			return windowInstance;
		};

		service.create = function(element, scope, windowOptions) {
			var settings = $jqCommon.getParams(windowOptions, 'options');
			angular.extend(settings, {
				scope: scope,
				element: element
			});
			return new WindowWrapper(settings);
		};
		
		return service;
	} ]);
	
	module.directive('jqWindow', [ '$compile', '$timeout', '$jqCommon', '$jqWindow', function JqSplitterDirective($compile, $timeout, $jqCommon, $jqWindow) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var params = scope.$eval(attributes.jqWindow);
				var windowInstance = $jqWindow.create(element, scope, params);
				//scope.$parent[params].instance = windowInstance;
				params.instance = windowInstance;
			}
		};
	} ]);
	
}(window, jQuery));