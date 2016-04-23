(function() {
    'use strict';
    angular
        .module('adlviewerApp')
        .factory('Description', Description);

    Description.$inject = ['$resource'];

    function Description ($resource) {
        var resourceUrl =  'api/descriptions/:id';

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
