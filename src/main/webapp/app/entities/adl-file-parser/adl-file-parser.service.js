(function() {
    'use strict';
    angular
        .module('adlviewerApp')
        .factory('AdlFileParser', AdlFileParser);

    AdlFileParser.$inject = ['$resource'];

    function AdlFileParser ($resource) {
        var resourceUrl =  'api/adl-file-parsers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getAdlFileOption':{
            	method:'GET',
            	url:'api/adl-file-parsers/getAdlFileOption',
            	isArray: true,
            	transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
            }
        });
    }
})();
