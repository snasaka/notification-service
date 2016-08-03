angular.module('com.noteif.login', [])
    .controller('com.noteif.login.controller', ['$scope', controller]);

function controller($scope) {
    $scope.userInfo = {};
    $scope.login = function() {
      console.log($scope.userInfo);
    };
}

