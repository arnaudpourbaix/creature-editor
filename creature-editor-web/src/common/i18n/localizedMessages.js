(function() {
	'use strict';

	var i18n = angular.module('i18n.services', []);

	i18n.factory('localizedMessages', [ '$interpolate', 'I18N.MESSAGES', function($interpolate, i18nmessages) {

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

})();