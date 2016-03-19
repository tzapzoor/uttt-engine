PAViewer.controller('mainControl', ['$scope', '$rootScope','$route', '$location', function($scope, $rootScope, $route, $location) {

    $scope.isInitialized = false;
    $rootScope.fields = [];
    $scope.minimax = [];
    $rootScope.currentMove = 0;
    $rootScope.field = {};

    $scope.Init = function(){
        crtFieldIndex = 0;
        hasReachedEnd = false;

        //fetch fields
        while(hasReachedEnd === false) {
            $.ajax({
                dataType : 'json',
                async: false,
                url: "data/fields/field-" + crtFieldIndex + '.json',
                success: function(data, error) {
                    $rootScope.fields.push(data);
                    crtFieldIndex++;
                },
                error: function(error) {
                    //console.log("Error @mainControl::Init");
                    hasReachedEnd = true;
                }
            });
        }
        $rootScope.field = $rootScope.fields[$rootScope.currentMove];

        crtFieldIndex = 0;
        hasReachedEnd = false;
        //fetch minimax trees
        while(hasReachedEnd === false) {
            $.ajax({
                dataType : 'json',
                async: false,
                url: "data/minimax/minimax-" + crtFieldIndex + '.json',
                success: function(data, error) {
                    $scope.minimax.push(data);
                    crtFieldIndex++;
                },
                error: function(error) {
                    //console.log("Error @mainControl::Init");
                    hasReachedEnd = true;
                }
            });
        }
        $scope.isInitialized = true;
    };

    $scope.current_page = function() {
        return $location.path().slice(1);
    };

    $scope.changePage = function(page) {
        window.location.href="#" + page;
    };

    $scope.refresh = function() {
        window.location.href= "/";
    };

    $scope.toInt = function(number) {
        return parseInt(number);
    };

    $scope.getNumberList = function(num) {
        return new Array(num);
    }

    $scope.keyup = function(keyCode) {
        if ($scope.fields.length > 0) {
            switch (keyCode) {
                case 190:
                    $scope.currentMove = ($scope.currentMove + 1) % $scope.fields.length;
                    break;
                case 188:
                    $scope.currentMove = ($scope.currentMove - 1) % $scope.fields.length;
                    $scope.currentMove = ($scope.currentMove < 0) ? 0 : $scope.currentMove;
                    break;
            }
            $scope.field = $scope.fields[$scope.currentMove];
        }
    };

}]);
