Viewer.controller('mainControl', ['$scope', '$rootScope','$route', '$location', function($scope, $rootScope, $route, $location) {

    $scope.isInitialized = false;
    $rootScope.fields = [];
    $rootScope.thinkingTime = [];
    $rootScope.currentMove = 0;
    $rootScope.field = {};

    $scope.minimax = [];

    $scope.Init = function(){
        crtFieldIndex = 0;
        hasReachedEnd = false;

        //fetch fields
        while(hasReachedEnd === false) {
            $.ajax({
                url: "data/fields/field-" + crtFieldIndex + '.json',
                beforeSend: function(xhr){
                    if (xhr.overrideMimeType)
                    {
                      xhr.overrideMimeType("application/json");
                    }
                },
                dataType : 'json',
                async: false,
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

        //fetch thinking time for each move
        $.ajax({
            url: "data/thinking/thinking.json",
            beforeSend: function(xhr){
                if (xhr.overrideMimeType)
                {
                  xhr.overrideMimeType("application/json");
                }
            },
            dataType: 'json',
            async: false,
            success: function(data, error) {
                $rootScope.thinkingTime = data;
            },
            error: function(error) {
                console.log("Could not load thinking time data!");
            }
        });

        //fetch minimax trees
        crtFieldIndex = 0;
        hasReachedEnd = false;
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

    $scope.keyDown = function(keyCode) {
        if ($scope.fields.length > 0) {
            switch (keyCode) {
                case 190:
                    $scope.currentMove = ($scope.currentMove < $scope.fields.length - 1) ?
                                            $scope.currentMove + 1 : $scope.currentMove;
                    break;
                case 188:
                    $scope.currentMove = ($scope.currentMove > 0) ?
                            $scope.currentMove - 1 : $scope.currentMove;
                    break;
            }
            $scope.field = $scope.fields[$scope.currentMove];
        }
    };
}]);
