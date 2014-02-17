/* global jQuery */
(function(window, $) {
	'use strict';

	var module = angular.module('toolbox.services', []);

	module.factory('uiService', function() {
		var uiService = {};

		uiService.draggable = function(element, options) {
			var defaultOpts = {
				cursor : 'move',
				refreshPositions : true,
				opacity : 0.5,
				drag : function(e, ui) {
					ui.position.left = ui.position.left * 1.4;
					ui.position.top = ui.position.top * 1.0;
				}
			}, opts = angular.extend(defaultOpts, options || {});
			$(element).draggable(opts);
		};

		uiService.resizable = function(element, options) {
			var defaultOpts = {}, opts = angular.extend(defaultOpts, options || {});
			$(element).resizable(opts);
		};

		return uiService;

	});

}(window, jQuery));
