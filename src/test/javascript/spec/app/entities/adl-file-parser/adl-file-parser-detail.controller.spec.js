'use strict';

describe('Controller Tests', function() {

    describe('AdlFileParser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAdlFileParser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAdlFileParser = jasmine.createSpy('MockAdlFileParser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AdlFileParser': MockAdlFileParser
            };
            createController = function() {
                $injector.get('$controller')("AdlFileParserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'adlviewerApp:adlFileParserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
