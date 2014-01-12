/* global jasmine, inject, xdescribe, xit */

describe('localized messages', function() {
	"use strict";

	var localizedMessages, messages;
	beforeEach(function() {
		angular.module('test', [ 'notification.localizedMessages' ]).value('I18N.MESSAGES', messages = {});
		module('test');
	});
	beforeEach(inject(function(_localizedMessages_) {
		localizedMessages = _localizedMessages_;
	}));

	it('should return a localized message if defined', function() {
		messages.existing = 'Existing message!';
		expect(localizedMessages.get('existing')).toEqual('Existing message!');
	});

	it('should return a message key surrounded by a question mark for non-existing messages', function() {
		expect(localizedMessages.get('non.existing')).toEqual('?non.existing?');
	});

	it('should interpolate parameters', function() {
		messages.sth = 'en {{param}} us';
		expect(localizedMessages.get('sth', {
			param : 'value'
		})).toEqual('en value us');
	});

	it('should not break for missing params', function() {
		messages.sth = 'en {{param}} us';
		expect(localizedMessages.get('sth')).toEqual('en  us');
		expect(localizedMessages.get('sth', {
			other : 'value'
		})).toEqual('en  us');
	});
});