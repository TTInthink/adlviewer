(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlFileDialogController', AdlFileDialogController);

    AdlFileDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'AdlFile'];

    function AdlFileDialogController ($scope, $stateParams, $uibModalInstance, entity, AdlFile) {
        var vm = this;
        vm.adlFile = entity;
        vm.load = function(id) {
            AdlFile.get({id : id}, function(result) {
                vm.adlFile = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adlviewerApp:adlFileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.adlFile.id !== null) {
                AdlFile.update(vm.adlFile, onSaveSuccess, onSaveError);
            } else {
                AdlFile.save(vm.adlFile, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateUpload = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
