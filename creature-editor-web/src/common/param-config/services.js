(function() {
	'use strict';

	var module = angular.module('param-config.services', [ 'ngResource' ]);

	module.factory('Parameter', [ '$resource', 'appSettings', function SpellFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'parameter/';

		var res = $resource(baseUrl + ':id', {}, {
			'save' : {
				method : 'PUT'
			}
		});

		res.prototype.$id = function() {
			return this.id;
		};

		return res;
	} ]);

	module.factory('ParameterType', [ '$resource', 'appSettings', function SpellFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'parameterType/';

		var res = $resource(baseUrl + ':id', {}, {});

		return res;
	} ]);

})();
