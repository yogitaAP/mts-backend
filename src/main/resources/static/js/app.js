var app = angular.module('app', ['ngRoute','ngResource']);
app.config(function($routeProvider){
    $routeProvider
        .when('/buses',{
            templateUrl: '/views/buses.html',
            controller: 'busController'
        })
        .when('/stops',{
            templateUrl: '/views/stops.html',
            controller: 'stopController'
        })
        .when('/routes',{
                    templateUrl: '/views/routes.html',
                    controller: 'routeController'
                })
        .when('/efficiency',{
                    templateUrl: '/views/efficiency.html',
                    controller: 'efficiencyController'
                })
        .when('/info',{
                    templateUrl: '/views/info.html',
                    controller: 'infoController'
                })
        .otherwise(
            { redirectTo: '/'}
        );
});

