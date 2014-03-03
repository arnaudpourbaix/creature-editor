/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('jqwidgets.panel', [ 'jqwidgets.services' ]);

	module.directive('jqPanel', [ '$compile', '$jqwidgets', function JqPanelDirective($compile, $jqwidgets) {
		return {
			restrict : 'AE',
			replace : true,
			//scope : true,
			compile : function() {
				return {
					pre : function($scope, iElement, iAttrs) {
						var params = $scope.$eval(iAttrs.jqPanel);
						var options = angular.extend({}, $jqwidgets.commonOptions(), $jqwidgets.panelOptions(), params.options);
						iElement.jqxPanel(options);
						if (params.content) {
							var content = $compile(params.content)($scope);
							iElement.jqxPanel('append', content);
						}
					}
				};
			}
		};
	} ]);

}(window, jQuery));