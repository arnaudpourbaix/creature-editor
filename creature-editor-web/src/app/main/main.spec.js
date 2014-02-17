/* global jasmine, inject, xdescribe, xit */

describe('MainController', function() {
	'use strict';
	describe('isCurrentUrl', function() {
		var MainController, location, scope;

		beforeEach(module('creatureEditor.main'));

		beforeEach(inject(function($controller, $location, $rootScope) {
			location = $location;
			scope = $rootScope.$new();
			MainController = $controller('MainController', {
				$location : location,
				$scope : scope
			});
		}));

		it('should pass a dummy test', inject(function() {
			expect(MainController).toBeTruthy();
		}));
	});
});