(function() {
	'use strict';

	var module = angular.module('loading-animation', [ 'pascalprecht.translate' ]);

	function LoadingAnimationService(options, $timeout) {
		var minDuration = options.minDuration * 1000;
		var maxDuration = options.maxDuration * 1000;
		var activationDelay = options.activationDelay * 1000;
		var activationDelayPromise, minDurationPromise, maxDurationPromise;
		var stopPendingRequest = false;
		
		function log(message) {
			//console.debug(message);
		}

		function cancelTimeout(promise) {
			if (promise) {
				$timeout.cancel(promise);
			}
			return null;
		}

		function minDurationExpired() {
			log('minDurationExpired');
		}

		function maxDurationExpired() {
			log('maxDurationExpired');
			if (service.running) {
				stop();
			}
		}

		function start() {
			log('start');
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
			if (!service.running) {
				return;
			}
			log('stop');
			minDurationPromise = cancelTimeout(minDurationPromise);
			maxDurationPromise = cancelTimeout(maxDurationPromise);
			service.running = false;
			stopPendingRequest = false;
		}

		function requestStop() {
			log('request stop');
			if (minDurationPromise) {
				stopPendingRequest = true;
				minDurationPromise.then(stop);
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
			minDuration : 1, // minimum animation duration (in seconds)
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
					//console.info('$stateChangeSuccess', toState.name, 'success');
					$loadingAnimation.stop();
				});
				
				$rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
					console.error('$stateChangeError', toState.name, error);
					$loadingAnimation.stop();
				});

				$rootScope.$on('$viewContentLoaded', function() {
					//console.info('$viewContentLoaded', 'success');
					$loadingAnimation.stop();
				});

			} ]);

})();