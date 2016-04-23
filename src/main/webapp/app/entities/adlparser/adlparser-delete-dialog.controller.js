(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlparserDeleteController',AdlparserDeleteController);

    AdlparserDeleteController.$inject = ['$uibModalInstance', 'entity', 'Adlparser'];

    function AdlparserDeleteController($uibModalInstance, entity, Adlparser) {
        var vm = this;
        vm.adlparser = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Adlparser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
