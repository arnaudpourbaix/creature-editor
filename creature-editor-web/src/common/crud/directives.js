(function() {
	'use strict';

	var module = angular.module('crud.directives', [ 'pascalprecht.translate', 'alert-message' ]);

	module.directive('crudAddButton', function CrudAddButtonDirective() {
		return {
			restrict : 'E',
			replace : true,
			templateUrl : 'crud/crud-add-button.tpl.html'
		};
	});

	module.directive('crudButtons', function CrudButtonsDirective() {
		return {
			restrict : 'E',
			replace : true,
			templateUrl : 'crud/crud-buttons.tpl.html',
			controller : function crudButtonsController($scope, $element, $attrs) {
				$scope.removeDisabled = $attrs.remove ? $attrs.remove === 'false' : false;
			}
		};
	});

	// Apply this directive to an element at or below a form that will manage CRUD operations on a resource.
	// The resource must expose the following instance methods: $save(), $id() and $remove()
	module.directive('crudEdit', [ '$parse', '$translate', '$alertMessage', function crudEditDirective($parse, $translate, $alertMessage) {

		// This function controls that resource has all required methods
		function checkResourceMethods(resource, methods) {
			angular.forEach(methods, function(method, index) {
				if (!angular.isFunction(resource[method])) {
					throw new Error('crudEdit directive: resource must expose the ' + method + '() instance method');
				}
			});
		}

		// This function helps us extract the callback functions from the directive attributes
		function makeFn(scope, attrs, attrName) {
			var fn = scope.$eval(attrs[attrName]);
			if (!angular.isFunction(fn)) {
				throw new Error('crudEdit directive: attribute "' + attrName + '" must evaluate to a function');
			}
			return fn;
		}

		function sendNotification(key, type, notification, resource) {
			$translate(notification.entity).then(function(translation) {
				$alertMessage.push(key, type, {
					entity : translation,
					name : resource[notification.name]
				});
			});
		}

		return {
			// We ask this directive to create a new child scope so that when we add helper methods to the scope. It doesn't make a mess of the parent scope.
			// Be aware that if you write to the scope from within the form then you must remember that there is a child scope at the point
			scope : true,
			// We need access to a form so we require a FormController from this element or a parent element
			require : '^form',
			restrict : 'A',
			link : function(scope, element, attrs, form) {
				var params = scope.$eval(attrs.crudEdit);
				// We extract the value of the crudEdit attribute, it should be an assignable expression evaluating to the model (resource) that is going to be edited
				var resourceGetter = $parse(params.model);
				var resourceSetter = resourceGetter.assign;
				// Store the resource object for easy access
				var resource = resourceGetter(scope);
				// Store a copy for reverting the changes
				var original = angular.copy(resource);

				var notification = params.entity && params.name;

				checkResourceMethods(resource, [ '$save', '$id', '$remove' ]);

				// Set up callbacks with fallback
				var userOnCancel = attrs.onCancel ? makeFn(scope, attrs, 'onCancel') : (scope.onCancel || angular.noop);
				var userOnSave = attrs.onSave ? makeFn(scope, attrs, 'onSave') : (scope.onSave || angular.noop);
				var userOnSaveError = attrs.onSaveError ? makeFn(scope, attrs, 'onSaveError') : (scope.onSaveError || angular.noop);
				var userOnRemove = attrs.onRemove ? makeFn(scope, attrs, 'onRemove') : (scope.onRemove || angular.noop);
				var userOnRemoveError = attrs.onRemoveError ? makeFn(scope, attrs, 'onRemoveError') : (scope.onRemoveError || angular.noop);

				var onSave = function(result, status, headers, config) {
					// Reset the original to help with reverting and pristine checks
					original = result;
					if (notification) {
						sendNotification('CRUD.SAVE_SUCCESS', 'success', params, result);
					}
					userOnSave(result, status, headers, config);
				};
				var onSaveError = function(result) {
					if (notification) {
						sendNotification('CRUD.SAVE_ERROR', 'danger', params, resource);
					}
					userOnSaveError();
				};
				var onRemove = function(result, status, headers, config) {
					if (notification) {
						sendNotification('CRUD.REMOVE_SUCCESS', 'info', params, result);
					}
					userOnRemove(result, status, headers, config);
				};
				var onRemoveError = function(result) {
					if (notification) {
						sendNotification('CRUD.REMOVE_ERROR', 'danger', params, resource);
					}
					userOnRemoveError(result);
				};

				// The following functions should be triggered by elements on the form
				scope.cancel = function() {
					scope.revertChanges();
					userOnCancel();
				};
				scope.save = function() {
					resource.$save(onSave, onSaveError);
				};
				scope.revertChanges = function() {
					resource = angular.copy(original);
					resourceSetter(scope, resource);
					form.$setPristine();
				};
				scope.remove = function() {
					if (resource.$id()) {
						resource.$remove(onRemove, onRemoveError);
					} else {
						onRemove();
					}
				};

				// The following functions can be called to modify the behaviour of elements in the form
				scope.canSave = function() {
					return form.$valid && !angular.equals(resource, original);
				};
				scope.canRevert = function() {
					return !angular.equals(resource, original);
				};
				scope.canRemove = function() {
					return resource.$id();
				};
			}
		};
	} ]);

})();