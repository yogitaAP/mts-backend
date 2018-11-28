app.controller('usersController', function($scope, $http) {

    $scope.headingTitle = "User List";
});

app.controller('busController', function($scope, $http) {

    $scope.normalise = function(x) {
        p = (x.toExponential()).toString();
        return Number( p.substr(0,p.indexOf("e")));
    };

    $scope.loadData = function() {
        $http.get('http://localhost:8080//mts/bus/list').then(function(busResponse) {
                $scope.buses = {};
                busResponse.data.forEach(function(bus) {
                    $scope.buses[bus.id] = bus;
                });


                $http.get('http://localhost:8080//mts/bus/stops').
                                then(function(response) {
                                    $scope.stops = response.data;

                                    $http.get('http://localhost:8080//mts/bus/displayinfo').then(function(displayResponse) {
                                        $scope.displayInfo = displayResponse.data;
                                        for(var i=0; i < response.data.length; ++i) {
                                            var stop = response.data[i];
                                            var longitude = stop.location.longitude;
                                            var latitude = stop.location.latitude;

                                            longitude = $scope.normalise(longitude);
                                            latitude = $scope.normalise(latitude);

                                            var top = ((i + 1) * 120) + (longitude * 100);
                                            var left = 300 + (latitude * 100);
                                            stop.location["top"] = top;
                                            stop.location["left"] = left;
                                            stop.busInfo = [];

                                            $scope.displayInfo.forEach(function(displayData, index) {
                                                if(displayData.current_stop == stop.id) {
                                                    displayData["left"] = left;
                                                    displayData["top"] = 50 + (10 * index);
                                                    stop.busInfo.push(displayData);
                                                }
                                            });
                                        }

                                        $scope.stops = response.data;
                                        console.log("updated stop data " + $scope.stops);

                                    });
                                });

            });

    };

    $scope.loadData();




    $scope.moveBus = function() {
        $http.get('http://localhost:8080//mts/bus/move').then(function(moveBusResponse) {
            $scope.loadData();
        });
    }

});

app.controller('uploadController', function($scope, fileUploadService, $http) {

    $scope.uploadFile = function (fileType) {
                var file = $scope.myFile;
                console.log(file);
                var uploadUrl = "http://localhost:8080//mts/files/upload", //Url of webservice/api/server
                    promise = fileUploadService.uploadFileToUrl(file, fileType, uploadUrl);

                promise.then(function (response) {
                    $scope.serverResponse = response;
                    console.log("file uploaded");
                }, function () {
                    $scope.serverResponse = 'An error has occurred';
                })
            };

});

