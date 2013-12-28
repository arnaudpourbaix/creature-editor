/*describe('AppCtrl', function() {
	var scope;
		
	beforeEach(angular.mock.module('creatureEditor'));

	beforeEach(angular.mock.inject(function($rootScope, $controller, $location) {
		scope = $rootScope.$new();
		$controller('AppCtrl', { $location: $location, $scope: scope });
	}));
    console.log('before test', scope);
	it('should have variable text = "Hello World!"', function(){
		//expect(scope.text).toBe('Hello World!');
		expect('Hello World!').toBe('Hello World!');
	});		
});
*/
describe( 'AppCtrl', function() {
  describe( 'isCurrentUrl', function() {
    var AppCtrl, $location, $scope;

    beforeEach( module( 'creatureEditor' ) );

    beforeEach( inject( function( $controller, _$location_, $rootScope ) {
      $location = _$location_;
      $scope = $rootScope.$new();
      AppCtrl = $controller( 'AppCtrl', { $location: $location, $scope: $scope });
    }));

    it( 'should pass a dummy test', inject( function() {
      expect( AppCtrl ).toBeTruthy();
    }));
  });
});