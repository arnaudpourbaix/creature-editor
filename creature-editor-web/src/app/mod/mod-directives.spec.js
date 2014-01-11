/* global jasmine, inject, xdescribe */

xdescribe('Mod directives', function() {
	"use strict";

	describe('uniqueName', function() {
		var Mod, $scope, form;

		function setTestValue(value) {
			$scope.model.testValue = value;
			$scope.$digest();
		}

		beforeEach(function() {
			jasmine.Clock.useMock();
			angular.module('mock-Mod', []).factory('Mod', function() {
				/* jshint -W020 */
				Mod = jasmine.createSpyObj('Mod', [ 'query', 'getByName' ]);
				return Mod;
			});
		});

		beforeEach(module('creatureEditor.mod.directives', 'mock-Mod'));

		beforeEach(inject(function($compile, $rootScope) {
			$scope = $rootScope;
			var element = angular.element('<form name="form"><input name="testInput" ng-model="model.testValue" unique-name="{ id: 1 }"></form>');
			$scope.model = {
				testValue : ''
			};
			$compile(element)($scope);
			$scope.$digest();
			form = $scope.form;
		}));

		it('should be valid initially', function() {
			expect(form.testInput.$valid).toBe(true);
		});

		it('should call Mod.getByName when the model changes', function() {
			setTestValue('test');
			// jasmine.Clock.tick(300);
			expect(Mod.getByName).toHaveBeenCalled();
		});

		// it('should call Mod.getByName when the view changes', function() {
		// form.testInput.$setViewValue('test');
		// expect(Mod.getByName).toHaveBeenCalled();
		// });

		it('should set model to valid if entering a name that doesnt exist', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback({});
			});
			// form.testInput.$setViewValue('test');
			setTestValue('test');
			// jasmine.Clock.tick(300);
			expect(form.testInput.$valid).toBe(true);
		});

		it('should set model to valid if entering a name that exists on the same id', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback({
					id : 1,
					name : 'test'
				});
			});
			// form.testInput.$setViewValue('test');
			setTestValue('test');
			// jasmine.Clock.tick(300);
			expect(form.testInput.$valid).toBe(true);
		});

		it('should set model to invalid if entering a name that exists on a different id', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback({
					id : 2,
					name : 'test'
				});
			});
			// form.testInput.$setViewValue('test');
			setTestValue('test');
			// jasmine.Clock.tick(300);
			expect(form.testInput.$valid).toBe(false);
		});

		//
		// it('should set model to valid if the Mod callback contains no users', function() {
		// Mod.query.andCallFake(function(query, callback) {
		// callback([]);
		// });
		// form.testInput.$setViewValue('different');
		// expect(form.testInput.$valid).toBe(true);
		// });
	});

});
