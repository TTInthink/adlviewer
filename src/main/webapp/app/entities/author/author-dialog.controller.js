(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AuthorDialogController', AuthorDialogController);

    AuthorDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Author'];

    function AuthorDialogController ($scope, $stateParams, $uibModalInstance, entity, Author) {
        var vm = this;
        vm.author = entity;
        vm.load = function(id) {
            Author.get({id : id}, function(result) {
                vm.author = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adlviewerApp:authorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.author.id !== null) {
                Author.update(vm.author, onSaveSuccess, onSaveError);
            } else {
                Author.save(vm.author, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.publicationDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
