/**
 * This file/module contains all configuration for the build process.
 */
module.exports = {
	/**
	 * The `build_dir` folder is where our projects are compiled during development and the `compile_dir` folder is where our app resides once it's completely
	 * built.
	 */
	build_dir : 'build',
	compile_dir : 'bin',

	/**
	 * This is a collection of file patterns that refer to our app code (the stuff in `src/`). These file paths are used in the configuration of build tasks.
	 * `js` is all project javascript (except tests). `ctpl` contains our reusable components `src/common` template HTML files `atpl` contains the same, but for
	 * our app's code. `html` is just our main HTML file, `less` is our main stylesheet `unit` contains our app's unit tests.
	 */
	app_files : {
		js : [ 'src/**/*.js', '!src/**/*.spec.js', '!src/webapp/**/*.js', '!src/assets/**/*.js' ],
		jsunit : [ 'src/**/*.spec.js' ],

		atpl : [ 'src/app/**/*.tpl.html' ],
		ctpl : [ 'src/common/**/*.tpl.html' ],

		html : [ 'src/index.html' ],
		less : 'src/less/main.less'
	},

	/**
	 * This is a collection of files used during testing only.
	 */
	test_files : {
		js : [ 'vendor/angular-mocks/angular-mocks.js' ]
	},

	/**
	 * This is the same as `app_files`, except it contains patterns that reference vendor code (`vendor/`) that we need to place into the build process
	 * somewhere. While the `app_files` property ensures all standardized files are collected for compilation, it is the user's job to ensure non-standardized
	 * (i.e. vendor-related) files are handled appropriately in `vendor_files.js`. The `vendor_files.js` property holds files to be automatically concatenated
	 * and minified with our project source files. The `vendor_files.css` property holds any CSS files to be automatically included in our app.
	 */
	vendor_files : {
		/*
		 * js : [ 'vendor/jquery/jquery.min.js', 'vendor/angular/angular.min.js', 'vendor/angular-bootstrap/ui-bootstrap-tpls.min.js',
		 * 'vendor/angular-cookies/angular-cookies.min.js', 'vendor/angular-resource/angular-resource.min.js', 'vendor/bootstrap/dist/js/bootstrap.min.js',
		 * 'vendor/html5shiv/dist/html5shiv.js', 'vendor/html5shiv/dist/html5shiv-printshiv.js', 'vendor/placeholders/angular-placeholders-0.0.1-SNAPSHOT.min.js',
		 * 'vendor/angular-ui-router/release/angular-ui-router.min.js', 'vendor/angular-ui-utils/modules/route/route.js',
		 * 'vendor/angular-route/angular-route.min.js', 'vendor/jqwidgets/jqx-all.js' ],
		 */
		js : [ 'vendor/jquery/jquery.js', 'vendor/angular/angular.js', 'vendor/angular-bootstrap/ui-bootstrap-tpls.js',
				'vendor/angular-cookies/angular-cookies.js', 'vendor/angular-resource/angular-resource.js', 'vendor/bootstrap/dist/js/bootstrap.js',
				'vendor/html5shiv/dist/html5shiv.js', 'vendor/html5shiv/dist/html5shiv-printshiv.js',
				'vendor/placeholders/angular-placeholders-0.0.1-SNAPSHOT.min.js', 'vendor/angular-ui-router/release/angular-ui-router.js',
				'vendor/angular-ui-utils/modules/route/route.js', 'vendor/angular-route/angular-route.js', 'vendor/jqwidgets/jqx-all.js' ],
		css : [ /* 'vendor/jqwidgets/styles/jqx.base.css' */],
		assets : []

	}
};
