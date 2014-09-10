/**
 * @ngdoc overview
 * @name an-error
 * @requires pascalprecht-translate
 * @description
 * # an-error
 * This module is about exception's handling and error's processing. When any error or exception is thrown, it is catched by this module. 
 * An error modal will be shown with a generic error message and eventually a technical error message. 
 * At the same time, error can be sent to server with an ajax appender. 
 * Finally, user can try again to reload current location or restart application by emitting an event.
 *
 * To use it, first you activate it in a configuration block:
 * <pre>
 * angular.module('main.config').config(function($anErrorProvider) {
 *    "use strict";
 *    $anErrorProvider.defaults.active = true;
 * })
 * </pre>
 * 
 * Then, you need to configure i18n translations that are used in modal:
 * <pre>
 * angular.module('main.translate', []).config(function($translateProvider) {
 *    'use strict';
 *    $translateProvider.preferredLanguage('fr');
 *    $translateProvider.translations('fr', {
 *       "ERROR_MODAL" : {
 *          "TITLE" : "ERREUR",
 *          "DEFAULT_MESSAGE" : "Le serveur d'application a rencontré une erreur.<br/>Votre requête est inaccessible pour l'instant.",
 *          "RETRY" : "REESSAYER",
 *          "RESTART": "RELANCER L'APPLICATION"
 *       },			
 *       "ERROR": {
 *          "UNKNOWN_ERROR_CODE": "Code d'erreur inconnu: {{code}}",
 *          "OFFLINE": "Le serveur est déconnecté (404)",
 *          "TIMEOUT": "Le serveur n'a pas répondu dans les temps (timeout)",
 *          "CONVERT": "Problème de format de données",
 *          "TECHNICAL": "Le serveur a rencontré une erreur technique",
 *          "NO_QUERY_DATA": "Pas de données",
 *          "UNMATCH_USER": "Vous ne pouvez pas manipuler les données d'un autre utilisateur",
 *          "NO_SEARCH_DATA": "Aucun élément ne correspond à votre recherche"
 *       }
 *    });
 * });	
 * </pre>
 * 
 * At this point, all exceptions will get catched and process as described above.
 * 
 * Below an example of a manual throw exception.
 * <pre>
 * service.getTurnoverItemIndicators = function() {
 *    var result = $anHttp({
 *       method : 'POST',
 *       url : pubcaSettings.url + 'getItemIndicators',
 *       data: turnoverContext()
 *    });
 *    result.promise.then(null, function(error) {
 *       if (!error.message.aborted) {
 *          throw new CustomError(LOGGER_NAME + '#getTurnoverItemIndicators', error.message.data, true);
 *       } else {
 *          log.trace('ID:' + error.message.uuid, 'item indicators request was aborted');
 *       }
 *    });
 *    return result;
 * };
 * </pre>
 * 
 * It is even possible to manually open error modal with a custom message:
 * <pre>
 * UserService.user().then(null, function(response) {
 *    $anError.open("ERROR.NOT_LOGIN", { translate: true, defaultMessage: false, reloadButton: false, restartButton: false });			
 * });
 * </pre>
 */
angular.module('an-error', [ 'pascalprecht.translate' ])

/**
 * @ngdoc service
 * @name an-error.$anErrorProvider
 * @description
 * Use `$anErrorProvider` to change the default behavior of the {@link  an-error.$anError $anError} service.
 * # Example
 * <pre>
 * angular.module('main.config').config(function($anErrorProvider) {
 *    $anErrorProvider.defaults.active = true;
 * });
 * </pre>
 */
.provider('$anError', function() {
	"use strict";
	/**
	 * @ngdoc property
	 * @name an-error.$anErrorProvider#defaults
	 * @propertyOf an-error.$anErrorProvider
	 * @description Object containing default values for {@link  an-error.$anError $anError}. The object has following properties:
	 * 
	 * - **active** - `{boolean}` - If `true`, exceptions will be intercepted and an error modal will be shown.<br>
	 * - **showElement** - `{Object}` - Defines elements that are shown in error's modal.<br>
	 * - **messageCodes** - `{Object}` - Contains message codes used by service. i18n is not strongly integrated into this common module, so messages must be configured elsewhere.
	 * 
	 * @returns {Object} Default values object.
	 */		
	var defaults = this.defaults = {
			active: false,
			showElement : {
				defaultMessage: true,
				reloadButton: true,
				restartButton: true
			},
			messageCodes : {
				defaultMessage: 'ERROR_MODAL.DEFAULT_MESSAGE'	// {string} - Default message i18n code
			}
	};
	
	/**
	 * @ngdoc service
	 * @name an-error.$anError
     * @requires $translate
     * @requires $q
	 * @description
	 * # $anError
	 * This service is bound to error modal. It can be used to configure, open or close modal.<br>
	 * It can only handle one modal at any given moment.
	 */
	this.$get = function($translate, $q) {
		var service = {};
		var customMessage;
		var showElement;

		/**
		 * @ngdoc property
		 * @name an-error.$anError#active
		 * @propertyOf an-error.$anError
		 * @description If `true`, exceptions will be intercepted and an error modal will be shown.
		 * @returns {boolean} Active indicator.
		 */
		service.active = defaults.active;
		
		/**
		 * @ngdoc property
		 * @name an-error.$anError#visible
		 * @propertyOf an-error.$anError
		 * @description This indicator, used by error modal, shows or hides modal depending on its value.
		 * @returns {boolean} Visible indicator.
		 */
		service.visible = false;

		/**
		 * @ngdoc function
		 * @name an-error.$anError#defaultMessage
		 * @methodOf an-error.$anError
		 * @description Returns translated default message. This is used in error modal view.
		 */
		service.defaultMessage = function() {
			return $translate.instant(defaults.messageCodes.defaultMessage);
		};

		/**
		 * @ngdoc function
		 * @name an-error.$anError#customMessage
		 * @methodOf an-error.$anError
		 * @description Returns custom message. This is used in error modal view.
		 */
		service.customMessage = function() {
			return customMessage;
		};

		/**
		 * @ngdoc function
		 * @name an-error.$anError#showElement
		 * @methodOf an-error.$anError
		 * @description Returns showElement indicators. This is used in error modal view.
		 */
		service.showElement = function() {
			return showElement;
		};
		
		/**
		 * @ngdoc function
		 * @name an-error.$anError#open
		 * @methodOf an-error.$anError
		 * @description Opens error modal.
		 * @param {string} msg Message. It can be a message or a translate code. If you use the latter, you must use `translate` in options (see below).
		 * @param {Object} opts Options. It can be used to override any defaults (mainly in showElement).<br>
		 * It also contains `translate` indicator, `false` by default. If `true`, `msg` will be translated.
		 */
		service.open = function(msg, opts) {
			opts = opts || {};
			showElement = angular.extend({}, defaults.showElement, opts);
			var promise = opts.translate ? $translate(msg) : $q.when(msg); 
			promise.then(function(m) {
				customMessage = m;
				service.visible = true;
			});
		};

		/**
		 * @ngdoc function
		 * @name an-error.$anError#close
		 * @methodOf an-error.$anError
		 * @description Closes modal.
		 */
		service.close = function() {
			service.visible = false;
		};
		
		return service;
	};
})

/**
 * @ngdoc controller
 * @name an-error.controller:ErrorController
 * @requires $scope
 * @requires $anError
 * @description
 * ErrorController is a controller used by error's modal.
 */
.controller('ErrorController', function ErrorController($scope, $anError) {
	"use strict";
	$scope.$anError = $anError;
	
	/**
	 * @ngdoc function
	 * @name an-error.controller:ErrorController#retry
	 * @methodOf an-error.controller:ErrorController
	 * @description Reloads current document, just like a browser page's refresh.
	 */
	$scope.retry = function() {
		document.location.reload();
	};
	
	/**
	 * @ngdoc function
	 * @name an-error.controller:ErrorController#restart
	 * @methodOf an-error.controller:ErrorController
	 * @description This action emits a `restart-application` event. This provides a way to restart application when an error occurs. 
	 */
	$scope.restart = function() {
		$scope.$emit("restart-application");
		$anError.close();
	};
}) 



/**
 * @description Global configuration for exception handling and error processing. 
 * @requires $provide
 * @requires $anLogger
 * @requires $anError
 * @requires $translate
 * @requires $q
 */
.config(function($provide) {
	"use strict";
	$provide.decorator("$exceptionHandler", function($delegate, $injector) {
		/**
		 * @description Decorator's behavior. 
		 * @param {Object} exception Should be a {@link window.function:CustomError CustomError} object for better integration. 
		 * In all cases, it must contain a `message` property with one of the following content:
		 * 
		 * - a simple message.
		 * - a message code (which needs to be translated).
		 * - an object containing a message code.
		 * 
		 * @param {Object} cause (unknown)
		 */
		return function(exception, cause){
			var $anError = $injector.get("$anError");
			if (!$anError.active) { // delegate on the original $exceptionHandler if not active
				return $delegate(exception, cause);
			}
			
			var $anLogger = $injector.get("$anLogger");
			var $translate = $injector.get("$translate");
			var $q = $injector.get("$q");
			// exception.name should contain location (see CustomError), if not, rootLogger will be used
			var logger = exception.name ? $anLogger.logger(exception.name) : $anLogger.rootLogger();

			var errorProcess = function(message) {
				if (!exception.server) {
					logger.fatal(message || "[no text]");
				}
				$anError.open(message);
			};

			if (!exception.message) { // no need to continue if message is empty (it should not happen anyways)
				return errorProcess();
			}
			
			var messagePromise = $q.when("");
			var errorCode;
			
			if (angular.isString(exception.message)) { 
				// message is a string, it should be an error code (i18n translation)
				errorCode = exception.message;
				messagePromise = $translate(errorCode);
			} else if (exception.message.error && exception.message.error.defaultMessage) {
				// message is a deep object containing a defautMessage property, then it is assumed that it has already been translated
				messagePromise = $q.when(exception.message.error.defaultMessage);
			} else if (exception.message.error && exception.message.error.code) {
				// message is a deep object containing a code property, it should be an error code (i18n translation)
				errorCode = exception.message.error.code;
				messagePromise = $translate(errorCode);
			} else if (exception.message.errorCode) {
				// message is a simple object with an errorCode property (i18n translation)
				errorCode = exception.message.errorCode;
				messagePromise = $translate(errorCode);
			}
			
			messagePromise.then(errorProcess, function() {
				// if translation fails, process error with error code (which might eventually be a simple message) 
				errorProcess(errorCode);
			});
		};
	});
});


/**
 * @ngdoc function
 * @name window.function:CustomError
 * @description This is a custom error object that inherit from `Error` object.
 * @param {string} location Location's information. It should help to localize an exception's origin, which is very important. 
 * @param {string|Object} message Message or object.
 * @param {boolean=} server Must be `true` if this error comes from server. If `false`, a fatal error log message will be output. This is a convenient way to send exceptions to server with an ajax appender.
 * 
 */
function CustomError(location, message, server) {
	"use strict";
    this.name = location;
    this.message = message || "";
    this.server = server || false;
}
CustomError.prototype = Error.prototype;
