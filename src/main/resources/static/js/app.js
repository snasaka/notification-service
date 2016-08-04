angular.module('noteifApp', ['ngRoute', 'com.noteif.userProfile', 'chart.js'])
   .controller('com.noteif.home.controller', ['$http', '$location', '$scope', controller])
   .config(function($routeProvider, $httpProvider){
    console.log("How about here");
    $routeProvider
        .when('/user/:providerId', {
            templateUrl: 'js/userProfile/userProfile.html',
            controller: 'com.noteif.userProfile.controller'
        });
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});

function controller($http, $location, $scope) {
    $http.get("/user").success(function(data) {
        if (data.name) {
            $scope.user = data;
            $scope.authenticated = true;
            $location.path("/user/" + $scope.user.providerId);
        } else {
            $scope.user = "N/A";
            $scope.authenticated = false;
        }
    }).error(function() {
        $scope.user = "N/A";
        $scope.authenticated = false;
    });
    $scope.logout = function() {
        $http.post('logout', {}).success(function() {
            $scope.authenticated = false;
            $location.path("/");
        }).error(function(data) {
            console.log("Logout failed");
            $scope.authenticated = false;
        });
    };
}