app.controller('usersController', function($scope, $http) {
    $http.get('http://localhost:8080/mts/bus/list').
            then(function(response) {
                $scope.buses = response;
            });
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});

