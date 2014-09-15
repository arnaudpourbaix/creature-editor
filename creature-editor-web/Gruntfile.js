/*jslint node: true */
'use strict';

module.exports = function(grunt) {

	var mountFolder = function(connect, dir) {
		"use strict";
		return connect.static(require('path').resolve(dir));
	};	
	
	require('load-grunt-tasks')(grunt);

	/**
	 * utility function to get all app JavaScript sources.
	 */
	function filterForJS(files) {
		return files.filter(function(file) {
			return file.match(/\.js$/);
		});
	}

	/**
	 * utility function to get all app CSS sources.
	 */
	function filterForCSS(files) {
		return files.filter(function(file) {
			return file.match(/\.css$/);
		});
	}

	var app_folders = {
		src : 'src',
		vendor : 'bower_components',
		build : 'target/build',
		compile : 'target/compile',
	};

	grunt.initConfig({
		pkg: require('./package.json'),
		output_filename: '<%= pkg.name %>-<%= pkg.version %>',
		output_filename_css: '<%= output_filename %>.css',
		output_filename_js: '<%= output_filename %>.js',
		
		app_folders : app_folders,

		/**
		 * This is a collection of file patterns that refer to our app code. These file paths are used in the configuration of build tasks. `js` is all project javascript (except
		 * tests). `ctpl` contains our reusable components `src/common` template HTML files `atpl` contains the same, but for our app's code. `html` is just our main HTML file,
		 * `less` is our main stylesheet `unit` contains our app's unit tests.
		 */
		app_files : {
			js : [ '<%= app_folders.src %>/**/*.js', '!<%= app_folders.src %>/**/*.spec.js', '!<%= app_folders.src %>/assets/**/*.js' ],
			json : [ '<%= app_folders.src %>/**/*.json', '!<%= app_folders.src %>/assets/**/*.json' ],
			jsunit : [ '<%= app_folders.src %>/**/*.spec.js' ],

			atpl : [ '<%= app_folders.src %>/app/**/*.tpl.html' ],
			ctpl : [ '<%= app_folders.src %>/common/**/*.tpl.html' ],

			css : [ '<%= app_folders.src %>/**/*.css', '!<%= app_folders.src %>/assets/**/*.css' ],
			less : [ '<%= app_folders.src %>/**/*.less' ],
			index_html : [ '<%= app_folders.src %>/index.html' ],
			index_less : '<%= app_folders.src %>/app.less'
		},

		/**
		 * This is a collection of files used during testing only.
		 */
		test_files : {
			js : [ '<%=vendor_dir%>angular-mocks/angular-mocks.js' ]
		},

		/**
		 * This is the same as `app_files`, except it contains patterns that reference vendor code that we need to place into the build process somewhere. While the `app_files`
		 * property ensures all standardized files are collected for compilation, it is the user's job to ensure non-standardized (i.e. vendor-related) files are handled
		 * appropriately in `vendor_files.js`. The `vendor_files.js` property holds files to be automatically concatenated and minified with our project source files. The
		 * `vendor_files.css` property holds any CSS files to be automatically included in our app.
		 */
		vendor_files : {
			js : [ 
			      '<%= app_folders.vendor %>/jquery/jquery.min.js', 
			      '<%= app_folders.vendor %>/angular/angular.js', 
			      '<%= app_folders.vendor %>/angular-cookies/angular-cookies.min.js',
					'<%= app_folders.vendor %>/angular-resource/angular-resource.min.js', 
					'<%= app_folders.vendor %>/angular-animate/angular-animate.min.js', 
					'<%= app_folders.vendor %>/angular-sanitize/angular-sanitize.min.js',
					'<%= app_folders.vendor %>/angular-translate/angular-translate.min.js',
					'<%= app_folders.vendor %>/angular-translate-storage-cookie/angular-translate-storage-cookie.min.js', 
					'<%= app_folders.vendor %>/angular-translate-storage-local/angular-translate-storage-local.min.js',
					'<%= app_folders.vendor %>/angular-translate-loader-partial/angular-translate-loader-partial.min.js', 
					'<%= app_folders.vendor %>/angular-ui-utils/ui-utils.min.js',
					'<%= app_folders.vendor %>/angular-ui-router/release/angular-ui-router.js',
					'<%= app_folders.vendor %>/ng-grid/build/ng-grid.min.js',
					'<%= app_folders.vendor %>/angular-local-storage/angular-local-storage.min.js',
					'<%= app_folders.vendor %>/angular-bootstrap/ui-bootstrap-tpls.min.js', 
					'<%= app_folders.vendor %>/lodash/dist/lodash.min.js', 
					'<%= app_folders.src %>/assets/jqwidgets/jqx-all.js', 
					'<%= app_folders.src %>/assets/Long.js',
					'<%= app_folders.vendor %>/log4javascript/log4javascript.js'
				],
			css : [ 
			     '<%= app_folders.vendor %>/font-awesome/css/font-awesome.min.css',
			     '<%= app_folders.vendor %>/bootstrap/dist/css/bootstrap.min.css',
			     '<%= app_folders.vendor %>/ng-grid/ng-grid.min.css'
			   ],
			assets : [ 
			     '<%= app_folders.vendor %>/bootstrap/dist/fonts/*', 
			     '<%= app_folders.vendor %>/bootstrap/dist/css/bootstrap.min.css', 
			     '<%= app_folders.vendor %>/bootstrap/dist/css/bootstrap-theme.min.css', 
			     '<%= app_folders.vendor %>/roboto-fontface/*.css',
			     '<%= app_folders.vendor %>/roboto-fontface/fonts/*' 
			   ]

		},

		connect : {
			options : {
					hostname : '0.0.0.0',
					port : 9000,
					base : '<%= app_folders.build %>'
			},
			proxies : [ { // redirect /rest calls to web application server (tomcat)
				 context : '/rest',
				 host : 'localhost',
				 port : 8090,
				 https : false,
				 changeOrigin : false,
				 rewrite : {
					 'rest' : 'editor/rest'
				 }
			} ],
			main : {
				options : {
					middleware : function(connect) {
						return [ 
						         require('grunt-connect-proxy/lib/utils').proxyRequest, 
						         require('connect-livereload')(), 
						         mountFolder(connect, grunt.config('app_folders.build')) 
						       ];
					}
				}
			}			
		},

		watch : {
			options : {
				livereload : true,
				livereloadOnError : false,
				spawn : false
			},
			
//			gruntfile : {
//				files : 'Gruntfile.js',
//				tasks : [ 'build' ],
//				options : {
//					livereload : false
//				}
//			},

			jssrc : {
				files : [ '<%= app_files.js %>' ],
				tasks : [ 'jshint:src', 'copy:buildAppJs' ]
			},
			
			html : {
				files : [ '<%= app_files.index_html %>' ],
				tasks : [ 'index:build' ]
			},

			tpls : {
				files : [ '<%= app_files.atpl %>', '<%= app_files.ctpl %>' ],
				tasks : [ 'html2js' ]
			},

			less : {
				files : [ '<%= app_files.less %>' ],
				tasks : [ 'less:development', 'autoprefixer' ]
			},
			
			css : {
				files : [ '<%= app_files.css %>' ],
				tasks : [ 'copy:buildAppCss' ]
			},
		
			jsonsrc : {
				files : [ '<%= app_files.json %>' ],
				tasks : [ 'copy:buildAppJson' ]
			}

//			assets : {
//				files : [ '<%= app_folders.src %>/assets/**/*' ],
//				tasks : [ 'copy:buildAppAssets' ]
//			},
//			
//			vendorjs : {
//				files : [ '<%= vendor_files.js %>' ],
//				tasks : [ 'copy:buildVendorJs' ]
//			}
			
		},

		clean : [ '<%= app_folders.build %>' ],
		
		/**
		 * The `index` task compiles the `index.html` file as a Grunt template. CSS and JS files co-exist here but they get split apart later.
		 */
		index : {
			/**
			 * During development, we don't want to have wait for compilation, concatenation, minification, etc. So to avoid these steps, we simply add all script files directly to
			 * the `<head>` of `index.html`. The `src` property contains the list of included files.
			 */
			build : {
				dir : '<%= app_folders.build %>',
				src : [ '<%= vendor_files.js %>', '<%= app_folders.build %>/src/**/*.js',
				        '<%= html2js.app.dest %>', '<%= html2js.common.dest %>',  
				        '<%= app_files.css %>', '<%= vendor_files.css %>', '<%= app_folders.build %>/assets/<%= output_filename_css %>' ]
			// src : [ , '<%= less.development.files.dest %>' ]
			},

			/**
			 * When it is time to have a completely compiled application, we can alter the above to include only a single JavaScript and a single CSS file.
			 */
			compile : {
				dir : '<%= app_folders.compile %>',
				src : [ '<%= concat.compileVendorJs.dest %>', '<%= concat.compileJs.dest %>', '<%= recess.compile.dest %>' ]
			}
		},

		copy : {
			buildAppJs : {
				files : [ {
					src : [ '<%= app_files.js %>' ],
					dest : '<%= app_folders.build %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildVendorJs : {
				files : [ {
					src : [ '<%= vendor_files.js %>' ],
					dest : '<%= app_folders.build %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildAppCss : {
				files : [ {
					src : [ '<%= app_files.css %>' ],
					dest : '<%= app_folders.build %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildVendorCss : {
				files : [ {
					src : [ '<%= vendor_files.css %>' ],
					dest : '<%= app_folders.build %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildAppAssets : {
				files : [ {
					src : [ '**' ],
					dest : '<%= app_folders.build %>/assets/',
					cwd : 'src/assets',
					expand : true
				} ]
			},
			buildVendorAssets : {
				files : [ {
					src : [ '<%= vendor_files.assets %>' ],
					dest : '<%= app_folders.build %>/assets',
					cwd : 'vendor',
					expand : true
				// , flatten : true
				} ]
			},
			buildAppJson : {
				files : [ {
					src : [ '<%= app_files.json %>' ],
					dest : '<%= app_folders.build %>/',
					cwd : '.',
					expand : true
				} ]
			},
			compileAppJson : {
				files : [ {
					src : [ '**/*.json' ],
					dest : '<%= compile %>',
					cwd : '<%= app_folders.build %>',
					expand : true
				} ]
			},
			compileAssets : {
				files : [ {
					src : [ '**' ],
					dest : '<%= compile %>/assets',
					cwd : '<%= app_folders.build %>/assets',
					expand : true
				} ]
			}
		},

		less: {
			development: {
				options: {
				},
				files: {
					"<%= app_folders.build %>/assets/<%= output_filename_css %>" : "<%= app_files.index_less %>"
				}
			},
			production: {
				options: {
					cleancss: true,
					compress: true
				},
				files: {
					"<%= app_folders.build %>/assets/appli.css": "<%= app_files.index_less %>"
				}
			}
		},		

		// Add vendor prefixed styles
		autoprefixer : {
			options : {
				browsers : [ 'last 1 version' ]
			},
			main: {
				files : [ {
					src : "<%= app_folders.build %>/assets/<%= output_filename_css %>",
					dest : "<%= app_folders.build %>/assets/<%= output_filename_css %>"
				} ]
			}
		},
		
		jshint : {
			options : {
				jshintrc : '.jshintrc'
			},
			src : '<%= app_files.js %>'
		},

		html2js : {
			app : {
				options : {
					base : '<%= app_folders.src %>/app'
				},
				src : [ '<%= app_files.atpl %>' ],
				dest : '<%= app_folders.build %>/templates-app.js'
			},
			common : {
				options : {
					base : '<%= app_folders.src %>/common'
				},
				src : [ '<%= app_files.ctpl %>' ],
				dest : '<%= app_folders.build %>/templates-common.js'
			}
		},

		cssmin : {
			main : {
				src : [ 'temp/app.css', '<%= dom_munger.data.appcss %>' ],
				dest : 'dist/app.full.min.css'
			}
		},

		concat : {
			main : {
				src : [ '<%= dom_munger.data.appjs %>', '<%= ngtemplates.main.dest %>' ],
				dest : 'temp/app.full.js'
			}
		},

		ngAnnotate : {
			main : {
				src : 'temp/app.full.js',
				dest : 'temp/app.full.js'
			}
		},

		uglify : {
			main : {
				src : 'temp/app.full.js',
				dest : 'dist/app.full.min.js'
			}
		},

		htmlmin : {
			main : {
				options : {
					collapseBooleanAttributes : true,
					collapseWhitespace : true,
					removeAttributeQuotes : true,
					removeComments : true,
					removeEmptyAttributes : true,
					removeScriptTypeAttributes : true,
					removeStyleLinkTypeAttributes : true
				},
				files : {
					'dist/index.html' : 'dist/index.html'
				}
			}
		},

		imagemin : {
			main : {
				files : [ {
					expand : true,
					cwd : 'dist/',
					src : [ '**/{*.png,*.jpg}' ],
					dest : 'dist/'
				} ]
			}
		},

		karma : {
			options : {
				frameworks : [ 'jasmine' ],
				files : [ // this files data is also updated in the watch handler, if updated change there too
				'bower_components/angular-mocks/angular-mocks.js' ],
				logLevel : 'ERROR',
				reporters : [ 'mocha' ],
				autoWatch : false, // watching is handled by grunt-contrib-watch
				singleRun : true
			},
			all_tests : {
				browsers : [ 'PhantomJS', 'Chrome', 'Firefox' ]
			},
			during_watch : {
				browsers : [ 'PhantomJS' ]
			},
		}
	});

	/**
	 * The index.html template includes the stylesheet and javascript sources based on dynamic names calculated in this Gruntfile. This task assembles the list into variables for
	 * the template to use and then runs the compilation.
	 */
	grunt.registerMultiTask('index', 'Process index.html template', function() {
		var dirRE = new RegExp('^(' + grunt.config('app_folders.build') + '|' + grunt.config('app_folders.compile') + ')\/', 'g');
		var jsFiles = filterForJS(this.filesSrc).map(function(file) {
			return file.replace(dirRE, '');
		});
		var cssFiles = filterForCSS(this.filesSrc).map(function(file) {
			return file.replace(dirRE, '');
		});
		grunt.file.copy(grunt.config('app_files.index_html'), this.data.dir + '/index.html', {
			process : function(contents, path) {
				return grunt.template.process(contents, {
					data : {
						scripts : jsFiles,
						styles : cssFiles,
						version : grunt.config('pkg.version')
					}
				});
			}
		});
	});

	grunt.registerTask('build', [ 'jshint', 'clean', 'copy:buildAppJs', 'copy:buildVendorJs', 'copy:buildAppCss', 'copy:buildVendorCss',
	                              'copy:buildAppJson',
	                              'less:development', 'autoprefixer', 'html2js', 'index:build' ]);
	// grunt.registerTask('build', [ 'ngtemplates', 'cssmin', 'concat', 'ngmin', 'uglify', 'copy', 'htmlmin', 'imagemin' ]);
	grunt.registerTask('serve', [ 'build', 'configureProxies', 'connect:main', 'watch' ]);
	grunt.registerTask('test', [ 'karma:all_tests' ]);

};
