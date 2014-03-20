(function(window) {
	'use strict';

	var module = angular.module('translate-wrapper', [ 'pascalprecht.translate' ]);

	module.provider('$translateWrapper', function() {
		this.$get = [ '$translate', '$q', function($translate, $q) {
			function getTranslations(translationIds) {
				var promises = [];
				angular.forEach(translationIds, function(translationId) {
					promises.push($translate(translationId));
				});
				return $q.all(promises).then(function(translations) {
					var result = [];
					angular.forEach(translations, function(translation, index) {
						result[translationIds[index]] = translation;
					});
					return result;
				});
			}
			
			var $translateWrapper = function(translationId, interpolateParams, interpolationId) {
				if (angular.isArray(translationId)) {
					return getTranslations(translationId);
				}
				return $translate(translationId, interpolateParams, interpolationId);
			};
			return $translateWrapper;
		} ];
	});

}(window));
