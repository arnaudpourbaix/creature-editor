/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.docking', [ 'jqwidgets.common' ]);

	module.provider('$jqDocking', function JqDockingProvider() {
		var options = {};

		this.$get = [ '$jqCommon', function jqDockingService($jqCommon) {
			var service = {};
				
			service.options = function() {
					return options;
			};
			
			service.setWindowProperties = function(element, id, properties) {
				var props = {};
				if (properties.height) {
					props.maxHeight = properties.height;
				}
				if (properties.width) {
					props.maxWidth = properties.width;
				}
				angular.extend(props, _.omit(properties, [ 'template', 'templateUrl' ]));
				console.log(props);
				angular.forEach(props, function(value, key) {
					console.log(id, key, value);
					element.jqxDocking('setWindowProperty', id, key, value);
				});
			};
			
			return service;
		} ];
	});

	module.directive('jqDocking', [ '$compile', '$timeout', '$q', '$jqCommon', '$jqDocking', function JqDockingDirective($compile, $timeout, $q, $jqCommon, $jqDocking) {
		return {
			restrict : 'AE',
			template : '<div class="jq-docking"><div data-ng-scope-element="jqDockingWindows"></div></div>',
			replace : true,
			compile : function() {
				return {
					post : function($scope, iElement, iAttrs) {
						var getParams = function() {
							return $jqCommon.getParams($scope.$eval(iAttrs.jqDocking), [ 'windows' ], [ 'options' ]);
						};
						var params = getParams();
						var promises = [];
						angular.forEach(params.windows, function(win) {
							promises.push($jqCommon.getView(win, null, null, $scope));
						});
						$q.all(promises).then(function(views) {
							$scope.jqDockingWindows.append(views);
							var options = angular.extend({}, $jqCommon.options(), $jqDocking.options(), params.options);
							iElement.jqxDocking(options);
							angular.forEach(params.windows, function(win, index) {
								$jqDocking.setWindowProperties(iElement, views[index].attr('id'), win);
							});
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery, _));