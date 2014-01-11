/* global jasmine, inject, xdescribe, xit */

describe('Mod controllers', function() {
	"use strict";

	describe('list', function() {

		beforeEach(function() {
			angular.module('I18N-mock', []).value('I18N.MESSAGES', {});
		});
		beforeEach(module('creatureEditor.mod.controllers', 'I18N-mock'));

		it('should set up the scope correctly', inject(function($controller) {
			var locals = {
				$scope : {},
				crudListMethods : jasmine.createSpy('crudListMethods'),
				i18nNotifications : jasmine.createSpyObj('i18nNotifications', [ 'pushForCurrentRoute' ]),
				mods : [ {
					id : 1,
					name : 'mod1'
				}, {
					id : 2,
					name : 'mod2'
				}, {
					id : 3,
					name : 'mod3'
				} ]
			};
			$controller('ModListController', locals);
			expect(locals.$scope.mods).toBe(locals.mods);
			expect(locals.crudListMethods).toHaveBeenCalled();
			expect(locals.$scope.mods.length).toBe(3);
		}));

	});

	describe('edit', function() {

		beforeEach(function() {
			angular.module('I18N-mock', []).value('I18N.MESSAGES', {});
		});
		beforeEach(module('creatureEditor.mod.controllers', 'I18N-mock'));

		describe('ModEditController', function() {
			function createLocals() {
				return {
					$scope : {},
					$location : jasmine.createSpyObj('$location', [ 'path' ]),
					$modalInstance : jasmine.createSpyObj('$modalInstance', [ 'close' ]),
					i18nNotifications : jasmine.createSpyObj('i18nNotifications', [ 'pushForCurrentRoute' ]),
					mod : {
						$id : jasmine.createSpy('$id'),
						$name : 'Test'
					}
				};
			}
			function runController(locals) {
				inject(function($controller) {
					$controller('ModEditController', locals);
				});
			}

			xit('should set up the scope correctly', function() {
				var locals = createLocals();
				runController(locals);
				expect(locals.$scope.mod.id).toBe(1);
				expect(locals.$scope.mod.name).toBe('Test');
			});

			xit('should call $location & i18nNotifications services when $scope.onSave() is called', function() {
				var locals = createLocals();
				runController(locals);

				locals.$scope.onSave({
					$id : angular.noop
				});

				expect(locals.i18nNotifications.pushForCurrentRoute).toHaveBeenCalled();
				expect(locals.$location.path).toHaveBeenCalled();
			});

			xit('should set updateError in onError', function() {
				var locals = createLocals();
				runController(locals);

				locals.$scope.onError();
				expect(locals.i18nNotifications.pushForCurrentRoute).toHaveBeenCalled();
			});
		});

	});

});
