'use strict';


// Declare app level module which depends on filters, and services
angular.module('direnaj',
        ['direnaj.filters',
         'direnaj.services',
         'direnaj.directives',
         'direnaj.controllers',
         'ui.bootstrap',
         'ngGrid']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/toolkit/test', {templateUrl: 'partials/toolkit/test.html', controller: 'ToolkitCtrl'});
    $routeProvider.when('/statuses/filter/:campaign_id', {templateUrl: 'partials/statuses/filter.html', controller: 'StatusesFilterCtrl'});
    $routeProvider.when('/', {templateUrl: 'partials/homepage/index.html', controller: 'HomepageCtrl'});
    $routeProvider.otherwise({redirectTo: '/'});
  }]);
