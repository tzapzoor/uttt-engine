PAViewer.config( function( $routeProvider, $locationProvider ) {

    $routeProvider.when('/', {
        templateUrl: 'partials/field.html',
        controller: 'mainControl'
    });

    $routeProvider.when('/minimax', {
        templateUrl: 'partials/minimax.html',
        controller: 'mainControl'
    });

    $routeProvider .otherwise({
        redirectTo: '/'
    });

});