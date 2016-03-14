PAViewer.controller('mainControl', ['$scope', '$route', '$location', function($scope, $route, $location) {

    $scope.isInitialized = false;
    $scope.fields = [];
    $scope.minimax = [];

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
                    $scope.fields.push(data);
                    crtFieldIndex++;
                },
                error: function(error) {
                    //console.log("Error @mainControl::Init");
                    hasReachedEnd = true;
                }
            });
        }

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
}]);


