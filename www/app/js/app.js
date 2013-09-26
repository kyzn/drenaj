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
    $routeProvider.when('/users/view', {templateUrl: 'partials/partial1.html', controller: 'MyCtrl1'});
    $routeProvider.when('/statuses/filter', {templateUrl: 'partials/statuses/filter.html', controller: 'StatusesFilterCtrl'});
    $routeProvider.otherwise({redirectTo: '/'});
  }]);
