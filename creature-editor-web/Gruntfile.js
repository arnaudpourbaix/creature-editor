var lrSnippet = require('grunt-contrib-livereload/lib/utils').livereloadSnippet;

var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest;

var mountFolder = function(connect, dir) {
	"use strict";
	return connect.static(require('path').resolve(dir));
};

module.exports = function(grunt) {
	"use strict";
	grunt.loadNpmTasks('grunt-contrib-connect');
	grunt.loadNpmTasks('grunt-connect-proxy');
	grunt.loadNpmTasks('grunt-contrib-livereload');
	grunt.loadNpmTasks('grunt-open');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-conventional-changelog');
	grunt.loadNpmTasks('grunt-bump');
	grunt.loadNpmTasks('grunt-recess');
	grunt.loadNpmTasks('grunt-karma');
	grunt.loadNpmTasks('grunt-ngmin');
	grunt.loadNpmTasks('grunt-html2js');

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

	var userConfig = require('./build.config.js');

	var taskConfig = {
		pkg : grunt.file.readJSON("package.json"),

		connect : {
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
			options : {
				base : 'build',
				port : 9000,
				hostname : '0.0.0.0' // Change this to '0.0.0.0' to access the server from outside.
			},
			livereload : {
				options : {
					middleware : function(connect) {
						return [ proxySnippet, lrSnippet, mountFolder(connect, 'build') ];
					}
				}
			},
			test : {
				options : {
					middleware : function(connect) {
						return [ mountFolder(connect, 'build'), mountFolder(connect, 'test') ];
					}
				}
			}
		},

		open : {
			server : {
				url : 'http://localhost:<%=connect.options.port%>'
			}
		},

		/**
		 * The banner is the comment that is placed at the top of our compiled source files. It is first processed as a Grunt template, where the `<%=` pairs are
		 * evaluated based on this very configuration object.
		 */
		meta : {
			banner : '/**\n * <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %>\n *\n * Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author %>\n'
					+ ' */\n'
		},

		/**
		 * Creates a changelog on a new version.
		 */
		changelog : {
			options : {
				dest : 'CHANGELOG.md',
				template : 'changelog.tpl'
			}
		},

		/**
		 * Increments the version number.
		 */
		bump : {
			options : {
				files : [ "package.json", "bower.json" ],
				commit : false,
				commitMessage : 'chore(release): v%VERSION%',
				commitFiles : [ "package.json", "bower.json" ],
				createTag : false,
				tagName : 'v%VERSION%',
				tagMessage : 'Version %VERSION%',
				push : false,
				pushTo : 'origin'
			}
		},

		clean : [ '<%=build_dir%>', '<%=compile_dir%>' ],

		copy : {
			buildAppJs : {
				files : [ {
					src : [ '<%= app_files.js %>' ],
					dest : '<%= build_dir %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildAppJson : {
				files : [ {
					src : [ '<%= app_files.json %>' ],
					dest : '<%= build_dir %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildVendorJs : {
				files : [ {
					src : [ '<%= vendor_files.js %>' ],
					dest : '<%= build_dir %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildAppCss : {
				files : [ {
					src : [ '<%= app_files.css %>' ],
					dest : '<%= build_dir %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildVendorCss : {
				files : [ {
					src : [ '<%= vendor_files.css %>' ],
					dest : '<%= build_dir %>/',
					cwd : '.',
					expand : true
				} ]
			},
			buildAppAssets : {
				files : [ {
					src : [ '**' ],
					dest : '<%= build_dir %>/assets/',
					cwd : 'src/assets',
					expand : true
				} ]
			},
			buildVendorAssets : {
				files : [ {
					src : [ '<%= vendor_files.assets %>' ],
					dest : '<%= build_dir %>/assets',
					cwd : 'vendor',
					expand : true
				// , flatten : true
				} ]
			},
			compileAppJson : {
				files : [ {
					src : [ '**/*.json' ],
					dest : '<%= compile_dir %>',
					cwd : '<%= build_dir %>',
					expand : true
				} ]
			},
			compileAssets : {
				files : [ {
					src : [ '**' ],
					dest : '<%= compile_dir %>/assets',
					cwd : '<%= build_dir %>/assets',
					expand : true
				} ]
			}
		},

		concat : {
			/**
			 * The `compile_css` target concatenates compiled CSS and vendor CSS together.
			 */
			compileCss : {
				src : [ '<%= vendor_files.css %>', '<%= recess.build.dest %>' ],
				dest : '<%= recess.build.dest %>'
			},
			/**
			 * The `compileJs` target is the concatenation of our application source code.
			 */
			compileJs : {
				options : {
					banner : '<%= meta.banner %>'
				},
				src : [ 'module.prefix', '<%= build_dir %>/src/**/*.js', '<%= html2js.app.dest %>', '<%= html2js.common.dest %>', 'module.suffix' ],
				dest : '<%= compile_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.js'
			},
			/**
			 * The `compileVendorJs` target is the concatenation of all specified vendor source code into a single file.
			 */
			compileVendorJs : {
				src : [ '<%= vendor_files.js %>' ],
				dest : '<%= compile_dir %>/assets/<%= pkg.name %>-vendors-<%= pkg.version %>.js'
			}
		},

		/**
		 * `ng-min` annotates the sources before minifying. That is, it allows us to code without the array syntax.
		 */
		ngmin : {
			compile : {
				files : [ {
					src : [ '<%= app_files.js %>' ],
					cwd : '<%= build_dir %>',
					dest : '<%= build_dir %>',
					expand : true
				} ]
			}
		},

		/**
		 * Minify the sources.
		 */
		uglify : {
			compile : {
				options : {
					banner : '<%= meta.banner %>'
				},
				files : {
					'<%= concat.compileJs.dest %>' : '<%= concat.compileJs.dest %>'
				}
			}
		},

		/**
		 * `recess` handles our LESS compilation and uglification automatically. Only our `main.less` file is included in compilation; all other files must be
		 * imported from this file.
		 */
		recess : {
			build : {
				src : [ '<%= app_files.less %>' ],
				dest : '<%= build_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.css',
				options : {
					compile : true,
					compress : false,
					noUnderscores : false,
					noIDs : false,
					zeroUnits : false
				}
			},
			compile : {
				src : [ '<%= recess.build.dest %>' ],
				dest : '<%= recess.build.dest %>',
				options : {
					compile : true,
					compress : true,
					noUnderscores : false,
					noIDs : false,
					zeroUnits : false
				}
			}
		},

		/**
		 * `jshint` defines the rules of our linter as well as which files we should check. All javascript sources, and all our unit tests are linted based on the
		 * policies listed in `options`. But we can also specify exclusionary patterns by prefixing them with an exclamation point (!); this is useful when code
		 * comes from a third party but is nonetheless inside `src/`.
		 */
		jshint : {
			src : [ '<%= app_files.js %>' ],
			test : [ '<%= app_files.jsunit %>' ],
			options : {
				jshintrc : '.jshintrc'
			}
		},

		/**
		 * HTML2JS is a Grunt plugin that takes all of your template files and places them into JavaScript files as strings that are added to AngularJS's template
		 * cache. This means that the templates too become part of the initial payload as one JavaScript file.
		 */
		html2js : {
			/**
			 * These are the templates from `src/app`.
			 */
			app : {
				options : {
					base : 'src/app'
				},
				src : [ '<%= app_files.atpl %>' ],
				dest : '<%= build_dir %>/templates-app.js'
			},

			/**
			 * These are the templates from `src/common`.
			 */
			common : {
				options : {
					base : 'src/common'
				},
				src : [ '<%= app_files.ctpl %>' ],
				dest : '<%= build_dir %>/templates-common.js'
			}
		},

		/**
		 * The `index` task compiles the `index.html` file as a Grunt template. CSS and JS files co-exist here but they get split apart later.
		 */
		index : {
			/**
			 * During development, we don't want to have wait for compilation, concatenation, minification, etc. So to avoid these steps, we simply add all script
			 * files directly to the `<head>` of `index.html`. The `src` property contains the list of included files.
			 */
			build : {
				dir : '<%= build_dir %>',
				src : [ '<%= vendor_files.js %>', '<%= build_dir %>/src/**/*.js', '<%= html2js.common.dest %>', '<%= html2js.app.dest %>',
				        '<%= app_files.css %>', '<%= vendor_files.css %>', '<%= recess.build.dest %>' ]
			},

			/**
			 * When it is time to have a completely compiled application, we can alter the above to include only a single JavaScript and a single CSS file.
			 */
			compile : {
				dir : '<%= compile_dir %>',
				src : [ '<%= concat.compileVendorJs.dest %>', '<%= concat.compileJs.dest %>', '<%= recess.compile.dest %>' ]
			}
		},

		/**
		 * The Karma configurations.
		 */
		karma : {
			options : {
				configFile : '<%= build_dir %>/karma-unit.js'
			},
			unit : {
				port : 9877,
				background : true
			},
			continuous : {
				singleRun : true
			}
		},

		/**
		 * This task compiles the karma template so that changes to its file array don't have to be managed manually.
		 */
		karmaconfig : {
			unit : {
				dir : '<%= build_dir %>',
				src : [ '<%= vendor_files.js %>', '<%= html2js.app.dest %>', '<%= html2js.common.dest %>', '<%= test_files.js %>' ]
			}
		},

		/**
		 * And for rapid development, we have a watch set up that checks to see if any of the files listed below change, and then to execute the listed tasks when
		 * they do. This just saves us from having to type "grunt" into the command-line every time we want to see what we're working on; we can instead just
		 * leave "grunt watch" running in a background terminal. Set it and forget it, as Ron Popeil used to tell us. But we don't need the same thing to happen
		 * for all the files.
		 */
		delta : {
			/**
			 * By default, we want the Live Reload to work for all tasks; this is overridden in some tasks (like this file) where browser resources are unaffected.
			 * It runs by default on port 35729, which your browser plugin should auto-detect.
			 */
			options : {
				livereload : true
			},

			/**
			 * When the Gruntfile changes, we just want to lint it. In fact, when your Gruntfile changes, it will automatically be reloaded!
			 */
			gruntfile : {
				files : 'Gruntfile.js',
				tasks : [ 'jshint:gruntfile' ],
				options : {
					livereload : false
				}
			},

			config : {
				files : 'build.config.js',
				tasks : [ 'buildnotest' ]
			},

			/**
			 * When our JavaScript source files change, we want to run lint them and run our unit tests.
			 */
			jssrc : {
				files : [ '<%= app_files.js %>' ],
				tasks : [ 'jshint:src', 'copy:buildAppJs', 'karma:unit:run' ]
			},

			jsonsrc : {
				files : [ '<%= app_files.json %>' ],
				tasks : [ 'copy:buildAppJson' ]
			},

			/**
			 * When assets are changed, copy them. Note that this will *not* copy new files, so this is probably not very useful.
			 */
			assets : {
				files : [ 'src/assets/**/*' ],
				tasks : [ 'copy:buildAssets' ]
			},

			/**
			 * When index.html changes, we need to compile it.
			 */
			html : {
				files : [ '<%= app_files.html %>' ],
				tasks : [ 'index:build' ]
			},

			/**
			 * When our templates change, we only rewrite the template cache.
			 */
			tpls : {
				files : [ '<%= app_files.atpl %>', '<%= app_files.ctpl %>' ],
				tasks : [ 'html2js' ]
			},

			/**
			 * When the CSS files change, we need to compile and minify them.
			 */
			less : {
				files : [ 'src/**/*.less' ],
				tasks : [ 'recess:build', 'concat:compileCss' ]
			},

			/**
			 * When a JavaScript unit test file changes, we only want to lint it and run the unit tests. We don't want to do any live reloading.
			 */
			jsunit : {
				files : [ '<%= app_files.jsunit %>' ],
				tasks : [ 'jshint:test', 'karma:unit:run' ],
				options : {
					livereload : false
				}
			}
		}
	};

	grunt.initConfig(grunt.util._.extend(taskConfig, userConfig));

	/**
	 * In order to make it safe to just compile or copy *only* what was changed, we need to ensure we are starting from a clean, fresh build. So we rename the
	 * `watch` task to `delta` (that's why the configuration var above is `delta`) and then add a new task called `watch` that does a clean build before watching
	 * for changes.
	 */
	grunt.renameTask('watch', 'delta');
	grunt.registerTask('watch', [ 'build', 'karma:unit', 'delta' ]);

	/**
	 * The default task is to build and compile.
	 */
	grunt.registerTask('default', [ 'build', 'compile' ]);

	/**
	 * The `build` task gets your app ready to run for development and testing.
	 */
	grunt.registerTask('build', [ 'buildnotest', 'karmaconfig', 'karma:continuous' ]);

	grunt.registerTask('buildnotest', [ 'clean', 'html2js', 'jshint', 'recess:build', 'copy:buildAppAssets', 'copy:buildVendorAssets', 'copy:buildAppJs',
			'copy:buildAppJson', 'copy:buildVendorJs', 'copy:buildAppCss', 'copy:buildVendorCss', 'index:build' ]);

	/**
	 * The `compile` task gets your app ready for deployment by concatenating and minifying your code.
	 */
	grunt.registerTask('compile', [ 'concat:compileCss', 'recess:compile', 'copy:compileAssets', 'ngmin', 'copy:compileAppJson', 'concat:compileVendorJs',
			'concat:compileJs', 'uglify', 'index:compile' ]);

	grunt.registerTask('server', [ 'configureProxies', 'connect:livereload', 'open', 'watch' ]);

	grunt.registerTask('production', [ 'configureProxies', 'connect:livereload', 'open', 'build', 'compile', 'delta' ]);

	/**
	 * The index.html template includes the stylesheet and javascript sources based on dynamic names calculated in this Gruntfile. This task assembles the list
	 * into variables for the template to use and then runs the compilation.
	 */
	grunt.registerMultiTask('index', 'Process index.html template', function() {
		//console.log(this.filesSrc);
		var dirRE = new RegExp('^(' + grunt.config('build_dir') + '|' + grunt.config('compile_dir') + ')\/', 'g');
		var jsFiles = filterForJS(this.filesSrc).map(function(file) {
			return file.replace(dirRE, '');
		});
		var cssFiles = filterForCSS(this.filesSrc).map(function(file) {
			return file.replace(dirRE, '');
		});
		grunt.file.copy('src/index.html', this.data.dir + '/index.html', {
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

	/**
	 * In order to avoid having to specify manually the files needed for karma to run, we use grunt to manage the list for us. The `karma/*` files are compiled
	 * as grunt templates for use by Karma. Yay!
	 */
	grunt.registerMultiTask('karmaconfig', 'Process karma config templates', function() {
		var jsFiles = filterForJS(this.filesSrc);

		grunt.file.copy('karma/karma-unit.tpl.js', grunt.config('build_dir') + '/karma-unit.js', {
			process : function(contents, path) {
				return grunt.template.process(contents, {
					data : {
						scripts : jsFiles
					}
				});
			}
		});
	});

};
