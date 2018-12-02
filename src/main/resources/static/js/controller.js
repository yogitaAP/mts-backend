app.controller('appController', function($scope, fileUploadService, $http) {

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

     $scope.host = "http://localhost:8080";
     $scope.getBusList = $scope.host + "//mts/bus/list";
     $scope.getStops = $scope.host + "//mts/bus/stops";
     $scope.getDisplayInfo = $scope.host + "//mts/bus/displayinfo";
     $scope.getSystemEfficiency = $scope.host + "//mts/systemefficiency/compute";
     $scope.getLogicalTime = $scope.host + '//mts/system/logicaltime';
     $scope.getRoutes = $scope.host + '//mts/system/routes';
     $scope.getMoveBus = $scope.host + '//mts/bus/move';
     $scope.replayBus = $scope.host + '//mts/bus/replay';
     $scope.getRoutes = $scope.host + '//mts/system/routes';
     $scope.refreshSystem = $scope.host + '//mts/system/refresh';
     $scope.uploadFiles = $scope.host + '//mts/files/upload';
     $scope.updateConstants = $scope.host + '//mts/systemefficiency/constants';
     $scope.updateBusInfo = $scope.host + '//mts/bus/updateinfo';


    $scope.normalise = function(x) {
        p = (x.toExponential()).toString();
        return Number( p.substr(0,p.indexOf("e")));
    };

    $scope.loadData = function(isPlot) {
        $http.get($scope.getBusList).then(function(busResponse) {
                $scope.buses = {};
                busResponse.data.forEach(function(bus) {
                    $scope.buses[bus.id] = bus;
                });


                $http.get($scope.getStops).then(function(response) {

                    $scope.stops = response.data;
                    $scope.stopMap = {};

                    $scope.stops.forEach(function(val)  {
                        var randomColorStopIndex = parseInt(val.id);
                        randomColorStopIndex = isNaN(randomColorStopIndex) ? 0 :  (randomColorStopIndex % 12);
                        val['busInfo'] = [];
                        val['color'] = $scope.busColors[randomColorStopIndex];
                        $scope.stopMap[val.id] = val;

                    });

                    $http.get($scope.getDisplayInfo).then(function(displayResponse) {
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

                        $http.get($scope.getSystemEfficiency).then(function(efficiencyresponse) {
                            $scope.efficiency = efficiencyresponse.data;

                            $http.get($scope.getLogicalTime).then(function(timeResponse) {
                               $scope.logicaltime = timeResponse.data.logicalTime;

                               $http.get($scope.getRoutes).then(function(routeReponse) {
                                        $scope.routeMap = {};
                                        $scope.routes = [];
                                        routeReponse.data.forEach(function(routeVal) {
                                            var randomColorRouteIndex = parseInt(routeVal.id);
                                            randomColorRouteIndex = isNaN(randomColorRouteIndex) ? 0 :  (randomColorRouteIndex % 12);
                                            routeVal["color"] = $scope.busColors[randomColorRouteIndex];
                                            $scope.routeMap[routeVal.id] = routeVal;
                                            $scope.routes.push(routeVal);
                                        });

                                        $('#myModal').modal('hide');
                                        isPlot && $scope.plotStops();
                                      });
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

            var stoptext = new createjs.Text(stopData.id, "16px Arial", stopData.color);
            stoptext.x = stop.x + 20;
            stoptext.y = stop.y + 18;

            stage.addChild(stoptext);

            if(stops[i]["busInfo"].length) {
                for(var j=0; j<stops[i]["busInfo"].length; ++j) {
                    var bus = new createjs.Bitmap("/images/bus.png");
                    bus.scaleX = 0.06;
                    bus.scaleY = 0.06;
                    bus.x = stop.x + 50;
                    bus.y = stop.y + (j * 20);

                    stage.addChild(bus);

                    var busId = stops[i]["busInfo"][j]["bus_id"];
                    var nextStop = stops[i]["busInfo"][j]["next_stop"];
                    nextStop = "next Stop " + nextStop;
                    var riders = stops[i]["busInfo"][j]["riders"];
                    riders = "riders " + riders;

                    var color = $scope.buses[busId]["color"];
                    var text = new createjs.Text(busId, "14px Arial", color);
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
        $http.get($scope.getMoveBus).then(function(moveBusResponse) {
            $scope.loadData(true);
        });
    }


    $scope.replay = function() {
        if(!$scope.stops.length)
            return false;
        $http.get($scope.replayBus).then(function(moveBusResponse) {
            $scope.loadData(true);
        });
    }

     $scope.uploadFile = function (fileType) {
            var systemFile = $scope.systemFile;
            var passengerFile = $scope.passengerFile;
            var systemFileType = "systemInfo";
            var passengerFileType = "passengerInfo";

            if(systemFile == undefined) {
                $.notify("Please upload system data file to proceed");
                return;
            } else {
                if(passengerFile == undefined) {
                     $http.get($scope.refreshSystem).then(function(refreshResponse) {
                        var uploadUrl = $scope.uploadFiles, //Url of webservice/api/server
                                        promise = fileUploadService.uploadFileToUrl(systemFile, systemFileType, uploadUrl);

                        promise.then(function () {
                            $.notify("Files uploaded successfully", "success");
                            $scope.loadData(true);
                        }, function () {
                            $scope.loadData(true);
                        });
                    });
                } else {
                    $http.get($scope.refreshSystem).then(function(refreshResponse) {
                        var uploadUrl = $scope.uploadFiles, //Url of webservice/api/server
                                        promise = fileUploadService.uploadFileToUrl(systemFile, systemFileType, uploadUrl);

                        promise.then(function () {

                            var uploadUrl2 = $scope.uploadFiles, //Url of webservice/api/server
                                            promise2 = fileUploadService.uploadFileToUrl(passengerFile, passengerFileType, uploadUrl2);

                            promise2.then(function () {
                                $.notify("Files uploaded successfully", "success");
                                $scope.loadData(true);
                            }, function () {
                                $scope.loadData(true);
                            });
                        }, function () {
                            $scope.loadData(true);
                        });
                    });
                }
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
            $http.post($scope.updateConstants, data).then(function(response) {
                $.notify("System data updated successfully", "success");
                $('#efficiencyModal').modal('hide');

            }, function() {
                $.notify("An error has occurred", "error");
            });
        }
     };


     $scope.updateBus = function(attr) {
        var data = {};

        if($scope.buses[$scope.busId] == null) {
            $.notify("Invalid bus id", 'error');
            return;
        }

        if($scope.passengerCapacity != undefined && ($scope.passengerCapacity < 0 || $scope.passengerCapacity > 100)) {
            $.notify("Invalid passenger capacity", 'error');
            return;
        }

        if($scope.speed != undefined && ($scope.speed < 0 || $scope.speed > 500)) {
            $.notify("Invalid speed", 'error');
            return;
        }



        if($scope.nextStop != undefined && $scope.stopMap[$scope.nextstop] == null) {
            $.notify("Invalid stop id", 'error');
            return;
        }

        if($scope.route != undefined && $scope.routeMap[$scope.route] == null) {
            $.notify("Invalid route id", 'error');
            return;
        }

        if($scope.passengerCapacity != undefined && !isNaN(parseInt($scope.passengerCapacity)))
            data["passenger"] = $scope.passengerCapacity;
        if($scope.busId != undefined && !isNaN(parseInt($scope.busId)))
            data["busId"] = $scope.busId;
        if($scope.speed != undefined && !isNaN(parseInt($scope.speed)))
            data["speed"] = $scope.speed;
        if($scope.route != undefined && !isNaN(parseInt($scope.route)))
            data["routeId"] = $scope.route;
        if($scope.nextstop != undefined && !isNaN(parseInt($scope.nextstop)))
            data["nextStop"] = $scope.nextstop;


        if(Object.keys(data).length) {
            $http.post($scope.updateBusInfo, data).then(function(response) {
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
