(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('adl-file-parser', {
            parent: 'entity',
            url: '/adl-file-parser?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.adlFileParser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adl-file-parser/adl-file-parsers.html',
                    controller: 'AdlFileParserController',
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
                    $translatePartialLoader.addPart('adlFileParser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('adl-file-parser-detail', {
            parent: 'entity',
            url: '/adl-file-parser/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.adlFileParser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adl-file-parser/adl-file-parser-detail.html',
                    controller: 'AdlFileParserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('adlFileParser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AdlFileParser', function($stateParams, AdlFileParser) {
                    return AdlFileParser.get({id : $stateParams.id});
                }]
            }
        })
        .state('adl-file-parser.new', {
            parent: 'adl-file-parser',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adl-file-parser/adl-file-parser-dialog.html',
                    controller: 'AdlFileParserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                archetypeID: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('adl-file-parser', null, { reload: true });
                }, function() {
                    $state.go('adl-file-parser');
                });
            }]
        })
        .state('adl-file-parser.edit', {
            parent: 'adl-file-parser',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adl-file-parser/adl-file-parser-dialog.html',
                    controller: 'AdlFileParserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdlFileParser', function(AdlFileParser) {
                            return AdlFileParser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('adl-file-parser', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('adl-file-parser.delete', {
            parent: 'adl-file-parser',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adl-file-parser/adl-file-parser-delete-dialog.html',
                    controller: 'AdlFileParserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AdlFileParser', function(AdlFileParser) {
                            return AdlFileParser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('adl-file-parser', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
