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
		 * @param {string} id Docking window id.
		 * @param {Object} properties Window properties:
		 * 
		 * - **height** - `{number}` - Window height<br>
		 * - **width** - `{number}` - Window width<br>
		 */
		service.setWindowProperties = function(element, id, properties) {
			var props = {};
			if (properties.height && properties.height > 600) {
				props.maxHeight = properties.height;
			}
			angular.extend(props, _.omit(properties, [ 'title' ]));
			angular.forEach(props, function(value, key) {
				element.jqxDocking('setWindowProperty', id, key, value);
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
 * Directive attribute's value is an object that contains widget properties.
 * 
 * # Example:
 * 
 * <pre>
<div data-jq-docking="{ width : 1200, height : 800 }">
	<div id="creature-list" data-jq-docking-window="{ height: 600, title: 'CREATURE.LIST_TITLE' }">
		<button type="button" class="btn btn-primary" data-ng-click="import()" data-translate>CREATURE.BUTTONS.IMPORT</button>		
		<button type="button" class="btn btn-danger" data-ng-click="stopImport()" data-translate>CREATURE.BUTTONS.CANCEL</button>
		<div data-jq-grid="creatureGrid"></div>
		<div class="creature-edit" data-ui-view="creature-edit"></div>
	</div>	
</div>
 * </pre>
 */
.directive('jqDocking', function($timeout, jqCommon, jqDocking) {
	return {
		restrict : 'A',
		replace : true,
		transclude : true,
		template : '<div><div ng-transclude></div></div>',
		controller: function($scope, $element) {
			/*
			 * @description Set window properties.
			 * @param {string} id Docking window id.
			 * @param {Object} properties Window properties:
			 */
			this.setWindowProperties = function(id, properties) {
				jqDocking.setWindowProperties($element, id, properties);
			};
		},
		compile : function() {
			return {
				pre : function preLink($scope, element, attrs) { // pre-compile because docking must be applied before setting children windows
					var params = $scope.$eval(attrs.jqDocking) || {};
					var settings = angular.extend({}, jqCommon.defaults, params);
					$timeout(function() { // must use timeout to be sure template will correctly be set at that point
						element.jqxDocking(settings);
					});
				}
			};
		}
	};
})

/**
 * @ngdoc directive
 * @name apx-jqwidgets.directive:jqDockingWindow
 * @restrict A
 * @priority 0
 * @description Defines a windows inside a docking widget.
 * Directive attribute's value is an object that has following properties:
 * 
 * - **title** - `{string}` - Window's title. Should be a string code that will be translated with {@link $translate $translate} service.<br>
 * - **height** - `{number=}` - Height.<br>
 * - **width** - `{number}` - Width.<br>
 * 
 * # Example:
 * 
 * <pre>
 * <div data-jq-docking-window="{ id: 'creature-list', height: 700, title: 'CREATURE.LIST_TITLE' }">
 * </pre>
 */
.directive('jqDockingWindow', function($timeout, jqCommon, jqDocking) {
	return {
		restrict : 'A',
		require: '^^jqDocking',
		transclude : true,
		template : '<div>{{ ::title | translate }}</div><div ng-transclude></div>',
		scope: true,
		link : function($scope, element, attrs, jqDockingCtrl) {
			if (!attrs.id) {
				throw new Error('docking window must have an id attribute!');
			}
			var params = jqCommon.getParams($scope.$eval(attrs.jqDockingWindow) || {}, [ 'title' ], []);
			$scope.title = params.title;
			$timeout(function() { // must use timeout to be sure template will correctly be set at that point
				jqDockingCtrl.setWindowProperties(attrs.id, params);	
			});
		}
	};
})

;