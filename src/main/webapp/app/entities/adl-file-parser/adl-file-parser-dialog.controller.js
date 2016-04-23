(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlFileParserDialogController', AdlFileParserDialogController);

    AdlFileParserDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'AdlFileParser'];

    function AdlFileParserDialogController ($scope, $stateParams, $uibModalInstance, entity, AdlFileParser) {
        var vm = this;
        vm.adlFileParser = entity;
        
		vm.archetypeIdData = AdlFileParser.getAdlFileOption({},function(result) {
		
		});


        vm.load = function(id) {
            AdlFileParser.get({id : id}, function(result) {
                vm.adlFileParser = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adlviewerApp:adlFileParserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.adlFileParser.id !== null) {
                AdlFileParser.update(vm.adlFileParser, onSaveSuccess, onSaveError);
            } else {
                AdlFileParser.save(vm.adlFileParser, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
