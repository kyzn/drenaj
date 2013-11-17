'use strict';

/* Directives */


angular.module('direnaj.directives').
  directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }]).
  directive('d3Lines', ['d3', function(d3) {
    return {
      restrict: 'EA',
      // directive code
      scope: {
          data: "="
      },
      link: function(scope, iElement, iAttrs) {

          var margin = {top: 20, right: 20, bottom: 30, left: 50},
                width = 960 - margin.left - margin.right,
                height = 500 - margin.top - margin.bottom;

          var svg = d3.select(iElement[0])
              .append("svg")
//              .attr("width", "100%")
                 .attr("width", width + margin.left + margin.right)
                 .attr("height", height + margin.top + margin.bottom)
              .append("g")
              .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        // on window resize, re-render d3 canvas
        window.onresize = function() {
            return scope.$apply();
        };

        scope.$watch(function(){
            return angular.element(window)[0].innerWidth;
        }, function(){
            return scope.render(scope.data);
        }
        );

        console.log(scope.data);

        // watch for data changes and re-render
        scope.$watch('data', function(newVals, oldVals) {
            return scope.render(newVals);
        }, true);

        // define render function
        scope.render = function(input_map){
            svg.selectAll("*").remove();
            console.log('render 1 ' + input_map);
            if (!input_map) {
                return;
            }
            console.log('render length ' + input_map.length);

            input_map = d3.map(input_map);
            console.log('render 2' + input_map);
            console.log(input_map);

            var parseHourMinute = d3.time.format("%H:%M").parse;

            var data = [];

            var keys = input_map.keys().sort();
            for (var j = 0; j < keys.length; j++) {
              var key = keys[j];
              var x_value = "00";
              if (key.length == 2) {
                x_value = key + ":00";
              } else if (key.length == 4) {
                var tmp = Math.floor(key/60);
                var mins = (key-tmp*60);
                console.log(tmp);
                if (tmp < 10) {
                  x_value =  "0"+tmp+":"+mins;
                } else {
                  x_value =  tmp+":"+mins;
                }
              } else {
                x_value = ''+j;
              }
              data = data.concat({'x': parseHourMinute(x_value), 'y': input_map.get(key)});
            }

            //var x = d3.scale.linear()
            //    .range([0, width]);
            var x = d3.time.scale()
                .range([0, width]);

            var y = d3.scale.linear()
                .range([height, 0]);

            var xAxis = d3.svg.axis()
                .scale(x)
                .orient("bottom");
//                .tickFormat(function(d) { return d+":00"; });

            var yAxis = d3.svg.axis()
                .scale(y)
                .orient("left");

            var line = d3.svg.line()
                .x(function(d) { return x(d.x); })
                .y(function(d) { return y(d.y); });

            x.domain(d3.extent(data, function(d) { return d.x; }));
            y.domain(d3.extent(data, function(d) { return d.y; }));

            svg.append("path")
                .datum(data)
                .attr("class", "line")
                .attr("d", line);

            svg.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);

            svg.append("g")
                .attr("class", "y axis")
                .call(yAxis)
                .append("text")
                .attr("transform", "rotate(-90)")
                .attr("y", 6)
                .attr("dy", ".71em")
                .style("text-anchor", "end")
                .text("Frequency");
            //});

        };
            }
            }
  }]).
  directive('d3Bars', ['d3', function(d3) {
    return {
      restrict: 'EA',
      // directive code
      scope: {
          data: "=",
          x_axis_time_based: "=timeAxis"
      },
      link: function(scope, iElement, iAttrs) {
          var number_of_days_from_1_Jan_0000_to_1_Jan_1970 = 719529.0;

          var drnj_time2epoch = function (dt) {
              return (dt-number_of_days_from_1_Jan_0000_to_1_Jan_1970)*24.0*60*60*1000;         }

          var margin = {top: 20, right: 20, bottom: 30, left: 50},
                width = 960 - margin.left - margin.right,
                height = 500 - margin.top - margin.bottom;

          var svg = d3.select(iElement[0])
              .append("svg")
              .attr("width", width + margin.left + margin.right)
              .attr("height", height + margin.top + margin.bottom)
              .append("g")
              .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

          // on window resize, re-render d3 canvas
          window.onresize = function() {
              return scope.$apply();
          };

          scope.$watch(function(){
              return angular.element(window)[0].innerWidth;
          }, function(){
              return scope.render(scope.data);
          });

          console.log(scope.data);

          // watch for data changes and re-render
          scope.$watch('data', function(newVals, oldVals) {
              return scope.render(newVals);
          }, true);

          // define render function
          scope.render = function(input_map){

              svg.selectAll("*").remove();
              console.log('render bars 1 ' + input_map);
              if (!input_map) {
                  return;
              }
              console.log(input_map);

              var formatCount = d3.format(",.0f");
              var timeFormat = d3.time.format("%Y-%m-%d");

              var data = [];
              var bins = input_map.bins;
              for (var j = 0; j < input_map.data.length; j++) {
                  data = data.concat({x: input_map.bins[j], y: input_map.data[j], dx: (input_map.bins[j+1]-input_map.bins[j])})
              }
              // var data = d3.layout.histogram()
              //     .bins(scope.data.bins)
              //     (scope.data.data);

              var x = d3.scale.linear()
                  .domain([0, d3.max(data, function(d) { return d.x; })-d3.min(data, function(d) { return d.x; })])
                  .range([0, width]);

              var x_axis_scale = d3.scale.linear()
                  .domain([d3.min(data, function(d) { return d.x; }), d3.max(data, function(d) { return d.x; })])
                  .range([0, width]);

              var y = d3.scale.linear()
                  .domain([0, d3.max(data, function(d) { return d.y; })])
                  .range([height, 0]);

              var xAxis = d3.svg.axis()
                  .scale(x_axis_scale)
                  .orient("bottom");

              if (scope.x_axis_time_based) {
                  console.log('axis is time based');
                  xAxis.tickFormat(function (d) {
                        var date = new Date(drnj_time2epoch(d));
                        return timeFormat(date);
                  });
              }

              var bar = svg.selectAll(".bar")
                  .data(data)
                  .enter().append("g")
                  .attr("class", "bar")
                  .attr("transform", function(d) { return "translate(" + x_axis_scale(d.x) + "," + y(d.y) + ")"; });

              bar.append("rect")
                  .attr("x", 1)
                  .attr("width", x(data[0].dx))
                  .attr("height", function(d) { return height - y(d.y); });

              bar.append("text")
                  .attr("dy", ".75em")
                  .attr("y", 6)
                  .attr("x", x(data[0].dx) / 2)
                  .attr("text-anchor", "middle")
                  .text(function(d) { return formatCount(d.y); });

              bar.append("svg:title")
                  .text(function(d) {
                      if (scope.x_axis_time_based) {
                          return timeFormat(new Date(drnj_time2epoch(d.x)));
                      } else {
                          return d.x;
                      }
                  });

              svg.append("g")
                  .attr("class", "x axis")
                  .attr("transform", "translate(0," + height + ")")
                  .call(xAxis);

              // input_map = d3.map(input_map);
              // console.log('render bars 2' + input_map);
              // console.log(input_map);

              // var parseHourMinute = d3.time.format("%H:%M").parse;

              // var data = [];

              // var keys = input_map.keys().sort();
              // for (var j = 0; j < keys.length; j++) {
              //   var key = keys[j];
              //   var x_value = "00";
              //   if (key.length == 2) {
              //     x_value = key + ":00";
              //   } else if (key.length == 4) {
              //     var tmp = Math.floor(key/60);
              //     var mins = (key-tmp*60);
              //     console.log(tmp);
              //     if (tmp < 10) {
              //       x_value =  "0"+tmp+":"+mins;
              //     } else {
              //       x_value =  tmp+":"+mins;
              //     }
              //   } else {
              //     x_value = ''+j;
              //   }
              //   data = data.concat({'x': parseHourMinute(x_value), 'y': input_map.get(key)});
              // }

              // //var x = d3.scale.linear()
              // //    .range([0, width]);
              // var x = d3.time.scale()
              //     .range([0, width]);

              // var y = d3.scale.linear()
              //     .range([height, 0]);

              // var xAxis = d3.svg.axis()
              //     .scale(x)
              //     .orient("bottom");
//            //       .tickFormat(function(d) { return d+":00"; });

              // var yAxis = d3.svg.axis()
              //     .scale(y)
              //     .orient("left");

              // var line = d3.svg.line()
              //     .x(function(d) { return x(d.x); })
              //     .y(function(d) { return y(d.y); });

              // x.domain(d3.extent(data, function(d) { return d.x; }));
              // y.domain(d3.extent(data, function(d) { return d.y; }));

              // svg.append("path")
              //     .datum(data)
              //     .attr("class", "line")
              //     .attr("d", line);

              // svg.append("g")
              //     .attr("class", "x axis")
              //     .attr("transform", "translate(0," + height + ")")
              //     .call(xAxis);

              // svg.append("g")
              //     .attr("class", "y axis")
              //     .call(yAxis)
              //     .append("text")
              //     .attr("transform", "rotate(-90)")
              //     .attr("y", 6)
              //     .attr("dy", ".71em")
              //     .style("text-anchor", "end")
              //     .text("Frequency");
              // //});

          };
        }
      }
  }]);

//             var svg = d3.select("body").append("svg")
//                 .attr("width", width + margin.left + margin.right)
//                 .attr("height", height + margin.top + margin.bottom)
//                 .append("g")
//                 .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            //d3.tsv("data.tsv", function(error, data) {
//            data.forEach(function(key, value) {
//                d.date = parseDate(d.date);
//                d.close = +d.close;
//            });


//            // your changing d3 code here
//            // remove all previous items before render
//            svg.selectAll("*").remove();
//
//            // setup variables
//            var width, height, max;
//            width = d3.select(iElement[0])[0][0].offsetWidth - 20;
//            // 20 is for margins and can be changed
//            height = data.length * 35;
//            // 35 = 30(bar height) + 5(margin between bars)
//            max = 98;
//            // this can also be found dynamically when the data is not static
//            // max = Math.max.apply(Math, _.map(data, ((val)-> val.count)))
//
//            // set the height based on the calculations above
//            svg.attr('height', height);
//
//            //create the rectangles for the bar chart
//            svg.selectAll("rect")
//            .data(data)
//            .enter()
//                .append("rect")
//                .attr("height", 30) // height of each bar
//                .attr("width", 0) // initial width of 0 for transition
//                .attr("x", 10) // half of the 20 side margin specified above
//                .attr("y", function(d, i){return i * 35;}) // height + margin between bars
//                .transition()
//                .duration(1000) // time of duration
//                .attr("width", function(d){return d.score/(max/width);}); // width based on scale
