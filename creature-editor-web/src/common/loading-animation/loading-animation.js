(function() {
	'use strict';

	var module = angular.module('loading-animation', [ 'pascalprecht.translate' ]);

	function LoadingAnimationService(options, $timeout) {
		var minDuration = options.minDuration * 1000;
		var maxDuration = options.maxDuration * 1000;
		var activationDelay = options.activationDelay * 1000;
		var activationDelayPromise, minDurationPromise, maxDurationPromise;
		var stopPendingRequest = false;

		function cancelTimeout(promise) {
			if (promise) {
				$timeout.cancel(promise);
			}
		}

		function minDurationExpired() {
			if (service.running && stopPendingRequest) {
				stop();
			}
		}

		function maxDurationExpired() {
			if (service.running) {
				stop();
			}
		}

		function start() {
			service.running = true;
			activationDelayPromise = cancelTimeout(activationDelayPromise);
			if (minDuration) {
				minDurationPromise = $timeout(minDurationExpired, minDuration);
			}
			if (maxDuration) {
				maxDurationPromise = $timeout(maxDurationExpired, maxDuration);
			}
		}

		function stop() {
			// console.log('stop');
			cancelTimeout(minDurationPromise);
			cancelTimeout(maxDurationPromise);
			minDurationPromise = null;
			maxDurationPromise = null;
			service.running = false;
			stopPendingRequest = false;
		}

		function requestStop() {
			// console.log('request stop');
			if (minDurationPromise) {
				stopPendingRequest = true;
			} else {
				stop();
			}
		}

		var service = {
			running : false,
			start : function() {
				activationDelayPromise = $timeout(start, activationDelay);
			},
			stop : function() {
				activationDelayPromise = cancelTimeout(activationDelayPromise);
				if (service.running && !stopPendingRequest) {
					requestStop();
				}
			}
		};

		return service;
	}

	module.config([ '$translatePartialLoaderProvider', function LoadingAnimationTranslateConfig($translatePartialLoaderProvider) {
		$translatePartialLoaderProvider.addPart('common/loading-animation');
	} ]);

	module.provider('$loadingAnimation', function LoadingAnimationProvider() {
		var options = {
			activationDelay : 0.75, // delay before animation is triggered (in seconds)
			minDuration : 0, // minimum animation duration (in seconds)
			maxDuration : 0
		// maximum animation duration (in seconds)
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

		this.$get = [ '$timeout', function loadingAnimationService($timeout) {
			return new LoadingAnimationService(options, $timeout);
		} ];
	});

	module.controller('LoadingAnimationController', [ '$scope', '$rootScope', '$loadingAnimation',
			function LoadingAnimationController($scope, $rootScope, $loadingAnimation) {
				$scope.loadingAnimation = $loadingAnimation;

				$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
					$loadingAnimation.start();
				});

				$rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
					$loadingAnimation.stop();
				});

				$rootScope.$on('$viewContentLoaded', function() {
					$loadingAnimation.stop();
				});

			} ]);

})();