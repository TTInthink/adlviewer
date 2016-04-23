(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('DescriptionDialogController', DescriptionDialogController);

    DescriptionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Description'];

    function DescriptionDialogController ($scope, $stateParams, $uibModalInstance, entity, Description) {
        var vm = this;
        vm.description = entity;
        vm.load = function(id) {
            Description.get({id : id}, function(result) {
                vm.description = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adlviewerApp:descriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.description.id !== null) {
                Description.update(vm.description, onSaveSuccess, onSaveError);
            } else {
                Description.save(vm.description, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
