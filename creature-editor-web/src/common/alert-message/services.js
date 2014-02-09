(function() {
	'use strict';

	var module = angular.module('alertMessage.services', [ 'pascalprecht.translate' ]);

	module.factory('alertMessageService', [ '$interpolate', '$translate', function($interpolate, $translate) {

		var service = {};

		function addMessage(messageObj) {
			if (!angular.isObject(messageObj)) {
				throw new Error("Only object can be added to the alertMessages service");
			}
			service.messages.push(messageObj);
			return messageObj;
		}

		service.messages = [];

		service.empty = function() {
			return service.messages.length === 0;
		};

		service.push = function(key, type, params) {
			var message = {
				text : $translate(key, params),
				type : type
			};
			return addMessage(message);
		};

		service.pop = function() {
			return service.messages.length > 0 ? service.messages[0] : null;
		};

		service.remove = function() {
			service.messages.splice(0, 1);
		};

		service.removeAll = function() {
			service.messages = [];
		};

		return service;
	} ]);

})();