/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.menu', [ 'jqwidgets.common' ]);

	module.provider('$jqMenu', function JqMenuProvider() {
		var options = {
			showTopLevelArrows : true,
			enableHover : true,
			autoOpen : true
		};

		this.$get = [ '$jqCommon', '$rootScope', '$compile', '$timeout', function jqMenuService($jqCommon, $rootScope, $compile, $timeout) {
			var checkParams = function(params) {
				$jqCommon.getParams(params, [ 'items' ]);
				if (!angular.isArray(params.items)) {
					throw new Error("items must be an array: " + params.items);
				}
				angular.forEach(params.items, function(item, index) {
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

			function selectItem(label, scope, params, getEntity) {
				var item = _.find(params.items, function(item, index) {
					return item.label === label;
				});
				scope.$apply(function() {
					var entity = getEntity();
					scope.$eval(item.action)(entity);
				});
			}

			var service = {
				options : function() {
					return options;
				},
				getContextual : function(element, params, scope, getEntity) {
					checkParams(params);
					var templateOptions = {
						template : '<ul><li data-ng-repeat="item in items">{{item.label}}</li></ul>'
					};
					var dependencies = {
						items : params.items
					};
					return $jqCommon.getView(templateOptions, 'JqContextualMenuController', dependencies).then(function(view) {
						var settings = angular.extend({}, params.options, {
							autoOpenPopup : false,
							mode : 'popup'
						});
						$timeout(function() {
							element.jqxMenu(settings);
							element.on('itemclick', function(event) {
								selectItem($(event.target).text(), scope, params, getEntity);
							});
						});
						return element.html(view);
					});
				}
			};
			return service;
		} ];
	});

	module.directive('jqMenu', [ '$compile', '$jqCommon', '$jqMenu', '$timeout', function JqMenuDirective($compile, $jqCommon, $jqMenu, $timeout) {
		return {
			restrict : 'AE',
			link : function(scope, element, attributes) {
				var settings = angular.extend({}, $jqCommon.options(), $jqMenu.options(), scope.$eval(attributes.jqMenu));
				element.jqxMenu(settings);
			}
		};
	} ]);

	module.controller('JqContextualMenuController', [ '$scope', 'items', function JqContextualMenuController($scope, items) {
		$scope.items = items;

	} ]);

}(window, jQuery, _));