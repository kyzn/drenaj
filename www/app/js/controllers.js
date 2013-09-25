'use strict';

/* Controllers */

angular.module('direnaj.controllers', []).
controller('MyCtrl1', [function() {

}])
.controller('StatusesFilterCtrl', ['$scope', '$http', function(scope, http) {
    /*scope.statuses = [{name:'a'}, {name: 'b'}];*/
    http.get('/statuses/filter', {
        params: {
            limit: 1,
            campaign_id: 'syria',
            auth_user_id: 'direnaj',
            auth_password: 'tamtam'
        }
    }).success(function(data, status, headers, config) {
        // this callback will be called asynchronously
        // when the response is available
        console.log(data);
        scope.statuses = data.results;
    }).error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        alert(data);
    });
}]);
