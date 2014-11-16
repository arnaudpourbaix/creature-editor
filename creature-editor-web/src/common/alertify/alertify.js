/* global alertify */
angular.module('apx-alertify', [])

/**
 * @ngdoc service
 * @name apx-alertify.$alertifyProvider
 * @description
 * Use `$alertifyProvider` to change the default behavior of the {@link  apx-alertify.$alertify $alertify} service.
 */
.provider('$alertify', function() {
	/**
	 * @ngdoc property
	 * @name apx-alertify.$alertifyProvider#defaults
	 * @propertyOf apx-alertify.$alertifyProvider
	 * @description Object containing default values for {@link apx-alertify.$alertify $alertify}. The object has following properties:
	 * 
	 * @returns {Object} Default values object.
	 */		
	var defaults = this.defaults = {
	};

	/**
	 * @ngdoc service
	 * @name apx-alertify.$alertify
     * @requires $rootScope
     * @requires $compile
     * @requires $timeout
	 * @description
	 * # $alertify
	 * This service is a wrapper for {@link http://fabien-d.github.io/alertify.js/ alertify}.<br>
	 */
	this.$get = function($q, $compile, $timeout) {

		var service = {};

		/**
		 * @ngdoc function
		 * @name apx-alertify.$alertify#alert
		 * @methodOf apx-alertify.$alertify
		 * @description Display an alert message box. An alert box is often used if you want to make sure information comes through to the user. When an alert box pops up, the user will have to click "OK" to proceed.
		 * @param {string} message Text message.
		 */
		service.alert = function(message) {
			alertify.alert(message);		
		};
		
		/**
		 * @ngdoc function
		 * @name apx-alertify.$alertify#confirm
		 * @methodOf apx-alertify.$alertify
		 * @description Display a confirmation message box. A confirm box is often used if you want the user to verify or accept something. When a confirm box pops up, the user will have to click either "OK" or "Cancel" to proceed. If the user clicks "OK", returning promise is resolved. If the user clicks "Cancel", returning promise is rejected.
		 * @param {string} message Text message.
		 * @returns {promise} Returns a promise, resolved or rejected depending on user choice.
		 */
		service.confirm = function(message) {
			var deferred = $q.defer();
			alertify.confirm(message, function(result) {
				if (result) {
					deferred.resolve();
				} else {
					deferred.reject();
				}
			});		
			return deferred.promise;
		};

		/**
		 * @ngdoc function
		 * @name apx-alertify.$alertify#prompt
		 * @methodOf apx-alertify.$alertify
		 * @description Display a prompt message box. A prompt box is often used if you want the user to input a value before entering a page. When a prompt box pops up, the user will have to click either "OK" or "Cancel" to proceed after entering an input value. If the user clicks "OK" the box returns the input value. If the user clicks "Cancel" the box returns null.
		 * @param {string} message Text message.
		 * @returns {promise} Returns a promise, resolved or rejected depending on user choice. On resolve, it will return text.
		 */
		service.prompt = function(message) {
			var deferred = $q.defer();
			alertify.prompt(message, function(result, text) {
				if (result) {
					deferred.resolve(text);
				} else {
					deferred.reject();
				}
			});		
			return deferred.promise;
		};

		/**
		 * @ngdoc function
		 * @name apx-alertify.$alertify#log
		 * @methodOf apx-alertify.$alertify
		 * @description Show a new log message box.
		 * @param {string} message Text message.
		 * @param {string=} type Type of log message. If a type is passed, a class name "alertify-log-{type}" will get added.
		 * @param {number=} wait Time (in ms) to wait before auto-hiding the log.
		 */
		service.log = function(message, type, wait) {
			alertify.log(message, type, wait);
		};

		/**
		 * @ngdoc function
		 * @name apx-alertify.$alertify#success
		 * @methodOf apx-alertify.$alertify
		 * @description Show a success log message box.
		 * @param {string} message Text message.
		 */
		service.success = function(message, type, wait) {
			alertify.success(message);
		};

		/**
		 * @ngdoc function
		 * @name apx-alertify.$alertify#error
		 * @methodOf apx-alertify.$alertify
		 * @description Show an error log message box.
		 * @param {string} message Text message.
		 */
		service.error = function(message, type, wait) {
			alertify.error(message);
		};
		
		return service;
	};
})

;