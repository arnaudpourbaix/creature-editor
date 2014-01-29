(function() {
	'use strict';

	var module = angular.module('creatureEditor.category.directives', [ 'creatureEditor.category.services' ]);

	module.directive('jqxtree', function() {
		return {
			restrict : 'A',
			scope : {
				data : '=jqxtree'
			},
			link : function(scope, element, attrs) {
				scope.$watch('data', function() {
					$(element).jqxTree({
						source : scope.data,
						allowDrag : true,
						allowDrop : true,
						dragEnd : function() {
						}
					});
				}, false);
			}

		};
	});

})();
