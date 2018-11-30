app.controller('usersController', function($scope, $http) {

    $scope.headingTitle = "User List";
});

app.controller('busController', function($scope, fileUploadService, $http) {

     $scope.busColors =
     ["#81bbe4",
     "#869bcc",
     "#d69dce",
     "#bd78a2",
     "#c66561",
     "#e87881",
     "#f2ad47",
     "#b1922e",
     "#099f34",
     "#1485c6",
     "#ffc108",
     "#30d6c5",
     "#444444"]

    $scope.normalise = function(x) {
        p = (x.toExponential()).toString();
        return Number( p.substr(0,p.indexOf("e")));
    };

    $scope.loadData = function(isPlot) {
        $http.get('http://localhost:8080//mts/bus/list').then(function(busResponse) {
                $scope.buses = {};
                busResponse.data.forEach(function(bus) {
                    $scope.buses[bus.id] = bus;
                });


                $http.get('http://localhost:8080//mts/bus/stops').then(function(response) {

                    $scope.stops = response.data;
                    $scope.stopMap = {};

                    $scope.stops.forEach(function(val)  {
                        var randomColorStopIndex = parseInt(val.id);
                        randomColorStopIndex = isNaN(randomColorStopIndex) ? 0 :  (randomColorStopIndex % 12);
                        val['busInfo'] = [];
                        val['color'] = $scope.busColors[randomColorStopIndex];
                        $scope.stopMap[val.id] = val;

                    });

                    $http.get('http://localhost:8080//mts/bus/displayinfo').then(function(displayResponse) {
                        $scope.displayInfo = displayResponse.data;
                            $scope.displayInfo.forEach(function(display) {
                                    var busUpdatedData = $scope.buses[display.bus_id];
                                    busUpdatedData["display"] = display;
                                    var randomColorIndex = parseInt(display.bus_id);
                                    randomColorIndex = isNaN(randomColorIndex) ? 0 :  (randomColorIndex % 12);
                                    busUpdatedData["color"] = $scope.busColors[randomColorIndex];
                                    $scope.buses[display.bus_id] = busUpdatedData;
                                    $scope.stopMap[display.current_stop]["busInfo"].push(display);
                            });



                        $scope.stops = Object.values($scope.stopMap);

                        $http.get('http://localhost:8080//mts/systemefficiency/compute').then(function(efficiencyresponse) {
                            $scope.efficiency = efficiencyresponse.data;

                            $http.get('http://localhost:8080/mts/system/logicaltime').then(function(timeResponse) {
                               $scope.logicaltime = timeResponse.data.logicalTime;

                               $('#myModal').modal('hide');
                               isPlot && $scope.plotStops();
                            });


                        });
                    });
                });

        });
    }


    $scope.loadData(true);

    $scope.plotStops = function() {

        if(!$scope.stops.length) {
            $('#myModal').modal('show');
        }

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
        stage.clear();
        for(var i=0; i<stops.length; ++i) {
            var stopData = stops[i];
            var stop = new createjs.Bitmap("/images/bus_stop.png");
            stop.scaleX = 0.1;
            stop.scaleY = 0.1;

            stop.y = ((stopData.location.latitude - minLat) * 350) / xRange;
            stop.x = ((stopData.location.longitude - minLong) * 350) / yRange;
            stage.addChild(stop);

            if(stops[i]["busInfo"].length) {
                for(var j=0; j<stops[i]["busInfo"].length; ++j) {
                    var bus = new createjs.Bitmap("/images/bus.png");
                    bus.scaleX = 0.06;
                    bus.scaleY = 0.06;
                    bus.x = stop.x + 70;
                    bus.y = stop.y + (j * 20);

                    stage.addChild(bus);

                    var busId = stops[i]["busInfo"][j]["bus_id"];
                    var nextStop = stops[i]["busInfo"][j]["next_stop"];
                    nextStop = "next Stop " + nextStop;
                    var riders = stops[i]["busInfo"][j]["riders"];
                    riders = "riders " + riders;

                    var color = $scope.buses[busId]["color"];
                    var text = new createjs.Text(busId, "12px Arial", color);
                    text.x = bus.x + 30;
                    text.y = bus.y + 10;

                    stage.addChild(text);
                }
            }
        }

        stage.update();

    }

    $scope.moveBus = function() {
        if(!$scope.stops.length)
            return false;
        $http.get('http://localhost:8080//mts/bus/move').then(function(moveBusResponse) {
            $scope.loadData(true);
        });
    }


    $scope.replay = function() {
        if(!$scope.stops.length)
            return false;
        $http.get('http://localhost:8080//mts/bus/replay').then(function(moveBusResponse) {
            $scope.loadData(true);
        });
    }

     $scope.uploadFile = function (fileType) {
            var file = $scope.myFile;
            $scope.fileType = fileType;
            console.log(file);

            if(fileType == "systemInfo" && $scope.stops.length) {
                $http.get('http://localhost:8080//mts/system/refresh').then(function(refreshResponse) {
                            var uploadUrl = "http://localhost:8080//mts/files/upload", //Url of webservice/api/server
                                            promise = fileUploadService.uploadFileToUrl(file, fileType, uploadUrl);

                                        promise.then(function () {
                                            console.log("file uploaded");
                                            $.notify("Files uploaded successfully", "success");
                                            $scope.loadData(true);
                                        }, function () {
                                            $.notify("An error has occurred", "error");
                                            $scope.loadData(true);
                                        })
                        });

            } else {
                var uploadUrl = "http://localhost:8080//mts/files/upload", //Url of webservice/api/server
                                promise = fileUploadService.uploadFileToUrl(file, fileType, uploadUrl);

                            promise.then(function () {
                                console.log("file uploaded");
                                $.notify("Files uploaded successfully", "success");
                                $scope.loadData(true);
                            }, function () {
                                $.notify("An error has occurred", "error");
                                $scope.loadData(true);
                            })

            }


     };

     $scope.updateEfficiency = function() {
         var data = {
            "kSpeed": $scope.kSpeed,
            "kCapacity": $scope.kCapacity,
            "kWaiting": $scope.kWaiting,
            "kCombined": $scope.kCombined,
            "kBuses": $scope.kBuses
        }


        if(Object.keys(data).length) {
            $http.post('http://localhost:8080//mts/systemefficiency/constants', data).then(function(response) {
                $.notify("System data updated successfully", "success");
                $('#efficiencyModal').modal('hide');

            }, function() {
                $.notify("An error has occurred", "error");
            });
        }
     };


     $scope.updateBus = function(attr) {
        var data = {};

        if($scope.passengerCapacity < 0 || $scope.passengerCapacity > 100) {
            $.notify("Invalid passenger capacity", 'error');
            return;
        }
        if($scope.speed < 0 || $scope.speed > 500) {
            $.notify("Invalid passenger capacity", 'error');
            return;
        }
        if($scope.buses[$scope.busId] == null) {
            $.notify("Invalid bus id", 'error');
            return;
        }
        if($scope.stopMap[$scope.nextstop] == null) {
            $.notify("Invalid stop id", 'error');
            return;
        }

        else {
            data = {
                        "passenger": $scope.passengerCapacity,
                        "busId": $scope.busId,
                        "speed": $scope.speed,
                        "routeId": $scope.route,
                        "nextStop": $scope.nextstop
                    }
        }

        if(Object.keys(data).length) {
            $http.post('http://localhost:8080//mts/bus/updateinfo', data).then(function(response) {
                $.notify("Bus data updated successfully", "success");
                $('#busModal').modal('hide');
            }, function() {
                $.notify("An error has occurred", "error");
            });
        }
     };

     $('#myModal').on('hide.bs.modal', function() {

       if(!$scope.stops.length)
         {
            $.notify("Please upload system data files to proceed", "error");
            return false;
         }
     });


});
