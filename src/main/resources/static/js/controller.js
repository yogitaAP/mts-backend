app.controller('usersController', function($scope, $http) {
    $http.get('http://localhost:8080/mts/bus/list').
            then(function(response) {
                $scope.buses = response;
                console.log(response);
            });
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});

app.controller('uploadController', function($scope, fileUploadService) {

    $scope.uploadFile = function () {
                var file = $scope.myFile;
                console.log(file);
                var uploadUrl = "http://localhost:8080//mts/files/upload", //Url of webservice/api/server
                    promise = fileUploadService.uploadFileToUrl(file, uploadUrl);

                promise.then(function (response) {
                    $scope.serverResponse = response;
                    console.log("file uploaded");
                }, function () {
                    $scope.serverResponse = 'An error has occurred';
                })
            };

});

