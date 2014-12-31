angular.module('editor.creature.import.controllers', [])

.controller('CreatureImportController', function($scope, $translate, toaster, $alertify, CreatureImportService, $panelInstance, mods) {
	
	$scope.mods = mods;
	
	$scope.options = {
			mod: _.find(mods, function(mod) {
				return mod.id === 1;
			}),
			resource: 'A.*',
			override: false,
			onlyName: true
	};
	
	$scope.validate = function() {
		$panelInstance.close('validate');
//		CreatureImportService.import($scope.options).then(function(response) {
//			$scope.job = response;
//			$scope.job.end.then(function() {
//				$scope.job = null;
//				$panelInstance.close();
//			});
//		}, function(response) {
//			toaster.pop('danger', null, $translate.instant(response.data.code));
//		});
	};

	$scope.cancel = function() {
		if (!$scope.job) {
			$panelInstance.dismiss('cancel');
		} else {
			$scope.job.cancel();
			$panelInstance.dismiss('cancel');
//			$alertify.confirm($translate.instant("CREATURE.IMPORT.CANCEL_CONFIRM")).then(function() {
//				$scope.job.cancel();
//				$panelInstance.close();
//			});
		}
	};
	
	
})

;

