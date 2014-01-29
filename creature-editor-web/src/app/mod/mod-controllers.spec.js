/* global jasmine, inject, xdescribe, xit */

xdescribe('Mod controllers', function() {
	"use strict";

	describe('list', function() {

		var Mod, $rootScope, createController;
		var i18nNotifications = jasmine.createSpyObj('i18nNotifications', [ 'pushForCurrentRoute' ]);
		var crudListMethods = jasmine.createSpy('crudListMethods');
		var notify = jasmine.createSpy('notify');

		beforeEach(function() {
			angular.module('I18N-mock', []).value('I18N.MESSAGES', {});
		});
		beforeEach(module('creatureEditor.mod.controllers', 'creatureEditor.mod.services', 'I18N-mock'));

		beforeEach(inject(function($injector) {
			Mod = $injector.get('Mod');
			Mod.query = jasmine.createSpy("query spy").andReturn([ {
				id : 1,
				name : 'mod1'
			}, {
				id : 2,
				name : 'mod2'
			}, {
				id : 3,
				name : 'mod3'
			} ]);
		}));

		beforeEach(inject(function($injector) {
			$rootScope = $injector.get('$rootScope');
			var $controller = $injector.get('$controller');
			createController = function() {
				return $controller('ModListController', {
					$scope : $rootScope,
					i18nNotifications : i18nNotifications,
					notify : notify,
					crudListMethods : crudListMethods,
					mods : Mod.query()
				});
			};
		}));

		it('should set up the scope', inject(function($controller) {
			createController();
			expect(crudListMethods).toHaveBeenCalled();
			expect($rootScope.mods.length).toBe(3);
		}));

	});

	describe('edit', function() {

		var Mod, $rootScope, i18nNotifications = jasmine.createSpyObj('i18nNotifications', [ 'pushForCurrentRoute' ]), $modalInstance = jasmine.createSpyObj(
				'$modalInstance', [ 'close' ]), form, createController;

		beforeEach(function() {
			angular.module('I18N-mock', []).value('I18N.MESSAGES', {});
		});
		beforeEach(module('creatureEditor.mod.controllers', 'creatureEditor.mod.services', 'I18N-mock'));

		beforeEach(inject(function($injector) {
			Mod = $injector.get('Mod');
			Mod.get = jasmine.createSpy("get spy").andReturn({
				id : 1,
				name : 'BGT'
			});
		}));

		beforeEach(inject(function($injector) {
			$rootScope = $injector.get('$rootScope');
			var $controller = $injector.get('$controller');
			createController = function() {
				return $controller('ModEditController', {
					$scope : $rootScope,
					$modalInstance : $modalInstance,
					i18nNotifications : i18nNotifications,
					mod : Mod.get()
				});
			};
		}));

		it('should set up the scope correctly', function() {
			createController();
			expect($rootScope.mod.id).toBe(1);
			expect($rootScope.mod.name).toBe('BGT');
		});

		it('should call i18nNotifications services and close modal when $scope.onSave() is called', function() {
			createController();
			$rootScope.onSave($rootScope.mod);
			expect(i18nNotifications.pushForCurrentRoute).toHaveBeenCalled();
			expect($modalInstance.close).toHaveBeenCalled();
		});

		it('should call i18nNotifications services and close modal when $scope.onRemove() is called', function() {
			createController();
			$rootScope.onRemove($rootScope.mod);
			expect(i18nNotifications.pushForCurrentRoute).toHaveBeenCalled();
			expect($modalInstance.close).toHaveBeenCalled();
		});

		xit('should set updateError in onError', function() {
			createController();
			$rootScope.onError($rootScope.mod);
			expect(i18nNotifications.pushForCurrentRoute).toHaveBeenCalled();
			expect($modalInstance.close).not.toHaveBeenCalled();
		});
	});

});
