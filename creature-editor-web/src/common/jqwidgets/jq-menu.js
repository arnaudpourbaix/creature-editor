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

		this.$get = [ '$jqCommon', '$rootScope', '$compile', function jqMenuService($jqCommon, $rootScope, $compile) {
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
					$jqCommon.getTemplatePromise({
						// templateUrl : 'jqwidgets/jq-menu.tpl.html'
						template : '<ul><li data-ng-repeat="item in items">{{item.label}}</li></ul>'
					}).then(function(tpl) {
						var scope = $rootScope.$new();
						$jqCommon.instanciateController('JqContextualMenuController', scope, {
							items : params.items,
						});
						var content = $compile(tpl)(scope);
						//$("#testMenu").html(content);
						// var contextMenu = $("#jqxMenu").jqxMenu({ width: '120px', height: '56px', autoOpenPopup: false, mode: 'popup' });
						return content;
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