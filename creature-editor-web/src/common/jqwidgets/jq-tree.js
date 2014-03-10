/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.tree', [ 'jqwidgets.common' ]);

	module.provider('$jqTree', function JqTreeProvider() {
		var options = {};

		this.$get = [ '$jqCommon', '$jqDataAdapter', function jqTreeService($jqCommon, $jqDataAdapter) {
			var service = {
				create : function(element, scope, params) {
					if (!scope[params.data]) {
						throw new Error("undefined data in scope: " + params.data);
					}
					var source = angular.extend({
						datafields : params.datafields,
						datatype : "array",
						localdata : scope[params.data],
						id : params.id
					});
					var dataAdapter = $jqDataAdapter.getRecordsHierarchy(source, params.id, params.parent, params.display);
					var settings = angular.extend({}, $jqCommon.options(), options, params.options, {
						source : dataAdapter
					});
					element.jqxTree(settings);
				}
			};
			return service;
		} ];
	});

	module.directive('jqTree', [
			'$compile',
			'$jqCommon',
			'$jqTree',
			'$jqMenu',
			function JqTreeDirective($compile, $jqCommon, $jqTree, $jqMenu) {
				return {
					restrict : 'AE',
					replace : true,
					scope : true,
					compile : function() {
						return {
							pre : function($scope, iElement, iAttrs) {
								var getParams = function() {
									return $jqCommon.getParams($scope.$eval(iAttrs.jqTree), [ 'data', 'datafields', 'id', 'parent', 'display' ], [ 'options', 'events',
											'contextMenu' ]);
								};
								var bindEvents = function(params) {
									iElement.off();
									if (params.events.itemClick) {
									}
									if (params.events.contextMenu) {
										iElement.on('contextmenu', 'li', function(event) {
											// disable the default browser's context menu
											event.preventDefault();
											var target = angular.element(event.target).parents('li:first')[0];
											if (target != null) {
												iElement.jqxTree('selectItem', target);
												var posX = angular.element(window).scrollLeft() + parseInt(event.clientX) + 5;
												var posY = angular.element(window).scrollTop() + parseInt(event.clientY) + 5;
												//contextMenu.jqxMenu('open', posX, posY);
												console.log($scope.contextual);
											}
										});
//										var contextMenu = $("#jqxMenu").jqxMenu({
//											width : '120px',
//											height : '56px',
//											autoOpenPopup : false,
//											mode : 'popup'
//										});
										$scope.contextual = $jqMenu.getContextual(params.events.contextMenu);
									}
								};

								var params = getParams();
								bindEvents(params);
								$jqTree.create(iElement, $scope, params);

								$scope.$parent.$watch(iAttrs.jqTree, function(newValue, oldValue) {
									if (newValue === oldValue) {
										return;
									}
									params = getParams();
									bindEvents(params);
									$jqTree.create(iElement, $scope, params);
								});
							}
						};
					}
				};
			} ]);

}(window, jQuery));