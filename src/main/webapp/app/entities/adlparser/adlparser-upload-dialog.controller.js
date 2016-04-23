(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlparserUploadController', AdlparserUploadController);

    AdlparserUploadController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Adlparser','FileUploader'];

    function AdlparserUploadController ($scope, $stateParams, $uibModalInstance, entity, Adlparser,FileUploader) {
        console.debug('Uploader function');
    	var vm = this;
		var csrftoken = getCookie('CSRF-TOKEN');
        vm.uploader = new FileUploader({
            url: 'api/adlparsers/upload',
            headers:{'X-CSRF-TOKEN':csrftoken},
			removeAfterUpload: true,
			autoUpload: false,
			withCredentials: true,
			queueLimit: 1
        });
        vm.adlparser = entity;
        vm.load = function(id) {
            Adlparser.get({id : id}, function(result) {
                vm.adlparser = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adlviewerApp:adlparserUpdate', result);

            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
        	console.debug('saving function');
        	vm.uploader.uploadAll();

        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        vm.uploader.onAfterAddingFile = function(fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        vm.uploader.onSuccessItem = function(fileItem, response, status, headers) {
        	$scope.$emit('adlviewerApp:adlparserUpdate', response);
            $uibModalInstance.close(true);
            console.info('onSuccessItem', fileItem, response, status, headers);
			 
        };
        vm.uploader.onErrorItem = function(fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
        };
    }
	
	// using jQuery
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
