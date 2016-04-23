(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlFileParserDeleteController',AdlFileParserDeleteController);

    AdlFileParserDeleteController.$inject = ['$uibModalInstance', 'entity', 'AdlFileParser'];

    function AdlFileParserDeleteController($uibModalInstance, entity, AdlFileParser) {
        var vm = this;
        vm.adlFileParser = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            AdlFileParser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
