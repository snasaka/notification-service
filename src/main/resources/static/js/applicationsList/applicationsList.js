angular.module('com.noteif.applicationsList', [])
    .directive('applicationsList', applicationsListDirective)
    .controller('com.noteif.applicationsList.controller', [
        '$scope',
        applicationsListCtrl]);

function applicationsListDirective() {
    return {
        restrict: 'E',
        templateUrl: 'js/applicationsList/applicationsList.html',
        controller: 'com.noteif.applicationsList.controller',
        scope: {
            applications:'='
        }
    };
}
function applicationsListCtrl($scope) {
    $scope.$on('applicationRegistered', function(message, args) {
        $scope.selectedApplication = args.application;
    });

    $scope.applicationSelected = function(application) {
        $scope.selectedApplication = application;
        $scope.$emit('applicationSelected', {application: application});
    };

    $scope.applicationSelected({isNew:true});
}

