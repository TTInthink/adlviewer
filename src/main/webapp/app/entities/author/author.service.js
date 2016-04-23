(function() {
    'use strict';
    angular
        .module('adlviewerApp')
        .factory('Author', Author);

    Author.$inject = ['$resource', 'DateUtils'];

    function Author ($resource, DateUtils) {
        var resourceUrl =  'api/authors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.publicationDate = DateUtils.convertLocalDateFromServer(data.publicationDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.publicationDate = DateUtils.convertLocalDateToServer(data.publicationDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.publicationDate = DateUtils.convertLocalDateToServer(data.publicationDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
