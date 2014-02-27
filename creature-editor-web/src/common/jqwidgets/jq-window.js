/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.window', [ 'ui.router' ]);

	function JqWindowService(globalOptions, $injector, $rootScope, $q, $http, $templateCache, $controller, $compile, $interpolate, $state) {
		var windowInstances = [], windowSequence = 0, containerId, jqSelector;

		function log(message) {
			// console.debug(message);
		}

		function WindowWrapper(windowOptions, sequence) {
			var windowResultDeferred = $q.defer();
			var windowOpenedDeferred = $q.defer();

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
				$controller(windowOptions.controller, ctrlLocals);
			}
			
			function open(tplAndVars) {
				var windowScope = (windowOptions.scope || $rootScope).$new();
				instanciateController(windowOptions, windowScope, tplAndVars);

				var options = angular.extend({}, globalOptions, windowOptions.options, {
					title : windowOptions.title ? $interpolate(windowOptions.title)(windowScope) : '&nbsp;',
					content : $compile(tplAndVars[0])(windowScope)
				});
				containerId = 'jqWindow' + sequence;
				$(document.body).append('<div id="' + containerId + '"><div></div><div></div></div>');
				jqSelector = $('#' + containerId);
				console.log(jqSelector);
				jqSelector.jqxWindow(options);
				jqSelector.on('close', function(event) {
					windowScope.$apply(function() {
						console.log('close event', jqSelector, event);
						jqSelector.remove();
						windowResultDeferred.resolve();
						return false;
					});
				});
			}
			
			var self = {};

			self.result = windowResultDeferred.promise;
			self.opened = windowOpenedDeferred.promise;

			self.close = function(result) {
				console.log('close request', sequence, result);
				// $jqWindowStack.close(windowInstance, result);
			};

			self.dismiss = function(reason) {
				console.log('dismiss request', sequence, reason);
				// $jqWindowStack.dismiss(windowInstance, reason);
			};
			
			(function constructor() {
				console.log('request open window', sequence);
				var templateAndResolvePromise = $q.all([ getTemplatePromise(windowOptions) ].concat(getResolvePromises(windowOptions.resolve)));
				templateAndResolvePromise.then(function resolveSuccess(tplAndVars) {
					console.log('-> window open', sequence);
					open(tplAndVars);
					windowOpenedDeferred.resolve(true);
				}, function resolveError(reason) {
					console.error('-> fail opening window', sequence);
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
				//windowInstances[sequence] = windowInstance;
				return windowInstance;
			}
		};

		return service;
	}

	module.provider('$jqWindow', function JqWindowProvider() {
		var options = {
			theme : 'bootstrap',
			showCollapseButton : true,
			isModal : false
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

	module.factory('$jqWindowStack', [ '$transition', '$timeout', '$document', '$compile', '$rootScope', '$$stackedMap',
			function($transition, $timeout, $document, $compile, $rootScope, $$stackedMap) {

				var OPENED_MODAL_CLASS = 'modal-open';

				var backdropDomEl, backdropScope;
				var openedWindows = $$stackedMap.createNew();
				var $jqWindowStack = {};

				function backdropIndex() {
					var topBackdropIndex = -1;
					var opened = openedWindows.keys();
					for (var i = 0; i < opened.length; i++) {
						if (openedWindows.get(opened[i]).value.backdrop) {
							topBackdropIndex = i;
						}
					}
					return topBackdropIndex;
				}

				$rootScope.$watch(backdropIndex, function(newBackdropIndex) {
					if (backdropScope) {
						backdropScope.index = newBackdropIndex;
					}
				});

				function removeModalWindow(modalInstance) {

					var body = $document.find('body').eq(0);
					var modalWindow = openedWindows.get(modalInstance).value;

					// clean up the stack
					openedWindows.remove(modalInstance);

					// remove window DOM element
					body.toggleClass(OPENED_MODAL_CLASS, openedWindows.length() > 0);
				}

				function checkRemoveBackdrop() {
					// remove backdrop if no longer needed
					if (backdropDomEl && backdropIndex() === -1) {
						var backdropScopeRef = backdropScope;
						backdropScopeRef.$destroy();
						backdropScopeRef = null;
						backdropDomEl = undefined;
						backdropScope = undefined;
					}
				}

				$document.bind('keydown', function(evt) {
					var modal;

					if (evt.which === 27) {
						modal = openedWindows.top();
						if (modal && modal.value.keyboard) {
							$rootScope.$apply(function() {
								$jqWindowStack.dismiss(modal.key);
							});
						}
					}
				});

				$jqWindowStack.open = function(modalInstance, modal) {

					openedWindows.add(modalInstance, {
						deferred : modal.deferred,
						modalScope : modal.scope,
						backdrop : modal.backdrop,
						keyboard : modal.keyboard
					});

					var body = $document.find('body').eq(0), currBackdropIndex = backdropIndex();

					if (currBackdropIndex >= 0 && !backdropDomEl) {
						backdropScope = $rootScope.$new(true);
						backdropScope.index = currBackdropIndex;
						backdropDomEl = $compile('<div modal-backdrop></div>')(backdropScope);
						body.append(backdropDomEl);
					}

					var angularDomEl = angular.element('<div modal-window></div>');
					angularDomEl.attr('window-class', modal.windowClass);
					angularDomEl.attr('index', openedWindows.length() - 1);
					angularDomEl.attr('animate', 'animate');
					angularDomEl.html(modal.content);

					var modalDomEl = $compile(angularDomEl)(modal.scope);
					openedWindows.top().value.modalDomEl = modalDomEl;
					body.append(modalDomEl);
					body.addClass(OPENED_MODAL_CLASS);
				};

				$jqWindowStack.close = function(modalInstance, result) {
					var modalWindow = openedWindows.get(modalInstance).value;
					if (modalWindow) {
						modalWindow.deferred.resolve(result);
						removeModalWindow(modalInstance);
					}
				};

				$jqWindowStack.dismiss = function(modalInstance, reason) {
					var modalWindow = openedWindows.get(modalInstance).value;
					if (modalWindow) {
						modalWindow.deferred.reject(reason);
						removeModalWindow(modalInstance);
					}
				};

				$jqWindowStack.dismissAll = function(reason) {
					var topModal = this.getTop();
					while (topModal) {
						this.dismiss(topModal.key, reason);
						topModal = this.getTop();
					}
				};

				$jqWindowStack.getTop = function() {
					return openedWindows.top();
				};

				return $jqWindowStack;
			} ]);

}(window, jQuery));