(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('adl-file', {
            parent: 'entity',
            url: '/adl-file?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.adlFile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adl-file/adl-files.html',
                    controller: 'AdlFileController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('adlFile');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('adl-file-detail', {
            parent: 'entity',
            url: '/adl-file/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.adlFile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adl-file/adl-file-detail.html',
                    controller: 'AdlFileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('adlFile');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AdlFile', function($stateParams, AdlFile) {
                    return AdlFile.get({id : $stateParams.id});
                }]
            }
        })
        .state('adl-file.new', {
            parent: 'adl-file',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adl-file/adl-file-dialog.html',
                    controller: 'AdlFileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                filename: null,
                                size: null,
                                dateUpload: null,
                                comment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('adl-file', null, { reload: true });
                }, function() {
                    $state.go('adl-file');
                });
            }]
        })
        .state('adl-file.upload', {
            parent: 'adl-file',
            url: '/upload',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adl-file/adl-file-upload-dialog.html',
                    controller: 'AdlFileUploadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                filename: null,
                                size: null,
                                dateUpload: null,
                                comment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('adl-file', null, { reload: true });
                }, function() {
                    $state.go('adl-file');
                });
            }]
        })
        .state('adl-file.edit', {
            parent: 'adl-file',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adl-file/adl-file-dialog.html',
                    controller: 'AdlFileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdlFile', function(AdlFile) {
                            return AdlFile.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('adl-file', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('adl-file.delete', {
            parent: 'adl-file',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adl-file/adl-file-delete-dialog.html',
                    controller: 'AdlFileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AdlFile', function(AdlFile) {
                            return AdlFile.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('adl-file', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
