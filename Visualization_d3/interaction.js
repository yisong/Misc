var list = [];
var map = {};
var version = 0;
var maxver = 0;

var order = 1;
var selected = 0;
var globalname = "placeholder";


//layout parameter

var logWidth = 115;
var logHeight = 30;

    getVersion();
setglobalname();
    var m = [30, 10, 10, 100],
    w = window.innerWidth - 200 - m[1] - m[3],
    h = 400 - m[0] - m[2];


    var format = d3.format(",.0f");

    var x = d3.scale.linear().range([0, w]),
    y = d3.scale.ordinal().rangeRoundBands([0, h], .1);

    var xAxis = d3.svg.axis().scale(x).orient("top").tickSize(-h),
    yAxis = d3.svg.axis().scale(y).orient("left").tickSize(0);

    var chart = d3.select("body").append("svg")
	.attr("width", w + m[1] + m[3])
    .attr("height", h + m[0] + m[2]);

    var svg = chart.append("g")
    .attr("transform", "translate(" + m[3] + "," + m[0] + ")");
    var svg3 = chart.append("g");

var svg4x = 350 + (window.innerWidth - 930)/2;

    var svg4 = chart.append("g")
    .attr("transform", "translate(" + svg4x + "," + 10 + ")");

var continuousScale;

var rows;
var rowkey="ok";
var rowcolumn=[];
var rows2;
var row2key="orderkey";
var row2column=[];




var w2 = 160;
var h2 = h;

var svg2 = d3.select("body").append("svg")
    .attr("width", w2).attr("height", h2 + m[0] + m[2])
    .append("g")
    .attr("transform", "translate(" + 25 + "," + m[0] + ")");      


function drawTable(){


    d3.json("getdata.php?v="+version, function(data) {

	// Parse numbers, and sort by value.
	data.forEach(function(d) { d.value = +d.value; });
	data.sort(function(a, b) { return b.name > a.name? 1 : -1; });

	// Set the scale domain.
	x.domain([0, d3.max(data, function(d) { return d.value; })]);
	y.domain(data.map(function(d) { return d.name; }));

	var bar = svg.selectAll("g.bar")
	    .data(data, function(d) { return d.name; })
	    .enter().append("g")
	    .attr("class", "bar display")
	    .attr("transform", function(d) { return "translate(0," + y(d.name) + ")"; })
	    .on("click", clickEvent)
	    .on("mouseover", mouseOverEvent)
	    .on("mouseout", mouseOutEvent)
	    .each(function(d, i) { this._current = data[i]; });

	bar.append("rect")
	    .attr("width", function(d) { return x(d.value); })
	    .attr("height", y.rangeBand());

	bar.append("text")
	    .attr("class", "value")
	    .attr("x", function(d) { return x(d.value); })
	    .attr("y", y.rangeBand() / 2)
	    .attr("dx", -3)
	    .attr("dy", ".35em")
	    .attr("text-anchor", "end")
	    .text(function(d) { return format(d.value); });

	svg.append("g")
	    .attr("class", "x axis")
	    .call(xAxis);

	svg.append("g")
	    .attr("class", "y axis")
	    .call(yAxis);
	
    });


    d3.json("getlog.php", function(data) {

	var bar = svg2.selectAll("g.bar")
	    .data(data, function(d) { return d.name+d.sign+d.value; })
	    .enter().append("g")
	    .attr("class", "bar log")
	    .attr("transform", function(d, i) { return "translate(0," + logHeight*i + ")"; })
	    .on("dblclick", clicklogEvent)
	    .on("mouseover", logmouseover)
	    .on("mouseout", logmouseout);

	drawLogRect(bar);
	colorNewTuple();

    });

    d3.json("getdata.php?v="+version, function(data) {
	var w = 300;
	var h = 300;
	var r = Math.min(w, h) / 2;
	var color = d3.scale.category20();
	var arc = d3.svg.arc().outerRadius(r);

	data.sort(function(a, b) { return b.name > a.name? 1 : -1; });
	data = data.filter(function(d){return d.value > 0;});

	svg3
	    .attr("width", w)
	    .attr("height", h)
	    .attr("transform", "translate(" + 500 + "," + r + ")");

	var pie = d3.layout.pie().sort(null)   
            .value(function(d) { return d.value; }); 

	var arcs = svg3.selectAll("g.slice")
	    .data(pie(data), function(d, i){return d.data.name;})	
	    .enter().append("g")
	    .attr("class", "slice")
	    .on("click", clickEvent)
	    .on("mouseover", mouseOverEvent)
	    .on("mouseout", mouseOutEvent)
	    .each(function(d, i) { this._current = d.data; });

	arcs.append("path")
	    .attr("fill", function(d, i) { return color(i); })
	    .attr("d", arc)
	    .each(function(d) { this._current = d; });

	arcs.append("text")
	    .attr("transform", function(d) { 
		d.innerRadius = 0;
                d.outerRadius = r;
		return "translate(" + arc.centroid(d) + ")"; })
	    .attr("text-anchor", "middle")
	    .style("fill", "White")
	    .style("font", "bold 12px Arial")
	    .text(function(d, i) { return d.data.name + ": " + d.value; });

   });


    var noData = '#eee';

    var projection = d3.geo.mercator()
	.translate([0, 250])
	.scale(750);

    var path = d3.geo.path().projection(projection);


    var countries;


    d3.json("./world-countries.json", function(json) {
	countries = svg4.selectAll("g.country")
	    .data(json.features)
	    .enter().append("g")
	    .attr("class", "country")
	    .on("click", clickEvent)
	    .on("mouseover", mouseOverEvent)
	    .on("mouseout", mouseOutEvent)
	    .each(function(d, i) { this._current = d.properties; });


	countries.append("path")
	    .attr("d", path)
            .style("fill", noData);

    });

    d3.json("getdata.php?v="+version, function(data){

	var nestD = d3.nest()
	    .key(function(d) { return d.name; })
	    .rollup(function(a) { return a[0].value; })
	    .map(data);

	var max = d3.max(data, function(d) { return d.value; });

	continuousScale = d3.scale.linear()
	    .domain([0, max])
	    .interpolate(d3.interpolateHsl).range(["#eee", "hsl(250,100%,50%)"]);

	countries.selectAll("path")
	    .each( function(d){
		var v = nestD[d.properties.name];
		this.c = v? continuousScale(v) 
		    : noData;
	    }).style("fill", function(){return this.c;})
	    .append("title")
	    .text(function(d){
		var v = nestD[d.properties.name];
		return d.properties.name + ": " + (v? v : 0);});

	var legend = svg4.append('g')
	    .attr('transform', 'translate('+ (chart.attr('width')-svg4x-m[1] - 35) +', 280)')
	    .attr('class', 'legend');
	var legendGradient = legend.append('g');
	var legendTicks = legend.append('g').attr("class", "tick");


	legendGradient.selectAll('rect')
	    .data(d3.range(1, 0, -0.1))
	    .enter()
	    .insert('rect')
	    .attr('x', 1)
	    .attr('y', function (d, i) {
		return i*10;
	    })
	    .attr('width', 30)
	    .attr('height', 10)
	    .style('fill', function (d, i) {

		return continuousScale(d*max);
	    });

	legendTicks.selectAll('text')
	    .data([max, 0])
	    .enter()
	    .insert('text')
	    .attr('class', 'tick')
	    .attr('text-anchor', 'end')
	    .attr('x', -4)
	    .attr('y', function (d, i) {
		return i * 100 + 5;
	    })
	    .text(function(d, i) {
		return d3.format('.0f')(d);
	    });

    });



}

function colorNewTuple(){
    var latest = svg2.selectAll("g.bar").filter(function(d, i){ return d.version > version; });
	latest.select("rect").style("fill", "AFEEEE");

   svg2.selectAll("g.bar").filter(function(d, i){ return d.version <= version; })
	.select("rect").style("fill", "#ccc");
}

function clickEvent(d){
    selected = 0;
    d = this._current;

    for(i = 0; i < list.length; i++){
	if(list[i][0].name == d.name){
	    list.splice(i, 1);
	 
	    svg.selectAll("g.bar").filter(function(d1, i){return this._current.name == d.name;}).select("rect").style("stroke", "");
	    svg3.selectAll("g.slice").filter(function(d1, i){return this._current.name == d.name;}).select("path").style("stroke", "");
	    svg4.selectAll("g.country").filter(function(d1, i){return this._current.name == d.name;}).select("path")
		.style("fill", function(){return this.c;});

	    svg2.selectAll("g.bar").filter(function(d1, i){ return d1.name == d.name; }).select("path").style("display", "none");
	    oldtabledehl(d.name);
	    newtabledehl(d.name);
	    return;
	}
    }

   if(!event.shiftKey){
      emptyList();
   }

    svg.selectAll("g.bar").filter(function(d1, i){return this._current.name == d.name;}).select("rect").style("stroke", "black").style("stroke-width", 3);
    svg3.selectAll("g.slice").filter(function(d1, i){return this._current.name == d.name;}).select("path").style("stroke", "black").style("stroke-width", 3);
    svg4.selectAll("g.country").filter(function(d1, i){return this._current.name == d.name;}).select("path").style("fill", "FFFF66");

   var name = d.name;
    newtablehl(name);
    oldtablehl(name);
   svg2.selectAll("g.bar").filter(function(d, i){ return d.name == name; })
	.select("path").style("display", "block");
   list.push([d, this]);
}

function mouseOverEvent(d){
   d3.select(this).select("rect").style("fill-opacity", 0.5);
   d3.select(this).select("path").style("fill-opacity", 0.5);
}

function mouseOutEvent(d){
   d3.select(this).select("rect").style("fill-opacity", 1);
   d3.select(this).select("path").style("fill-opacity", 1);
}

function emptyList(){
    selected = 0;
   while(list.length > 0){
       var tmp = list.pop();

       svg.selectAll("g.bar").filter(function(d1, i){return this._current.name == tmp[0].name;}).select("rect").style("stroke", "");
       svg3.selectAll("g.slice").filter(function(d1, i){return this._current.name == tmp[0].name;}).select("path").style("stroke", "");
       svg4.selectAll("g.country").filter(function(d1, i){return this._current.name == tmp[0].name;}).select("path")
       		.style("fill", function(){return this.c;});
      svg2.selectAll("g.bar").filter(function(d, i){ return d.name == tmp[0].name; }).select("path").style("display", "none");
       oldtabledehl(tmp[0].name);
       newtabledehl(tmp[0].name);
   }
}


function commit(){
   xmlhttp = new XMLHttpRequest();
   xmlhttp.onreadystatechange = function(){
      if (xmlhttp.readyState==4 && xmlhttp.status==200){
         document.getElementById("load").style.display = "none";
         document.getElementById("interaction").style.display = "block";
         var message = xmlhttp.responseText;
          if (message.length > 0) {alert(message); return;}
	  maxver += 1;
	  next();
      }
   }
   document.getElementById("load").style.display = "block";
   document.getElementById("interaction").style.display = "none";
   xmlhttp.open("GET", "commit.php", true);
   xmlhttp.send();
}


function sendRequest(){
   if (list.length == 0){return;}

   xmlhttp = new XMLHttpRequest();
   xmlhttp.onreadystatechange=function(){
     if (xmlhttp.readyState==4 && xmlhttp.status==200){
	 reDrawLogTable();
        var message = xmlhttp.responseText;
        if (message.length> 0) {alert(message);}
     }
   }

   if(version != maxver){alert("error for version");}
   var mark;
   for (i=0;i<document.option.sign.length;i++){
      if (document.option.sign[i].checked==true){
         mark = document.option.sign[i].value;
      }
   }
   var value = document.getElementById("value").value;
   var url = "update.php?sign=" + mark + "&value=" + value 
	+ "&version=" + (version + 1) + "&name=";   

    if (selected == 1) {
	url += globalname;
	emptyList();
    } else {
   while(list.length > 0){
      var tmp = list.pop();
       svg.selectAll("g.bar").filter(function(d1, i){return this._current.name == tmp[0].name;}).select("rect").style("stroke", "");
       svg3.selectAll("g.slice").filter(function(d1, i){return this._current.name == tmp[0].name;}).select("path").style("stroke", "");
       svg4.selectAll("g.country").filter(function(d1, i){return this._current.name == tmp[0].name;}).select("path")
       		.style("fill", function(){return this.c;});
      var name = escape(tmp[0].name);

       oldtabledehl(name);
       newtabledehl(name);
       svg2.selectAll("g.bar").filter(function(d, i){ return d.name == tmp[0].name; }).select("path").style("display", "none");
      url += name;
      if(list.length > 0){url += "+%+";}
   }
    }

   xmlhttp.open("GET", url, true);
   xmlhttp.send();

}

function getVersion(){
    xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    version = parseInt(xmlhttp.responseText);
	    maxver = version;
	    setVersion();
	    drawTable();
	    showBar(); 
	    getoldcolumn();
	    getnewcolumn();
	}
    }

    xmlhttp.open("GET", "getversion.php", true);
    xmlhttp.send();
}

function prev(){
    if(version < 2){return ;}
    version -= 1;
    setVersion();
    reDrawTable();
    d3.select("table.new").select("tbody").selectAll("tr").remove();
    drawCompleteTable();
}

function next(){
    if(version >= maxver) {return;}
    version += 1;
    setVersion();
    reDrawTable();
    d3.select("table.new").select("tbody").selectAll("tr").remove();
    drawCompleteTable();
}

function setVersion(){
    document.getElementById("version").innerHTML = "Hypothetical Data version: " + version;
    if(version < maxver) {
	document.getElementById("editoption").style.display = "none";
	document.getElementById("editable").style.display = "block";
    } else {
	document.getElementById("editoption").style.display = "block";
	document.getElementById("editable").style.display = "none";
    }
}

function setEditable(){
    var edit = confirm("Decide to use current version?");
    if(!edit){return;}

    maxver = version;
    setVersion();
    deleteOldVersion();
}

function deleteOldVersion(){
    xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var message = xmlhttp.responseText;
            if (message.length> 0) {alert(message);}
	    reDrawLogTable();
	}
    }

    xmlhttp.open("GET", "deleteold.php?v="+version, true);
    xmlhttp.send();

}

function reDrawTable(){
    d3.json("getdata.php?v="+version, function(data) {

	data.forEach(function(d) { d.value = +d.value; });
	data.sort(function(a, b) { return b.name > a.name? 1 : -1; });

	var currentmax = x.domain()[1];
	var nextmax = d3.max(data, function(d) { return d.value; });
	if (nextmax > currentmax){
	    x.domain([0,nextmax]);
	} 

	y.domain(data.map(function(d) { return d.name; }));

	var bar = svg.selectAll("g.bar")
	    .data(data, function(d){return d.name; });

	bar.transition().duration(500).attr("transform", function(d) { return "translate(0," + y(d.name) + ")"; });

	bar.select("rect").transition().duration(500)
	    .attr("width", function(d) { return x(d.value); })
	    .attr("height", y.rangeBand());

	bar.select("text").transition().duration(500)
	    .attr("x", function(d) { return x(d.value); })
	    .attr("y", y.rangeBand() / 2)
	    .text(function(d) { return format(d.value); });

	svg.selectAll(".x.axis").transition().duration(500).call(xAxis);
	svg.selectAll(".y.axis").transition().duration(500).call(yAxis);


	var newbar = bar.enter().insert("g", "g")
	    .attr("class", "bar display")
	    .attr("transform", function(d) { return "translate(0," + y(d.name) + ")"; })
	    .on("click", clickEvent)
	    .on("mouseover", mouseOverEvent)
	    .on("mouseout", mouseOutEvent)
	    .each(function(d, i) { this._current = data[i]; });

	newbar.append("rect")
	    .attr("width", function(d) { return x(d.value); })
	    .attr("height", y.rangeBand());

	newbar.append("text")
	    .attr("class", "value")
	    .attr("x", function(d) { return x(d.value); })
	    .attr("y", y.rangeBand() / 2)
	    .attr("dx", -3)
	    .attr("dy", ".35em")
	    .attr("text-anchor", "end")
	    .text(function(d) { return format(d.value); });



    });

    d3.json("getdata.php?v="+version, function(data) {
	
	data.sort(function(a, b) { return b.name > a.name? 1 : -1; });
	data = data.filter(function(d){return d.value > 0;});

	var pie = d3.layout.pie().sort(null)   
            .value(function(d) { return d.value; }); 

	var arcs = svg3.selectAll("g.slice")
	    .data(pie(data), function(d, i){return d.data.name;})
	    .each(function(d, i) { this._current = d.data; });

	arcs.exit().remove();

	var arc = d3.svg.arc().outerRadius(150);
	var color = d3.scale.category20();


	arcs.select("path").transition().duration(500)
	    .attr("fill", function(d, i) { return color(i); })
	    .attr("d", arc);

	arcs.select("text").transition().duration(500)
	    .attr("transform", function(d) {
		d.innerRadius = 0;
                d.outerRadius = 150;
		return "translate(" + arc.centroid(d) +")"; })
	    .text(function(d, i) { return d.data.name + ": " + d.value; });

	var newarcs = arcs
	    .enter().append("g")
	    .attr("class", "slice")
	    .on("click", clickEvent)
	    .on("mouseover", mouseOverEvent)
	    .on("mouseout", mouseOutEvent)
	    .each(function(d, i) { this._current = d.data; });

	newarcs.append("path")
	    .attr("fill", function(d, i) { return color(i); })
	    .attr("d", arc)
	    .each(function(d) { this._current = d; });

	newarcs.append("text")
	    .attr("transform", function(d) { 
		d.innerRadius = 0;
                d.outerRadius = 150;
		return "translate(" + arc.centroid(d) + ")"; })
	    .attr("text-anchor", "middle")
	    .style("fill", "White")
	    .style("font", "bold 12px Arial")
	    .text(function(d, i) { return d.data.name + ": " + d.value; });

    });


    d3.json("getdata.php?v="+version, function(data){
	var noData = '#eee';

	var nestD = d3.nest()
	    .key(function(d) { return d.name; })
	    .rollup(function(a) { return a[0].value; })
	    .map(data);

	var max = d3.max(data, function(d) { return d.value; });


	if (continuousScale.domain()[1] > max){
	    max = continuousScale.domain()[1];
	
	}

	continuousScale = d3.scale.linear()
	    .domain([0, max])
	    .interpolate(d3.interpolateHsl).range(["#eee", "hsl(250,100%,50%)"]);

	svg4.selectAll("g.country").selectAll("path")
	    .each( function(d){
		var v = nestD[d.properties.name];
		this.c = v? continuousScale(v) 
		    : noData;
	    })
		.transition().duration(500)
		.style("fill", function(){return this.c;})
	    .selectAll("title")
	    .text(function(d){
		var v = nestD[d.properties.name];
		return d.properties.name + ": " + (v? v : 0);});

	svg4.selectAll("g.legend").selectAll("g.tick").selectAll('text')
	    .data([max, 0])
	    .attr('class', 'legend-tick')
	    .attr('text-anchor', 'end')
	    .attr('x', -4)
	    .attr('y', function (d, i) {
		return i * 100 + 5;
	    })
	    .text(function(d, i) {
		return d3.format('.0f')(d);
	    });

    });


    colorNewTuple();
}

function reDrawLogTable(){
    d3.json("getlog.php", function(data) {

	var element = svg2.selectAll("g.bar")
	    .data(data, function(d) { return d.name+d.sign+d.value; });

	var bar = element
	    .enter().append("g")
	    .attr("class", "bar log")
	    .attr("transform", function(d, i) { return "translate(0," + logHeight*i + ")"; })
	    .on("dblclick", clicklogEvent)
	    .on("mouseover", logmouseover)
	    .on("mouseout", logmouseout);

	drawLogRect(bar);
	element.exit().remove();
	element.transition().duration(500)
	    .attr("transform", function(d, i) { return "translate(0," + logHeight*i + ")"; });

    });
}

function clicklogEvent(d, i){
    if (version < maxver || d.version <= version){return;}
    xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var message = xmlhttp.responseText;
            if (message.length> 0) {alert(message);}
	    reDrawLogTable();
	}
    }

    xmlhttp.open("GET", "deletelog.php?name="+escape(d.name)+"&sign="+d.sign+"&value="+d.value+"&version="+d.version, true);
    xmlhttp.send();

}

function init(){

}



function drawLogRect(bar){
    
    bar.append("rect")
	.attr("width", logWidth)
	.attr("height", logHeight)
        .style("fill", "AFEEEE").style("fill-opacity", "0.8")
        .style("stroke", "black").style("stroke-width", 1);

    bar.append("text")
	.attr("class", "value")
	.attr("x", 0)
	.attr("y", logHeight / 2)
	.attr("dx", 10)
	.attr("dy", ".35em")
	.attr("text-anchor", "start")
	.text(function(d) { return d.name + " " + (d.sign == ">="? '\u2265' : '\u2264') + " " + format(d.value); })
	.style("fill", "black").style("font-weight", "bold");

    bar.append("path") 
	.attr('d', function(d) { 
            return 'M -10 6 l 8 8 l -8 8 z'; 
	})
        .style("fill", "red")
        .style("display", "none");

}

function showBar() {
    svg.style("display", "block");
    svg3.style("display", "none");  
    svg4.style("display", "none");  
    d3.select("#showPie").attr("class", "last");
    d3.select("#showMap").attr("class", "");
    d3.select("#showBar").attr("class", "first active");
}

function showMap() {

    svg.style("display", "none");
    svg3.style("display", "none");  
    svg4.style("display", "block");  
    d3.select("#showMap").attr("class", "active");
    d3.select("#showPie").attr("class", "last");
    d3.select("#showBar").attr("class", "first");
}

function showPie() {
    svg3.style("display", "block");
    svg.style("display", "none"); 
    svg4.style("display", "none");  
    d3.select("#showPie").attr("class", "last active");
    d3.select("#showBar").attr("class", "first");
    d3.select("#showMap").attr("class", "");
}

function drawCompleteTable(sortd){

    
    var b = document.getElementById("diff");
    b.setAttribute("active", "0");
    b.innerHTML = "Show Differences";



    d3.json("getcompletedata.php?v="+version, function(data) {
	
	if(sortd) {
	    data.sort(function(a, b){ return order*(b[sortd] >= a[sortd]? 1 : -1);});
	    order *= -1;
	}

	var  columns = row2column;
	var table = d3.select("table.new"),
        thead = table.select("thead"),
        tbody = table.select("tbody");


    // create a row for each object in the data
    rows2 = tbody.selectAll("tr")
            .data(data, function(d){return d.tid;})
        .enter()
        .append("tr");


    // create a cell in each row for each column
    var cells = rows2.selectAll("td")
        .data(function(row) {
            return columns.map(function(column) {
                return {column: column, value: row[column]};
            });
        })
        .enter()
        .append("td")
            .text(function(d) { return d.value; });

	for(i = 0; i < list.length; i++){
	    var name = list[i][0].name;
	    newtablehl(name);
	}

	if(sortd=="pattern"){ diffbutton();}

    });




}

function drawOldTable(){
    d3.json("getolddata.php", function(data) {

	var  columns = rowcolumn;
	var table = d3.select("#oldtable").append("table").attr("class", "old"),
        thead = table.append("thead"),
        tbody = table.append("tbody");

    // append the header row
    thead.append("tr")
        .selectAll("th")
        .data(columns)
        .enter()
        .append("th")
            .text(function(column) { return column; });

    // create a row for each object in the data
    rows = tbody.selectAll("tr")
        .data(data)
        .enter()
        .append("tr");


    // create a cell in each row for each column
    var cells = rows.selectAll("td")
        .data(function(row) {
            return columns.map(function(column) {
                return {column: column, value: row[column]};
            });
        })
        .enter()
        .append("td")
            .text(function(d) { return d.value; });
    });
}

function getoldcolumn(){
    var xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var array = JSON.parse(xmlhttp.responseText);
	    for(i=0; i < array.length; i++){
		if (array[i].column_name == "tid"){ continue;}
		rowcolumn.push(array[i].column_name);
	    }
	    drawOldTable();

	}
    }

    xmlhttp.open("GET", "getoldcolumn.php", true);
    xmlhttp.send();
}

function getnewcolumn(){
    var xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var array = JSON.parse(xmlhttp.responseText);
	    for(i=0; i < array.length-3; i++){
		row2column.push(array[i].column_name);
	    }

	    var  columns = row2column;
	    var table = d3.select("#newtable")
		.append("table").attr("class", "new"),
            thead = table.append("thead"),
            tbody = table.append("tbody");

	    // append the header row
	    thead.append("tr")
		.selectAll("th")
		.data(columns)
		.enter()
		.append("th")
	        .on("click", tablesort)
	        .on("mouseover", tablemouseOver)
	    	.on("mouseout", tablemouseOut)
		.text(function(column) { return column; });
	    drawCompleteTable();

	}
    }

    xmlhttp.open("GET", "getnewcolumn.php", true);
    xmlhttp.send();
}

function logmouseover(d, i){
    if (version < maxver || d.version <= version){return;}
    d3.select(this).select("rect").style("fill-opacity", 0.4);
}

function logmouseout(d, i){
    if (version < maxver || d.version <= version){return;}
    d3.select(this).select("rect").style("fill-opacity", 0.8);
}

function newtablehl(name){
    var xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var array = JSON.parse(xmlhttp.responseText);
	    for(i=0; i < array.length; i++){
		rows2.filter(function(d)
			     {return d["tid"] == array[i].tid;})
		    .attr("class","select"); 
	    }

	}
    }

    xmlhttp.open("GET", "getnewtabletid.php?v="+version+"&n="+escape(name), true);
    xmlhttp.send();

}

function newtabledehl(name){
    var xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var array = JSON.parse(xmlhttp.responseText);
	    for(i=0; i < array.length; i++){
		rows2.filter(function(d)
			     {return d["tid"] == array[i].tid;})
		    .attr("class",""); 
	    }

	}
    }

    xmlhttp.open("GET", "getnewtabletid.php?v="+version+"&n="+escape(name), true);
    xmlhttp.send();

}

function oldtablehl(name){
    var xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var array = JSON.parse(xmlhttp.responseText);
	    for(i=0; i < array.length; i++){
		rows.filter(function(d)
			     {return d["tid"] == array[i].tid;})
		    .attr("class","select"); 
	    }

	}
    }

    xmlhttp.open("GET", "getoldtabletid.php?v="+version+"&n="+escape(name), true);
    xmlhttp.send();

}

function oldtabledehl(name){
    var xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var array = JSON.parse(xmlhttp.responseText);
	    for(i=0; i < array.length; i++){
		rows.filter(function(d)
			     {return d["tid"] == array[i].tid;})
		    .attr("class",""); 
	    }

	}
    }

    xmlhttp.open("GET", "getoldtabletid.php?v="+version+"&n="+escape(name), true);
    xmlhttp.send();

}

function diffbutton(){
    var b = document.getElementById("diff");
    if (b.getAttribute("active") == "0"){
	b.setAttribute("active", "1");
	b.innerHTML = "Hide Differences";
	showDiff();
    } else {
	b.setAttribute("active", "0");
	b.innerHTML = "Show Differences";
	cancelDiff();
   }
}

function showDiff(){
    emptyList();
    d3.select("#diffsort").style("display", "block");

    rows2.each(function(d, i) {
	var p = d["pattern"];
	for(var i = 0; i < p.length; i++){
	    if(p.charAt(i) == "1"){
		d3.select(this).selectAll("td").filter(function(d){ 
		    return d.column == row2column[i];})
		    .attr("class", "select");
		

	    }
	}
	
    });

    d3.json("getdeleted.php?v="+version, function(data) {
	var columns = row2column;
	var del = d3.select("table.new").select("tbody").selectAll("tr")
            .data(data, function(d){return d.tid;})
        .enter()
            .append("tr").attr("class", "del");


	// create a cell in each row for each column
	var cells = del.selectAll("td")
            .data(function(row) {
		return columns.map(function(column) {
                    return {column: column, value: row[column]};
		});
            })
            .enter()
            .append("td")
            .text(function(d) { return d.value; });

    });
}

function cancelDiff(){
    rows2.selectAll("td").attr("class", "");
    d3.select("table.new").select("tbody").selectAll("tr.del").remove();
    d3.select("#diffsort").style("display", "none");
}

function tablesort(d, i){

    d3.select("table.new").select("tbody").selectAll("tr").remove();
    drawCompleteTable(d);
}

function tablemouseOver(){
    d3.select(this).style("color", "white");
}

function tablemouseOut(){
    d3.select(this).style("color", "black");
}

function diffsort(){
    order = 1;
    tablesort("pattern", 0);
}

function selectAll(){
    if (selected == 1) {
	emptyList();
	return;
    } else { 
	emptyList();
	selected = 1;
    }

    svg.selectAll("g.bar").each(function(d){

    d = this._current;

    svg.selectAll("g.bar").filter(function(d1, i){return this._current.name == d.name;}).select("rect").style("stroke", "black").style("stroke-width", 3);
    svg3.selectAll("g.slice").filter(function(d1, i){return this._current.name == d.name;}).select("path").style("stroke", "black").style("stroke-width", 3);
    svg4.selectAll("g.country").filter(function(d1, i){return this._current.name == d.name;}).select("path").style("fill", "FFFF66");

   var name = d.name;
    newtablehl(name);
    oldtablehl(name);
   svg2.selectAll("g.bar").filter(function(d, i){ return d.name == name; })
	.select("path").style("display", "block");
   list.push([d, this]);

    });
}

function setglobalname (){
    var xmlhttp = new XMLHttpRequest();                                       
    xmlhttp.onreadystatechange=function(){  
	if (xmlhttp.readyState==4 && xmlhttp.status==200){    
	    var array = JSON.parse(xmlhttp.responseText);
	    globalname = array[0].name;

	}
    }

    xmlhttp.open("GET", "getglobalname.php", true);
    xmlhttp.send();
}