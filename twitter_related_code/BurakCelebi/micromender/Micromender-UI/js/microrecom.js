////////// <d3> ////////
function bubleChart (json, renderTo) {
			
	var r = 480,
	format = d3.format(",d"),
	fill = d3.scale.category20c();
	
	// var keeper;	
	
	var bubble = d3.layout.pack()
	    .sort(null)
	    .size([r, r]);
	
	var vis = d3.select('#' + renderTo).append("svg:svg")
	    .attr("width", r)
	    .attr("height", r)
	    .attr("class", "bubble");
	
	//d3.json("../data/flaretest.json", function(json) {
	
	//	keeper = json;
	console.log(json);
	
	var node = vis.selectAll("g.node")
	           .data(bubble.nodes(classes(json))
	           .filter(function(d) { return !d.children; }))
	           .enter().append("svg:g")
	           .attr("class", "node")
	           .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
	
	node.append("svg:title")
	    .text(function(d) { return d.className + ": " + format(d.value); });
	
	node.append("svg:circle")
	    .attr("r", function(d) { return d.r; })
	    .style("fill", function(d) { return fill(d.packageName); });
	
	node.append("svg:text")
	    .attr("text-anchor", "middle")
	    .attr("dy", ".3em")
	    .text(function(d) { return d.className.substring(0, d.r / 3); });
	
	console.log(node);	  
	
	/*
	function switchIt() {
	    var nodes = bubble.nodes(classes(keeper))
	      .filter(function(d) { return !d.children; });
	    nodes.forEach(function(n,i) {
	        n.r/= 2;
	    }); 
	
	
	
	  var snode = vis.selectAll("g.node")
	      .data(nodes, function(d,i) { 
	           return d.className; });
	
	console.log(snode);
	 
	  snode.select("text")
		 .transition()
		 .delay(100)
		 .duration(500)
		 .text(function(d) { 
	             var s = d.className + ": " + format(d.value); 
	             console.log(s);
	             return s;	
	         });
	
	  snode.select("circle")
		 .transition()
		 .delay(100)
		 .duration(500)
	      .attr("r", function(d) { return d.r; })
	      .style("fill", function(d) { return fill(d.packageName); });
	
	
	};
	*/
	
	// Returns a flattened hierarchy containing all leaf nodes under the root.
	function classes(root) {
	  var classes = [];
	
	  function recurse(name, node) {
	    if (node.children) node.children.forEach(function(child) { recurse(node.name, child); });
	    else classes.push({packageName: name, className: node.name, value: node.size});
	  }
	  
	  recurse(null, root);
	  return {children: classes};
	}
	
	// d3.select("#knop").on("click", switchIt);
} // f:bubleChart

///////// </d3> ////////

function redirectTo(page) {
	window.location = page;
}

function trimStr(str) {
	return str.replace(/^\s+|\s+$/g, '');
}


function isNull(str) {
	str = trimStr(str);
	return str==null || str=='';
}

function hidediv(id) {
	//safe function to hide an element with a specified id
	if (document.getElementById) { // DOM3 = IE5, NS6
		document.getElementById(id).style.display = 'none';
	}
	else {
		if (document.layers) { // Netscape 4
			document.id.display = 'none';
		}
		else { // IE 4
			document.all.id.style.display = 'none';
		}
	}
}


function showdiv(id) {
	//safe function to show an element with a specified id
		  
	if (document.getElementById) { // DOM3 = IE5, NS6yo
		document.getElementById(id).style.display = 'block';
	}
	else {
		if (document.layers) { // Netscape 4
			document.id.display = 'block';
		}
		else { // IE 4
			document.all.id.style.display = 'block';
		}
	}
}


function showHide(divId){
	
	if(document.getElementById(divId).style.display == 'none') {
		document.getElementById(divId).style.display='block';
	} else {
		document.getElementById(divId).style.display = 'none';
	}
}
