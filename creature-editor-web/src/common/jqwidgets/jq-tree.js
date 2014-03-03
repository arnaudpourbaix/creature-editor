/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.tree', [ 'jqwidgets.services' ]);

	var createTree = function($jqwidgets, element, scope, params) {
		if (!params.data || !scope[params.data]) {
			throw new Error("undefined data!");
		}
		if (!params.datafields) {
			throw new Error("undefined param 'datafields'!");
		}
		if (!params.id) {
			throw new Error("undefined param 'id'!");
		}
		if (!params.parent) {
			throw new Error("undefined param 'parent'!");
		}
		if (!params.display) {
			throw new Error("undefined param 'display'!");
		}
		var source = angular.extend({
			datafields : params.datafields,
			datatype : "json",
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
						var params = $scope.$eval(iAttrs.jqTree);
						createTree($jqwidgets, iElement, $scope, params);
						$scope.$parent.$watch(iAttrs.jqGrid, function() {
							params = $scope.$eval(iAttrs.jqTree);
							createTree($jqwidgets, iElement, $scope, params);
						});
					}
				};
			}
		};
	} ]);

}(window, jQuery));