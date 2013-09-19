<?php include_once 'preprocess.php'; ?>

<h2>A recommender based on social-semantic web!</h2>
<p>
Lorem ipsum dolor sit amet Consectetur adipiscing elit Integer molestie lorem at massa Facilisis in pretium nisl aliquet Nulla volutpat aliquam velit Phasellus iaculis neque Purus sodales ultricies Vestibulum laoreet porttitor sem Ac tristique libero volutpat at Faucibus porta lacus fringilla vel Aenean sit amet erat nunc Eget porttitor lorem
</p>

<address>
	<strong>Burak Celebi</strong><br/>
	Bogazici University<br/>
	SosLab, Department of Computer Engineering<br/> 
	Bebek, Istanbul, Turkey<br/>
	<a href="http://twitter.com/burakcelebi" title="@burakcelebi">@burakcelebi</a><br/> 
	<a href="http://about.me/burakcelebi">more..</a>
</address>

	<?php 
	if (!isset($_GET['p'])) {
		echo 'display all'; 
	}
	?>	

<!-- <visualsearch> -->

    <div id="search_box_container"></div>
    <div id="search_query">&nbsp;</div>

    <script type="text/javascript" charset="utf-8">
	var statuses = 0;
	var chattiness = 0;

	var query = '';
	<?php 
	if (isset($_GET['p'])) { ?>

		<?php if (isset($_GET['statuses'])) { ?>
			statuses  = <?php echo $_GET['statuses']; ?>;
			query += ' statuses: ' + statuses;
		<?php } ?>
	
		<?php if (isset($_GET['chattiness'])) { ?>
			chattiness  = <?php echo $_GET['chattiness']; ?>;
			query += ' chattiness: ' + chattiness;
		<?php } ?>
		
	<?php 
	} 
	?>
    
      $(document).ready(function() {
        var visualSearch = VS.init({
          container  : $('#search_box_container'),
          query      : query,
          // query      : 'statuses: ' + statuses + ' chattiness: ' +  chattiness,
          unquotable : [
            'text',
            'account',
            'filter',
            'access'
          ],
          callbacks  : {
            search : function(query, searchCollection) {
              var $query = $('#search_query');
              var count  = searchCollection.size();
              $query.stop().animate({opacity : 1}, {duration: 300, queue: false});

              var paramStr = visualSearch.searchBox.value();
              paramStr = paramStr.replace(/: "/g, '=');
              paramStr = paramStr.replace(/"/g, '');
              paramStr = paramStr.replace(/ /g, '&');
              window.location = 'contact_vs.php?' + paramStr + '&p=1';
              
              
              /*
              $query.html('<span class="raquo">&raquo;</span> You searched for: ' +
                          '<b>' + (query || '<i>nothing</i>') + '</b>. ' +
                          '(' + count + ' facet' + (count==1 ? '' : 's') + ')');
			  */

              clearTimeout(window.queryHideDelay);
              window.queryHideDelay = setTimeout(function() {
                $query.animate({
                  opacity : 0
                }, {
                  duration: 1000,
                  queue: false
                });
              }, 2000);
            },
            facetMatches : function(callback) {
              callback([
                'account', 'filter', 'access', 'title', 'statuses', 'chattiness',
                { label: 'city',    category: 'location' },
                { label: 'country', category: 'location' },
                { label: 'state',   category: 'location' },
              ]);
            },
            valueMatches : function(facet, searchTerm, callback) {
              switch (facet) {
              case 'account':
                  callback([
                    { value: '1-amanda', label: 'Amanda' },
                    { value: '2-aron',   label: 'Aron' },
                    { value: '3-eric',   label: 'Eric' },
                    { value: '4-jeremy', label: 'Jeremy' },
                    { value: '5-samuel', label: 'Samuel' },
                    { value: '6-scott',  label: 'Scott' }
                  ]);
                  break;
                case 'filter':
                  callback(['published', 'unpublished', 'draft']);
                  break;
                case 'access':
                  callback(['public', 'private', 'protected']);
                  break;
                case 'title':
                  callback([
                    'Pentagon Papers',
                    'CoffeeScript Manual',
                    'Laboratory for Object Oriented Thinking',
                    'A Repository Grows in Brooklyn'
                  ]);
                  break;
                case 'city':
                  callback([
                    'Cleveland',
                    'New York City',
                    'Brooklyn',
                    'Manhattan',
                    'Queens',
                    'The Bronx',
                    'Staten Island',
                    'San Francisco',
                    'Los Angeles',
                    'Seattle',
                    'London',
                    'Portland',
                    'Chicago',
                    'Boston'
                  ]);
                  break;
                case 'state':
                  callback([
                    "Alabama", "Alaska", "Arizona", "Arkansas", "California",
                    "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida",
                    "Georgia", "Guam", "Hawaii", "Idaho", "Illinois",
                    "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana",
                    "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                    "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
                    "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina",
                    "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
                    "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee",
                    "Texas", "Utah", "Vermont", "Virginia", "Virgin Islands",
                    "Washington", "West Virginia", "Wisconsin", "Wyoming"
                  ]);
                  break;
                case 'country':
                  callback([
                    "China", "India", "United States", "Indonesia", "Brazil",
                    "Pakistan", "Bangladesh", "Nigeria", "Russia", "Japan",
                    "Mexico", "Philippines", "Vietnam", "Ethiopia", "Egypt",
                    "Germany", "Turkey", "Iran", "Thailand", "D. R. of Congo",
                    "France", "United Kingdom", "Italy", "Myanmar", "South Africa",
                    "South Korea", "Colombia", "Ukraine", "Spain", "Tanzania",
                    "Sudan", "Kenya", "Argentina", "Poland", "Algeria",
                    "Canada", "Uganda", "Morocco", "Iraq", "Nepal",
                    "Peru", "Afghanistan", "Venezuela", "Malaysia", "Uzbekistan",
                    "Saudi Arabia", "Ghana", "Yemen", "North Korea", "Mozambique",
                    "Taiwan", "Syria", "Ivory Coast", "Australia", "Romania",
                    "Sri Lanka", "Madagascar", "Cameroon", "Angola", "Chile",
                    "Netherlands", "Burkina Faso", "Niger", "Kazakhstan", "Malawi",
                    "Cambodia", "Guatemala", "Ecuador", "Mali", "Zambia",
                    "Senegal", "Zimbabwe", "Chad", "Cuba", "Greece",
                    "Portugal", "Belgium", "Czech Republic", "Tunisia", "Guinea",
                    "Rwanda", "Dominican Republic", "Haiti", "Bolivia", "Hungary",
                    "Belarus", "Somalia", "Sweden", "Benin", "Azerbaijan",
                    "Burundi", "Austria", "Honduras", "Switzerland", "Bulgaria",
                    "Serbia", "Israel", "Tajikistan", "Hong Kong", "Papua New Guinea",
                    "Togo", "Libya", "Jordan", "Paraguay", "Laos",
                    "El Salvador", "Sierra Leone", "Nicaragua", "Kyrgyzstan", "Denmark",
                    "Slovakia", "Finland", "Eritrea", "Turkmenistan"
                  ], {preserveOrder: true});
                  break;
                case 'statuses':
                    callback(['50','100', '200']);
                    break;
                case 'chattiness':
                    callback(['0','0.25', '0.50', '1', '1.5']);
                    break;
              }
            }
          }
        });
      });
    </script>

<!-- </visualsearch> -->




<script type="text/javascript">
	var active_nav = "nav_contact";
</script>

<?php include_once 'postprocess.php'; ?>