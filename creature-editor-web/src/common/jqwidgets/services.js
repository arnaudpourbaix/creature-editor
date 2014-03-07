/* global _ */
(function(window, _) {
	'use strict';

	var module = angular.module('jqwidgets.services', []);

	function JqCommonService(commonOptions) {
		var service = {
			/**
			 * return params object after controlling required properties and adding missing optional properties
			 * @param params : object (required)
			 * @param requiredProps : array of string (optional)
			 * @param optionalProps : array of string (optional)
			 */
			getParams : function(params, requiredProps, optionalProps) {
				var paramKeys = _.keys(params);
				if (angular.isArray(requiredProps)) {
					var difference = _.difference(requiredProps, paramKeys);
					if (difference.length) {
						throw new Error("missing required params: " + difference.toString());
					}
				}
				if (angular.isArray(optionalProps)) {
					angular.forEach(optionalProps, function(prop, index) {
						params[prop] = params[prop] || {};
					});
				}
				return params;
			}
		};
		return service;
	}
	
	function JqDataAdapterService(commonService, dataAdapterOptions) {
		
		var getDatafields = function(datafields) {
			var result = [];
			var props = [ 'name', 'type', 'map', 'format', 'values' ];
			angular.forEach(datafields, function(datafield, index) {
				var name = datafield.name || datafield.datafield;
				if (!name) {
					return;
				}
				result.push(angular.extend(_.pick(datafield, props), {
					name : name
				}));
			});
			return result;
		};

		var getDataAdapter = function(source, settings) {
			source.datafields = getDatafields(source.datafields);
			var params = angular.extend({}, dataAdapterOptions, settings);
			return new $.jqx.dataAdapter(source, params);
		};

		var service = {
			get : function(source, settings) {
				return getDataAdapter(source, settings);
			},
			getDatafields : function(datafields) {
				return getDatafields(datafields);
			},
			getRecordsHierarchy : function(source, id, parent, display, settings) {
				var dataAdapter = getDataAdapter(source, settings);
				return dataAdapter.getRecordsHierarchy(id, parent, null, [ {
					name : display,
					map : 'label'
				} ]);
			}
		};
		return service;
	}

	function JqMenuService(commonService, menuOptions) {
		
		var checkParams = function(params) {
			commonService.getParams(params, ['items']);
			if (!angular.isArray(params.items)) {
				throw new Error("items must be an array: " + params.items);
			}
			angular.forEach(items, function(item, index) {
				
			});
		};
		
		var service = {
			getContextual : function(params) {
				checkParams(params);
				//var contextMenu = $("#jqxMenu").jqxMenu({ width: '120px',  height: '56px', autoOpenPopup: false, mode: 'popup' });
				console.log('getContextual', params);
			}
		};
		return service;
	}
	
	function JqWidgetService(dataAdapterOptions, commonOptions, gridOptions, dropDownListOptions, windowOptions, panelOptions, treeOptions, menuOptions) {
		var commonService = new JqCommonService(commonOptions);
		var dataAdapterService = new JqDataAdapterService(commonService, dataAdapterOptions);
		var menuService = new JqMenuService(commonService, menuOptions);
		var service = {
			dataAdapter : function() {
				return dataAdapterService;
			},
			menu : function() {
				return menuService;
			},
			common : function() {
				return commonService;
			},
			commonOptions : function() {
				return commonOptions;
			},
			gridOptions : function() {
				return gridOptions;
			},
			dropDownListOptions : function() {
				return dropDownListOptions;
			},
			windowOptions : function() {
				return windowOptions;
			},
			panelOptions : function() {
				return panelOptions;
			},
			treeOptions : function() {
				return treeOptions;
			},
			menuOptions : function() {
				return menuOptions;
			}
		};
		return service;
	}

	module.provider('$jqwidgets', function JqWidgetProvider() {
		var commonOptions = {
			theme : 'classic'
		};
		var gridOptions = {
			altRows : true,
			columnsResize : true,
			sortable : true,
			showfilterrow : true,
			filterable : true,
			selectionMode : 'singlerow',
			pagermode : 'simple',
			enabletooltips : true
		};
		var dropDownListOptions = {
			searchMode : 'containsignorecase'
		};
		var windowOptions = {
			showCollapseButton : true,
			isModal : true
		};
		var panelOptions = {

		};
		var treeOptions = {

		};
		var menuOptions = {
			showTopLevelArrows : true,
			enableHover : true,
			autoOpen : true
		};
		var dataAdapterOptions = {
			autoBind : true
		};
		this.theme = function(value) {
			commonOptions.theme = value;
		};

		this.$get = [ function jqWidgetService() {
			return new JqWidgetService(dataAdapterOptions, commonOptions, gridOptions, dropDownListOptions, windowOptions, panelOptions, treeOptions, menuOptions);
		} ];
	});

}(window, _));