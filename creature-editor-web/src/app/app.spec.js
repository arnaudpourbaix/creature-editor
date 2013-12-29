describe('AppCtrl', function() {
	'use strict';
	describe('isCurrentUrl', function() {
		var AppCtrl, location, scope;

		beforeEach(angular.mock.module('creatureEditor'));

		beforeEach(angular.mock.inject(function($controller, $location, $rootScope) {
			location = $location;
			scope = $rootScope.$new();
			AppCtrl = $controller('AppCtrl', {
				$location : location,
				$scope : scope
			});
		}));

		it('should pass a dummy test', angular.mock.inject(function() {
			expect(AppCtrl).toBeTruthy();
		}));
	});
});