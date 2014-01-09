/* global jasmine, inject */

describe('category section', function() {
	"use strict";

	describe('Category list controller', function() {
		var location, scope, state;

		beforeEach(module('creatureEditor.category'));

		beforeEach(inject(function($rootScope, $controller, Mod) {
			scope = $rootScope.$new();
			$controller('CategoryListController', {
				$scope : scope,
				mods : mods
			});
		}));

		it('should have a dummy test', inject(function() {
			expect(true).toBeTruthy();
		}));
	});

});
