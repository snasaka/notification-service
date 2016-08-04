angular.module('com.noteif.applicationDashboard', [])
    .directive('applicationDashboard', applicationDashboardDirective)
    .controller('com.noteif.applicationDashboard.controller', [
        '$scope',
        'com.noteif.applicationDashboard.repository',
        applicationDashboardCtrl])
    .service('com.noteif.applicationDashboard.repository', [
        '$http',
        '$q',
        applicationDashboardRepository]);

function applicationDashboardDirective() {
    return {
        restrict: 'E',
        templateUrl: 'js/applicationDashboard/applicationDashboard.html',
        controller: 'com.noteif.applicationDashboard.controller',
        service: 'com.noteif.applicationDashboard.repository',
        scope: {
            application:'='
        }
    };
}
function applicationDashboardCtrl($scope, applicationDashboardRepository) {

    $scope.loadChart = function(selectedTimeline) {
        applicationDashboardRepository.getApplicationDataPoints($scope.application.id, selectedTimeline).then(function (data) {
            $scope.labels = _.map(_.keys(data), function (num) {
                return _.indexOf(_.keys(data), num) % 4 == 0 ? num : "";
            });
            $scope.data = [_.values(data)];
        });
    };

    $scope.labels = [];
    $scope.data = [];

    $scope.series = ['No of messages'];
    $scope.datasetOverride = [{ yAxisID: 'count' }];
    $scope.options = {
        scales: {
            yAxes: [
                {
                    id: 'count',
                    type: 'linear',
                    display: true,
                    position: 'left'
                }
            ]
        }
    };

    if(!$scope.application.isNew) {
        $scope.loadChart(15);
    }
}

function applicationDashboardRepository($http, $q) {

    this.getApplicationDataPoints = function(applicationId, timeline) {
        var defer = $q.defer();

        var config = {
            url: "/data-points/" + applicationId,
            method: "GET",
            params: {
                timeline: timeline
            }
        };

        $http(config)
            .then(function successCallback(response) {
                defer.resolve(response.data);
            }, function errorCallback(response) {
                defer.reject(response);
            });

        return defer.promise;
    };
}
