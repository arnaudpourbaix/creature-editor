(function() {
	'use strict';

	var module = angular.module('alertMessage.services', []);

	module.factory('i18nMessages', [ '$interpolate', 'I18N.MESSAGES', function($interpolate, i18nmessages) {
		return {
			get : function(msgKey, interpolateParams) {
				var msg = i18nmessages[msgKey];
				return msg ? $interpolate(msg)(interpolateParams) : '?' + msgKey + '?';
			}
		};
	} ]);

	module.factory('alertMessageService', [ '$rootScope', 'i18nMessages', function($rootScope, i18nMessages) {
		var addMessage = function(messageObj) {
			if (!angular.isObject(messageObj)) {
				throw new Error("Only object can be added to the alertMessages service");
			}
			service.messages.push(messageObj);
			return messageObj;
		};

		var service = {};

		service.messages = [];

		service.empty = function() {
			return service.messages.length === 0;
		};

		service.push = function(msgKey, type, interpolateParams, otherProperties) {
			var message = angular.extend({
				text : i18nMessages.get(msgKey, interpolateParams),
				type : type
			}, otherProperties);
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