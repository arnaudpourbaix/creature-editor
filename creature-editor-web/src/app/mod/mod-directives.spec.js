/* global jasmine, inject, xdescribe, xit */

describe('Mod directives', function() {
	"use strict";

	describe('uniqueName', function() {
		var Mod, $scope, form;

		function setNameValue(value) {
			$scope.mod.name = value;
			$scope.$digest();
		}

		beforeEach(function() {
			angular.module('mock-Mod', []).factory('Mod', function() {
				/* jshint -W020 */
				Mod = jasmine.createSpyObj('Mod', [ 'query', 'getByName' ]);
				return Mod;
			});
		});

		beforeEach(module('creatureEditor.mod.directives', 'mock-Mod'));

		beforeEach(inject(function($compile, $rootScope) {
			$scope = $rootScope;
			var element = angular.element('<form name="form"><input name="modName" ng-model="mod.name" unique-name="{ id: 1 }"></form>');
			$scope.mod = {
				name : null
			};
			$compile(element)($scope);
			$scope.$digest();
			form = $scope.form;
		}));

		it('should be valid initially and should not call Mods.query', function() {
			expect(form.modName.$valid).toBe(true);
			expect(Mod.getByName).not.toHaveBeenCalled();
		});
		it('should not call Mods.getByName when the model changes', function() {
			setNameValue('different');
			expect(Mod.getByName).not.toHaveBeenCalled();
		});
		it('should call Mod.getByName when the view changes', function() {
			form.modName.$setViewValue('different');
			expect(Mod.getByName).toHaveBeenCalled();
		});

		it('should set model to valid with a name that doesnt exist', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback({});
			});
			form.modName.$setViewValue('different');
			expect(form.modName.$valid).toBe(true);
		});

		it('should set model to valid with a name that exists on the same id', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback({
					id : 1,
					name : 'different'
				});
			});
			form.modName.$setViewValue('different');
			expect(form.modName.$valid).toBe(true);
		});

		it('should set model to invalid with a name that exists on a different id', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback({
					id : 2,
					name : 'different'
				});
			});
			form.modName.$setViewValue('different');
			expect(form.modName.$valid).toBe(false);
		});

		xit('should set model to invalid if the Mod callback contains users', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback([ 'someUser' ]);
			});
			form.modName.$setViewValue('different');
			expect(form.modName.$valid).toBe(false);
		});
		xit('should set model to valid if the Mod callback contains no users', function() {
			Mod.getByName.andCallFake(function(query, callback) {
				callback([]);
			});
			form.modName.$setViewValue('different');
			expect(form.modName.$valid).toBe(true);
		});
	});

});
