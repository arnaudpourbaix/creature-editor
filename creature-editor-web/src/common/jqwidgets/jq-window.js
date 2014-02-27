/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.window', [ 'ui.router' ]);

	function JqWindowService(globalOptions, $injector, $rootScope, $q, $http, $templateCache, $controller, $compile, $interpolate, $state) {

		function log(message) {
			// console.debug(message);
		}

		function ModalWrapper(modalOptions) {
			var modalResultDeferred = $q.defer();
			var modalOpenedDeferred = $q.defer();

			var self = {};

			self.result = modalResultDeferred.promise;
			self.opened = modalOpenedDeferred.promise;

			self.open = function() {
				var modalScope = (modalOptions.scope || $rootScope).$new();
				var ctrlInstance, ctrlLocals = {};
				var resolveIter = 1;
				// controllers
				if (modalOptions.controller) {
					ctrlLocals.$scope = modalScope;
					ctrlLocals.$modalInstance = modalInstance;
					angular.forEach(modalOptions.resolve, function(value, key) {
						ctrlLocals[key] = tplAndVars[resolveIter++];
					});
					ctrlInstance = $controller(modalOptions.controller, ctrlLocals);
				}

				var options = angular.extend({}, globalOptions, modalOptions.options, {
					title : modalOptions.title ? $interpolate(modalOptions.title)(modalScope) : '&nbsp;',
					content : $compile(tplAndVars[0])(modalScope)
				});

				$(document.body).append('<div id="newWindow"><div></div><div></div></div>');
				var modalWindow = $('#newWindow');
				modalWindow.jqxWindow(options);
				modalWindow.on('close', function(event) {
					modalScope.$apply(function() {
						console.log('event', event);
						modalWindow.remove();
						modalResultDeferred.resolve();
					});
				});
			};

			self.close = function(result) {
				console.log('close request', result);
				// $jqWindowStack.close(modalInstance, result);
			};

			self.dismiss = function(reason) {
				console.log('dismiss request', reason);
				// $jqWindowStack.dismiss(modalInstance, reason);
			};
		}

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

		var service = {
			open : function(modalOptions) {
				// verify options
				if (!modalOptions.template && !modalOptions.templateUrl) {
					throw new Error('One of template or templateUrl options is required.');
				}
				modalOptions.resolve = modalOptions.resolve || {};

				var modalInstance = new ModalWrapper(modalOptions);

				var templateAndResolvePromise = $q.all([ getTemplatePromise(modalOptions) ].concat(getResolvePromises(modalOptions.resolve)));

				templateAndResolvePromise.then(function resolveSuccess(tplAndVars) {
					modalInstance.open();
					modalOpenedDeferred.resolve(true);
				}, function resolveError(reason) {
					modalResultDeferred.reject(reason);
					modalOpenedDeferred.reject(false);
				});

				return modalInstance;
			},

			stop : function() {
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

		this.activationDelay = function(value) {
			options.activationDelay = value;
		};

		this.minDuration = function(value) {
			options.minDuration = value;
		};

		this.maxDuration = function(value) {
			options.maxDuration = value;
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