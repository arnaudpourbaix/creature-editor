angular.module('editor.main.controllers', [])

/**
 * @ngdoc controller
 * @name editor.main.controller:MainController
 * @requires $scope
 * @requires $rootScope
 * @requires $state
 * @requires $stateParams
 * @requires an-log4javascript.$anLogger
 * @requires an-error.$anError
 * @requires editor.user.UserService
 * @requires editor.pubca.service:PubcaContext
 * @description Application main controller, it contains authentication control, restart application method and route logging.
 * When controller is instancied, it tries to authentificate user and open an error modal if it fails.
 */
.controller('MainController', function($scope, $rootScope, $state, $stateParams, $anLogger, $anError, appSettings) {
	"use strict";
	var LOGGER_NAME = 'editor.main.controllers.MainController', log = $anLogger.logger(LOGGER_NAME);
	
	/**
	 * @ngdoc event
	 * @name $stateChangeStart
	 * @eventOf editor.main.controller:MainController
	 * @description Listen to **$stateChangeStart** event and ensure that user has been retrieved before going on.
	 * If not, state changed is delayed until it is done.
	 */	
	$scope.$on('$stateChangeStart', function(event, toState, toParams) {
		log.trace('$stateChangeStart', toState.name, 'start');
	});
	
	/**
	 * @ngdoc event
	 * @name $stateChangeSuccess
	 * @eventOf editor.main.controller:MainController
	 * @description Listen to **$stateChangeSuccess** event. Do nothing beside logging.
	 */	
	$scope.$on('$stateChangeSuccess', function(event, toState) {
		log.trace('$stateChangeSuccess', toState.name, 'success');
	});

	/**
	 * @ngdoc event
	 * @name $stateChangeError
	 * @eventOf editor.main.controller:MainController
	 * @description Listen to **$stateChangeError** event. Display error modal if not already visible.
	 */	
	$scope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
		if (!$anError.visible) { // don't add another error if error modal is already opened
			log.error('$stateChangeError', toState.name, error);
		}
	});

	/**
	 * @ngdoc event
	 * @name $viewContentLoading
	 * @eventOf editor.main.controller:MainController
	 * @description Listen to **$viewContentLoading** event. Do nothing beside logging.
	 */	
	$scope.$on('$viewContentLoading ', function() {
		log.trace('$viewContentLoading ', 'success');
	});

	/**
	 * @ngdoc event
	 * @name $viewContentLoaded
	 * @eventOf editor.main.controller:MainController
	 * @description Listen to **$viewContentLoaded** event. Do nothing beside logging.
	 */	
	$scope.$on('$viewContentLoaded', function() {
		log.trace('$viewContentLoaded', 'success');
	});

	/**
	 * @ngdoc event
	 * @name restart-application
	 * @eventOf editor.main.controller:MainController
	 * @description Listen to **restart-application** event. Restart application by loading initial context.
	 */	
	$scope.$on('restart-application', function() {
	});
	
});
