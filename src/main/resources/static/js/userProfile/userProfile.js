angular.module('com.noteif.userProfile', ['com.noteif.applicationsList', 'com.noteif.applicationInfo'])
    .controller('com.noteif.userProfile.controller', [
        '$scope',
        '$routeParams',
        'com.noteif.userProfile.repository',
        userProfileCtrl])
    .service('com.noteif.userProfile.repository', [
        '$http',
        '$q',
        userProfileRepository]);

function userProfileCtrl($scope, $routeParams,userProfileRepository) {
    $scope.infoOrDashboard = 'info';

    $scope.$on('applicationSelected', function(message, args) {
        $scope.selectedApplication = args.application;
    });

    $scope.$on('registerUser', function(message, args) {
        registerUser(args.user);
    });

    $scope.$on('registerApplication', function(message, args) {
        registerApplication();
    });

    getUserProfile();

    function getUserProfile() {
        userProfileRepository.getUserProfile($routeParams.providerId).then(function successCallback(data) {
            if (data) {
                $scope.userProfile = data;
                console.log($scope.userProfile);
            }
        });
    }

    function registerUser(newUser) {
        if(newUser.username) {
            $scope.selectedApplication.xmppUsers.push(newUser);
            userProfileRepository.saveApplication($scope.selectedApplication).then(function successCallback(data) {
                $scope.selectedApplication = data;
            });
        }
    }

    function registerApplication() {
        $scope.userProfile.applications.push($scope.selectedApplication);
        userProfileRepository.saveUserProfile($routeParams.providerId, $scope.userProfile).then(function successCallback(data) {
            $scope.userProfile = data;
            $scope.selectedApplication = _.findWhere($scope.userProfile.applications, {name: $scope.selectedApplication.name});
            $scope.$broadcast('applicationRegistered', {
                application: $scope.selectedApplication
            });
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
