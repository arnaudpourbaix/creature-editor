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








.factory('$panelStack', function ($transition, $timeout, $document, $compile, $rootScope, $$stackedMap) {
	var OPENED_MODAL_CLASS = 'modal-open';
	var backdropDomEl, backdropScope;
	var openedPanels = $$stackedMap.createNew();
	var $panelStack = {};
	
	function backdropIndex() {
		var topBackdropIndex = -1;
		var opened = openedPanels.keys();
		for (var i = 0; i < opened.length; i++) {
			if (openedPanels.get(opened[i]).value.backdrop) {
				topBackdropIndex = i;
			}
		}
		return topBackdropIndex;
	}
	
	$rootScope.$watch(backdropIndex, function(newBackdropIndex){
		if (backdropScope) {
			backdropScope.index = newBackdropIndex;
		}
	});
	
	function removePanel(panelInstance) {
		var body = $document.find('body').eq(0);
		var panel = openedPanels.get(panelInstance).value;
		// clean up the stack
		openedPanels.remove(panelInstance);
		// remove window DOM element
		removeAfterAnimate(panel.modalDomEl, panel.modalScope, 300, function() {
			panel.modalScope.$destroy();
			body.toggleClass(OPENED_MODAL_CLASS, openedPanels.length() > 0);
			checkRemoveBackdrop();
		});
	}
	
	function checkRemoveBackdrop() {
		//remove backdrop if no longer needed
		if (backdropDomEl && backdropIndex() === -1) {
			var backdropScopeRef = backdropScope;
			removeAfterAnimate(backdropDomEl, backdropScope, 150, function () {
				backdropScopeRef.$destroy();
				backdropScopeRef = null;
			});
			backdropDomEl = undefined;
			backdropScope = undefined;
		}
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
			$timeout(afterAnimating, 0);
		}
	}
	
	$document.bind('keydown', function (evt) {
		var modal;
		if (evt.which === 27) {
			modal = openedPanels.top();
			if (modal && modal.value.keyboard) {
				evt.preventDefault();
				$rootScope.$apply(function () {
					$panelStack.dismiss(modal.key, 'escape key press');
				});
			}
		}
	});
	
	$panelStack.open = function (panelInstance, modal) {
		openedPanels.add(panelInstance, {
			deferred: modal.deferred,
			modalScope: modal.scope,
			backdrop: modal.backdrop,
			keyboard: modal.keyboard
		});
	
		var body = $document.find('body').eq(0);
		var currBackdropIndex = backdropIndex();
	
		if (currBackdropIndex >= 0 && !backdropDomEl) {
			backdropScope = $rootScope.$new(true);
			backdropScope.index = currBackdropIndex;
			backdropDomEl = $compile('<div panel-backdrop></div>')(backdropScope);
			body.append(backdropDomEl);
		}
	
		var angularDomEl = angular.element('<div panel-window></div>');
		angularDomEl.attr({
			'window-class': modal.windowClass,
			'index': openedPanels.length() - 1,
			'animate': 'animate'
		}).html(modal.content);
	
		var modalDomEl = $compile(angularDomEl)(modal.scope);
		openedPanels.top().value.modalDomEl = modalDomEl;
		body.append(modalDomEl);
		body.addClass(OPENED_MODAL_CLASS);
	};
	
	$panelStack.close = function (panelInstance, result) {
		var panel = openedPanels.get(panelInstance).value;
		if (panel) {
			panel.deferred.resolve(result);
			removePanel(panelInstance);
		}
	};
	
	$panelStack.dismiss = function (panelInstance, reason) {
		var panel = openedPanels.get(panelInstance).value;
		if (panel) {
			panel.deferred.reject(reason);
			removePanel(panelInstance);
		}
	};
	
	$panelStack.dismissAll = function (reason) {
		var topModal = this.getTop();
		while (topModal) {
			this.dismiss(topModal.key, reason);
			topModal = this.getTop();
		}
	};
	
	$panelStack.getTop = function () {
		return openedPanels.top();
	};
	
	return $panelStack;
})



.factory('apxSidePanel', function ($q, $http, $templateCache, $injector, $controller, $rootScope, $panelStack) {
	var defaults = {
		backdrop: true,
		keyboard: true
	};

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
	
	return function (settings) {
		var deferred = $q.defer();
		// prepare an instance of a panel to be injected into controllers and returned to a caller
		var panelInstance = {
				result: deferred.promise,
				close: function(result) {
					console.log('close panel', result);
					$panelStack.close(panelInstance, result);
				},
				dismiss: function(reason) {
					console.log('dismiss panel', reason);
					$panelStack.dismiss(panelInstance, reason);
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
			$panelStack.open(panelInstance, {
				scope: panelScope,
				deferred: deferred,
				content: tplAndVars[0],
				backdrop: settings.backdrop,
				keyboard: settings.keyboard,
				windowClass: settings.windowClass,
				windowTemplateUrl: settings.windowTemplateUrl,
				size: settings.size
			});
		}, function(reason) {
			console.log('panel rejected', reason);
			deferred.reject(reason);
		});
	
		return panelInstance;
	};
})

;