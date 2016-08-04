define([
    'angular'
], function (angular) {
    'use strict';

    var notificationsModule = angular.module('notifications-module', []);
    notificationsModule.factory('notificationsService', ['$http', '$q', function($http, $q) {
        var connection;
        var defer;
        function onConnect(status)
        {
            if (status === Strophe.Status.CONNECTING) {
                console.log('Strophe is connecting.');
            } else if (status === Strophe.Status.CONNFAIL) {
                console.log('Strophe failed to connect.');
                defer.reject(false);
            } else if (status === Strophe.Status.DISCONNECTING) {
                console.log('Strophe is disconnecting.');
                defer.reject(false);
            } else if (status === Strophe.Status.DISCONNECTED) {
                console.log('Strophe is disconnected.');
                defer.reject(false);
            } else if (status === Strophe.Status.CONNECTED) {
                console.log('Strophe is connected.');
                defer.resolve(true);
            }
        }
        return {
            connect: function (user, pass) {
                connection = new Strophe.Connection('http://ec2-52-43-70-97.us-west-2.compute.amazonaws.com:7070/http-bind/');
                connection.connect(user, pass, onConnect);
            },
            getConnection: function () {
                return connection;
            },
            getNotificationsServiceLoginDetails: function() {
                defer = $q.defer();
                this.connect("admin@ec2-52-43-70-97.us-west-2.compute.amazonaws.com", "admin");
                // $http({
                //     url: "",// url to get username,password,
                //     method: 'get'
                // }).success(function (data) {
                //     this.connect(data.username + "@ec2-52-43-70-97.us-west-2.compute.amazonaws.com", data.pass);
                // }).error(function (data) {
                //     defer.reject(data);
                // });
                return defer.promise;
            }
        };
    }]);

    return notificationsModule;
});