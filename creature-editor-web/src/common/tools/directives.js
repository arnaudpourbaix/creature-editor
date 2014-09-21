angular.module('apx-tools.directives', [])

.directive('focusMe', function($timeout) {
	return function(scope, element, attrs) {
		$timeout(function() {
			element.focus();
		}, 700);
	};
})


 /**
  * @ngdoc directive
  * @name apx-tools.directive:anPanel
  * @restrict A
  * @priority 0
  * @description
  * Opens/closes a panel. <br>
  * Add an overlay on the div to close it when clicked outsite. <br>
  * The div have to be closed when scope.showPanel is set to false. <br>
  * # Example
  * <pre>
  * <div apx-panel="myCondition" apx-panel-on-close="myCondition=false">
  *     <!-- Panel content -->
  * </div>
  * <!-- This panel will be opened whenever myCondition is set to true -->
  * <!-- It will be closed automatically when clicked outside or when myCondition is set to false. -->
  * </pre>
  */
  .directive('apxPanel', function($animate, anTranscluder, $parse) { "use strict";
      return {
          restrict : 'A',
          transclude: true,
          template: '<div></div><div></div>',
          link : function(scope, element, attributes,controller,transclude) {
              var transcludeContainer = $(element.children()[0]);
              var maskContainer       = $(element.children()[1]);
              var condition           = attributes.apxPanel;
              var closeAction         = attributes.apxPanelOnClose;
              var transcluder         = anTranscluder(transcludeContainer, transclude);

              element.css('z-index','1001');
              transcludeContainer.css('z-index','1001');
              transcludeContainer.css('position','relative');

              maskContainer.click(function(){
                  scope.$eval(closeAction);
                  if($parse(condition)(scope)){
                      console.warn('The close function should make the apxPanel condition falsy. The panel will not be closed');
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



;
