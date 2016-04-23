(function() {
    'use strict';
    angular
        .module('adlviewerApp')
        .factory('Adlparser', Adlparser);

    Adlparser.$inject = ['$resource'];

    function Adlparser ($resource) {
        var resourceUrl =  'api/adlparsers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
    
    
    
})();
