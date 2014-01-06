describe('mod section', function() {
	"use strict";

	var modListController, location, scope, state;
	var stateProvider, locationProvider, templateParams, ctrlName;

	beforeEach(function() {
		angular.mock.module('ui.router');
		angular.mock.module('ngRoute');
		angular.mock.module('ngResource');
		angular.mock.module('creatureEditor');
		angular.mock.module('creatureEditor.mod');
	});

	beforeEach(angular.mock.inject(function($rootScope, $controller, Mod) {
		scope = $rootScope.$new();
		modListController = $controller('ModListController', {
			$scope : scope,
			mods : [ 'mod1', 'mod2', 'mod3' ]
		// mods : Mod.query()
		});
	}));

	it('should have a dummy test', angular.mock.inject(function() {
		expect(true).toBeTruthy();
	}));

	it('should have 3 mods', function() {
		expect(scope.mods.length).toBe(3);
	});

	// it('should have an empty events array', function(){
	// console.log(scope);
	// expect(scope.mods).toBe([]);
	// });

	// beforeEach(angular.mock.inject(function($state, $rootScope) {
	// $rootScope.$apply(function() {
	// $state.go("mods");
	// state = $state;
	// });
	// }));

	// beforeEach(angular.mock.inject(function($controller, $location, $rootScope) {
	// location = $location;
	// scope = $rootScope.$new();
	// ModListController = $controller('ModListController', {
	// $location : location,
	// $scope : scope
	// });
	// }));

	// beforeEach(angular.mock.inject(function($controller, $rootScope, $state, $stateParams) {
	// scope = $rootScope.$new();
	// ModListController = function() {
	// $state.transitionTo('mods', {});
	// $rootScope.$digest();
	// return $controller('ModListController', {
	// $scope : scope,
	// $state : $state
	// });
	// };
	// }));

	// it('should have title for about set', angular.mock.inject(function() {
	// var controller = createController();
	// expect(scope.mods).toBeTruthy();
	// }));

	// it('state should be mods', angular.mock.inject(function() {
	// expect(state.current.name).to.equal("mods");
	// expect(true).toBeTruthy();
	// expect(ModListController).toBeTruthy();
	// }));

});
