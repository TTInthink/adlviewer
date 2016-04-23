(function() {
    'use strict';
    angular
        .module('adlviewerApp')
        .factory('AdlFile', AdlFile);

    AdlFile.$inject = ['$resource', 'DateUtils'];

    function AdlFile ($resource, DateUtils) {
        var resourceUrl =  'api/adl-files/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateUpload = DateUtils.convertLocalDateFromServer(data.dateUpload);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateUpload = DateUtils.convertLocalDateToServer(data.dateUpload);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateUpload = DateUtils.convertLocalDateToServer(data.dateUpload);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
