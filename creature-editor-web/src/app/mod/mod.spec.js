/* global jasmine, inject */

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

	describe('Mod list controller', function() {
		var location, scope, state;

		beforeEach(module('creatureEditor.mod'));
		// beforeEach(module('notification.i18n'));
		// beforeEach(module('i18n.services'));

		beforeEach(inject(function($rootScope, $controller, Mod/* , i18nNotifications */) {
			scope = $rootScope.$new();
			$controller('ModListController', {
				$scope : scope,
				mods : mods
			// ,i18nNotifications : i18nNotifications
			});
		}));

		// it('should have 3 mods', function() {
		// expect(scope.mods.length).toBe(3);
		// });
		it('should have a dummy test', inject(function() {
			expect(true).toBeTruthy();
		}));

	});

});
