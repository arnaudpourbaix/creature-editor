/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.window', [ 'ui.router' ]);
	
	module.provider('$jqWindow', function () {
		var $jqWindowProvider = {
				options: {
					theme : 'bootstrap',
					showCollapseButton : true,
					isModal: true
				},
			$get: ['$injector', '$rootScope', '$q', '$http', '$templateCache', '$controller', '$compile', '$interpolate', '$state', function ($injector, $rootScope, $q, $http, $templateCache, $controller, $compile, $interpolate, $state) {
				var $jqWindow = {}, number = 0;

				function getTemplatePromise(options) {
					return options.template ? $q.when(options.template) :	$http.get(options.templateUrl, {cache: $templateCache}).then(function (result) { return result.data; });
				}

				function getResolvePromises(resolves) {
					var promisesArr = [];
					angular.forEach(resolves, function (value, key) {
						if (angular.isFunction(value) || angular.isArray(value)) {
							promisesArr.push($q.when($injector.invoke(value)));
						}
					});
					return promisesArr;
				}

				$jqWindow.open = function (modalOptions) {
					var modalResultDeferred = $q.defer();
					var modalOpenedDeferred = $q.defer();
					// prepare an instance of a modal to be injected into controllers and returned to a caller
					var modalInstance = {
							result: modalResultDeferred.promise,
							opened: modalOpenedDeferred.promise,
							close: function (result) {
								console.log('close request');
								//$modalStack.close(modalInstance, result);
							},
							dismiss: function (reason) {
								console.log('dismiss request');
								//$modalStack.dismiss(modalInstance, reason);
							}
					};

					// verify options
					modalOptions.resolve = modalOptions.resolve || {};
					if (!modalOptions.template && !modalOptions.templateUrl) {
						throw new Error('One of template or templateUrl options is required.');
					}

					var templateAndResolvePromise = $q.all([getTemplatePromise(modalOptions)].concat(getResolvePromises(modalOptions.resolve)));

					templateAndResolvePromise.then(function resolveSuccess(tplAndVars) {
						var modalScope = (modalOptions.scope || $rootScope).$new();
						var ctrlInstance, ctrlLocals = {};
						var resolveIter = 1;
						// controllers
						if (modalOptions.controller) {
							ctrlLocals.$scope = modalScope;
							angular.forEach(modalOptions.resolve, function (value, key) {
								ctrlLocals[key] = tplAndVars[resolveIter++];
							});
							ctrlInstance = $controller(modalOptions.controller, ctrlLocals);
						}
						
						var content = $compile(tplAndVars[0])(modalScope);
						var title = modalOptions.title ? $interpolate(modalOptions.title)(modalScope) : '&nbsp;';
						var options = angular.extend({}, $jqWindowProvider.options, modalOptions.options, { title: title, content: content });
						
						$(document.body).append('<div id="newWindow"><div></div><div></div></div>');
						$('#newWindow').jqxWindow(options);
						$('#newWindow').on('close', function (event) {
							modalScope.$apply(function() {
								console.log("close modal");
								$('#newWindow').remove();
								$state.go('^');
							});
						});
					}, function resolveError(reason) {
						modalResultDeferred.reject(reason);
					});

					templateAndResolvePromise.then(function () {
						modalOpenedDeferred.resolve(true);
					}, function () {
						modalOpenedDeferred.reject(false);
					});

					return modalInstance;
				};

				return $jqWindow;
			}]
		};
		return $jqWindowProvider;
	});

}(window, jQuery));