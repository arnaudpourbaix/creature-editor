var app = angular.module('creatureEditor', [
  'templates-app',
  'templates-common',
  'creatureEditor.category',
  'creatureEditor.spell',
  'creatureEditor.mod',
  'ui.state',
  'ui.route'
]);

app.config(function myAppConfig($stateProvider, $urlRouterProvider) { 'use strict';
  $urlRouterProvider.otherwise('/mod');
});

/*app.factory('UserFactory', function($resource){
    return $resource('Users/users.json');
});*/

app.controller('AppCtrl', function($scope, $location) { 'use strict';

});

app.run(function run() { 'use strict';

});
