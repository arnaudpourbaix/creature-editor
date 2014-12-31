angular.module("apx-tools.panel", [])

.factory('apxSidePanel', function ($q, $http, $templateCache, $document, $injector, $controller, $rootScope, $compile, $timeout) { "use strict";
	var defaults = {
		side: 'left',
		speed: 0.5,
		size: '300px',
		zindex: 1050,
		backdrop: true,
		modal: true,
		keyboard: true,
		autofocus: false
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
		angularDomEl.html(panelSettings.content);
	
		panelDomEl = $compile(angularDomEl)(panelSettings.scope);
		body.append(panelDomEl);
	}
	
	function removePanel() {
		var promises = [ panelSettings.scope.$closeAnimation() ];
		if (backdropDomEl) {
			promises.push(backdropScope.$closeAnimation());
		}
		$q.all(promises).then(function() {
			panelDomEl.remove();
			panelDomEl = undefined;
			panelSettings.scope.$destroy();
			panelInstance = undefined;
			panelSettings = undefined;			
			removeBackdrop();
		});
	}

	function removeBackdrop() {
		if (!backdropDomEl) {
			return;
		}
		backdropScope.$destroy();
		backdropScope = undefined;
		backdropDomEl.remove();
		backdropDomEl = undefined;
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
				factory.close(result);
			},
			dismiss: function(reason) {
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
			panelSettings = angular.extend({}, settings, {
				scope: panelScope,
				deferred: deferred,
				content: tplAndVars[0]
			});
			open();
		}, function(reason) {
			deferred.reject(reason);
		});
	
		return panelInstance;
	};
	
	factory.settings = function() {
		return panelSettings || {}; 
	};
	
	factory.close = function(result) {
		deferred.resolve(result);
		removePanel();
	};
	
	factory.dismiss = function(reason) {
		deferred.reject(reason);
		removePanel();
	};
	
	return factory;
})

.directive("apxSidePanelWindow", function($timeout, $document, apxSidePanel) { "use strict";
	return {
		restrict : 'EA',
		replace : true,
		transclude : true,
		template : '<div tabindex="-1" role="dialog" class="apx-side-panel"><div ng-transclude></div></div>',
		link : function($scope, element, attrs) {
			var settings = apxSidePanel.settings();
			
			element.addClass(settings.windowClass || '');
			element.css('zIndex', settings.zindex);
			element.css('position', 'fixed');
			element.css('width', 0);
			element.css('height', 0);
			element.css('transitionDuration', settings.speed + 's');
			element.css('transitionProperty', 'width, height');
	
			var content = element.children();
			content.css('display', 'none');
			var updateAttr;
			
			switch (settings.side){
			case 'right':
				element.css('height', settings.customHeight || '100%');
				element.css('top', settings.customTop ||  '0');
				element.css('bottom', settings.customBottom ||  '0');
				element.css('right', settings.customRight ||  '0');
				updateAttr = 'width';
				break;
			case 'left':
				element.css('height', settings.customHeight || '100%');
				element.css('top', settings.customTop || '0');
				element.css('bottom', settings.customBottom || '0');
				element.css('left', settings.customLeft || '0');
				updateAttr = 'width';
				break;
			case 'top':
				element.css('width', settings.customWidth || '100%');
				element.css('left', settings.customLeft || '0');
				element.css('top', settings.customTop || '0');
				element.css('right', settings.customRight || '0');
				updateAttr = 'height';
				break;
			case 'bottom':
				element.css('width', settings.customWidth || '100%');
				element.css('bottom', settings.customBottom || '0');
				element.css('left', settings.customLeft || '0');
				element.css('right', settings.customRight || '0');
				updateAttr = 'height';
				break;
			}
			
			element.bind('keydown', function(evt) {
				if (evt.which === 27) {
					if (apxSidePanel.settings().keyboard) {
						evt.preventDefault();
						$scope.$apply(function () {
							apxSidePanel.dismiss('escape key press');
						});
					}
		        }
			});
			
			$timeout(function() {
				element.css(updateAttr, settings.size);
				$timeout(function() {
					content.css('display', 'block');
					if (settings.autofocus) {
						element[0].focus();
					}
				}, settings.speed * 1000);
				
			});
	
			$scope.$closeAnimation = function() {
				content.css('display', 'none');
				element.css(updateAttr, 0);
				return $timeout(angular.noop, settings.speed * 1000);
			};
		}
	};
})

.directive("apxSidePanelBackdrop", function($timeout, apxSidePanel) { "use strict";
	return {
		restrict : 'EA',
		replace : true,
		template : '<div class="apx-panel-backdrop" ng-click="close($event)"></div>',
		link : function($scope, element, attrs) {
			var settings = apxSidePanel.settings();
			
			element.css('zIndex', 1050);
			element.css('opacity', 0);
			$timeout(function() {
				element.css('opacity', 0.5);
				element.css('transitionDuration', settings.speed + 's');
				element.css('transitionProperty', 'opacity');
			});
	
			$scope.close = function(evt) {
				if (apxSidePanel.settings().backdrop && (evt.target === evt.currentTarget)) {
					evt.preventDefault();
					evt.stopPropagation();
					apxSidePanel.dismiss('backdrop click');
				}
			};
			
			$scope.$closeAnimation = function() {
				element.css('opacity', 0);
				return $timeout(angular.noop, settings.speed * 1000);
			};
		}
	};
})

;
