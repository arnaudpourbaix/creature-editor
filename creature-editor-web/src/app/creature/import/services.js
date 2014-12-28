angular.module('editor.creature.import.services', [])

.service('CreatureImportService', function($http, $interval, $q, $translate, toaster, creatureSettings, Mod, apxPanel, sidePanel) {

	var ImportContainer = function(itemCount) {
		var self = this;
		var interval;
		var creatures = [];
		var deferred = new $q.defer();
		
		var updateStatus = function() {
			$http({
				method : 'GET',
				url : creatureSettings.url + 'import/gather'
			}).then(function(response) {
				creatures.push.apply(creatures, response.data);
				self.progressValue = parseInt(100 / itemCount * creatures.length, 10);
				if (creatures.length === itemCount) {
					$interval.cancel(interval);
					deferred.resolve();
					toaster.pop('success', null, $translate.instant("CREATURE.IMPORT.SUCCESS"));
				}
			}, function(response) {
				$interval.cancel(interval);
				toaster.pop('danger', null, $translate.instant("CREATURE.IMPORT.ERROR"));
			});
		};

		self.creatures = creatures;
		self.progressValue = 0;
		self.end = deferred.promise;
		
		self.cancel = function() {
			$http({
				method : 'GET',
				url : creatureSettings.url + 'import/cancel'
			}).then(function(response) {
				$interval.cancel(interval);
				toaster.pop('warning', null, $translate.instant("CREATURE.IMPORT.CANCELLED"));
			});
		};
		
		(function() { // constructor
			interval = $interval(updateStatus, 2000);
		})();
		
	};
	
	var service = {};
	
	service.getPanel = function() {
		//var panel = apxPanel.open({
		var panel = sidePanel({
			templateUrl : 'creature/import/import-panel.tpl.html',
			controller : 'CreatureImportController',
			resolve : {
				mods : Mod.query().$promise
			},
			backdrop: false
		});
		return panel;
	};
	
	service.import = function(options) {
		return $http({
			method : 'POST',
			url : creatureSettings.url + 'import',
			data : options
		}).then(function(response) {
			return new ImportContainer(response.data);
		});
	};

	return service;
})
	
;
