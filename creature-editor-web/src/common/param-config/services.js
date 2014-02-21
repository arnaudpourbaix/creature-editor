(function() {
	'use strict';

	var module = angular.module('param-config.services', [ 'ngResource' ]);

	module.factory('Parameter', [ '$resource', 'appSettings', function SpellFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'parameter/';

		var res = $resource(baseUrl + ':id', {}, {
			'save' : {
				method : 'PUT',
				params: { type: '@datatype' }
			}
		});

		res.prototype.$id = function() {
			return this.name;
		};

		return res;
	} ]);

	module.factory('ParameterType', [ '$resource', 'appSettings', function SpellFactory($resource, appSettings) {
		var baseUrl = appSettings.restBaseUrl + 'parameterType/';

		var res = $resource(baseUrl + ':id', {}, {});

		return res;
	} ]);

	module.service('ParameterService', [ '$http', '$interval', '$alertMessage', function ParameterService($http, $interval, $alertMessage) {
		var service = {
			checkFolder : function(folder) {
				return $http({
					method : 'GET',
					url : 'rest/parameter/checkFolder',
					params : {
						folder : folder
					}
				});
			}
		};

		return service;
	} ]);

})();
