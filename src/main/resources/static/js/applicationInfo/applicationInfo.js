angular.module('com.noteif.applicationInfo', [])
    .directive('applicationInfo', applicationInfoDirective)
    .controller('com.noteif.applicationInfo.controller', [
        '$scope',
        applicationInfoCtrl]);

function applicationInfoDirective() {
    return {
        restrict: 'E',
        templateUrl: 'js/applicationInfo/applicationInfo.html',
        controller: 'com.noteif.applicationInfo.controller',
        scope: {
            application:'='
        }
    };
}
function applicationInfoCtrl($scope) {
    $scope.newUser = {};

    $scope.registerUser = function() {
        $scope.$emit('registerUser', {
            user: $scope.newUser
        });
        $scope.newUser = {};
    };

    $scope.registerApplication = function() {
        $scope.$emit('registerApplication');
    };
}
