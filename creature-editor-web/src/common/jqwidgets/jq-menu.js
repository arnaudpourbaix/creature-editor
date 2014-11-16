angular.module('apx-jqwidgets.menu', [])

/**
 * @ngdoc service
 * @name apx-jqwidgets.jqMenuProvider
 * @description
 * Use `jqMenuProvider` to change the default behavior of the {@link  apx-jqwidgets.jqMenu jqMenu} service.
 */
.provider('jqMenu', function() {
	/**
	 * @ngdoc property
	 * @name apx-jqwidgets.jqMenuProvider#defaults
	 * @propertyOf apx-jqwidgets.jqMenuProvider
	 * @description Object containing default values for {@link apx-jqwidgets.jqMenu jqMenu}. The object has following properties:
	 * 
	 * - **enableHover** - `{boolean}` - Enables or disables the hover state.<br>
	 * - **autoOpen** - `{boolean}` - Opens the top level menu items when the user hovers them.<br>
	 * 
	 * @returns {Object} Default values object.
	 */		
	var defaults = this.defaults = {
		enableHover : true,
		autoOpen : true
	};

	/**
	 * @ngdoc service
	 * @name apx-jqwidgets.jqMenu
     * @requires apx-jqwidgets.jqCommon
     * @requires $rootScope
     * @requires $compile
     * @requires $timeout
	 * @description
	 * # jqMenu
	 * This service is a wrapper for menu widget.<br>
	 */
	this.$get = function(jqCommon, $rootScope, $compile, $timeout) {
		
		/**
		 * @description Check that menu items are valid and throw an exception on any error.
		 * @param {Object} settings Should contain a property items wich should be an array of objects (menu items).
		 */
		var checkMenuItems = function(settings) {
			jqCommon.getParams(settings, [ 'items' ]); // check required properties
			if (!angular.isArray(settings.items)) {
				throw new Error("items must be an array: " + settings.items);
			}
			angular.forEach(settings.items, function(item, index) {
				var difference = _.difference([ 'label', 'action' ], _.keys(item));
				if (difference.length) {
					throw new Error("missing item parameters: " + difference.toString());
				}
				if (!angular.isString(item.label)) {
					throw new Error("label must be a string: " + item.label.toString());
				}
				if (!angular.isString(item.action)) {
					throw new Error("action must be a string: " + item.action);
				}
			});
		};

		/**
		 * @description Select a menu item and calls its action.
		 * @param {string} label Item label.
		 * @param {Object} $scope Controller's scope.
		 * @param {Object} params Contextual menu parameters. Has following properties:
		 * 
		 * - **settings** - `{Object=}` - Set of key/value pairs that configure the jqxMenu plug-in. All settings are optional.<br>
		 * - **items** - `{array}` - Contains a list of menu items. Each item has 2 properties: label {string} and action {string} (must be a method within scope).<br>
		 * 
		 * @param {Object} getEntity Callback function to get an entity that will be passed as a parameter to menu actions.
		 */
		var selectItem = function(label, $scope, params, getEntity) {
			var item = _.find(params.items, function(item) {
				return item.label === label;
			});
			$scope.$apply(function() {
				var entity = getEntity();
				$scope.$eval(item.action)(entity);
			});
		};

		var service = {};
		
		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqMenu#getContextual
		 * @methodOf apx-jqwidgets.jqMenu
		 * @description Returns a contextual menu.
		 * @param {Object} params Contextual menu parameters. Has following properties:
		 * 
		 * - **settings** - `{Object=}` - Set of key/value pairs that configure the jqxMenu plug-in. All settings are optional.<br>
		 * - **items** - `{array}` - Contains a list of menu items. Each item has 2 properties: label {string} and action {string} (must be a method within scope).<br>
		 * 
		 * @param {Object} $scope Controller's scope.
		 * @param {Object} getEntity Callback function to get an entity that will be passed as a parameter to menu actions.
		 * @returns {Object} Promise containing compiled DOM.
		 */
		service.getContextual = function(params, $scope, getEntity) {
			checkMenuItems(params);
			var templateOptions = {
				template : '<div><ul><li data-ng-repeat="item in items">{{item.label}}</li></ul></div>'
			};
			var dependencies = {
				items : params.items
			};
			return jqCommon.getView(templateOptions, 'JqContextualMenuController', dependencies).then(function(view) {
				var options = angular.extend({}, params.settings, {
					autoOpenPopup : false,
					mode : 'popup'
				});
				return $timeout(function() {
					view.jqxMenu(options);
					view.on('itemclick', function(event) {
						selectItem($(event.target).text(), $scope, params, getEntity);
					});
					return view;
				});
			}).then(function(view) {
				return view;
			});
		};
		
		return service;
	};
})

/**
 * @ngdoc directive
 * @name apx-jqwidgets.directive:jqMenu
 * @restrict AE
 * @priority 0
 * @description Apply menu widget on element.
 * Directive attribute's value is an object that contains widget properties.
 * 
 */
.directive('jqMenu', function($compile, jqCommon, jqMenu, $timeout) {
	return {
		restrict : 'AE',
		link : function($scope, element, attributes) {
			var settings = angular.extend({}, jqCommon.defaults, jqMenu.defaults, $scope.$eval(attributes.jqMenu));
			element.jqxMenu(settings);
		}
	};
})

/**
 * @ngdoc controller
 * @name apx-jqwidgets.controller:JqContextualMenuController
 * @description Contextual menu widget controller.
 */
.controller('JqContextualMenuController', function($scope, items) {
	/**
	 * @ngdoc property
	 * @name items
	 * @propertyOf apx-jqwidgets.controller:JqContextualMenuController
	 * @description Menu items. 
	 */
	$scope.items = items;
})

;