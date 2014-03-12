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
					if (!angular.isFunction(item.action)) {
						throw new Error("action must be a function: " + item.action.toString());
					}
				});
			};

			var service = {
				options : function() {
					return options;
				},
				getContextual : function(params) {
					checkParams(params);
					var templateOptions = {
						template : '<ul><li data-ng-repeat="item in items">{{item.label}}</li></ul>'
					};
					var dependencies = {
						items : params.items
					};
					return $jqCommon.getView(templateOptions, 'JqContextualMenuController', dependencies).then(function(view) {
						var container = document.createElement("div");
						container.appendChild(view[0].cloneNode(true));
						console.log('view', view[0]);
						console.log('container', container);
						var html = container.innerHTML;
						
						var element;
						if (params.domSelector) {
							element = angular.element(params.domSelector);
						}
						var settings = angular.extend({}, params.options, {
							autoOpenPopup : false,
							mode : 'popup'
						});
						//view = '<ul class="ng-scope"><!-- ngRepeat: item in items --><li data-ng-repeat="item in items" class="ng-scope ng-binding">Add category</li><!-- end ngRepeat: item in items --><li data-ng-rapeat="item in items" class="ng-scope ng-binding">Remove category</li><!-- end ngRepeat: item in items --></ul>';
						//view = '<ul class="ng-scope"><!-- ngRepeat: item in items --><li data-ng-repeat="item in items" class="ng-scope ng-binding">Add category</li><!-- end ngRepeat: item in items --><li data-ng-repeat="item in items" class="ng-scope ng-binding">Remove category</li><!-- end ngRepeat: item in items --></ul>';
						//return element.html(view.html()).jqxMenu(settings);
						console.log('html', html);
						return element.html(html).jqxMenu(settings);
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