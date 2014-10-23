/* global jasmine, inject, xdescribe */

xdescribe('category section', function() {
	"use strict";

	describe('Category list controller', function() {
		var scope, state, categories;

		beforeEach(module('creatureEditor.category'));

		// FIXME you need to add jqwidget to karma config to be able to use it below
		// beforeEach(inject(function($rootScope, $controller, Category) {
		// scope = $rootScope.$new();
		// $controller('CategoryListController', {
		// $scope : scope,
		// categories : categories
		// });
		// }));

		it('should have a dummy test', inject(function() {
			expect(true).toBeTruthy();
		}));
	});

});
