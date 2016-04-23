(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlparserDetailController', AdlparserDetailController);

    AdlparserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Adlparser'];

    function AdlparserDetailController($scope, $rootScope, $stateParams, entity, Adlparser) {
        var vm = this;
        vm.adlparser = entity;
        vm.load = function (id) {
            Adlparser.get({id: id}, function(result) {
                vm.adlparser = result;
            });
        };
        var unsubscribe = $rootScope.$on('adlviewerApp:adlparserUpdate', function(event, result) {
            vm.adlparser = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
