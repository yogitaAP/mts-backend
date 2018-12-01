var app = angular.module('app', ['ngRoute','ngResource']);

app.directive('demoFileModel', function ($parse) {
    return {
        restrict: 'A', //the directive can be used as an attribute only

        /*
         link is a function that defines functionality of directive
         scope: scope associated with the element
         element: element on which this directive used
         attrs: key value pair of element attributes
         */
        link: function (scope, element, attrs) {
            var model = $parse(attrs.demoFileModel),
                modelSetter = model.assign; //define a setter for demoFileModel

            //Bind change event on the element
            element.bind('change', function () {
                //Call apply on scope, it checks for value changes and reflect them on UI
                scope.$apply(function () {
                    //set the model value
                    if(element[0].files != null)
                        modelSetter(scope, element[0].files[0]);
                    else
                        modelSetter(scope, element[0].value);
                });
            });
        }
    };
});

app.service('fileUploadService', function ($http, $q) {

        this.uploadFileToUrl = function (file, fileType, uploadUrl) {
            //FormData, object of key/value pair for form fields and values
            var fileFormData = new FormData();
            fileFormData.append('file', file);
            fileFormData.append('fileType', fileType);

            var deffered = $q.defer();
            $http.post(uploadUrl, fileFormData, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
                //'multipart/form-data;boundary="--ABC123Boundary"'

            }).success(function (response) {
                deffered.resolve(response);

            }).error(function (response) {
                deffered.reject(response);
            });

            return deffered.promise;
        }
    });

app.config(function($routeProvider){
    $routeProvider
        .when('/app',{
            templateUrl: '/views/app.html',
            controller: 'appController'
        })
        .otherwise(
            { redirectTo: '/'}
        );
});

