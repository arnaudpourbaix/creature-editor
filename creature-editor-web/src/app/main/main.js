/**
 * @ngdoc overview
 * @name editor.main
 * @module editor.main
 * @description
 *
 * # Main module
 * This is the application core module. It contains many critical features:<br>
 * 
 ** Providers configuration<br>
 ** Block any route until user is authentificated<br>
 ** Default route and initial context loader<br>
 ** Handles invalid route<br>
 ** Constants definition<br>
 ** Loggers declaration<br>
 ** Translation properties<br>
 */
angular.module('editor.main', [
			'editor.main.config', 
			'editor.main.controllers', 
			'editor.main.translate',
			'editor.main.loggers' 
]);

