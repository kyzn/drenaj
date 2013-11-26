package testPackage.nlpTests;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import direnaj.functionalities.sna.AnalysisIntervals;
import direnaj.functionalities.sna.HashtagAnalysis;

public class HashtagServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String userId = session.getAttribute("userID").toString();
        String password = session.getAttribute("pass").toString();
        String campaignId = session.getAttribute("campaignID").toString();
        String skip = session.getAttribute("skip").toString();
        String limit = session.getAttribute("limit").toString();

        String timeDuration = request.getParameter("timeAmount");
        String searchedHashtag = request.getParameter("searchedHashtagName");
        String timeInterval = request.getParameter("timeInterval");

        HashtagAnalysis hashtagAnalysis = new HashtagAnalysis();
        try {
            HashMap<DateTime, Integer> timelyIntervalAnalysis = hashtagAnalysis.getTimelyIntervalAnalysis(userId, password, campaignId, Integer.valueOf(skip),
                    Integer.valueOf(limit), searchedHashtag, AnalysisIntervals.valueOf(timeInterval), timeDuration);
            
        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
