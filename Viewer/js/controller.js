Viewer.controller('mainControl', ['$scope', '$rootScope','$route', '$location', function($scope, $rootScope, $route, $location) {

    $scope.isInitialized = false;
    $rootScope.fields = [];
    $rootScope.thinkingTimeData = [];
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
                //convert data to plot.ly format
                var player1Trace = {'x': [],
                                    'y': [],
                                    'type': 'scatter',
                                    'mode': 'lines+markers',
                                    'name': "player1",
                                    'line': {
                                        'color': '#FF5552'
                                    }};
                var player2Trace = {'x': [],
                                    'y': [],
                                    'type': 'scatter',
                                    'mode': 'lines+markers',
                                    'name': 'player2',
                                    'line': {
                                        'color': '#69A1FE'
                                    }};

                //populate player traces
                for (var i = 2; i < data.length; i++) {
                    if (data[i]['mPlayerId'] == 1) {
                        player1Trace['x'].push(i);
                        player1Trace['y'].push(data[i]['mThinkingTime']);
                    } else {
                        player2Trace['x'].push(i);
                        player2Trace['y'].push(data[i]['mThinkingTime']);
                    }
                }

                $rootScope.thinkingTimeData.push(player1Trace)
                $rootScope.thinkingTimeData.push(player2Trace);
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

    $scope.drawPlot = function() {
        console.log($rootScope.thinkingTimeData);
        var layout = {
          xaxis: {
            title: 'Move',
            showgrid: true,
            zeroline: true,
            zerolinecolor: 'white'
          },
          yaxis: {
            title: 'time (ms)',
            showline: false,
            zeroline: true,
            zerolinecolor: 'white'
          },
          paper_bgcolor: 'transparent',
          plot_bgcolor: '#1F2225',
          font: {
              color: 'white'
          }

        };
        Plotly.newPlot('thinking-plot', $rootScope.thinkingTimeData, layout);
    };

}]);
