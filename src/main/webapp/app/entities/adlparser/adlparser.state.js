(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('adlparser', {
            parent: 'entity',
            url: '/adlparser?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.adlparser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adlparser/adlparsers.html',
                    controller: 'AdlparserController',
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
                    $translatePartialLoader.addPart('adlparser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('adlparser-detail', {
            parent: 'entity',
            url: '/adlparser/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.adlparser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adlparser/adlparser-detail.html',
                    controller: 'AdlparserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('adlparser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Adlparser', function($stateParams, Adlparser) {
                    return Adlparser.get({id : $stateParams.id});
                }]
            }
        })
        .state('adlparser.new', {
            parent: 'adlparser',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adlparser/adlparser-dialog.html',
                    controller: 'AdlparserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                filename: null,
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('adlparser', null, { reload: true });
                }, function() {
                    $state.go('adlparser');
                });
            }]
        })
        .state('adlparser.upload', {
            parent: 'adlparser',
            url: '/upload',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adlparser/adlparser-upload-dialog.html',
                    controller: 'AdlparserUploadController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                filename: null,
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                	console.debug('ADL Parser ---result');
                    $state.go('adlparser', null, { reload: true });
                }, function() {
                	console.debug('ADL Parser --GO^^-result');
                    $state.go('adlparser');
                });
            }]
        })
        .state('adlparser.edit', {
            parent: 'adlparser',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adlparser/adlparser-dialog.html',
                    controller: 'AdlparserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Adlparser', function(Adlparser) {
                            return Adlparser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('adlparser', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('adlparser.delete', {
            parent: 'adlparser',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adlparser/adlparser-delete-dialog.html',
                    controller: 'AdlparserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Adlparser', function(Adlparser) {
                            return Adlparser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                	
                    $state.go('adlparser', null, { reload: true });
                }, function() {
                	
                    $state.go('^');
                });
            }]
        });
    }

})();
