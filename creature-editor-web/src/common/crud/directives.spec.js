/* global jasmine, inject, xdescribe, xit */

describe(
		'crud directive',
		function() {
			"use strict";

			var resourceMock;

			beforeEach(module('crud.directives'));
			beforeEach(function() {
				resourceMock = jasmine.createSpyObj('resource', [ '$save', '$remove', '$id' ]);
				inject(function($rootScope) {
					$rootScope.resource = resourceMock;
				});
			});

			describe('scope initialization', function() {
				it('requires a valid resource object', function() {
					inject(function($compile, $rootScope) {
						expect(function() {
							$compile('<form crud-edit="resource"></form>')($rootScope);
						}).not.toThrow();
						expect(function() {
							$compile('<form crud-edit></form>')($rootScope);
						}).toThrow();
						expect(function() {
							$compile('<form crud-edit="nonObject"></form>')($rootScope);
						}).toThrow();
						expect(function() {
							$rootScope.nonResourceObject = {};
							$compile('<form crud-edit="nonResourceObject"></form>')($rootScope);
						}).toThrow();
					});
				});

				it('should evaluate a complex expression for the resource', function() {
					inject(function($compile, $rootScope) {
						expect(function() {
							$rootScope.wrapper = {
								wrappedResourceFn : function() {
									return resourceMock;
								}
							};
							$compile('<form crud-edit="wrapper.wrappedResourceFn()"></form>')($rootScope);
						}).not.toThrow();
					});
				});

				it('requires a form element', function() {
					inject(function($compile, $rootScope) {
						expect(function() {
							$compile('<form crud-edit="resource"></form>')($rootScope);
						}).not.toThrow();
						expect(function() {
							$compile('<div crud-edit="resource"></div>')($rootScope);
						}).toThrow();
					});
				});

				it('should attach methods to the scope', function() {
					inject(function($compile, $rootScope) {
						var element = $compile('<form crud-edit="resource"></form>')($rootScope);
						expect(element.scope().save).toBeDefined();
						expect(element.scope().canSave).toBeDefined();
						expect(element.scope().revertChanges).toBeDefined();
						expect(element.scope().canRevert).toBeDefined();
						expect(element.scope().remove).toBeDefined();
						expect(element.scope().canRemove).toBeDefined();
					});
				});

				it('should not modify the parent scope', function() {
					inject(function($compile, $rootScope) {
						var element = $compile('<form crud-edit="resource"></form>')($rootScope);
						expect($rootScope.save).not.toBeDefined();
						expect($rootScope.canSave).not.toBeDefined();
						expect($rootScope.revertChanges).not.toBeDefined();
						expect($rootScope.canRevert).not.toBeDefined();
						expect($rootScope.remove).not.toBeDefined();
						expect($rootScope.canRemove).not.toBeDefined();
					});
				});
			});

			describe(
					'scope methods',
					function() {
						var element, scope, form, someField, $rootScope;
						beforeEach(inject(function($compile, _$rootScope_) {
							$rootScope = _$rootScope_;
							$rootScope.resource.someVal = 'original';
							element = $compile('<form name="form" crud-edit="resource"><input name="someField" ng-model="resource.someVal"></form>')($rootScope);
							scope = element.scope();
							someField = scope.form.someField;
							resourceMock.$save.andCallFake(function(onSave, onError) {
								onSave();
								onError();
							});
						}));

						describe(
								'save',
								function() {
									it('should call resource.$save', function() {
										scope.save();
										expect(resourceMock.$save).toHaveBeenCalled();
									});
									it('should call resource.$save with scope callbacks if specified', function() {
										// We must recompile/link the directive to get it to pick up the new callbacks
										inject(function($compile) {
											$rootScope.onSave = jasmine.createSpy('onSave');
											$rootScope.onSaveError = jasmine.createSpy('onSaveError');

											element = $compile('<form name="form" crud-edit="resource"><input name="someField" ng-model="resource.someVal"></form>')(
													$rootScope);
											scope = element.scope();
										});

										scope.save();
										expect(resourceMock.$save).toHaveBeenCalled();
									});

									it(
											'should call resource.$save with attribute callbacks if specified',
											function() {
												// We must recompile/link the directive to get it to pick up the new callbacks
												inject(function($compile) {
													$rootScope.onSaveAttr = jasmine.createSpy('onSaveAttr');
													$rootScope.onSaveErrorAttr = jasmine.createSpy('onSaveErrorAttr');
													$rootScope.onSave = jasmine.createSpy('onSave');
													$rootScope.onSaveError = jasmine.createSpy('onSaveError');

													element = $compile(
															'<form name="form" crud-edit="resource" on-save="onSaveAttr" on-save-error="onSaveErrorAttr"><input name="someField" ng-model="resource.someVal"></form>')
															($rootScope);
													scope = element.scope();
												});

												scope.save();
												expect(resourceMock.$save).toHaveBeenCalled();
												expect($rootScope.onSaveAttr).toHaveBeenCalled();
												expect($rootScope.onSaveErrorAttr).toHaveBeenCalled();
											});

									it('should update the "original" object', function() {
										resourceMock.$save.andCallFake(function(onSave) {
											onSave({
												someVal : 'newValue'
											});
										});
										someField.$setViewValue('newValue');
										expect(scope.canSave()).toBeTruthy();
										scope.save();
										expect(scope.canSave()).toBeFalsy();
									});
								});

						describe('canSave', function() {
							it('should return true if the model is valid and different to the original', function() {
								expect(scope.canSave()).toBeFalsy();
								someField.$setViewValue('different');
								expect(scope.canSave()).toBeTruthy();
							});
							it('should return false if the model is invalid', function() {
								expect(scope.canSave()).toBeFalsy();
								someField.$setValidity('required', false);
								expect(scope.canSave()).toBeFalsy();
								someField.$setViewValue('different');
								expect(scope.canSave()).toBeFalsy();
							});
						});

						describe('revertChanges', function() {
							it('should reset the resource to its original state', function() {
								var original = angular.copy(scope.resource);
								expect(scope.resource).toEqual(original);
								scope.resource.someVal = 'different';
								expect(scope.resource).not.toEqual(original);
								scope.revertChanges();
								expect(scope.resource).toEqual(original);
							});
						});

						describe('canRevert', function() {
							it('should return false if the model is same as the original', function() {
								expect(scope.canRevert()).toBeFalsy();
								someField.$setValidity('required', false);
								expect(scope.canRevert()).toBeFalsy();
							});
							it('should return true if the model is different to the original', function() {
								expect(scope.canRevert()).toBeFalsy();
								someField.$setViewValue('different');
								expect(scope.canRevert()).toBeTruthy();
								someField.$setValidity('required', false);
								expect(scope.canRevert()).toBeTruthy();
							});
						});

						describe('remove', function() {
							it('scope.remove should call resource.$remove if resource.$id returns true', function() {
								resourceMock.$id = jasmine.createSpy('$id').andReturn(true);
								scope.remove();
								expect(resourceMock.$remove).toHaveBeenCalledWith(jasmine.any(Function), jasmine.any(Function));
							});

							it('scope.remove should call resource.$remove with scope callbacks if specified',
									function() {
										// We must recompile/link the directive to get it to pick up the new callbacks
										inject(function($compile) {
											$rootScope.onRemove = jasmine.createSpy('onRemove');
											$rootScope.onRemoveError = jasmine.createSpy('onRemoveError');

											element = $compile('<form name="form" crud-edit="resource"><input name="someField" ng-model="resource.someVal"></form>')(
													$rootScope);
											scope = element.scope();
										});

										resourceMock.$id = jasmine.createSpy('$id').andReturn(true);
										resourceMock.$remove.andCallFake(function(onRemove, onError) {
											onRemove();
											onError();
										});
										scope.remove();
										expect(resourceMock.$remove).toHaveBeenCalledWith(jasmine.any(Function), jasmine.any(Function));
										expect(scope.onRemove).toHaveBeenCalled();
										expect(scope.onRemoveError).toHaveBeenCalled();
									});

							it('scope.remove should call resource.$remove if resource.$id returns false', function() {
								resourceMock.$id = jasmine.createSpy('$id').andReturn(false);
								scope.remove();
								expect(resourceMock.$remove).not.toHaveBeenCalled();
							});
						});

					});
		});