/* global jasmine, inject, xdescribe, xit */

describe('AppController', function() {
	'use strict';
	describe('isCurrentUrl', function() {
		var AppController, location, scope;

		beforeEach(module('creatureEditor'));

		beforeEach(inject(function($controller, $location, $rootScope) {
			location = $location;
			scope = $rootScope.$new();
			AppController = $controller('AppController', {
				$location : location,
				$scope : scope
			});
		}));

		it('should pass a dummy test', inject(function() {
			expect(AppController).toBeTruthy();
		}));
	});
});