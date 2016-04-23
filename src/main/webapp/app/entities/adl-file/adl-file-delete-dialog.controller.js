(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlFileDeleteController',AdlFileDeleteController);

    AdlFileDeleteController.$inject = ['$uibModalInstance', 'entity', 'AdlFile'];

    function AdlFileDeleteController($uibModalInstance, entity, AdlFile) {
        var vm = this;
        vm.adlFile = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            AdlFile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
