/**
 * @description Application loggers configuration.  
 */
angular.module('editor.main.loggers', [])
	
	.config([ '$anLoggerProvider', function MainAnLoggerProviderConfig($anLoggerProvider) {
		"use strict";
		$anLoggerProvider.level('ERROR');
		$anLoggerProvider.useBrowserConsole(false);
	}])

	.run([ '$anLogger', 'appSettings', function MainAnLoggerProviderRun($anLogger, appSettings) {
		"use strict";
//		$anLogger.setAjaxAppender('ERROR', appSettings.restBaseUrl + 'log', true);
//		$anLogger.addAppender('PopUpAppender', 'ALL');
//		$anLogger.addAppender('BrowserConsoleAppender', 'ALL');
//		$anLogger.setLevel('TRACE', 'editor.pubca.topProduct');
//		$anLogger.setLevel('TRACE', 'editor.pubca');
	} ])
;
