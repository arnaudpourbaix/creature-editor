/* global jasmine */

describe('mod section', function() {
	"use strict";

	var mods = [ {
		id : 1,
		name : 'mod1'
	}, {
		id : 2,
		name : 'mod2'
	}, {
		id : 3,
		name : 'mod3'
	} ];

	describe('uniqueName directive', function() {
		var mod, $scope, form;

		function setTestValue(value) {
			$scope.model.testValue = value;
			$scope.$digest();
		}

		beforeEach(module('creatureEditor.mod'));

		beforeEach(function(Mod) {
			mod = Mod.get({
				id : 2
			});
			// Mockup Mod resource
			angular.module('mock-Mod', []).factory('Mod', function() {
				mods = jasmine.createSpyObj('Mod', [ 'query' ]);
				return mods;
			});
		});
		console.log(mod);
		it('should have a dummy test', angular.mock.inject(function() {
			expect(mod).toBeDefined();
		}));

		// beforeEach(inject(function($compile, $rootScope) {
		// $scope = $rootScope;
		// var element = angular.element('<form name="form"><input name="testInput" ng-model="model.testValue" unique-email></form>');
		// $scope.model = {
		// testValue : null
		// };
		// $compile(element)($scope);
		// $scope.$digest();
		// form = $scope.form;
		// }));
		// it('should be valid initially', function() {
		// expect(form.testInput.$valid).toBe(true);
		// });
		// it('should not call Users.query when the model changes', function() {
		// setTestValue('different');
		// expect(Users.query).not.toHaveBeenCalled();
		// });
		// it('should call Users.query when the view changes', function() {
		// form.testInput.$setViewValue('different');
		// expect(Users.query).toHaveBeenCalled();
		// });
		// it('should set model to invalid if the Users callback contains users', function() {
		// Users.query.andCallFake(function(query, callback) {
		// callback([ 'someUser' ]);
		// });
		// form.testInput.$setViewValue('different');
		// expect(form.testInput.$valid).toBe(false);
		// });
		// it('should set model to valid if the Users callback contains no users', function() {
		// Users.query.andCallFake(function(query, callback) {
		// callback([]);
		// });
		// form.testInput.$setViewValue('different');
		// expect(form.testInput.$valid).toBe(true);
		// });
	});

	describe('Mod list controller', function() {
		var location, scope, state;

		beforeEach(module('creatureEditor.mod'));

		beforeEach(angular.mock.inject(function($rootScope, $controller, Mod) {
			scope = $rootScope.$new();
			$controller('ModListController', {
				$scope : scope,
				mods : mods
			});
		}));

		it('should have 3 mods', function() {
			expect(scope.mods.length).toBe(3);
		});
	});

});
