angular.module('apx-jqwidgets.data-adapter', [])

/**
 * @ngdoc service
 * @name apx-jqwidgets.jqDataAdapterProvider
 * @description
 * Use `jqDataAdapterProvider` to change the default behavior of the {@link  apx-jqwidgets.jqDataAdapter jqDataAdapter} service.
 */
.provider('jqDataAdapter', function() {
	/**
	 * @ngdoc property
	 * @name apx-jqwidgets.jqDataAdapterProvider#sourceDefaults
	 * @propertyOf apx-jqwidgets.jqDataAdapterProvider
	 * @description Object containing source default values for {@link apx-jqwidgets.jqDataAdapter jqDataAdapter}. The object has following properties:
	 * 
	 * - **mapchar** - `{string}` - Specifies the mapping char (used to access object children).<br>
	 * 
	 * @returns {Object} Default values object.
	 */		
	var sourceDefaults = this.sourceDefaults = {
		mapchar : '.'
	};

	/**
	 * @ngdoc property
	 * @name apx-jqwidgets.jqDataAdapterProvider#settingDefaults
	 * @propertyOf apx-jqwidgets.jqDataAdapterProvider
	 * @description Object containing settings default values for {@link apx-jqwidgets.jqDataAdapter jqDataAdapter}. The object has following properties:
	 * 
	 * - **autoBind** - `{boolean}` - Automatically calls the dataAdapter's dataBind method on initialization.<br>
	 * 
	 * @returns {Object} Default values object.
	 */		
	var settingDefaults = this.settingDefaults = {
		autoBind : true
	};
	
	/**
	 * @ngdoc service
	 * @name apx-jqwidgets.jqDataAdapter
     * @requires apx-jqwidgets.jqCommon
	 * @description
	 * # jqDataAdapter
	 * This service is a wrapper for data-adapter widget.<br>
	 */
	this.$get = function(jqCommon) {

		var service = {};

		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqDataAdapter#getDatafields
		 * @methodOf apx-jqwidgets.jqDataAdapter
		 * @description Returns an array of fields that only contain valid properties. Invalid properties or fields will be ignored.
		 * @param {array} datafields An array describing the fields in a particular record. Each datafield must define the following members: name, type, map (optional), format (optional).
		 * @returns {array} Valid fields array.
		 */
		service.getDatafields = function(datafields) {
			var result = [];
			var props = [ 'name', 'type', 'map', 'format' ];
			angular.forEach(datafields, function(datafield, index) {
				var name = datafield.name || datafield.datafield;
				if (name) {
					result.push(angular.extend(_.pick(datafield, props), { name : name }));
				}
			});
			return result;
		};
		
		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqDataAdapter#get
		 * @methodOf apx-jqwidgets.jqDataAdapter
		 * @description Returns a new data-adapter.
		 * @param {Object} source Set of key/value pairs that configure the jqxDataAdapter's source object.
		 * @param {Object=} settings Set of key/value pairs that configure the jqxDataAdapter plug-in. All settings are optional.
		 * @returns {Object} Data-adapter.
		 */
		service.get = function(source, settings) {
			source.datafields = service.getDatafields(source.datafields);
			var params = angular.extend({}, settingDefaults, settings);
			var src = angular.extend({}, sourceDefaults, source);
			return new $.jqx.dataAdapter(src, params);
		};
			
		/**
		 * @ngdoc function
		 * @name apx-jqwidgets.jqDataAdapter#getRecordsHierarchy
		 * @methodOf apx-jqwidgets.jqDataAdapter
		 * @description Returns a data-adapter and builds a data tree.
		 * @param {string} id Field’s id.
		 * @param {string} parent Parent field’s id.
		 * @param {string} display Display label.
		 * @returns {Object} Hierarchical data-adapter.
		 */
		service.getRecordsHierarchy = function(source, id, parent, display, settings) {
				var dataAdapter = service.get(source, settings);
				return dataAdapter.getRecordsHierarchy(id, parent, null, [ {
					name : display,
					map : 'label'
				} ]);
		};
		
		return service;
	};
})

;