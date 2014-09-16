/* global log4javascript */
(function(window, angular, log4javascript) {
	'use strict';

	/**
	 * @ngdoc overview
	 * @name an-log4javascript
	 * @module an-log4javascript
	 * @description # an-log4javascript This module is a wrapper for log4javascript library.
	 * 
	 * log4javascript has hierarchical loggers, implemented in the same way as log4j. In summary, you specify a logger's parent logger by means of a dot between the parent logger
	 * name and the child logger name. Therefore the logger `tim.app.security` inherits from `tim.app`, which in turn inherits from `tim` which, finally, inherits from the root
	 * logger.
	 * 
	 * What inheritance means for a logger is that in the absence of a threshold level set specifically on the logger it inherits its level from its parent; also, a logger inherits
	 * all its parent's appenders (this is known as appender additivity in log4j. This behaviour can be enabled or disabled via setAdditivity()). In the above example, if the root
	 * logger has a level of DEBUG and one appender, each of the loggers tim.app.security, tim.app and tim would inherit the root level's appender. If, say, tim.app's threshold
	 * level was set to WARN, tim's effective level would remain at DEBUG (inherited from the root logger) while tim.app.security's effective level would be WARN, inherited from
	 * tim.app. The important thing to note is that appenders accumulate down the logger hierarchy while levels are simply inherited from the nearest ancestor with a threshold level
	 * set.
	 * 
	 * To use it, you need to configure loggers similarly to log4j in a run block.
	 * 
	 * <pre>
	 * angular.module('main.loggers', []).config(function($anLoggerProvider) {
	 *    $anLoggerProvider.level('ERROR');
	 * });
	 * angular.module('main.loggers').run(function($anLogger, appSettings) {
	 *    $anLogger.addAppender('BrowserConsoleAppender', 'ALL');
	 *    $anLogger.setLevel('INFO', 'pilotageAuchanFrance.pubca.period');
	 *    $anLogger.setLevel('TRACE', 'pilotageAuchanFrance.pubca.axis');
	 * });
	 * </pre>
	 * 
	 * In this example, loggers have a default level of `ERROR`. There is a root appender set on `ALL` to catch all messages. Finally, we have 2 loggers with their own level.
	 * 
	 * Because appenders and loggers have their own level, you must pay attention in loggers configuration. It is easy to end up with bad configuration.
	 * 
	 * <pre>
	 * angular.module('main.loggers', []).config(function($anLoggerProvider) {
	 *    $anLoggerProvider.level('ALL');
	 * });
	 * angular.module('pilotageAuchanFrance.main.loggers').run(function($anLogger, appSettings) {
	 *    $anLogger.addAppender('BrowserConsoleAppender', 'ERROR');
	 *    $anLogger.setLevel('OFF', 'pilotageAuchanFrance.pubca.period');
	 *    $anLogger.setLevel('TRACE', 'pilotageAuchanFrance.pubca.axis');
	 * });
	 * </pre>
	 * 
	 * In this example, loggers have a default level of `ALL`. There is a root appender set on `ERROR` to catch error messages. It is possible to shutdown a logger because it won't
	 * try to append a message. But it is not possible to put a lower level priority on logger because this appender is set on `ERROR` and thus, will take precedence over logger.
	 * 
	 * It is probably better to define appenders on `ALL` and then configure loggers properly as shown in the first example.
	 * 
	 * You might also want to add an ajax appender for error logs:
	 * 
	 * <pre>
	 * angular.module('main.loggers', []).config(function($anLoggerProvider) {
	 *    $anLoggerProvider.level('ERROR');
	 * });
	 * angular.module('pilotageAuchanFrance.main.loggers').run(function($anLogger, appSettings) {
	 *    $anLogger.setAjaxAppender('ERROR', appSettings.restBaseUrl + 'log', true);
	 *    $anLogger.addAppender('BrowserConsoleAppender', 'ALL');
	 *    $anLogger.setLevel('INFO', 'pilotageAuchanFrance.pubca.period');
	 *    $anLogger.setLevel('TRACE', 'pilotageAuchanFrance.pubca.axis');
	 * });
	 * </pre>
	 * 
	 * Below an example on how to use loggers:
	 * 
	 * <pre>
	 * angular.module('pubca.axis').service('AxisService', function AxisService($http, $q, appSettings, localStorageService, $anLogger) {
	 *    var LOGGER_NAME = 'pilotageAuchanFrance.pubca.axis.' + this.constructor.name, log = $anLogger.logger(LOGGER_NAME);
	 *    return {
	 *       init : function() {
	 *          log.trace('axisCache already initialized');
	 *          log.error('test axis');
	 *          throw new CustomError(LOGGER_NAME + '#init', e.data, true);
	 *       }
	 *    };
	 * });
	 * </pre>
	 * 
	 * First, we define a constant `LOGGER_NAME` which is a concatenation of angular module name with provider name. Then, we define `log` which is just a shortcut for
	 * `$anLogger.logger(LOGGER_NAME)`. `LOGGER_NAME` constant is pretty usefull if we need to throw exception. See {@link an-error.$anError $anError} for more information.
	 * 
	 */
	angular.module('an-log4javascript', [])

	/**
	 * @ngdoc service
	 * @name an-log4javascript.$anLoggerProvider
	 * @description Use `$anLoggerProvider` to change the default behavior of the {@link an-log4javascript.$anLogger $anLogger} service. # Example
	 * 
	 * <pre>
	 * angular.module('main.loggers', []).config(function($anLoggerProvider) {
	 *    $anLoggerProvider.level('ALL');
	 *    $anLoggerProvider.useBrowserConsole(true);
	 * });
	 * </pre>
	 */
	.provider('$anLogger', function anLoggerProvider() {
		/**
		 * @ngdoc property
		 * @name an-log4javascript.$anLoggerProvider#defaults
		 * @propertyOf an-log4javascript.$anLoggerProvider
		 * @description Object containing default values for {@link an-log4javascript.$anLogger $anLogger}. The object has following properties: - **level** - `{log4javascript.Level
		 *              object}` - Default level used when parameter is omitted.<br> - **layout** - `{string}` - Default layout pattern. Currently, all appenders use this layout so
		 *              it must be defined here.<br> - **useBrowserConsole** - `{boolean}` - Uses browser's console instead of log4javascript's logger. It can be used to debug
		 *              something but should be off most of the times.<br>
		 * 
		 * @returns {Object} Default values object.
		 */
		var defaults = this.defaults = {
			level : log4javascript.Level.ERROR,
			layout : new log4javascript.PatternLayout("%d{HH:mm:ss:SSS} - %-5p - %c{4} - %m{4}%n"),
			useBrowserConsole : false
		};

		/**
		 * @description Returns `true` if it is a production distribution. Production distribution omits all appenders except AjaxAppender, resulting in a drastically reduced file
		 *              size compared to the standard edition.
		 */
		var isProduction = function() {
			return !angular.isFunction(log4javascript.BrowserConsoleAppender);
		};

		var levels = {
			'ALL' : log4javascript.Level.ALL,
			'TRACE' : log4javascript.Level.TRACE,
			'DEBUG' : log4javascript.Level.DEBUG,
			'INFO' : log4javascript.Level.INFO,
			'WARN' : log4javascript.Level.WARN,
			'ERROR' : log4javascript.Level.ERROR,
			'FATAL' : log4javascript.Level.FATAL,
			'OFF' : log4javascript.Level.OFF
		};

		/**
		 * @description Returns a log4javascript level object from its name.<br>
		 *              Name is converted to uppercase for better flexibility in the configuration file.<br>
		 *              If empty, level `ERROR` will be used.
		 * @param {string}
		 *           level Level's name `(ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF)`.
		 */
		var getLevel = function(level) {
			level = level ? level.toUpperCase() : 'ERROR';
			return levels[level];
		};

		/**
		 * @description Returns a log4javascript appender object from its name.<br>
		 *              Throws an exception if no match with available appenders.
		 * @param {string}
		 *           appender Appender's name `(Appender, AjaxAppender, AlertAppender, PopUpAppender, InPageAppender, BrowserConsoleAppender)`
		 */
		var getAppender = function(appender) {
			if (appender === 'AjaxAppender') {
				return new log4javascript.AjaxAppender();
			} else if (isProduction()) {
				return null;
			} else if (appender === 'Appender') {
				return new log4javascript.Appender();
			} else if (appender === 'AlertAppender') {
				return new log4javascript.AlertAppender();
			} else if (appender === 'PopUpAppender') {
				return new log4javascript.PopUpAppender();
			} else if (appender === 'InPageAppender') {
				return new log4javascript.InPageAppender();
			} else if (appender === 'BrowserConsoleAppender') {
				return new log4javascript.BrowserConsoleAppender();
			} else {
				throw new Error("Unknown appender: " + appender);
			}
		};

		/**
		 * @ngdoc method
		 * @name an-log4javascript.$anLoggerProvider#level
		 * @methodOf an-log4javascript.$anLoggerProvider
		 * @description Set default level.
		 * @param {string}
		 *           level Level's name `(ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF)`.
		 */
		this.level = function(level) {
			defaults.level = getLevel(level);
		};

		/**
		 * @ngdoc method
		 * @name an-log4javascript.$anLoggerProvider#layout
		 * @methodOf an-log4javascript.$anLoggerProvider
		 * @description Set default layout.
		 * @param {Object}
		 *           layout Layout's object. See {@link http://log4javascript.org/docs/manual.html#layouts documentation}.
		 */
		this.layout = function(pattern) {
			defaults.layout = new log4javascript.PatternLayout(pattern);
		};

		/**
		 * @ngdoc method
		 * @name an-log4javascript.$anLoggerProvider#useBrowserConsole
		 * @methodOf an-log4javascript.$anLoggerProvider
		 * @description Set `uses browser's console` indicator. In production, it can not be changed and will silently be ignored.
		 * @param {boolean}
		 *           value Boolean value.
		 */
		this.useBrowserConsole = function(value) {
			if (!isProduction()) {
				defaults.useBrowserConsole = value;
			}
		};

		/**
		 * @ngdoc service
		 * @name an-log4javascript.$anLogger
		 * @description # $anLogger This service is a wrapper for log4javascript and contains everything needed to properly use loggers in Angular.
		 */
		this.$get = function() {

			/**
			 * @description Returns a log4javascript logger from its name. Throws an exception if name is empty.
			 * @param {string}
			 *           logger Logger's name.
			 * 
			 */
			var getLogger = function(name) {
				if (!name) {
					throw new Error("invalid logger name");
				}
				return log4javascript.getLogger(name);
			};

			/**
			 * @description Adds an appender to a logger. Level and default layout are affected to it.
			 * @param {string}
			 *           logger Logger's name.
			 * @param {string}
			 *           appender Appender's name `(Appender, AjaxAppender, AlertAppender, PopUpAppender, InPageAppender, BrowserConsoleAppender)`
			 * @param {Object}
			 *           level log4javascript.Level object.
			 * 
			 */
			var addAppender = function(logger, appender, level) {
				appender = getAppender(appender);
				if (appender) {
					appender.setLayout(defaults.layout);
					appender.setThreshold(level);
					logger.addAppender(appender);
				}
			};

			var service = {};

			/**
			 * @ngdoc function
			 * @name an-log4javascript.$anLogger#logger
			 * @methodOf an-log4javascript.$anLogger
			 * @description Returns a logger by its name. It is created if it doesn't exist. Throws an exception if name is empty.<br>
			 *              If option `useBrowserConsole` is `true`, it returns browser's console object.<br>
			 *              In production mode, it returns a null logger.
			 * @param {string}
			 *           name Logger's name.
			 * @returns {Object} log4javascript's logger.
			 */
			service.logger = function(name) {
				var logger = defaults.useBrowserConsole ? console : (getLogger(name) || log4javascript.getNullLogger());
				return logger;
			};

			/**
			 * @ngdoc function
			 * @name an-log4javascript.$anLogger#rootLogger
			 * @methodOf an-log4javascript.$anLogger
			 * @description Returns root logger.
			 * @returns {Object} log4javascript's root logger.
			 */
			service.rootLogger = function() {
				return log4javascript.getRootLogger();
			};

			/**
			 * @ngdoc function
			 * @name an-log4javascript.$anLogger#setLogger
			 * @methodOf an-log4javascript.$anLogger
			 * @description Defines a logger with a name.
			 * @param {string}
			 *           name Logger's name.
			 * @param {string=}
			 *           level Level's name `(ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF)`.<br>
			 *           If `level` is omitted, `default level` will be used.
			 * @param {string|array=}
			 *           appenders Appender's names `(Appender, AjaxAppender, AlertAppender, PopUpAppender,` `InPageAppender, BrowserConsoleAppender)`.<br>
			 *           If `appenders` is omitted, this logger won't produce any log unless a parent logger has an appender.
			 */
			service.setLogger = function(name, level, appenders) {
				var logger = log4javascript.getLogger(name);
				level = level ? getLevel(level) : defaults.level;
				if (appenders) {
					appenders = angular.isArray(appenders) ? appenders : [ appenders ];
					angular.forEach(appenders, function(appender) {
						addAppender(logger, appender, level);
					});
				}
				logger.setLevel(level);
			};

			/**
			 * @ngdoc function
			 * @name an-log4javascript.$anLogger#setAjaxAppender
			 * @methodOf an-log4javascript.$anLogger
			 * @description Defines a flexible appender that asynchronously sends log messages to a server via HTTP.
			 * @param {string=}
			 *           level Level's name `(ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF)`.<br>
			 *           If omitted, `default level` will be used
			 * @param {string}
			 *           url The URL to which log messages should be sent. Note that this is subject to the usual Ajax restrictions: the URL should be in the same domain as that of
			 *           the page making the request.
			 * @param {boolean=}
			 *           credentials Specifies whether cookies should be sent with each request. `false` by default.
			 * @param {string=}
			 *           logger Logger's name. If omitted, `rootLogger` will be used.
			 */
			service.setAjaxAppender = function(level, url, credentials, logger) {
				logger = logger ? getLogger(logger) : log4javascript.getRootLogger();
				level = level ? getLevel(level) : defaults.level;
				var appender = new log4javascript.AjaxAppender(url, credentials);
				appender.setLayout(new log4javascript.HttpPostDataLayout());
				appender.setThreshold(level);
				appender.setFailCallback(angular.noop);
				appender.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
				logger.addAppender(appender);
			};

			/**
			 * @ngdoc function
			 * @name an-log4javascript.$anLogger#setLevel
			 * @methodOf an-log4javascript.$anLogger
			 * @description Set a level for a logger.
			 * @param {string}
			 *           level Level's name `(ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF)`.
			 * @param {string=}
			 *           logger Logger's name. If omitted, `rootLogger` will be used.
			 */
			service.setLevel = function(level, logger) {
				logger = logger ? getLogger(logger) : log4javascript.getRootLogger();
				logger.setLevel(getLevel(level));
			};

			/**
			 * @ngdoc function
			 * @name an-log4javascript.$anLogger#addAppender
			 * @methodOf an-log4javascript.$anLogger
			 * @description Returns a logger by its name. It is created if it doesn't exist.<br>
			 *              If option `useBrowserConsole` is `true`, it returns browser's console object.<br>
			 *              In production mode, it returns a null logger.
			 * @param {string}
			 *           appender Appender's name `(Appender, AjaxAppender, AlertAppender, PopUpAppender,`<br>
			 *           `InPageAppender, BrowserConsoleAppender)`
			 * @param {string=}
			 *           level Level's name `(ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF)`.<br>
			 *           If `level` is omitted, `default level` will be used.
			 * @param {string=}
			 *           name Logger's name. If omitted, `rootLogger` will be used
			 */
			service.addAppender = function(appender, level, logger) {
				logger = logger ? getLogger(logger) : log4javascript.getRootLogger();
				level = level ? getLevel(level) : defaults.level;
				addAppender(logger, appender, level);
			};

			/**
			 * @description service constructor: - activate quiet mode - define root logger's level - add missing console levels (used when useBrowserConsole is true)
			 */
			(function init() {
				log4javascript.logLog.setQuietMode(true);
				log4javascript.getRootLogger().setLevel(defaults.level);
				if (!console.fatal) {
					console.fatal = console.error;
				}
				if (!console.trace) {
					console.trace = console.debug;
				}
			})();

			return service;
		};
	});

})(window, window.angular, log4javascript);