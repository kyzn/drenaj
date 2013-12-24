<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="direnaj.domain.*"%>
<%@ page import="direnaj.functionalities.sna.communityDetection.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Direnaj Test Center / Community Detection</title>
</head>
<body>
	<h1>Analysis Parameters</h1>
	<p>
		CampaignID : <b>${campaignId}</b> | Limit : <b>${limit}</b>
	</p>
	<h1>Communities</h1>
	<table>
		<tr>
			<%
			    DetectedCommunities communitiesInCampaign = (DetectedCommunities) request
			            .getAttribute("detectedCommunities");
			    List<Community> communities = communitiesInCampaign.getDetectedCommunties();
			    int communityCount = communities.size();
			    Collections.sort(communities, new Comparator<Community>(){
			    	public int compare(Community c2, Community c1) {
			    		if (c1.getUsersInCommunity().size() == c2.getUsersInCommunity().size())
			    			return 0;
			    		else if (c1.getUsersInCommunity().size() > c2.getUsersInCommunity().size())
			    			return 1;
			    		else
			    			return -1;
			    	}
			    });
			    for (int communityIndex = 0; communityIndex < communityCount; communityIndex++) {
			        String str = "<td><table>";
			        Community foundCommunity = communities.get(communityIndex);
			        List<User> usersInCommunity = foundCommunity.getUsersInCommunity();
			        str += "<tr><td> Community - " + communityIndex + 1 + " </td></tr>";
			        for (int i = 0; i < usersInCommunity.size(); i++) {
			            str += "<tr><td> <font size=1>" + usersInCommunity.get(i).getUserScreenName() + " </font></td></tr>";
			        }
			        str += "</table></td>";
			        out.println(str);
			    }
			%>
		</tr>
	</table>


</body>
</html>