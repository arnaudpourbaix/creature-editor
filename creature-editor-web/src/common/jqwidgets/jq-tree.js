/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.tree', [ 'jqwidgets.services' ]);

	var createTree = function($jqwidgets, element, scope, params) {
		if (!scope[params.data]) {
			throw new Error("undefined data in scope: " + params.data);
		}
		var source = angular.extend({
			datafields : params.datafields,
			datatype : "array",
			localdata : scope[params.data],
			id : params.id
		});
		var dataAdapter = $jqwidgets.dataAdapter().getRecordsHierarchy(source, params.id, params.parent, params.display);
		var settings = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.treeOptions(), params.options, {
			source : dataAdapter
		});
		element.jqxTree(settings);
	};
	
	module.directive('jqTree', [ '$compile', '$jqwidgets', function JqTreeDirective($compile, $jqwidgets) {
		return {
			restrict : 'AE',
			replace : true,
			scope : true,
			compile : function() {
				return {
					pre : function($scope, iElement, iAttrs) {
						var getParams = function() {
							return $jqwidgets.common().getParams($scope.$eval(iAttrs.jqTree), ['data', 'datafields', 'id', 'parent', 'display'], ['options', 'events', 'contextMenu']);
						};
						var bindEvents = function(params) {
							iElement.off();
							if (params.events.itemClick) {
							}
							if (params.events.contextMenu) {
								iElement.on('contextmenu', 'li', function (event) {
									// disable the default browser's context menu
									event.preventDefault();
									var target = angular.element(event.target).parents('li:first')[0];
									if (target != null) {
										iElement.jqxTree('selectItem', target);
										var posX = angular.element(window).scrollLeft() + parseInt(event.clientX) + 5;
										var posY = angular.element(window).scrollTop() + parseInt(event.clientY) + 5;
										contextMenu.jqxMenu('open', posX, posY);
									}
								});
								var contextMenu = $("#jqxMenu").jqxMenu({ width: '120px',  height: '56px', autoOpenPopup: false, mode: 'popup' });
								$jqwidgets.menu().getContextual(params.events.contextMenu);
							}
						};

						
						var params = getParams();
						bindEvents(params);
						createTree($jqwidgets, iElement, $scope, params);
						
						$scope.$parent.$watch(iAttrs.jqGrid, function(newValue, oldValue) {
							if (newValue === oldValue) {
								return;
							}
							params = getParams();
							bindEvents(params);
							createTree($jqwidgets, iElement, $scope, params);
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery));