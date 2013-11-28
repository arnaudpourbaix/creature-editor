angular.module( 'creatureEditor', [
  'templates-app',
  'templates-common',
  'creatureEditor.home',
  'creatureEditor.category',
  'ui.state',
  'ui.route'
])

.config( function myAppConfig ( $stateProvider, $urlRouterProvider ) { 'use strict';
  $urlRouterProvider.otherwise( '/home' );
})

.run( function run () { 'use strict';
	
})

.controller( 'AppCtrl', function AppCtrl ( $scope, $location ) { 'use strict';
})

;

