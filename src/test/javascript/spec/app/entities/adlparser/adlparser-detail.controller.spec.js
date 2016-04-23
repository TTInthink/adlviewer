'use strict';

describe('Controller Tests', function() {

    describe('Adlparser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAdlparser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAdlparser = jasmine.createSpy('MockAdlparser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Adlparser': MockAdlparser
            };
            createController = function() {
                $injector.get('$controller')("AdlparserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'adlviewerApp:adlparserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
