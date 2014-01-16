var allow = true, request;
var startUrl;
var pollUrl;

function Poll() {
	'use strict';

	this.start = function(start, poll) {

		startUrl = start;
		pollUrl = poll;

		if (request) {
			request.abort(); // abort any pending request
		}

		// fire off the request to MatchUpdateController
		request = $.ajax({
			url : startUrl,
			type : "get",
		});

		// This is jQuery 1.8+
		// callback handler that will be called on success
		request.done(function(reply) {

			console.log("Game on..." + reply);
			setInterval(function() {
				if (allow === true) {
					allow = false;
					getUpdate();
				}
			}, 500);
		});

		// callback handler that will be called on failure
		request.fail(function(jqXHR, textStatus, errorThrown) {
			// log the error to the console
			console.log("Start - the following error occured: " + textStatus, errorThrown);
		});

	};

	function getUpdate() {

		console.log("Okay let's go...");

		if (request) {
			request.abort(); // abort any pending request
		}

		// fire off the request to MatchUpdateController
		request = $.ajax({
			url : pollUrl,
			type : "get",
		});

		// This is jQuery 1.8+
		// callback handler that will be called on success
		request.done(function(message) {
			console.log("Received a message");

			var update = getMessage(message);
			$(update).insertAfter('.spell-import-progress');
		});

		function getMessage(message) {

			var update = "<div class='span-4  prepend-2'>" + "<p class='update'>Time:</p>" + "</div>" + "<div class='span-3 border'>" + "<p id='time' class='update'>"
					+ message.matchTime + "</p>" + "</div>" + "<div class='span-13 append-2 last' id='update-div'>" + "<p id='message' class='update'>" + message.messageText + "</p>"
					+ "</div>";
			return update;
		}

		// callback handler that will be called on failure
		request.fail(function(jqXHR, textStatus, errorThrown) {
			// log the error to the console
			console.log("Polling - the following error occured: " + textStatus, errorThrown);
			allow = false;
		});

		// callback handler that will be called regardless if the request failed or succeeded
		request.always(function() {
			allow = true;
		});
	}
}

(function() {
	'use strict';

	var spell = angular.module('creatureEditor.spell.controllers', [ 'ui.router', 'ngRoute', 'ngResource', 'crud.services' ]);

	spell.controller('SpellListController', function SpellListController($scope, $state, spells, crudListMethods, i18nNotifications) {

		angular.extend($scope, crudListMethods('/spells'));

		$scope.spells = spells;

		$scope.gridOptions = {
			data : 'spells',
			enableRowSelection : false,
			enableColumnResize : true,
			showFilter : true,
			columnDefs : [ {
				field : 'name',
				displayName : 'Name',
				cellClass : 'cellName',
				cellTemplate : '<div class="ngCellText" ng-class="col.colIndex()" ng-click="edit(row.entity.id)"><span>{{row.getProperty(col.field)}}</span></div>'
			}, {
				sortable : false,
				cellClass : 'actionColumn',
				width : '60px',
				cellTemplate : '<span class="glyphicon glyphicon-trash" title="Delete" ng-click="remove(row.entity)" />'
			} ]
		};

		$scope.$on('selectedMod', function(e, mod) {
			console.log('SpellListController', 'selectedMod', mod);
			$scope.mod = mod;
			e.stopPropagation();
		});

		$scope.remove = function(spell) {
			spell.$delete().then(function(response) {
				$scope.removeFromList($scope.spells, 'id', spell.id);
				i18nNotifications.pushForCurrentRoute('crud.spell.remove.success', 'success', {
					name : spell.name
				});
			});
		};

	});

	spell.controller('SpellImportController', function SpellImportController($scope, $modalInstance, $http, $interval, i18nNotifications) {

		$scope.mod = null;

		$scope.$on('selectedMod', function(e, mod) {
			console.log('SpellImportController', 'selectedMod', mod);
			$scope.mod = mod;
			e.stopPropagation();
		});

		var stop;

		$scope.import = function() {
			$scope.progressValue = 0;
			var startUrl = "rest/spell/import?modId=1";
			var pollUrl = "rest/spell/deferred";

			var poll = new Poll();
			poll.start(startUrl, pollUrl);

			// $http({
			// method : 'GET',
			// url : 'rest/spell/import',
			// params : {
			// modId : $scope.mod.id
			// },
			// }).then(function(data) {
			// console.log("Game on...", data);
			// stop = $interval(function() {
			// $scope.getUpdate();
			// }, 2000);
			// }, function(data, status, headers, config) {
			// console.log('error', data);
			// });
		};

		$scope.getUpdate = function() {
			// if (request) {
			// request.abort(); // abort any pending request
			// }
			// fire off the request to MatchUpdateController
			// var request = $http({
			$http({
				method : 'GET',
				url : 'rest/spell/deferred'
			}).then(function(message) {
				console.log("Received a message", message.messageText);
				// var update = getUpdate(message);
				// $(update).insertAfter('#first_row');
			}, function(data) {
				// log the error to the console
				console.log("Polling - the following error occured: ", data);
			});

			// callback handler that will be called regardless if the request failed or succeeded
			// request.always(function() {
			// allow = true;
			// });
		};

	});

	spell.controller('SpellEditController', function SpellEditController($scope, $modalInstance, spell, i18nNotifications) {

		$scope.spell = spell;

		$scope.onSave = function(spell) {
			i18nNotifications.pushForCurrentRoute('crud.spell.save.success', 'success', {
				name : spell.name
			});
			$modalInstance.close();
		};

		$scope.onError = function() {
			i18nNotifications.pushForCurrentRoute('crud.spell.save.error', 'danger');
		};

		$scope.onRemove = function(spell) {
			i18nNotifications.pushForCurrentRoute('crud.spell.remove.success', 'success', {
				name : spell.name
			});
			$modalInstance.close();
		};
	});

})();
