var app = angular.module('notifApp', ['ngRoute', 'com.noteif.login']);
app.config(function($routeProvider){
    $routeProvider
        .when('/login', {
            templateUrl: 'js/login/login.html',
            controller: 'com.noteif.login.controller'
        })
        .when('/user/:username', {
            templateUrl: 'js/registerProduct/registerProduct.html',
            controller: 'com.noteif.registerProduct'
        })
        .otherwise({redirectTo: '/'});
});
app.run(['$route', function() {}]);
