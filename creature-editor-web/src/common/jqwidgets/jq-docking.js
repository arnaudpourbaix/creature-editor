angular.module('apx-jqwidgets.docking', [])

/**
 * @ngdoc service
 * @name apx-jqwidgets.jqDockingProvider
 * @description
 * Use `jqDockingProvider` to change the default behavior of the {@link  apx-jqwidgets.jqDocking jqDocking} service.
 */
.provider('jqDocking', function() {
	/**
	 * @ngdoc service
	 * @name apx-jqwidgets.jqDocking
     * @requires apx-jqwidgets.jqCommon
	 * @description
	 * # jqDocking
	 * This service is a wrapper for docking widget.<br>
	 */
	this.$get = function(jqCommon) {
		var service = {};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqDocking#setWindowProperties
		 * @methodOf apx-jqwidgets.jqDocking
		 * @description Set window properties.
		 * @param {Object} element Docking DOM element.
		 * @param {Object} properties Window properties:
		 * 
		 * - **id** - `{string}` - Window element id<br>
		 * - **height** - `{number}` - Window height<br>
		 */
		service.setWindowProperties = function(element, properties) {
			var props = {};
			if (properties.height && properties.height > 600) {
				props.maxHeight = properties.height;
			}
			angular.extend(props, _.omit(properties, [ 'id', 'template', 'templateUrl' ]));
			angular.forEach(props, function(value, key) {
				element.jqxDocking('setWindowProperty', properties.id, key, value);
			});
		};

		return service;
	};
})

/**
 * @ngdoc directive
 * @name apx-jqwidgets.directive:jqDocking
 * @restrict A
 * @priority 0
 * @description Apply docking widget on element.
 * Directive attribute's value is an object that has following properties:
 * 
 * - **settings** - `{Object=}` - Set of key/value pairs that configure the jqxDocking plug-in. All settings are optional.<br>
 * - **windows** - `{array}` - Windows.<br>
 * 
 * windows property has following properties:
 * 
 * - **id** - `{string}` - Window element id<br>
 * - **height** - `{number}` - Window height<br>
 * 
 * # Example:
 * 
 * <pre>
 * <div data-jq-docking="layout"></div>
 * </pre>
 * 
 * <pre>
	$scope.layout = {
		settings : {
			width : 1200,
			height : 800
		},
		windows : [ {
			id : 'spell-list',
			height : 700
		}, {
			id : 'spell-import',
			height : 100,
		} ]
	};
 * </pre>
 */
.directive('jqDocking', function($compile, $timeout, jqCommon, jqDocking) {
	return {
		restrict : 'A',
		link : function($scope, element, attrs) {
			var params = jqCommon.getParams($scope.$eval(attrs.jqDocking) || {}, [], [ 'settings', 'windows' ]);
			var settings = angular.extend({}, jqCommon.defaults, params.settings);
			element.jqxDocking(settings);
			angular.forEach(params.windows, function(w) {
				jqDocking.setWindowProperties(element, w);
			});
		}
	};
})

;