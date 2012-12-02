var data; // loaded asynchronously

var noData = 'rgb(255,255,255)';

var projection = d3.geo.mercator()
    .translate([480, 350])
    .scale(770);

var path = d3.geo.path().projection(projection);

var svg = d3.select("#chart")
  .append("svg");

var countries = svg.append("g")
    .attr("id", "countries")
    .attr("class", "Blues");


d3.json("./d3/examples/data/world-countries.json", function(json) {
  countries.selectAll("path")
      .data(json.features)
    .enter().append("path")
	.attr("d", path)
        .style("fill", noData)
        .on("click", mouseOverEvent)
	.each(function(d, i) { this._current = d.properties; });
});

d3.json("getdata.php?v=1", function(data){

    var nestD = d3.nest()
	.key(function(d) { return d.name; })
	.rollup(function(a) { return a[0].value; })
	.map(data);

    var max = d3.max(data, function(d) { return d.value; });

    var continuousScale = d3.scale.linear()
	.domain([0, max])
	.range([0, 1]);

    countries.selectAll("path")
	.style("fill", function(d){
	    var v = nestD[d.properties.name];
	    return v? d3.hsl(230, 1, 1 - continuousScale(v / max)) 
		          : noData;
	});
});

function mouseOverEvent(){
    alert(this._current.name);
}
