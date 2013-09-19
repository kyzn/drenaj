$(document).ready(function() {
    var width = $(document).width();
    var height = $(document).height();
    var g = new Graph();
    g.edgeFactory.template.style.directed = true;
   
   
var render = function(r, n) {
        /* the Raphael set is obligatory, containing all you want to display */
        var set = r.set().push(
            /* custom objects go here */
            r.rect(n.point[0]-30, n.point[1]-13, 120, 90)
                .attr({"fill": "#fa8", "stroke-width": 2, r : "9px"}))
                .push(r.text(n.point[0] + 30, n.point[1] + 30, n.label)
                    .attr({"font-size":"20px"}));
        
		/* custom tooltip attached to the set */
        /*
		set.items.forEach(
            function(el) {
                el.tooltip(r.set().push(r.rect(0, 0, 30, 30)
                    .attr({"fill": "#fec", "stroke-width": 1, r : "9px"})))});
		*/
        return set;
    };

	
	
g.addNode("faviki", {label:"faviki", render:render});
g.addNode("ivan_herman", {label:"ivan_herman", render:render});
g.addNode("manusporny", {label:"manusporny", render:render});
g.addNode("planetrdf", {label:"planetrdf", render:render});
g.addNode("roylac", {label:"roylac", render:render});
g.addNode("SemanticBlogs", {label:"SemanticBlogs", render:render});


g.addEdge("faviki","#iswc2011", {label:"1"});
g.addEdge("faviki","#linkeddata", {label:"3"});
g.addEdge("faviki","#rdf", {label:"2"});
g.addEdge("faviki","#semanticweb", {label:"1"});
g.addEdge("faviki","#semweb", {label:"6"});
g.addEdge("faviki","#thankyousteve", {label:"1"});
g.addEdge("faviki","#turtle", {label:"1"});
g.addEdge("faviki","#webid", {label:"1"});
g.addEdge("ivan_herman","#classicalmusic", {label:"1"});
g.addEdge("ivan_herman","#force11", {label:"1"});
g.addEdge("ivan_herman","#iswc2011", {label:"3"});
g.addEdge("ivan_herman","#linkeddata", {label:"4"});
g.addEdge("ivan_herman","#netrights", {label:"1"});
g.addEdge("ivan_herman","#rdfa", {label:"2"});
g.addEdge("ivan_herman","#rnews", {label:"1"});
g.addEdge("ivan_herman","#semanticweb", {label:"1"});
g.addEdge("ivan_herman","#semweb", {label:"3"});
g.addEdge("ivan_herman","#w3c", {label:"1"});
g.addEdge("ivan_herman","#xs4all", {label:"1"});
g.addEdge("manusporny","#html5", {label:"1"});
g.addEdge("manusporny","#jsonld", {label:"1"});
g.addEdge("manusporny","#music", {label:"1"});
g.addEdge("manusporny","#occupycal", {label:"1"});
g.addEdge("manusporny","#ows", {label:"1"});
g.addEdge("manusporny","#payswarm", {label:"1"});
g.addEdge("manusporny","#poverty", {label:"1"});
g.addEdge("manusporny","#programming", {label:"1"});
g.addEdge("manusporny","#rdfa", {label:"2"});
g.addEdge("manusporny","#systemd", {label:"1"});
g.addEdge("manusporny","#ted", {label:"1"});
g.addEdge("manusporny","#tpac", {label:"1"});
g.addEdge("manusporny","#vie", {label:"1"});
g.addEdge("manusporny","#vote", {label:"1"});
g.addEdge("manusporny","#w3c", {label:"6"});
g.addEdge("manusporny","#w3cdds", {label:"1"});
g.addEdge("manusporny","#webid", {label:"1"});
g.addEdge("planetrdf","#dotnetrdf", {label:"1"});
g.addEdge("planetrdf","#html5", {label:"1"});
g.addEdge("planetrdf","#jena", {label:"1"});
g.addEdge("planetrdf","#linkeddata", {label:"1"});
g.addEdge("planetrdf","#nosql", {label:"1"});
g.addEdge("planetrdf","#rdf", {label:"1"});
g.addEdge("planetrdf","#schemaorg", {label:"1"});
g.addEdge("planetrdf","#semanticweb", {label:"1"});
g.addEdge("planetrdf","#sesame", {label:"2"});
g.addEdge("roylac","#linkeddata", {label:"1"});
g.addEdge("roylac","#microdata", {label:"2"});
g.addEdge("roylac","#rdf", {label:"3"});
g.addEdge("roylac","#semantic", {label:"1"});
g.addEdge("roylac","#semanticweb", {label:"1"});
g.addEdge("roylac","#semweb", {label:"1"});
g.addEdge("roylac","#seo", {label:"1"});
g.addEdge("roylac","#stwg", {label:"1"});
g.addEdge("roylac","#ted", {label:"1"});
g.addEdge("roylac","#topicmaps", {label:"1"});
g.addEdge("roylac","#w3c", {label:"1"});
g.addEdge("SemanticBlogs","#semantic", {label:"40"});


    /* colourising the shortest paths and setting labels */
	/*
    for(e in g.edges) {
        if(g.edges[e].target.predecessor === g.edges[e].source || g.edges[e].source.predecessor === g.edges[e].target) {
            g.edges[e].style.stroke = "#bfa";
            g.edges[e].style.fill = "#56f";
        } else {
            g.edges[e].style.stroke = "#aaa";
        }
    }
    */
	
    for(e in g.edges) {	 
	  g.edges[e].style.stroke = "#aaa";
    }
	
	/*
	for(n in g.nodes) {
		if (n[0] === '#') {
			// alert(g.nodes[n].text);
		}
	}
	*/
	
	// var layouter = new Graph.Layout.Ordered(g, topological_sort(g));
	var layouter = new Graph.Layout.Spring(g);
	layouter.layout();

    var renderer = new Graph.Renderer.Raphael('canvas', g, width, height);
});