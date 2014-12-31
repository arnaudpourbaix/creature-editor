angular.module("apx-tools.panel", [])

.factory('apxSidePanel', function ($q, $http, $templateCache, $document, $injector, $controller, $rootScope, $compile, $transition, $timeout) { "use strict";
	var defaults = {
		side: 'left',
		speed: 0.5,
		size: '300px',
		zindex: 1050,
		backdrop: true,
		modal: true,
		keyboard: true
	};
	var deferred;
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
	
		if (panelSettings.modal) {
			backdropScope = $rootScope.$new(true);
			backdropDomEl = $compile('<div apx-side-panel-backdrop></div>')(backdropScope);
			body.append(backdropDomEl);
		}
	
		var angularDomEl = angular.element('<div apx-side-panel-window></div>');
		angularDomEl.attr({
			'window-class': panelSettings.windowClass,
			'side': panelSettings.side,
			'size': panelSettings.size,
			'speed': panelSettings.speed,
			'zindex': panelSettings.zindex,
			'customTop': panelSettings.customTop,
			'customBottom': panelSettings.customBottom,
			'customLeft': panelSettings.customLeft,
			'customRight': panelSettings.customRight,
			'customHeight': panelSettings.customHeight,
			'customWidth': panelSettings.customWidth,
			'animate': 'animate'
		}).html(panelSettings.content);
	
		panelDomEl = $compile(angularDomEl)(panelSettings.scope);
		body.append(panelDomEl);
	}
	
	function removePanelWindow() {
		panelSettings.scope.$closePanelAnimation().then(function() {
			panelDomEl.remove();
			panelDomEl = undefined;
			panelSettings.scope.$destroy();
			panelInstance = undefined;
			panelSettings = undefined;			
			removeBackdrop();
//			backdropDomEl.remove();
//			backdropDomEl = undefined;
//			backdropScope.$destroy();
//			backdropScope = undefined;
		});
//		removeAfterAnimate(panelDomEl, panelSettings.scope, 300, function() {
//			panelSettings.scope.$destroy();
//			panelDomEl = undefined;
//			panelInstance = undefined;
//			panelSettings = undefined;			
//			removeBackdrop();
//		});
	}

	function removeBackdrop() {
		if (!backdropDomEl) {
			return;
		}
		var backdropScopeRef = backdropScope;
		removeAfterAnimate(backdropDomEl, backdropScope, 150, function() {
			backdropScopeRef.$destroy();
			backdropScopeRef = undefined;
			backdropDomEl = undefined;
			backdropScope = undefined;
		});
	}
	
	function removeAfterAnimate(domEl, scope, emulateTime, doneFn) {
		// Closing animation
		scope.animate = false;
	
		function afterAnimating() {
			if (afterAnimating.done) {
				return;
			}
			afterAnimating.done = true;
			domEl.remove();
			if (doneFn) {
				doneFn();
			}
		}
		
		var transitionEndEventName = $transition.transitionEndEventName;
		if (transitionEndEventName) {
			// transition out
			var timeout = $timeout(afterAnimating, emulateTime);
			domEl.bind(transitionEndEventName, function() {
				$timeout.cancel(timeout);
				afterAnimating();
				scope.$apply();
			});
		} else {
			// Ensure this call is async
			$timeout(afterAnimating);
		}
	}
	
	var factory = {};
	
	factory.open = function(settings) {
		if (panelInstance) {
			factory.close();
			//throw new Error('Panel is already opened.');
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
				modal: settings.modal,
				keyboard: settings.keyboard,
				windowClass: settings.windowClass,
				windowTemplateUrl: settings.windowTemplateUrl,
				side: settings.side,
				size: settings.size,
				speed: settings.speed,
				zindex: settings.zindex,
				customTop: settings.customTop,
				customBottom: settings.customBottom,
				customLeft: settings.customLeft,
				customRight: settings.customRight,
				customHeight: settings.customHeight,
				customWidth: settings.customWidth
			};
			open();
		}, function(reason) {
			console.log('panel rejected', reason);
			deferred.reject(reason);
		});
	
		return panelInstance;
	};
	
	factory.backdrop = function() {
		return panelSettings && panelSettings.backdrop; 
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

.directive("apxSidePanelWindow", function($timeout, apxSidePanel) { "use strict";
	return {
		restrict : 'EA',
		replace : true,
		transclude : true,
		template : '<div tabindex="-1" role="dialog" class="apx-side-panel"><div ng-transclude></div></div>',
		link : function(scope, element, attrs) {
			element.addClass(attrs.windowClass || '');
			element.css('transitionDuration', attrs.speed + 's');
			element.css('webkitTransitionDuration', attrs.speed + 's');
			element.css('zIndex', attrs.zindex);
			element.css('position', 'fixed');
			element.css('width', 0);
			element.css('height', 0);
			element.css('transitionProperty', 'width, height');
	
			var content = element.children();
			content.css('display', 'none');
			var updateAttr;
			
			switch (attrs.side){
			case 'right':
				element.css('height', attrs.customHeight || '100%');
				element.css('top', attrs.customTop ||  '0');
				element.css('bottom', attrs.customBottom ||  '0');
				element.css('right', attrs.customRight ||  '0');
				updateAttr = 'width';
				break;
			case 'left':
				element.css('height', attrs.customHeight || '100%');
				element.css('top', attrs.customTop || '0');
				element.css('bottom', attrs.customBottom || '0');
				element.css('left', attrs.customLeft || '0');
				updateAttr = 'width';
				break;
			case 'top':
				element.css('width', attrs.customWidth || '100%');
				element.css('left', attrs.customLeft || '0');
				element.css('top', attrs.customTop || '0');
				element.css('right', attrs.customRight || '0');
				updateAttr = 'height';
				break;
			case 'bottom':
				element.css('width', attrs.customWidth || '100%');
				element.css('bottom', attrs.customBottom || '0');
				element.css('left', attrs.customLeft || '0');
				element.css('right', attrs.customRight || '0');
				updateAttr = 'height';
				break;
			}
			
			$timeout(function() {
				element.css(updateAttr, attrs.size);
				$timeout(function() {
					content.css('display', 'block');
					// focus a freshly-opened modal
					element[0].focus();
				}, attrs.speed * 1000);
				
			});
	
			scope.$closePanelAnimation = function(evt) {
				content.css('display', 'none');
				element.css(updateAttr, 0);
				return $timeout(angular.noop, attrs.speed * 1000);
			};
		}
	};
})

.directive("apxSidePanelBackdrop", function($timeout, apxSidePanel) { "use strict";
	return {
		restrict : 'EA',
		replace : true,
		template : '<div class="apx-panel-backdrop apx-fade" ng-click="close($event)" ng-class="{\'apx-in\': animate}" ng-style="{\'z-index\': 1040 }"></div>',
		link : function(scope) {
			scope.animate = false;
			// trigger CSS transitions
			$timeout(function() {
				scope.animate = true;
			});
	
			scope.close = function(evt) {
				if (apxSidePanel.backdrop() && (evt.target === evt.currentTarget)) {
					evt.preventDefault();
					evt.stopPropagation();
					apxSidePanel.dismiss('backdrop click');
				}
			};
		}
	};
})

;