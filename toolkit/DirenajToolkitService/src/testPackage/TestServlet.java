package testPackage;


import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;


import direnajDriver.*;

/** Simple servlet for testing.
 *
 * Uses DirenajDataHandler from direnajAdapter package
 */

//@WebServlet("/test")
public class TestServlet extends HttpServlet {
  /**
	 *
	 */
	private static final long serialVersionUID = 1L;

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
		  											throws ServletException, IOException {

    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();

    // getting parameters from html form
    Map<String, String[]> params = request.getParameterMap();

    String userId = params.get("userID")[0];
    String password = params.get("pass")[0];
    String campaignId = params.get("campaignID")[0];
    int skip = Integer.parseInt(params.get("skip")[0]);
    int limit = Integer.parseInt(params.get("limit")[0]);

    String operation = params.get("operationType")[0];

    System.out.println(operation);

    // Start preparing objects

    // constructing the driver object that will handle our data retrieval
    // and processing requests, using DirenajDataHandler as a backbone
    DirenajDriver driver = new DirenajDriver(userId, password);


    ArrayList<String> tweetTexts = new ArrayList<String>();
    ArrayList<ArrayList<String>> allTags = new ArrayList<ArrayList<String>>();
    ArrayList<Map.Entry<String, Integer>> counts = new ArrayList<Map.Entry<String,Integer>>();

    // building the result page

    String retHtmlStr = "<!DOCTYPE html>\n" +
    					"<html>\n" +
    					"<head><title>Direnaj Test Center</title></head>\n" +
    					"<body bgcolor=\"#fdf5e6\">\n" +
    					"<h1>DirenajDriver Test</h1>\n" +
    					"<p>CampaignID : <b>" + campaignId + "</b> | Operation : <b>" + operation +
    					"</b> | Limit : <b>" + limit + "</b></p><hr>\n";


    try {
    	if(operation.equals("getTags")) {

    		allTags = driver.collectHashtags(campaignId, skip, limit);

    		retHtmlStr += "<ul>";
    		for (ArrayList<String> singleTweetTags: allTags) {
    			//if(singleTweetTags.size() != 0) {
	    	    	retHtmlStr += "<li>";
	    	    	for(String tag : singleTweetTags) {
	    	    		retHtmlStr += tag + " - ";
	    	    	}
	    	    	retHtmlStr += "</li>";
    			//}
    	    }
    		retHtmlStr += "</ul>";

    	} else if(operation.equals("getTagCounts")) {


    		counts = driver.countHastags(campaignId, skip, limit);

    		PrintWriter pw = new PrintWriter(new File("/home/direnaj/direnaj/tools/tags.txt"));


    		boolean skipFirst = true;

    		retHtmlStr += "<table width=100% border=0><tr valign=top><td><ul>";
    		for(Map.Entry<String, Integer> entry : counts) {
    	    	retHtmlStr += "<li><font size=\"2\">(" + entry.getKey() + " : " + entry.getValue() + ")</font></li>";

    	    	if(skipFirst) {
    	    		skipFirst = false;
    	    		continue;
    	    	} else {
    	    		pw.println(entry.getKey() + "\t" + entry.getValue());
    	    	}
    	    }
    		retHtmlStr += "</ul></td><td align=right>";

    		pw.close();


    	    String cmd = "java -jar /home/direnaj/direnaj/envs/staging/toolkit/DirenajToolkitService/WebContent/WEB-INF/lib/ibm-word-cloud.jar "
    	    		+ "-c /home/direnaj/direnaj/tools/caner.conf -w 800 -h 600 "
    	    		+ "-i /home/direnaj/direnaj/tools/tags.txt "
    	    		+ "-o /home/direnaj/direnaj/tools/images/caner.png";

    	    Process proc = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd});

    	    BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

    	    String line = null;
    	    while((line = br.readLine()) != null) {
    	    	System.out.println(line);
    	    }

            proc.waitFor();

            retHtmlStr += "<img src=\"images/caner.png\"/>   </td></tr></table>";


    	} else if(operation.equals("getTweetTexts")) {

    		tweetTexts = driver.collectTweetTexts(campaignId, skip, limit);

    		retHtmlStr += "<ul>";
    		for(String tweetText : tweetTexts) {
    			retHtmlStr += "<li><font size=\"2\">" + tweetText + "</font></li>";
    		}
    		retHtmlStr += "</ul>";

    	} else if(operation.equals("getSingleTweet")) {

    		retHtmlStr += driver.getSingleTweetInfo(campaignId, skip, limit);

    	} else {
    		retHtmlStr += "OPERATION NOT SUPPORTED!";
    	}

    } catch(Exception e) {
    	out.println(e.getMessage());
    	out.close();
    	System.out.println(e);
    }



    out.println(retHtmlStr + "</body></html>");
    out.close();
  }
}
