(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .controller('AdlFileParserDetailController', AdlFileParserDetailController);

    AdlFileParserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'AdlFileParser'];

    function AdlFileParserDetailController($scope, $rootScope, $stateParams, entity, AdlFileParser) {
        var vm = this;
        vm.adlFileParser = entity;
        vm.load = function (id) {
            AdlFileParser.get({id: id}, function(result) {
                vm.adlFileParser = result;
            });
        };
        var unsubscribe = $rootScope.$on('adlviewerApp:adlFileParserUpdate', function(event, result) {
            vm.adlFileParser = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
