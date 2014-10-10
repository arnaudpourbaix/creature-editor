angular.module('apx-tools.services', [])

.factory('$panelStack', function ($transition, $timeout, $document, $compile, $rootScope, $$stackedMap) {

	var OPENED_MODAL_CLASS = 'modal-open';
	
	var backdropDomEl, backdropScope;
	var openedWindows = $$stackedMap.createNew();
	var $panelStack = {};
	
	function backdropIndex() {
	  var topBackdropIndex = -1;
	  var opened = openedWindows.keys();
	  for (var i = 0; i < opened.length; i++) {
	    if (openedWindows.get(opened[i]).value.backdrop) {
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
	
	function removeModalWindow(modalInstance) {
	
	  var body = $document.find('body').eq(0);
	  var modalWindow = openedWindows.get(modalInstance).value;
	
	  //clean up the stack
	  openedWindows.remove(modalInstance);
	
	  //remove window DOM element
	  removeAfterAnimate(modalWindow.modalDomEl, modalWindow.modalScope, 300, function() {
	    modalWindow.modalScope.$destroy();
	    body.toggleClass(OPENED_MODAL_CLASS, openedWindows.length() > 0);
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
	    modal = openedWindows.top();
	    if (modal && modal.value.keyboard) {
	      evt.preventDefault();
	      $rootScope.$apply(function () {
	        $panelStack.dismiss(modal.key, 'escape key press');
	      });
	    }
	  }
	});
	
	$panelStack.open = function (modalInstance, modal) {
	
	  openedWindows.add(modalInstance, {
	    deferred: modal.deferred,
	    modalScope: modal.scope,
	    backdrop: modal.backdrop,
	    keyboard: modal.keyboard
	  });
	
	  var body = $document.find('body').eq(0),
	      currBackdropIndex = backdropIndex();
	
	  if (currBackdropIndex >= 0 && !backdropDomEl) {
	    backdropScope = $rootScope.$new(true);
	    backdropScope.index = currBackdropIndex;
	    backdropDomEl = $compile('<div panel-backdrop></div>')(backdropScope);
	    body.append(backdropDomEl);
	  }
	
	  var angularDomEl = angular.element('<div panel-window></div>');
	  angularDomEl.attr({
	    'window-class': modal.windowClass,
	    'index': openedWindows.length() - 1,
	    'animate': 'animate'
	  }).html(modal.content);
	
	  var modalDomEl = $compile(angularDomEl)(modal.scope);
	  openedWindows.top().value.modalDomEl = modalDomEl;
	  body.append(modalDomEl);
	  body.addClass(OPENED_MODAL_CLASS);
	};
	
	$panelStack.close = function (modalInstance, result) {
	  var modalWindow = openedWindows.get(modalInstance).value;
	  if (modalWindow) {
	    modalWindow.deferred.resolve(result);
	    removeModalWindow(modalInstance);
	  }
	};
	
	$panelStack.dismiss = function (modalInstance, reason) {
	  var modalWindow = openedWindows.get(modalInstance).value;
	  if (modalWindow) {
	    modalWindow.deferred.reject(reason);
	    removeModalWindow(modalInstance);
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
	  return openedWindows.top();
	};
	
	return $panelStack;
})


.provider('apxPanel', function () {
	var panelProvider = {
     options: {
       backdrop: true,
       keyboard: true
     },
     $get: ['$injector', '$rootScope', '$q', '$http', '$templateCache', '$controller', '$panelStack',
       function ($injector, $rootScope, $q, $http, $templateCache, $controller, $panelStack) {

         var $panel = {};

         function getTemplatePromise(options) {
           return options.template ? $q.when(options.template) :
             $http.get(options.templateUrl, {cache: $templateCache}).then(function (result) {
               return result.data;
             });
         }

         function getResolvePromises(resolves) {
           var promisesArr = [];
           angular.forEach(resolves, function (value, key) {
             if (angular.isFunction(value) || angular.isArray(value)) {
               promisesArr.push($q.when($injector.invoke(value)));
             }
           });
           return promisesArr;
         }

         $panel.open = function (panelOptions) {

           var panelResultDeferred = $q.defer();
           var panelOpenedDeferred = $q.defer();

           //prepare an instance of a panel to be injected into controllers and returned to a caller
           var panelInstance = {
             result: panelResultDeferred.promise,
             opened: panelOpenedDeferred.promise,
             close: function (result) {
               $panelStack.close(panelInstance, result);
             },
             dismiss: function (reason) {
               $panelStack.dismiss(panelInstance, reason);
             }
           };

           //merge and clean up options
           panelOptions = angular.extend({}, panelProvider.options, panelOptions);
           panelOptions.resolve = panelOptions.resolve || {};

           //verify options
           if (!panelOptions.template && !panelOptions.templateUrl) {
             throw new Error('One of template or templateUrl options is required.');
           }

           var templateAndResolvePromise = $q.all([getTemplatePromise(panelOptions)].concat(getResolvePromises(panelOptions.resolve)));

           templateAndResolvePromise.then(function resolveSuccess(tplAndVars) {

             var panelScope = (panelOptions.scope || $rootScope).$new();
             panelScope.$close = panelInstance.close;
             panelScope.$dismiss = panelInstance.dismiss;

             var ctrlInstance, ctrlLocals = {};
             var resolveIter = 1;

             //controllers
             if (panelOptions.controller) {
               ctrlLocals.$scope = panelScope;
               ctrlLocals.$panelInstance = panelInstance;
               angular.forEach(panelOptions.resolve, function (value, key) {
                 ctrlLocals[key] = tplAndVars[resolveIter++];
               });

               ctrlInstance = $controller(panelOptions.controller, ctrlLocals);
             }

             $panelStack.open(panelInstance, {
               scope: panelScope,
               deferred: panelResultDeferred,
               content: tplAndVars[0],
               backdrop: panelOptions.backdrop,
               keyboard: panelOptions.keyboard,
               windowClass: panelOptions.windowClass,
               windowTemplateUrl: panelOptions.windowTemplateUrl,
               size: panelOptions.size
             });

           }, function resolveError(reason) {
             panelResultDeferred.reject(reason);
           });

           templateAndResolvePromise.then(function () {
             panelOpenedDeferred.resolve(true);
           }, function () {
             panelOpenedDeferred.reject(false);
           });

           return panelInstance;
         };

         return $panel;
       }]
	};
	return panelProvider;
})

;
