(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('DescriptionDetailController', DescriptionDetailController);

    DescriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Description'];

    function DescriptionDetailController($scope, $rootScope, $stateParams, entity, Description) {
        var vm = this;
        vm.description = entity;
        vm.load = function (id) {
            Description.get({id: id}, function(result) {
                vm.description = result;
            });
        };
        var unsubscribe = $rootScope.$on('adlviewerApp:descriptionUpdate', function(event, result) {
            vm.description = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
