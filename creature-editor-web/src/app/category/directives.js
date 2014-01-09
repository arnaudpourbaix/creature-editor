(function() {
	'use strict';

	var category = angular.module('creatureEditor.category.directives', [ 'creatureEditor.category.services' ]);

	category.directive('jqxtree', function() {
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
