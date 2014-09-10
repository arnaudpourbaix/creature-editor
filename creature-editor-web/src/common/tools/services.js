(function(angular) { "use strict";

    angular.module('an-tools.services', [])

    /**
     * @ngdoc service
     * @name an-tools.service:anTranscluder
     * @requires $animate
     * @description
     * Helper to manipulate transclude functions.
     * The code is inspired (copied) from ngIf and ngShow sources.
     * # Exemple
     * <pre>
     * function link($scope,$element,$attributes,$controller,$transclude){
 *     var transcluder = anTranscluder($element, $transclude);
 *     transcluder.addTransclude();
 *     transcluder.hideTransclude();
 *     transcluder.showTransclude();
 *     transcluder.removeTransclude();
 * }
     * </pre>
     */
        .service('anTranscluder', function anTranscluder($animate) {
            return function (container, transclude) {
                var service             = {},
                    state               = 'idle',
                    transcludeScope     = null,
                    transcludeElements  = null,
                    addTranscludeCancelFn = null;

                /**
                 * @ngdoc method
                 * @name addTransclude
                 * @methodOf an-tools.service:anTranscluder
                 * @description Add the transcluded elements on the container
                 */
                service.addTransclude = function () {
                    state = 'adding';
                    if (!transcludeScope && !addTranscludeCancelFn) {
                        transclude(function (clone, newScope) {
                            transcludeScope = newScope;
                            transcludeElements = clone;
                            addTranscludeCancelFn = $animate.enter(clone, container, null, function () {
                                if(state==='removing'){
                                    service.cancelAddTransclude();
                                }
                                addTranscludeCancelFn = null;
                            });
                        });
                    }
                    return addTranscludeCancelFn;
                };
                service.cancelAddTransclude = function(){
                    if (typeof addTranscludeCancelFn === 'function') {
                        addTranscludeCancelFn();
                        addTranscludeCancelFn = null;
                    }
                };
                /**
                 * @ngdoc method
                 * @name removeTransclude
                 * @methodOf an-tools.service:anTranscluder
                 * @description Remove the transcluded elements from the container
                 */
                service.removeTransclude = function () {
                    state = 'removing';
                    service.cancelAddTransclude();
                    if (transcludeScope) {
                        transcludeScope.$destroy();
                        transcludeScope = null;
                    }
                    if (transcludeElements) {
                        return $animate.leave(transcludeElements, function () {
                            if(state==='removing') {
                                transcludeElements = null;
                            }
                        });
                    }
                };
                /**
                 * @ngdoc method
                 * @name showTransclude
                 * @methodOf an-tools.service:anTranscluder
                 * @description Show the container (remove ng-hide class)
                 */
                service.showTransclude = function (hideClass) {
                    return $animate.removeClass(container, hideClass || 'ng-hide');
                };
                /**
                 * @ngdoc method
                 * @name hideTransclude
                 * @methodOf an-tools.service:anTranscluder
                 * @description Hide the container (add a ng-hide class)
                 */
                service.hideTransclude = function (hideClass) {
                    return $animate.addClass(container, hideClass || 'ng-hide');
                };
                /**
                 * @ngdoc method
                 * @name isTranscluded
                 * @returns {boolean} true if the transcluded elements have been added on the container.
                 * @methodOf an-tools.service:anTranscluder
                 * @description Test if the transcluded elements have been added.
                 */
                service.isTranscluded = function () {
                    return transcludeElements !== null;
                };

                return service;
            };
        })
    ;
})(angular);