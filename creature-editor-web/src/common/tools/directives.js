(function(angular){ "use strict";

    angular.module('an-tools.directives', ['an-tools.services'])

    /**
     * @ngdoc directive
     * @name an-tools.directive:domPrint
     * @param {string} rel The JQuery selector to print.
     * @restrict AEC
     * @priority 0
     * @description
     * Print dom elements. Rel = jQuery selector to print
     */
        .directive('domPrint',function domPrint($filter, $timeout, $rootScope, appSettings){
            return function ($scope, element, $attrs) {
                var eventName = 'printTable'; //this is the eventName that should be passed to the ngGridPrintOn directive.

                element.click(function(){
                    $(".printloading").show().find(".text").html($filter('translate')('PRINT.STEP1'));
                    $timeout(function() {
                        $rootScope.$broadcast(eventName+"-start");
                    });
                });

                var offPrintTableReady = $rootScope.$on(eventName+'-ready',function(){
                    $(".printloading").show().find(".text").html($filter('translate')('PRINT.STEP2'));
                    $(".print .ngViewport").height($(".print .ngCanvas").height());
                    $(".print .ngGrid").show();
                    $($attrs.rel).print({
                        addGlobalStyles : false,
                        stylesheet : appSettings.staticResourceUrl + "assets/css/print.css",
                        rejectWindow : true,
                        noPrintSelector : ".no-print",
                        iframe : true,
                        append : null,
                        prepend : ".an-breadcrumb, .period-picker, .userpanel_fix .fix span"
                    });
                    $(".printloading").show().find(".text").html($filter('translate')('PRINT.STEP3'));

                    $timeout(function() {
                        //This is executed just after the user close the print window
                        $("#iframe + #iframe").remove();
                        $(".printloading").hide().find(".text").html("");
                        $rootScope.$broadcast(eventName+'-stop');
                    },5000);
                });

                $scope.$on('$destroy',function(){
                    element.unbind('click');
                    offPrintTableReady();
                });
            };
        })

    /**
     * @ngdoc directive
     * @name an-tools.directive:anPanel
     * @restrict A
     * @priority 0
     * @description
     * Opens/closes a panel. <br>
     * Add an overlay on the div to close it when clicked outsite. <br>
     * The div have to be closed when scope.showPanel is set to false. <br>
     * # Example
     * <pre>
     * <div an-panel="myCondition" an-panel-on-close="myCondition=false">
     *     <!-- Panel content -->
     * </div>
     * <!-- This panel will be opened whenever myCondition is set to true -->
     * <!-- It will be closed automatically when clicked outside or when myCondition is set to false. -->
     * </pre>
     */
        .directive('anPanel', function anPanel($animate, anTranscluder, $parse) {
            return {
                restrict : 'A',
                transclude: true,
                template: '<div></div><div></div>',
                link : function(scope, element, attributes,controller,transclude) {
                    var transcludeContainer = $(element.children()[0]);
                    var maskContainer       = $(element.children()[1]);
                    var condition           = attributes.anPanel;
                    var closeAction         = attributes.anPanelOnClose;
                    var transcluder         = anTranscluder(transcludeContainer, transclude);

                    //This should be defined through CSS classes. Hard to override this way :(
                    element.css('z-index','1001');
                    transcludeContainer.css('z-index','1001');
                    transcludeContainer.css('position','relative');

                    maskContainer.click(function(){
                        scope.$eval(closeAction);
                        if($parse(condition)(scope)){
                            console.warn('The close function should make the anPanel condition falsy. The panel will not be closed');
                        }
                        scope.$digest();
                    });

                    scope.$watch(condition, function(val){
                        if (val){
                            transcluder.addTransclude();
                            $animate.addClass(maskContainer,'main-overlay');
                        } else {
                            transcluder.removeTransclude();
                            $animate.removeClass(maskContainer,'main-overlay');
                        }
                    });

                    scope.$on('$destroy',function(){
                        transcluder.removeTransclude();
                        maskContainer.unbind('click');
                    });
                }
            };
        })

    /**
     * @ngdoc directive
     * @name an-tools.directive:anIfShow
     * @description
     * That is a ng-if that becomes a ng-show once activated.
     * + Fast initial rendering
     * + Fast hide/show
     * - Keep all rendered DOM elements and watchers until $destroy
     * <br><br>
     * We use an-hide (visiblity: hidden) instead of ng-hide (display: none)
     * because the ng-hide reset the columns width on tables.
     */
        .directive('anIfShow', function anIfShow(anTranscluder){
            return{
                multiElement: true,
                transclude: true,
                priority: 600,
                terminal: true,
                restrict: 'A',
                link: function($scope,$element,$attrs,controller,$transclude){
                    var transcluder = anTranscluder($element,$transclude);

                    $scope.$watch($attrs.anIfShow, function(value){
                        if(value){
                            if(!transcluder.isTranscluded()){
                                transcluder.addTransclude();
                            }
                            transcluder.showTransclude('an-hide');
                        }
                        else{
                            transcluder.hideTransclude('an-hide');
                        }
                    });

                    $scope.$on('$destroy',function(){
                        transcluder.removeTransclude();
                    });
                }
            };
        })
    ;
})(angular);