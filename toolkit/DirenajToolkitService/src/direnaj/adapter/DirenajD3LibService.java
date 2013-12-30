package direnaj.adapter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;

import direnaj.functionalities.graph.Relations;
import direnaj.functionalities.sna.communityDetection.CommunityDetector;
import direnaj.functionalities.sna.communityDetection.DetectedCommunities;
import direnaj.util.d3Lib.D3GraphicType;

public class DirenajD3LibService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userId = (String) session.getAttribute("userID");
        String password = (String) session.getAttribute("pass");
        String campaignId = (String) session.getAttribute("campaignID");
        int skip = (Integer) session.getAttribute("skip");
        int limit = (Integer) session.getAttribute("limit");

        List<Relations> relations = (List<Relations>) session.getAttribute("graphRelation");
        D3GraphicType d3GraphicType = (D3GraphicType) session.getAttribute("d3LibFormat");
        double modularityValue = Double.valueOf((String)session.getAttribute("modularityValue"));

        DetectedCommunities communitiesInCampaign = CommunityDetector.getCommunitiesInCampaign(userId, password,
                campaignId, skip, limit, Double.valueOf(modularityValue).doubleValue(), relations);

        String communityJSon;
        try {
            communityJSon = communitiesInCampaign.getJsonOfCommunities(d3GraphicType);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = response.getWriter();

        //        String jsonStr = "{" + "\"name\": \"flare\"," + "\"children\": [" + "{" + "\"name\": \"analytics\","
        //                + "\"children\": [" + "{" + " \"name\": \"cluster\"," + "\"children\": ["
        //                + "{\"name\": \"AgglomerativeCluster\", \"size\": 3938},"
        //                + "{\"name\": \"CommunityStructure\", \"size\": 3812},"
        //                + "{\"name\": \"HierarchicalCluster\", \"size\": 6714}," + "{\"name\": \"MergeEdge\", \"size\": 743}"
        //                + "]" + "}" + "]" + "}" + "]" + "}";

        printout.println(communityJSon);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

}
