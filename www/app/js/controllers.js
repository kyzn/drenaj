'use strict';

/* Controllers */

angular.module('direnaj.controllers', []).
controller('HomepageCtrl', ['$scope', '$http', function($scope, $http) {

}])
.controller('CampaignsCtrl', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {

    var page = 1;
    var pageSize = 10;

    var init_campaign_id = 'direnodtu';
    if ($routeParams.campaign_id !== '') {
        init_campaign_id = $routeParams.campaign_id;
    }

    // loading campaigns on direnaj
    $scope.campaigns =
    [
        {"last_date": "2013-11-07", "total": 0, "_id": "kizlierkekli"},
        {"last_date": "2013-11-07", "total": 0, "_id": "default"}
    ];
    $http.get('/campaigns/filter', {
        params: {
            skip: (page-1) * pageSize,
            limit: pageSize,
        auth_user_id: 'direnaj',
        auth_password: 'tamtam'
        }
    }).success(function (data) {
        $scope.campaigns = data;
    });

    $scope.d3Data = [
          {name: "Greg", score:28},
          {name: "Ari", score:96},
          {name: "Loser", score: 48}
    ];

    console.log($scope.d3Data);

    $scope.campaign_in_tab = {
        "campaign_id": "cok guzel bir kampanya",
        "series": {"hour": { "00": 11, "01": 22 }, "minute": { "0000": 7 }},
    };

    $scope.campaign_series_to_plot = $scope.campaign_in_tab.series.hour;

    $scope.selectCampaign = function(campaign_id) {
        console.log("You've selected the tab:  " + campaign_id);
        $http.get('/campaigns/view', {
            params: {
                campaign_id: campaign_id,
                auth_user_id: 'direnaj',
                auth_password: 'tamtam'
            }
        }).success(function (data) {
            console.log(data[0]);
            $scope.campaign_in_tab = {
                "campaign_id": data[0].campaign_id,
                "date": data[0].date,
                "series": data[0].series,
            };
            $scope.campaign_series_to_plot = $scope.campaign_in_tab.series.hour;
            console.log($scope.campaign_in_tab);
        });
    };

    $scope.switchSeries = function(series_key) {
        if (series_key === 'hour') {
            $scope.campaign_series_to_plot = $scope.campaign_in_tab.series[series_key];
        } else if (series_key === 'minute') {
            $scope.campaign_series_to_plot = $scope.campaign_in_tab.series[series_key];
        }
        console.log($scope.campaign_series_to_plot);
    };

    $scope.campaign_histogram_to_plot = null;

    $scope.n_bins = 100;

    $scope.getCampaignHistogram = function(campaign_id, recalculate, n_bins) {
        console.log("You've requested the histogram for campaign:  " + campaign_id);

        var params = {
            campaign_id: campaign_id,
            auth_user_id: 'direnaj',
            auth_password: 'tamtam',
        };

        if (recalculate) {
            params.re_calculate = 'yes';
        } else {
            params.re_calculate = 'no';
        }

        if (n_bins) {
            params.n_bins = n_bins;
        }

        $http.get('/campaigns/histograms', {
            params: params
        }).success(function (data) {
            $scope.campaign_histogram = data.histogram;
            $scope.campaign_histogram_to_plot = data.histogram.user_creation;
        });

    };



    $scope.setCampaignHistogram = function(histogram_type, time_based) {
        if (time_based) {
            $scope.campaign_histogram_is_time_based = true;
        }

        if (histogram_type === 'user_creation') {
            $scope.campaign_histogram_to_plot = $scope.campaign_histogram.user_creation;
        } else if (histogram_type === 'user_n_tweets') {
            $scope.campaign_histogram_to_plot = $scope.campaign_histogram.user_n_tweets;
        } if (histogram_type === 'user_n_tweets_overall') {
            $scope.campaign_histogram_to_plot = $scope.campaign_histogram.user_n_tweets_overall;
        }
    };

}])
.controller('ToolkitCtrl', ['$scope', '$http', function($scope, $http) {

}])
.controller('StatusesFilterCtrl', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {
    /*$scope.statuses = [];
    $http.get('/statuses/filter', {
        params: {
            limit: 10,
            campaign_id: 'syria',
            auth_user_id: 'direnaj',
            auth_password: 'tamtam'
        }
    }).success(function(data, status, headers, config) {
        // this callback will be called asynchronously
        // when the response is available
        console.log(data);
        angular.forEach(data.results, function(result, key) {
            console.log(result);
            if (result.tweet.id_str) {
                this.push(result.tweet);
                //alert('testing x');
            }
        }, $scope.statuses);
    }).error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        alert(data);
    });*/

    var init_campaign_id = 'direnodtu';
    if ($routeParams.campaign_id !== '') {
        init_campaign_id = $routeParams.campaign_id;
    }
    $scope.query_params = {
        campaign_id: init_campaign_id
    };
    $scope.word = /^[\w0-9]*$/;

    $scope.filterOptions = {
        filterText: "",
        useExternalFilter: true
    };
    $scope.totalServerItems = 0;
    $scope.pagingOptions = {
        pageSizes: [5, 10, 20],
        pageSize: 10,
        currentPage: 1
    };
    $scope.setPagingData = function(data, page, pageSize){
        var pagedData = data.results;
        var mappedData = pagedData.map(function(item, index) {
            if (item.tweet.id_str) {
                return {
                    id_str: item.tweet.id_str,
                    text: item.tweet.text,
                    screen_name: item.tweet.user.screen_name,
                    user_id_str: item.tweet.user.id_str,
                    user_profile_image_url: item.tweet.user.profile_image_url,
                };
            } else {
                return {
                    id_str: 'NA',
                    text:  'NA',
                    screen_name:  'NA',
                    user_id_str:  'NA',
                    user_profile_image_url:  'NA',
                };
            }
        });
        console.log(mappedData);
        $scope.myData = mappedData;
        $scope.totalServerItems = data.length;
        if (!$scope.$$phase) {
            $scope.$apply();
        }
    };
    $scope.getPagedDataAsync = function (pageSize, page, campaign_id, searchText) {
        setTimeout(function () {
            var data;
            /*if (searchText) {
                var ft = searchText.toLowerCase();
                $http.get('jsonFiles/largeLoad.json').success(function (largeLoad) {
                    data = largeLoad.filter(function(item) {
                        return JSON.stringify(item).toLowerCase().indexOf(ft) != -1;
                    });
                    $scope.setPagingData(data,page,pageSize);
                });
            } else {*/
            $http.get('/statuses/filter', {
                params: {
                    skip: (page-1) * pageSize,
                    limit: pageSize,
                campaign_id: campaign_id,
                auth_user_id: 'direnaj',
                auth_password: 'tamtam'
                }
            }).success(function (largeLoad) {
                $scope.setPagingData(largeLoad,page,pageSize);
            });
            /*}*/
        }, 100);
    };

    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.query_params.campaign_id);

    $scope.$watch('query_params.campaign_id', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.query_params.campaign_id, $scope.filterOptions.filterText);
        }
    }, true);
    $scope.$watch('pagingOptions', function (newVal, oldVal) {
        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.query_params.campaign_id, $scope.filterOptions.filterText);
        }
    }, true);
    $scope.$watch('filterOptions', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.query_params.campaign_id, $scope.filterOptions.filterText);
        }
    }, true);

    $scope.gridOptions = {
        data: 'myData',
        enablePaging: true,
        showFooter: true,
        totalServerItems: 'totalServerItems',
        pagingOptions: $scope.pagingOptions,
        filterOptions: $scope.filterOptions,
        columnDefs: [{ field: 'id_str', displayName: 'Tweet ID', width: "10%"},
                    { field: 'text', displayName: 'Tweet Text', width: "70%" },
        { field: 'screen_name', displayName: 'Screen Name', width: "10%" },
{ field: 'user_id_str', displayName: 'User ID', width: "10%" }],
    };
}]);
