angular.module('apx-jqwidgets.data-adapter', [])

.provider('$jqDataAdapter', function() {
	var defaultSource = {
		mapchar : '.'
	};
	var defaultSettings = {
		autoBind : true
	};
	this.autoBind = function(value) {
		defaultSettings.autoBind = value;
	};

	this.$get = [ '$jqCommon', function jqDataAdapterService($jqCommon) {
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
			var params = angular.extend({}, defaultSettings, settings);
			var src = angular.extend({}, defaultSource, source);
			return new $.jqx.dataAdapter(src, params);
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
	} ];
})

;
