/* global _ */

(function(_) {
	'use strict';

	var module = angular.module('param-config.services', [ 'ngResource' ]);

	module.factory('paramSettings', [ 'appSettings', function paramSettings(appSettings) {
		return {
			url : appSettings.restBaseUrl + 'parameter/',
			typeUrl : appSettings.restBaseUrl + 'parameterType/'
		};
	} ]);
	
	module.factory('Parameter', [ '$resource', 'paramSettings', function SpellFactory($resource, paramSettings) {
		var res = $resource(paramSettings.url + ':id', {}, {
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

	module.factory('ParameterType', [ '$resource', 'paramSettings', function SpellFactory($resource, paramSettings) {
		var res = $resource(paramSettings.typeUrl + ':id', {}, {});

		return res;
	} ]);
	
	module.service('ParameterService', [ '$http', 'paramSettings', function ParameterService($http, paramSettings) {
		var service = {};
		
		service.checkFolder = function(folder) {
			return $http({
				method : 'GET',
				url : paramSettings.url + 'checkFolder',
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
	} ]);

})(_);
