(function() {
	'use strict';

	var module = angular.module('alert-message', [ 'pascalprecht.translate', 'ui.bootstrap', 'ngAnimate' ]);

	function AlertMessageService(options, $translate, $timeout) {
		var showDuration = options.showDuration * 1000;
		var fadeOutDelay = options.fadeOutDelay * 1000;
		var messages = [];
		var currentDisplayedMessage;
		var showDisplayedMessage = false;
		
		function log(message) {
			//console.debug(message);
		}

		function empty() {
			return messages.length === 0;
		}

		function push(key, type, params) {
			return $translate(key, params).then(function(text) {
				var message = {
						text : text,
						type : type
				};
				messages.push(message);
				log('push: ' + message.text);
			});
		}

		function pop() {
			if (empty()) {
				return null;
			}
			var message = messages[0];
			messages.splice(0, 1);
			return message;
		}

		function removeAll() {
			messages = [];
		}
		
		function displayMessage() {
			if (currentDisplayedMessage) {
				log('already displaying message: ' + currentDisplayedMessage.text);
				return;
			}
			var message = pop();
			if (message == null) {
				log('no message in queue !');
				return;
			}
			startDisplayingMessage(message);
		}

		function startDisplayingMessage(message) {
			currentDisplayedMessage = message;
			showDisplayedMessage = true;
			log('displaying: ' + currentDisplayedMessage.text);
			$timeout(hideDisplayedMessage, showDuration);
		}

		function hideDisplayedMessage() {
			if (!showDisplayedMessage) {
				return;
			}
			log('hiding message: ' + currentDisplayedMessage.text);
			showDisplayedMessage = false;
			$timeout(removeDisplayedMessage, fadeOutDelay);
		}
		
		function removeDisplayedMessage() {
			if (!currentDisplayedMessage) {
				return;
			}
			log('remove message: ' + currentDisplayedMessage.text);
			currentDisplayedMessage = null;
			displayMessage();
		}
		
		var service = {
			showDuration : function() {
				return showDuration;
			},

			push : function(key, type, params) {
				push(key, type, params).then(displayMessage);
			},
			
			showDisplayedMessage : function() {
				return showDisplayedMessage;
			},
			
			currentDisplayedMessage : function() {
				return currentDisplayedMessage;
			},

			removeAll : function() {
				removeAll();
			}
		};

		return service;
	}

	module.provider('$alertMessage', function AlertMessageProvider() {
		var options = {
			showDuration : 4,
			fadeOutDelay : 1
		};

		this.showDuration = function(value) {
			options.showDuration = value;
		};

		this.fadeOutDelay = function(value) {
			options.fadeOutDelay = value;
		};
		
		this.$get = [ '$translate', '$timeout', function alertMessageService($translate, $timeout) {
			return new AlertMessageService(options, $translate, $timeout);
		} ];
	});
	
	module.controller('AlertMessageController', [ '$scope', '$timeout', '$alertMessage', function AlertMessageController($scope, $timeout, $alertMessage) {
		$scope.alertMessage = $alertMessage;
	} ]);
	
})();