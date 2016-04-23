(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlFileUploadDialogController', AdlFileUploadDialogController);

    AdlFileUploadDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'AdlFile','FileUploader'];

    function AdlFileUploadDialogController ($scope, $stateParams, $uibModalInstance, entity, AdlFile,FileUploader) {
        var vm = this;
        var csrftoken = getCookie('CSRF-TOKEN');
        vm.uploader = new FileUploader({
            url: 'api/adl-files/upload',
            headers:{'X-CSRF-TOKEN':csrftoken},
			removeAfterUpload: true,
			autoUpload: false,
			withCredentials: true,
			queueLimit: 1
        });
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
            vm.uploader.uploadAll();
//            if (vm.adlFile.id !== null) {
//                AdlFile.update(vm.adlFile, onSaveSuccess, onSaveError);
//            } else {
//                AdlFile.save(vm.adlFile, onSaveSuccess, onSaveError);
//            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateUpload = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
        
vm.uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
    console.info('onWhenAddingFileFailed', item, filter, options);
};
vm.uploader.onAfterAddingFile = function(fileItem) {
    console.info('onAfterAddingFile', fileItem);
};
vm.uploader.onBeforeUploadItem = function(fileItem) {
	fileItem.formData.push({dateUpload:vm.adlFile.dateUpload},{comment:vm.adlFile.comment});
	console.info('Form Item ',fileItem.formData);
    console.info('onBeforeUploadItem', fileItem);
};
vm.uploader.onSuccessItem = function(fileItem, response, status, headers) {
	$scope.$emit('adlviewerApp:adlFileUpdate', response);
    $uibModalInstance.close(true);
    console.info('onSuccessItem', fileItem, response, status, headers);
	 
};
vm.uploader.onErrorItem = function(fileItem, response, status, headers) {
    console.info('onErrorItem', fileItem, response, status, headers);
};
    }
    
//using jQuery
function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie != '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = jQuery.trim(cookies[i]);
            // Does this cookie string begin with the name we want?
            if (cookie.substring(0, name.length + 1) == (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}
})();
