angular.module("apx-tools.panel", [])

.directive('pageslide', [
    function (){
        var defaults = {};

        return {
            restrict: "EA",
            replace: false,
            transclude: false,
            scope: {
                psOpen: "=?",
                psAutoClose: "=?"
            },
            link: function ($scope, el, attrs) {
                /* parameters */
                var param = {};

                param.side = attrs.psSide || 'right';
                param.speed = attrs.psSpeed || '0.5';
                param.size = attrs.psSize || '300px';
                param.zindex = attrs.psZindex || 1000;
                param.className = attrs.psClass || 'ng-pageslide';
                
                /* DOM manipulation */
                var content = null;
                var slider = null;

                if (!attrs.href && el.children() && el.children().length) {
                    content = el.children()[0];  
                } else {

                    var targetId = (attrs.href || attrs.psTarget).substr(1);
                    content = document.getElementById(targetId);
                    slider = document.getElementById('pageslide-target-' + targetId);

                    if (!slider) {
                        slider = document.createElement('div');
                        slider.id = 'pageslide-target-' + targetId;
                    }
                }
                
                // Check for content
                if (!content) {
                    throw new Error('You have to elements inside the <pageslide> or you have not specified a target href');
                } 

                slider = slider || document.createElement('div');
                slider.className = param.className;

                /* Style setup */
                slider.style.transitionDuration = param.speed + 's';
                slider.style.webkitTransitionDuration = param.speed + 's';
                slider.style.zIndex = param.zindex;
                slider.style.position = 'fixed';
                slider.style.width = 0;
                slider.style.height = 0;
                slider.style.transitionProperty = 'width, height';

                switch (param.side){
                    case 'right':
                        slider.style.height = attrs.psCustomHeight || '100%'; 
                        slider.style.top = attrs.psCustomTop ||  '0px';
                        slider.style.bottom = attrs.psCustomBottom ||  '0px';
                        slider.style.right = attrs.psCustomRight ||  '0px';
                        break;
                    case 'left':
                        slider.style.height = attrs.psCustomHeight || '100%';   
                        slider.style.top = attrs.psCustomTop || '0px';
                        slider.style.bottom = attrs.psCustomBottom || '0px';
                        slider.style.left = attrs.psCustomLeft || '0px';
                        break;
                    case 'top':
                        slider.style.width = attrs.psCustomWidth || '100%';   
                        slider.style.left = attrs.psCustomLeft || '0px';
                        slider.style.top = attrs.psCustomTop || '0px';
                        slider.style.right = attrs.psCustomRight || '0px';
                        break;
                    case 'bottom':
                        slider.style.width = attrs.psCustomWidth || '100%'; 
                        slider.style.bottom = attrs.psCustomBottom || '0px';
                        slider.style.left = attrs.psCustomLeft || '0px';
                        slider.style.right = attrs.psCustomRight || '0px';
                        break;
                }


                /* Append */
                document.body.appendChild(slider);
                slider.appendChild(content);

                /* Closed */
                function psClose(slider,param){
                    if (slider && slider.style.width !== 0 && slider.style.width !== 0){
                        content.style.display = 'none';
                        switch (param.side){
                            case 'right':
                                slider.style.width = '0px'; 
                                break;
                            case 'left':
                                slider.style.width = '0px';
                                break;
                            case 'top':
                                slider.style.height = '0px'; 
                                break;
                            case 'bottom':
                                slider.style.height = '0px'; 
                                break;
                        }
                    }
                    $scope.psOpen = false;
                }

                /* Open */
                function psOpen(slider,param){
                    if (slider.style.width !== 0 && slider.style.width !== 0){
                        switch (param.side){
                            case 'right':
                                slider.style.width = param.size; 
                                break;
                            case 'left':
                                slider.style.width = param.size; 
                                break;
                            case 'top':
                                slider.style.height = param.size; 
                                break;
                            case 'bottom':
                                slider.style.height = param.size; 
                                break;
                        }
                        setTimeout(function(){
                            content.style.display = 'block';
                        },(param.speed * 1000));

                    }
                }

                function isFunction(functionToCheck){
                    var getType = {};
                    return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
                }

                /*
                * Watchers
                * */

                if(attrs.psSize){
                    $scope.$watch(function(){
                        return attrs.psSize;
                    }, function(newVal,oldVal) {
                        param.size = newVal;
                        if($scope.psOpen) {
                            psOpen(slider,param);
                        }
                    });
                }

                $scope.$watch("psOpen", function (value){
                    if (!!value) {
                        // Open
                        psOpen(slider,param);
                    } else {
                        // Close
                        psClose(slider,param);
                    }
                });

                // close panel on location change
                if($scope.psAutoClose){
                    $scope.$on("$locationChangeStart", function(){
                        psClose(slider, param);
                        if(isFunction($scope.psAutoClose)) {
                            $scope.psAutoClose();
                        }
                    });
                    $scope.$on("$stateChangeStart", function(){
                        psClose(slider, param);
                        if(isFunction($scope.psAutoClose)) {
                            $scope.psAutoClose();
                        }
                    });
                }



                /*
                * Events
                * */

                $scope.$on('$destroy', function() {
                    document.body.removeChild(slider);
                });

                var close_handler = (attrs.href) ? document.getElementById(attrs.href.substr(1) + '-close') : null;
                if (el[0].addEventListener) {
                    el[0].addEventListener('click',function(e){
                        e.preventDefault();
                        psOpen(slider,param);                    
                    });

                    if (close_handler){
                        close_handler.addEventListener('click', function(e){
                            e.preventDefault();
                            psClose(slider,param);
                        });
                    }
                } else {
                    // IE8 Fallback code
                    el[0].attachEvent('onclick',function(e){
                        e.returnValue = false;
                        psOpen(slider,param);                    
                    });

                    if (close_handler){
                        close_handler.attachEvent('onclick', function(e){
                            e.returnValue = false;
                            psClose(slider,param);
                        });
                    }
                }

            }
        };
    }
])




.factory('apxSidePanel', function ($q, $http, $templateCache, $document, $injector, $controller, $rootScope, $compile, $transition, $timeout) {
	var defaults = {
		backdrop: true,
		keyboard: true
	};
	var deferred;
	var OPENED_MODAL_CLASS = 'modal-open';
	var backdropDomEl, backdropScope;
	var panelDomEl, panelInstance, panelSettings;

	function getTemplatePromise(options) {
		if (options.template) {
			return $q.when(options.template);
		}
		return $http.get(options.templateUrl, {cache: $templateCache}).then(function(result) {
			return result.data;
		});
	}

	function getResolvePromises(resolves) {
		var promises = [];
		if (!resolves) {
			return promises;
		}
		angular.forEach(resolves, function(value, key) {
			if (angular.isFunction(value) || angular.isArray(value)) {
				promises.push($q.when($injector.invoke(value)));
			}
		});
		return promises;
	}

	function open() {
		var body = $document.find('body').eq(0);
	
		backdropScope = $rootScope.$new(true);
		backdropScope.index = -1;
		backdropDomEl = $compile('<div panel-backdrop></div>')(backdropScope);
		body.append(backdropDomEl);
	
		var angularDomEl = angular.element('<div panel-window></div>');
		angularDomEl.attr({
			'window-class': panelSettings.windowClass,
			'index': 0,
			'animate': 'animate'
		}).html(panelSettings.content);
	
		panelDomEl = $compile(angularDomEl)(panelSettings.scope);
		body.append(panelDomEl);
		body.addClass(OPENED_MODAL_CLASS);
	}
	
	function removePanelWindow() {
		var body = $document.find('body').eq(0);
		// remove window DOM element
		removeAfterAnimate(panelDomEl, panelSettings.scope, 300, function() {
			panelSettings.scope.$destroy();
			body.toggleClass(OPENED_MODAL_CLASS, false);
			panelDomEl = undefined;
			panelInstance = undefined;
			panelSettings = undefined;			
			checkRemoveBackdrop();
		});
	}
	
	function removeAfterAnimate(domEl, scope, emulateTime, done) {
		// Closing animation
		scope.animate = false;

		function afterAnimating() {
			if (afterAnimating.done) {
				return;
			}
			afterAnimating.done = true;
			domEl.remove();
			if (done) {
				done();
			}
		}
		
		var transitionEndEventName = $transition.transitionEndEventName;
		if (transitionEndEventName) {
			// transition out
			var timeout = $timeout(afterAnimating, emulateTime);
			domEl.bind(transitionEndEventName, function () {
				$timeout.cancel(timeout);
				afterAnimating();
				scope.$apply();
			});
		} else {
			// Ensure this call is async
			$timeout(afterAnimating);
		}
	}
	
	function checkRemoveBackdrop() {
		if (backdropDomEl) {
			var backdropScopeRef = backdropScope;
			removeAfterAnimate(backdropDomEl, backdropScope, 150, function () {
				backdropScopeRef.$destroy();
				backdropScopeRef = undefined;
			});
			backdropDomEl = undefined;
			backdropScope = undefined;
		}
	}
	
	var factory = {};
	
	factory.open = function(settings) {
		if (panelInstance) {
			throw new Error('Panel is already opened.');
		}
		deferred = $q.defer();
		// prepare an instance of a panel to be injected into controllers and returned to a caller
		panelInstance = {
			result: deferred.promise,
			close: function(result) {
				console.log('close panel', result);
				factory.close(result);
			},
			dismiss: function(reason) {
				console.log('dismiss panel', reason);
				factory.dismiss(reason);
			}
		};
        
		settings = angular.extend({}, defaults, settings);
		settings.resolve = settings.resolve || {}; 
		if (!settings.template && !settings.templateUrl) {
			throw new Error('One of template or templateUrl options is required.');
		}
		var templateAndResolvePromise = $q.all([getTemplatePromise(settings)].concat(getResolvePromises(settings.resolve)));
		templateAndResolvePromise.then(function(tplAndVars) {
			var panelScope = (settings.scope || $rootScope).$new();
			panelScope.$close = panelInstance.close;
			panelScope.$dismiss = panelInstance.dismiss;
			if (settings.controller) {
				var resolveIter = 1;
				var ctrlLocals = {};
				ctrlLocals.$scope = panelScope;
				ctrlLocals.$panelInstance = panelInstance;
				angular.forEach(settings.resolve, function(value, key) {
					ctrlLocals[key] = tplAndVars[resolveIter++];
				});
				$controller(settings.controller, ctrlLocals);
			}
			panelSettings = {
				scope: panelScope,
				deferred: deferred,
				content: tplAndVars[0],
				backdrop: settings.backdrop,
				keyboard: settings.keyboard,
				windowClass: settings.windowClass,
				windowTemplateUrl: settings.windowTemplateUrl,
				size: settings.size
			};
			open();
		}, function(reason) {
			console.log('panel rejected', reason);
			deferred.reject(reason);
		});
	
		return panelInstance;
	};
	
	factory.close = function(result) {
		deferred.resolve(result);
		removePanelWindow();
	};

	factory.dismiss = function(reason) {
		deferred.reject(reason);
		removePanelWindow();
	};
	
	return factory;
})

.directive("panelWindow", function($timeout, apxSidePanel) {
	return {
		restrict : 'EA',
		scope : {
			index : '@',
			animate : '='
		},
		replace : true,
		transclude : true,
		template : '<div tabindex="-1" class="left-panel" ng-class="{in: animate}" ng-style="{\'z-index\': 1050 + index*10, display: \'block\'}"><div class="left-panel-inner" ng-click="close($event)" ng-transclude></div></div>',
		link : function(scope, element, attrs) {
			element.addClass(attrs.windowClass || '');
			scope.size = attrs.size;

			$timeout(function() {
				// trigger CSS transitions
				scope.animate = true;
				// focus a freshly-opened modal
				element[0].focus();
			});

			scope.close = function(evt) {
				if (apxSidePanel.backdrop && apxSidePanel.backdrop !== 'static' && (evt.target === evt.currentTarget)) {
					evt.preventDefault();
					evt.stopPropagation();
					apxSidePanel.dismiss('backdrop click');
				}
			};
		}
	};
})

.directive("panelBackdrop", function($timeout, apxSidePanel) {
	return {
		restrict : 'EA',
		replace : true,
		template : '<div class="modal-backdrop fade" ng-click="close($event)" ng-class="{in: animate}" ng-style="{\'z-index\': 1040 + (index && 1 || 0) + index*10}"></div>',
		link : function(scope) {
			scope.animate = false;
			// trigger CSS transitions
			$timeout(function() {
				scope.animate = true;
			});

			scope.close = function(evt) {
				if (apxSidePanel.backdrop && apxSidePanel.backdrop !== 'static' && (evt.target === evt.currentTarget)) {
					evt.preventDefault();
					evt.stopPropagation();
					apxSidePanel.dismiss('backdrop click');
				}
			};
		}
	};
})

;