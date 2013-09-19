<?php include_once 'preprocess.php'; ?>

<h2>A recommender based on social-semantic web!</h2>
<p>
Lorem ipsum dolor sit amet Consectetur adipiscing elit Integer molestie lorem at massa Facilisis in pretium nisl aliquet Nulla volutpat aliquam velit Phasellus iaculis neque Purus sodales ultricies Vestibulum laoreet porttitor sem Ac tristique libero volutpat at Faucibus porta lacus fringilla vel Aenean sit amet erat nunc Eget porttitor lorem
</p>




<!-- 1a) Optional: add a theme file -->
<!--
<script type="text/javascript" src="../js/themes/gray.js"></script>
-->

<!-- 1b) Optional: the exporting module -->
<script type="text/javascript" src="../js/modules/exporting.js"></script>
		

<!-- 2. Add the JavaScript to initialize the chart on document ready -->
<script type="text/javascript">

			var chart;
$(document).ready(function() {
chart = new Highcharts.Chart({
	chart: {
	renderTo: 'container',
		defaultSeriesType: 'bar'
					},
		title: {
		text: 'Top Users\' Hashtags'
	},
		xAxis: {
			categories: ['#linkeddata', '#programming', '#rdf', '#rdfa', '#semantic', '#semanticweb', '#w3c']
					},
					yAxis: {
						min: 0,
						title: {
			text: 'Number of hashtags'
			},
			allowDecimals: false
			},
					legend: {
						backgroundColor: '#FFFFFF',
			reversed: true
	},
	tooltip: {
		formatter: function() {
		return '<b>'+ this.x +'</b><br/>'+
		this.series.name +': '+ this.y + '/'+ this.point.stackTotal;
		}
					},
					plotOptions: {
		series: {
		stacking: 'normal'
		}
		},
		series: [{
		name: 'planetrdf',
		data: [1,0,1,0,0,1,0]
		}, {
			name: 'SemanticBlogs',
			data: [0,0,0,0,40,0,0]
	}, {
			name: 'manusporny',
			data: [0,1,0,2,0,0,6]
			}, {
			name: 'faviki',
						data: [3,0,2,0,0,1,0]
			}, {
			name: 'ivan_herman',
						data: [4,0,0,2,0,1,1]
					
			}, {
			name: 'roylac',
						data: [1,0,3,0,1,1,1]
			}]
});


});

</script>

<!-- 3. Add the container -->
<div id="container" style="width: 800px; height: 400px; margin: 0 auto"></div>


          
<?php include_once 'postprocess.php'; ?>