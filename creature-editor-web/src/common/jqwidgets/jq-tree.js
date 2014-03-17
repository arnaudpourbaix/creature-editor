/* global jQuery, _ */
(function(window, $, _) {
	'use strict';

	var module = angular.module('jqwidgets.tree', [ 'jqwidgets.common', 'jqwidgets.data-adapter' ]);

	module
			.provider(
					'$jqTree',
					function JqTreeProvider() {
						var options = {};

						this.$get = [
								'$jqCommon',
								'$jqDataAdapter',
								'$compile',
								'$translate',
								function jqTreeService($jqCommon, $jqDataAdapter, $compile, $translate) {
									var service = {
										create : function(element, scope, params) {
											if (!scope[params.data]) {
												throw new Error("undefined data in scope: " + params.data);
											}
											var source = angular.extend({
												datafields : params.datafields,
												datatype : "json",
												localdata : scope[params.data],
												id : params.id
											});
											var dataAdapter = $jqDataAdapter.getRecordsHierarchy(source, params.id, params.parent, params.display);
											var settings = angular.extend({}, $jqCommon.options(), options, params.options, {
												source : dataAdapter
											});
											element.jqxTree(settings);
										},
										addButtons : function(element, scope, settings) {
											var template = '<div class="jq-tree-buttons">';
											if (settings.add) {
												template += '<button type="button" data-ng-click="add()" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-plus" />&nbsp;{{ \'JQWIDGETS.TREE.ADD\' | translate }}</button>';
											}
											if (settings.expandCollapse) {
												template += '<button type="button" data-ng-click="collapse()" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-collapse-down" />&nbsp;{{ \'JQWIDGETS.TREE.COLLAPSE\' | translate }}</button>';
												template += '<button type="button" data-ng-click="expand()" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-expand" />&nbsp;{{ \'JQWIDGETS.TREE.EXPAND\' | translate }}</button>';
											}
											template += '</div>';
											var html = $compile(template)(scope);
											angular.element(settings.selector).html(html);
										}
									};
									return service;
								} ];
					});

	module.directive('jqTree', [
			'$compile',
			'$timeout',
			'$jqCommon',
			'$jqTree',
			'$jqMenu',
			function JqTreeDirective($compile, $timeout, $jqCommon, $jqTree, $jqMenu) {
				return {
					restrict : 'AE',
					replace : true,
					scope : true,
					compile : function() {
						return {
							pre : function($scope, iElement, iAttrs) {
								var getParams = function() {
									return $jqCommon.getParams($scope.$eval(iAttrs.jqTree), [ 'data', 'datafields', 'id', 'parent', 'display' ], [ 'options', 'events', 'contextMenu',
											'buttons' ]);
								};
								var getEntity = function() {
									var selectedItem = iElement.jqxTree('getSelectedItem');
									console.log('selectedItem', selectedItem);
									var entity = _.find($scope[params.data], function(item) { /* jshint -W116 */
										return item[params.id] == selectedItem.id;
									});
									return entity;
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
											if (target == null) {
												throw new Error("Menu should have 'li' elements");
											}
											iElement.jqxTree('selectItem', target);
											var posX = angular.element(window).scrollLeft() + parseInt(event.clientX) + 5;
											var posY = angular.element(window).scrollTop() + parseInt(event.clientY) + 5;
											$scope.contextualMenu.jqxMenu('open', posX, posY);
										});
										$jqMenu.getContextual(params.events.contextMenu, $scope, getEntity).then(function(result) {
											$scope.contextualMenu = result;
										});
									}
								};

								var params = getParams();
								bindEvents(params);
								$jqTree.create(iElement, $scope, params);
								if (params.buttons) {
									$jqTree.addButtons(iElement, $scope, params.buttons);
								}

								$scope.$parent.$watch(iAttrs.jqTree, function(newValue, oldValue) {
									if (newValue === oldValue) {
										return;
									}
									params = getParams();
									bindEvents(params);
									$jqTree.create(iElement, $scope, params);
								});

								$scope.$parent.$watchCollection(params.data + '.length', function() {
									$jqTree.create(iElement, $scope, params);
								});

								$scope.add = function() {
									params.buttons.add();
								};

								$scope.expand = function() {
									iElement.jqxTree('expandAll');
								};

								$scope.collapse = function() {
									iElement.jqxTree('collapseAll');
								};
							}
						};
					}
				};
			} ]);

}(window, jQuery, _));