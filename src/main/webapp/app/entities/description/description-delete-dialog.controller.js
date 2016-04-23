(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('DescriptionDeleteController',DescriptionDeleteController);

    DescriptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Description'];

    function DescriptionDeleteController($uibModalInstance, entity, Description) {
        var vm = this;
        vm.description = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Description.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
