(function() {
	'use strict';

	angular.module('parameter.services', [])

	.factory('parameterSettings', [ 'appSettings', function(appSettings) {
		return {
			url : appSettings.restBaseUrl + 'parameter/',
			typeUrl : appSettings.restBaseUrl + 'parameterType/'
		};
	} ])
	
	.factory('Parameter', function($resource, parameterSettings) {
		var res = $resource(parameterSettings.url + ':id', {}, {
			'save' : {
				method : 'PUT',
				params: { type: '@datatype' }
			}
		});

		res.prototype.$id = function() {
			return this.name;
		};

		return res;
	})

	.factory('ParameterType', function($resource, parameterSettings) {
		var res = $resource(parameterSettings.typeUrl + ':id', {}, {});

		return res;
	})
	
	.service('ParameterService', function($http, parameterSettings) {
		var service = {};
		
		service.checkFolder = function(folder) {
			return $http({
				method : 'GET',
				url : parameterSettings.url + 'checkFolder',
				params : {
					folder : folder
				}
			});
		};

		service.getById = function(params, id) {
			return _.find(params, function(param) { /* jshint -W116 */
				return param.$id() == id;
			});
		};
		
		return service;
	});

})();
