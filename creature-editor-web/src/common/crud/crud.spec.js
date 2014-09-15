/* global jasmine, inject, xdescribe, xit */

describe('CRUD scope mix-ins', function() {
	"use strict";

	var $rootScope;

	describe('crud list methods', function() {
		var LocationMock = function() {
			this.pathStr = '';
			this.path = function(path) {
				return path ? this.pathStr = path : this.pathStr;
			};
		};

		var $location, crudListMethods;

		beforeEach(function() {
			angular.module('test', [ 'crud.services' ]).value('$location', $location = new LocationMock());
		});
		beforeEach(module('test'));
		beforeEach(inject(function(_$rootScope_, _crudListMethods_) {
			$rootScope = _$rootScope_;
			crudListMethods = _crudListMethods_;
		}));

		it('should support new method', function() {
			angular.extend($rootScope, crudListMethods('/prefix'));
			$rootScope['new']();
			expect($location.path()).toEqual('/prefix/new');
		});

		it('should support edit method', function() {
			angular.extend($rootScope, crudListMethods('/prefix'));
			$rootScope.edit('someId');
			expect($location.path()).toEqual('/prefix/someId');
		});
	});
});