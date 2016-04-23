(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlFileDetailController', AdlFileDetailController);

    AdlFileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'AdlFile'];

    function AdlFileDetailController($scope, $rootScope, $stateParams, entity, AdlFile) {
        var vm = this;
        vm.adlFile = entity;
        vm.load = function (id) {
            AdlFile.get({id: id}, function(result) {
                vm.adlFile = result;
            });
        };
        var unsubscribe = $rootScope.$on('adlviewerApp:adlFileUpdate', function(event, result) {
            vm.adlFile = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
