'use strict';

describe('Controller Tests', function() {

    describe('AdlFile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAdlFile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAdlFile = jasmine.createSpy('MockAdlFile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AdlFile': MockAdlFile
            };
            createController = function() {
                $injector.get('$controller')("AdlFileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'adlviewerApp:adlFileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
