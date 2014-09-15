(function() {
	'use strict';

	angular.module('parameter.directives', [])

	.directive('jqGrid', function($timeout) {
		return {
			restrict : 'A',
			link : function(scope, element, attrs, ctrl) {
				$timeout(function() {
					element.jqxDataTable({
						theme: "darkblue",
						altrows: true,
						sortable: true,
						columns: [
						          { text: 'Name', dataField: 'Name', width: 300 },
						          { text: 'Label', dataField: 'Label', width: 200 },
						          { text: 'Value', dataField: 'Value', width: 300 }
						          ]
					});		
				});
			}
		};
	})
	
	.directive('checkFolder', function(ParameterService) {
		return {
			require : 'ngModel',
			restrict : 'A',
			link : function(scope, element, attrs, ctrl) {
				// using push() here to run it as the last parser, after we are sure that other validators were run
				ctrl.$parsers.push(function(viewValue) {
					if (viewValue) {
						ParameterService.checkFolder(viewValue).then(function(result) {
							ctrl.$setValidity('folder', result.data === 'true');
						});
						return viewValue;
					}
				});
			}
		};
	})
	
	;

})();
