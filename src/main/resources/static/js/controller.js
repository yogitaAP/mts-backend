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

                                        }


                                    });
                                });

            });

    };

    $scope.loadData();

    $scope.plotStops = function() {
        var stops = $scope.stops;
        var stopMap = {};

        stops.forEach(function(eachStop) {
            stopMap[eachStop.id] = eachStop;
        });

        stops = Object.values(stopMap);


        var latitudes = [];
        var longitudes = [];

        for(var i=0; i< stops.length; ++i) {
            var location = stops[i].location;
            latitudes.push(location.latitude);
            longitudes.push(location.longitude);
        }

        var minLat = Math.min.apply(null, latitudes);
        var maxLat = Math.max.apply(null, latitudes);
        var minLong = Math.min.apply(null, longitudes);
        var maxLong = Math.max.apply(null, longitudes);
        xRange = maxLat - minLat;
        yRange = maxLong - minLong;


        var stage = new createjs.Stage("demoCanvas");
        for(var i=0; i<stops.length; ++i) {
            var stopData = stops[i];
            var stop = new createjs.Bitmap("/images/bus_stop.png");
            stop.scaleX = 0.1;
            stop.scaleY = 0.1;

            stop.y = ((stopData.location.latitude - minLat) * 350) / xRange;
            stop.x = ((stopData.location.longitude - minLong) * 350) / yRange;

            console.log(stopData.id + " -- x --" + stop.x);
            console.log(stopData.id + " -- y --" + stop.y);
            stage.addChild(stop);

        }

        stage.update();

    }




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

