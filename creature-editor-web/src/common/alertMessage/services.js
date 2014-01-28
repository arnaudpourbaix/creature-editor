(function() {
	'use strict';

	var module = angular.module('alertMessage.services', []);

	module.factory('i18nMessages', [ '$interpolate', 'I18N.MESSAGES', function($interpolate, i18nmessages) {

		var handleNotFound = function(msg, msgKey) {
			return msg || '?' + msgKey + '?';
		};

		return {
			get : function(msgKey, interpolateParams) {
				var msg = i18nmessages[msgKey];
				if (msg) {
					return $interpolate(msg)(interpolateParams);
				} else {
					return handleNotFound(msg, msgKey);
				}
			}
		};
	} ]);

	module.factory('alertMessages', [ '$rootScope', 'i18nMessages', function($rootScope, i18nMessages) {

		var messages = [];
		var service = {};

		var prepareNotification = function(msgKey, type, interpolateParams, otherProperties) {
			return angular.extend({
				message : i18nMessages.get(msgKey, interpolateParams),
				type : type
			}, otherProperties);
		};

		var addMessage = function(messageObj) {
			if (!angular.isObject(messageObj)) {
				throw new Error("Only object can be added to the alertMessages service");
			}
			messages.push(messageObj);
			return messageObj;
		};

		service.push = function(message) {
			return addMessage(message);
		};

		service.remove = function(message) {
			var idx = messages.indexOf(message);
			if (idx > -1) {
				messages.splice(idx, 1);
			}
		};

		service.removeAll = function() {
			messages = [];
		};

		return service;
	} ]);

})();