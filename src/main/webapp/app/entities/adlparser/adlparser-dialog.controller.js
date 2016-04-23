(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlparserDialogController', AdlparserDialogController);

    AdlparserDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Adlparser'];

    function AdlparserDialogController ($scope, $stateParams, $uibModalInstance, entity, Adlparser) {
        var vm = this;
        vm.adlparser = entity;
        vm.load = function(id) {
            Adlparser.get({id : id}, function(result) {
                vm.adlparser = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adlviewerApp:adlparserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.adlparser.id !== null) {
                Adlparser.update(vm.adlparser, onSaveSuccess, onSaveError);
            } else {
                Adlparser.save(vm.adlparser, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
