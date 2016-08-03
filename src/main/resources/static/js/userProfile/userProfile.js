angular.module('com.noteif.userProfile', [])
    .controller('com.noteif.userProfile.controller', [
        '$scope',
        '$routeParams',
        'com.noteif.userProfile.repository',
        userProfileCtrl])
    .service('com.noteif.userProfile.repository', [
        '$http',
        '$q',
        userProfileRepository]);

function userProfileCtrl($scope, $routeParams, userProfileRepository) {
    $scope.selectedApplication = {isNew:true};
    $scope.newUser = {};

    $scope.applicationSelected = function(application) {
        $scope.selectedApplication = application;
    };

    $scope.registerApplication = function() {
        $scope.userProfile.applications.push($scope.selectedApplication);
        userProfileRepository.saveUserProfile($routeParams.providerId, $scope.userProfile).then(function successCallback(data) {
            $scope.userProfile = data;
            $scope.selectedApplication = _.findWhere($scope.userProfile.applications, {name: $scope.selectedApplication.name});
            console.log($scope.userProfile);
        });
    };

    $scope.registerUser = function() {
        if($scope.newUser.username) {
            $scope.selectedApplication.xmppUsers.push($scope.newUser);
            userProfileRepository.saveApplication($scope.selectedApplication).then(function successCallback(data) {
                $scope.selectedApplication = data;
                $scope.newUser = {};
            });
        }
    };

    getUserProfile();

    function getUserProfile() {
        userProfileRepository.getUserProfile($routeParams.providerId).then(function successCallback(data) {
            if (data) {
                $scope.userProfile = data;
                console.log($scope.userProfile);
            }
        });
    }
}

function userProfileRepository($http, $q) {
    this.getUserProfile = function(providerId) {
        var defer = $q.defer();

        var config = {
            url: "/users/" + providerId + "/profile",
            method: "GET"
        };

        $http(config)
            .then(function successCallback(response) {
                defer.resolve(response.data);
            }, function errorCallback(response) {
                defer.reject(response);
            });

        return defer.promise;
    };

    this.saveUserProfile = function(providerId, userProfile) {
        var defer = $q.defer();

        var config = {
            url: "/users/" + providerId + "/profile",
            method: "POST",
            data: userProfile
        };

        $http(config)
            .then(function successCallback(response) {
                defer.resolve(response.data);
            }, function errorCallback(response) {
                defer.reject(response);
            });

        return defer.promise;
    };

    this.saveApplication = function(application) {
        var defer = $q.defer();

        var config = {
            url: "/users/applications",
            method: "POST",
            data: application
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
