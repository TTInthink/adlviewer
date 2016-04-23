(function() {
    'use strict';

    angular
        .module('adlviewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('description', {
            parent: 'entity',
            url: '/description?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.description.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/description/descriptions.html',
                    controller: 'DescriptionController',
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
                    $translatePartialLoader.addPart('description');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('description-detail', {
            parent: 'entity',
            url: '/description/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'adlviewerApp.description.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/description/description-detail.html',
                    controller: 'DescriptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('description');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Description', function($stateParams, Description) {
                    return Description.get({id : $stateParams.id});
                }]
            }
        })
        .state('description.new', {
            parent: 'description',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/description/description-dialog.html',
                    controller: 'DescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                concept: null,
                                longConcept: null,
                                author: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('description', null, { reload: true });
                }, function() {
                    $state.go('description');
                });
            }]
        })
        .state('description.edit', {
            parent: 'description',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/description/description-dialog.html',
                    controller: 'DescriptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Description', function(Description) {
                            return Description.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('description', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('description.delete', {
            parent: 'description',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/description/description-delete-dialog.html',
                    controller: 'DescriptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Description', function(Description) {
                            return Description.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('description', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
