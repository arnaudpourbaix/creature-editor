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
			
//			function getResolvePromises(resolves) {
//				var promisesArr = [];
//				angular.forEach(resolves, function(value, key) {
//					if (angular.isFunction(value) || angular.isArray(value)) {
//						promisesArr.push($q.when($injector.invoke(value)));
//					}
//				});
//				return promisesArr;
//			}
//			
//			function instanciateController(windowOptions, windowScope, tplAndVars) {
//				if (!windowOptions.controller) {
//					return;
//				}
//				var resolveIter = 2; // 0: view template, 1: title
//				var ctrlLocals = {
//					$scope : windowScope,
//					$windowInstance : self
//				};
//				angular.forEach(windowOptions.resolve, function(value, key) {
//					ctrlLocals[key] = tplAndVars[resolveIter++];
//				});
//				angular.forEach(windowOptions.inject, function(value, key) {
//					ctrlLocals[key] = value;
//				});
//				$controller(windowOptions.controller, ctrlLocals);
//			}
			
			function close() {
				jqSelector.remove();
				if (resultData) {
					windowResultDeferred.resolve(resultData);
				} else {
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
				console.log('final settings', settings);
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
			
			(function constructor() {
				windowScope = windowOptions.scope || $rootScope.$new();
				if (windowOptions.element) { // window opened by directive
					console.log('window opened by directive');
					open();
				} else {
					console.log('window opened by code');
					//$q.when(windowOptions.resolve)
					$jqCommon.getView(windowOptions, windowOptions.controller, windowOptions.resolve, windowScope).then(function(view) {
						angular.extend(windowOptions, {
							//title : tplAndVars[1],
							content : view
						});
						var containerId = 'jqWindow' + windowOptions.sequence;
						$(document.body).append('<div class="jq-window" id="' + containerId + '"><div></div><div></div></div>');
						windowOptions.element = $('#' + containerId);
						open();
					});
					
//					var templateAndResolvePromise = $q.all([ getTemplatePromise(windowOptions) ].concat([ getResolveTitle(windowOptions.title) ]).concat(getResolvePromises(windowOptions.resolve)));
//					templateAndResolvePromise.then(function resolveSuccess(tplAndVars) {
//						open(tplAndVars);
//						windowOpenedDeferred.resolve(true);
//					}, function resolveError(reason) {
//						windowOpenedDeferred.reject(false);
//						windowResultDeferred.reject(reason);
//					});
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
			console.log('settings', settings);
			new WindowWrapper(settings);
		};
		
		return service;
	} ]);
	
	module.directive('jqWindow', [ '$compile', '$timeout', '$jqCommon', '$jqWindow', function JqSplitterDirective($compile, $timeout, $jqCommon, $jqWindow) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var params = scope.$eval(attributes.jqWindow);
				$jqWindow.create(element, scope, params);
			}
		};
	} ]);
	
}(window, jQuery));