package emotion.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import emotion.shared.EmoAnalysisDO;
import emotion.shared.EmoDO;
import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoResultDO;
import emotion.shared.EmoResultValue;
import emotion.shared.EmoTweet;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EmotionWeb implements EntryPoint
{
   private final EmotionWebServiceAsync emotionWebService = GWT.create(EmotionWebService.class);

   private DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
   private SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
   
   private HorizontalPanel headerPanel = new HorizontalPanel();
   private HorizontalPanel headerRightPanel = new HorizontalPanel();
   private HorizontalPanel headerMiddlePanel = new HorizontalPanel();
   private HorizontalPanel headerHoldPanel = new HorizontalPanel();
   private TextBox searchTextBox = new TextBox();
   private RadioButton semRadioButton = new RadioButton("Semantic", "Semantic Analysis");
   private boolean semRadioButtonValue = false;
   private RadioButton usrSearchRadioButton = new RadioButton("UserSearch", "User Search");
   private boolean usrSearchRadioButtonValue = false;
   
   private RadioButton newsDataRadioButton = new RadioButton("newsData", "News Data Analysis");
   private boolean newsDataRadioButtonValue = false;
   private RadioButton userDataRadioButton = new RadioButton("userData", "User Data Analysis");
   private boolean userDataRadioButtonValue = false;
   
   private ToggleButton toggleButton = new ToggleButton("DB", "DB ON");
   private Button searchButton = new Button("Search");
   private Button setJobButton = new Button("Set Job");
   private Button getJobButton = new Button("Get Job");
   private Button analyzeButton = new Button("Analyze");
   private Label headerLabel = new Label("TWEMOTION");
   private Label twiggleHeaderLabel = new Label("twiggle");
   private Label headerNumberOfTweetsLabelText = new Label("# of Tweets:");
   private Label headerNumberOfTweetsLabel = new Label("0");
   private Label startDateLabel = new Label("Start Date \t: ");
   private Label endDateLabel = new Label("End Date \t: ");
   private DateBox startDateBoxLabel = new DateBox();
   private DateBox endDateBoxLabel = new DateBox();
   private VerticalPanel dateVerticalPanel = new VerticalPanel();
   private VerticalPanel buttonCriteriaVerticalPanel = new VerticalPanel();
   private VerticalPanel buttonDBAnalysisVerticalPanel = new VerticalPanel();
   private HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();
   private VerticalPanel navigationPanel = new VerticalPanel();
   private VerticalPanel mainverticalPanel = new VerticalPanel();
   private FlexTable wordListTable = new FlexTable();
   private TabLayoutPanel mainPanel = new TabLayoutPanel(1.9, Unit.EM);
   private HashMap<String,EmoDO> tabHashMap = new HashMap<String,EmoDO>();
   private ListBox  rightJobListBox = new ListBox ();
   private ListBox  middleArcListBox = new ListBox ();
   private ScrollPanel twiggleScrollPanel = new ScrollPanel();
   private HorizontalPanel twiggleRightPanel = new HorizontalPanel();
   
   public void onModuleLoad()
   {
     // runEmotionWeb();
      runtwiggle();
   }

   private void runtwiggle()
   {
      RootLayoutPanel.get().add(dockLayoutPanel);
      dockLayoutPanel.addStyleName("body");
      preparetwiggleHeader();
      prepareTwiggleMain();
      RootLayoutPanel.get().addStyleName("body");
   }



   private void preparetwiggleHeader()
   {
      prepareTwiggleRightPanel();
      twiggleRightPanel.setVisible(false);
      
      /*
      twiggleHeaderLabel.setStyleName("headertwiggleLabel");
      twiggleHeaderLabel.addStyleName("common_first_color");
    //  twiggleHeaderLabel.set
   */
     
      Image twiggleLogo = new Image("image/twiggle.png");  
      
      twiggleLogo.setSize("175px", "52px");
      
      searchButton.setStyleName("searchButton");
      searchButton.addStyleName("common_second_color");
      
      searchButton.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) 
        {
          searchAndReturn();
        }
      });
      
      searchTextBox.setStyleName("searchTextBox");
      searchTextBox.addStyleName("common_second_color");
      
      searchTextBox.addKeyPressHandler(new KeyPressHandler() {
         public void onKeyPress(KeyPressEvent event)
         {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)        
            {
               searchAndReturn();
            }
         }
      });

      
      toggleButton.addClickHandler(new ClickHandler() {
         public void onClick(ClickEvent event) {
           if (toggleButton.isDown()) 
           {
              twiggleRightPanel.setVisible(true);
           } 
           else 
           {
              twiggleRightPanel.setVisible(false);
           }
         }
       });
      
      toggleButton.setStyleName("toggleButton");
      toggleButton.addStyleName("common_second_color");
      

      
      
      semRadioButton.addStyleName("radioButton");
      semRadioButton.addClickHandler(new ClickHandler() 
      {
         public void onClick(ClickEvent event) 
         {
            semRadioButton.setValue(!semRadioButtonValue);
            semRadioButtonValue =!semRadioButtonValue;
         }
      }
      );
      
      usrSearchRadioButton.addStyleName("radioButton");
      usrSearchRadioButton.addClickHandler(new ClickHandler() 
      {
         public void onClick(ClickEvent event) 
         {
            usrSearchRadioButton.setValue(!usrSearchRadioButtonValue);
            usrSearchRadioButtonValue =!usrSearchRadioButtonValue;
         }
      }
      );
      
      buttonCriteriaVerticalPanel.addStyleName("buttonVerticalPanel");
      buttonCriteriaVerticalPanel.add(usrSearchRadioButton);
      buttonCriteriaVerticalPanel.add(semRadioButton);
      buttonCriteriaVerticalPanel.add(usrSearchRadioButton);
      
      
      
      
      newsDataRadioButton.addStyleName("radioButton");
      newsDataRadioButton.addClickHandler(new ClickHandler() 
      {
         public void onClick(ClickEvent event) 
         {
            twiggleRightPanel.setVisible(!newsDataRadioButtonValue);
            newsDataRadioButton.setValue(!newsDataRadioButtonValue);
            newsDataRadioButtonValue =!newsDataRadioButtonValue;
         }
      }
      );
      
      userDataRadioButton.addStyleName("radioButton");
      userDataRadioButton.addClickHandler(new ClickHandler() 
      {
         public void onClick(ClickEvent event) 
         {
            twiggleRightPanel.setVisible(!userDataRadioButtonValue);
            userDataRadioButton.setValue(!userDataRadioButtonValue);
            userDataRadioButtonValue =!userDataRadioButtonValue;
         }
      }
      );
      
      
      
      buttonDBAnalysisVerticalPanel.addStyleName("buttonVerticalPanel");
      buttonDBAnalysisVerticalPanel.add(newsDataRadioButton);
      buttonDBAnalysisVerticalPanel.add(userDataRadioButton);
      
      
      
      buttonHorizontalPanel.add(buttonCriteriaVerticalPanel);
      buttonHorizontalPanel.add(buttonDBAnalysisVerticalPanel);
      
      
      PushButton pushButton = new PushButton("Jump", "Jump?", new ClickHandler() {
         public void onClick(ClickEvent event) {

         }
       });
      pushButton.setStyleName("pushButton");
      pushButton.addStyleName("common_second_color");

      
      
      headerPanel.add(twiggleLogo);
      headerPanel.add(searchTextBox);
      headerPanel.add(searchButton);
      headerPanel.add(buttonHorizontalPanel);
      
      headerPanel.setSpacing(10);
   //   headerPanel.addStyleName("twiggleHeaderPanel");
      headerHoldPanel.add(headerPanel);
      
      
      headerHoldPanel.add(twiggleRightPanel);
      
 //     headerHoldPanel.addStyleName("twiggleHeaderPanel");
      dockLayoutPanel.addNorth(headerHoldPanel, 7);
    //  headerPanel.setStyleName("headerPanel");
    //  headerPanel.addStyleName("common_background_color");
    //  headerPanel.addStyleName("common_panel_border_style");
   }
   

   private void prepareTwiggleRightPanel()
   {
      updateArcJobList();
      
      twiggleRightPanel.setStyleName("headerTwiggleRightPanel");
      twiggleRightPanel.addStyleName("common_background_color");
      twiggleRightPanel.addStyleName("common_panel_border_style");
      
      middleArcListBox.addStyleName("middleTwiggleArcListBox");
      
      twiggleRightPanel.add(middleArcListBox);
      
      HorizontalPanel startDateHorizontalPanel = new HorizontalPanel();
      startDateLabel.addStyleName("dateObject");
      startDateBoxLabel.addStyleName("dateBox");
      startDateHorizontalPanel.add(startDateLabel);
      startDateHorizontalPanel.add(startDateBoxLabel);

      startDateHorizontalPanel.addStyleName("dateHorizontalPanel");
      HorizontalPanel endDateHorizontalPanel = new HorizontalPanel();
      endDateLabel.addStyleName("dateObject");
      endDateBoxLabel.addStyleName("dateBox");
      endDateHorizontalPanel.add(endDateLabel);
      endDateHorizontalPanel.add(endDateBoxLabel);
      endDateHorizontalPanel.addStyleName("dateHorizontalPanel");
      dateVerticalPanel.add(startDateHorizontalPanel);
      dateVerticalPanel.add(endDateHorizontalPanel);
      
      dateVerticalPanel.addStyleName("dateVerticalPanel");
      
      twiggleRightPanel.add(dateVerticalPanel);
      

      
      analyzeButton.setStyleName("analyzeButton");
      analyzeButton.addStyleName("common_second_color");
      
      analyzeButton.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) 
        {
          performAnalysis();
        }
      });
       
      twiggleRightPanel.add(analyzeButton); 
   }

   protected void searchAndReturn()
   {
      final String searchedWord = searchTextBox.getText();
      AsyncCallback<EmoResultDO> emoWebCB = new AsyncCallback<EmoResultDO>() 
      {
         @Override
         public void onFailure(Throwable caught)
         {
           
         }

         @Override
         public void onSuccess(EmoResultDO emoResultDO)
         {
            if(emoResultDO.isSemantic())
            {
               updateWikiandTweetTables(searchedWord,emoResultDO);
            }
            else
            {
               updateTweetsTable(searchedWord,emoResultDO);
            }               
         }
      }; 
      if(usrSearchRadioButtonValue)
      { 
         emotionWebService.searchUserAndReturnResults(searchTextBox.getText(),semRadioButtonValue, emoWebCB);
      } 
      else
      {
         emotionWebService.searchAndReturnResults(searchTextBox.getText(),semRadioButtonValue, emoWebCB);
      }   
        
   }

   
   protected void updateTweetsTable(String searchedWord, EmoResultDO emoResultDO)
   {
      twiggleScrollPanel.clear();
      FlexTable twitterTable = new FlexTable();
      HashMap<Long, ArrayList<EmoNvNTriplets>> tweetIdToNvNTripletsArrListHashMap = emoResultDO.getTweetIdToNvNTripletsArrListHashMap();
      HashMap<Long, EmoTweet> nvnTweetsHashMap = emoResultDO.getAllNVNTweetsHashMap();
      if(!nvnTweetsHashMap.isEmpty())
      {
         twitterTable.setTitle(searchedWord);
         twitterTable.setText(0, 0, "#");
         twitterTable.setText(0, 1, "Image");
         twitterTable.setText(0, 2, "User");
         twitterTable.setText(0, 3, "Tweet");
         twitterTable.setText(0, 4, "Date");
         twitterTable.setText(0, 5, "News Pattern");
         twitterTable.setCellPadding(6);
         twitterTable.getRowFormatter().addStyleName(0, "twitterTableHeader");
         
         int row = 1;
         Iterator<Long> nvnTweetsHashMapIterator = nvnTweetsHashMap.keySet().iterator();
         VerticalPanel patternverticalPanel;
         while(nvnTweetsHashMapIterator.hasNext()) 
         {
            patternverticalPanel = new VerticalPanel();
            Long key = nvnTweetsHashMapIterator.next(); 
            EmoTweet emoTweet = nvnTweetsHashMap.get(key);
            twitterTable.setText(row, 0, Integer.toString(row));
            Image img = new Image(emoTweet.getImageUrl());
            img.setSize("50px", "50px");
            twitterTable.setWidget(row, 1, img);
            twitterTable.setText(row, 2, emoTweet.getUserName());
            twitterTable.setText(row, 3, emoTweet.getTweetText());
            twitterTable.setText(row, 4, emoTweet.getTweetDate().toString());
            
            ArrayList<EmoNvNTriplets> emoNVNTripletsArrList =  tweetIdToNvNTripletsArrListHashMap.get(emoTweet.getTweetID());
            for (Iterator<EmoNvNTriplets> iterator = emoNVNTripletsArrList.iterator(); iterator.hasNext();)
            {
               EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
               patternverticalPanel.add(new Label(emoNvNTriplets.toString()));  
            }
            
            twitterTable.setWidget(row, 5, patternverticalPanel);
            row++;
         }
      }
      twiggleScrollPanel.setWidget(twitterTable);
   }

   protected void updateWikiandTweetTables(String searchedWord, EmoResultDO emoResultDO)
   {
      twiggleScrollPanel.clear();
      VerticalPanel resultVertPanel = null;
      Label absTextLabel = null;
      Label linkTextLabel = null;
      Label tweetTextLabel = null;
      Label emotionTextLabel = null;
      Label emotionOccLabel = null;
      ArrayList<String> hashTags = emoResultDO.getHashTags();
      FlexTable resultWikiTable = new FlexTable();
      ArrayList<EmoResultValue> emoResultValueArrList = emoResultDO.getEmoResultValueArrayList();
      
      if(emoResultValueArrList.size()>0)
      {
         
        // resultWikiTable.setTitle(key);
      /*   
         resultWikiTable.setText(0, 0, "#");
         resultWikiTable.setText(0, 1, "Image");
         resultWikiTable.setText(0, 2, "Name");
         resultWikiTable.setText(0, 3, "Description");
         resultWikiTable.setCellPadding(6);
         resultWikiTable.getRowFormatter().addStyleName(0, "twitterTableHeader");
       */  
         for (int i =1; i <=emoResultValueArrList.size(); i++)
         {
            EmoResultValue emoResultValue = emoResultValueArrList.get(i-1);
            resultVertPanel = new VerticalPanel();
        //    resultWikiTable.setText(i, 0, Integer.toString(i));
            Image img=null;
            if(emoResultValue.getImageUrl()!=null)
            {
               img = new Image(emoResultValue.getImageUrl());   
            }
            else
            {
               img = new Image("image/noImageAvailable.jpg");
            }   
            
            img.setSize("75px", "75px");
            resultWikiTable.setWidget(i, 1, img);
            
            HTML exthyperLink = new HTML("<b><a href="+emoResultValue.getWikiUrl()+">"+emoResultValue.getName()+"</a></b>");
     //       resultWikiTable.setWidget(i, 2, exthyperLink);
      //      resultWikiTable.setText(i, 3, emoResultValue.getAbstractText());
      //      ExternalHyperlink  exthyperLink = new ExternalHyperlink (emoResultValue.getName(), emoResultValue.getWikiUrl());
            absTextLabel = new Label(emoResultValue.getAbstractText());
            linkTextLabel = new Label(emoResultValue.getWikiUrl());
            linkTextLabel.addStyleName("linkTextLabel");
            resultVertPanel.add(exthyperLink);
            resultVertPanel.add(absTextLabel);
            resultVertPanel.add(linkTextLabel);
            HashMap<String, Integer> tweetTextsHashMap = emoResultValue.getEmoTweetHashMap();
            if(!tweetTextsHashMap.isEmpty())
            {
               Iterator<String> tweetHashmapIterator = tweetTextsHashMap.keySet().iterator();
               while(tweetHashmapIterator.hasNext()) 
               {
                  tweetTextLabel = new Label((String) tweetHashmapIterator.next());
                  tweetTextLabel.addStyleName("tweetText"); 
                  resultVertPanel.add(tweetTextLabel);
               }  
            }

            FlexTable emotionTable = new FlexTable();
            HashMap<String, Integer> emotionOccHashMap = emoResultValue.getEmotionOccHashMap();
            if(emotionOccHashMap!=null && !emotionOccHashMap.isEmpty())
            {
               Iterator<String> emotionOccHashMapIterator = emotionOccHashMap.keySet().iterator();
               int row=1;
               while(emotionOccHashMapIterator.hasNext()) 
               {
                  String key = (String) emotionOccHashMapIterator.next();
                  emotionTextLabel = new Label(key);
                  emotionOccLabel = new Label(Integer.toString(emotionOccHashMap.get(key)));
                  emotionTable.setWidget(row, 1, emotionTextLabel);
                  emotionTable.setWidget(row, 2, emotionOccLabel);
              //    tweetTextLabel.addStyleName("tweetText"); 
                  row++;
               }  
            }
            resultVertPanel.add(emotionTable);
            resultWikiTable.setWidget(i, 2, resultVertPanel);
           // resultWikiTable.setWidget(i, 3, emotionTable);
            
         } 
      }
      twiggleScrollPanel.setWidget(resultWikiTable);
   }

   private void prepareTwiggleMain()
   {
   //   mainverticalPanel.addStyleName("mainPanel"); 
      twiggleScrollPanel.addStyleName("mainPanel");
      dockLayoutPanel.add(twiggleScrollPanel);
  //   dockLayoutPanel.add(mainverticalPanel);
   //   mainverticalPanel.add(twiggleScrollPanel);
   }  

   private void runEmotionWeb()
   {
      RootLayoutPanel.get().add(splitLayoutPanel);
      splitLayoutPanel.addStyleName("body");
      prepareHeader();
      prepareNavigation();
      prepareMain();
      RootLayoutPanel.get().addStyleName("body"); 
   }

   private void prepareMain()
   {
      mainPanel.addStyleName("mainPanel"); 
      splitLayoutPanel.add(mainPanel);
   }

   protected void setSaveJob()
   {
     String selectedItem  = rightJobListBox.getItemText(rightJobListBox.getSelectedIndex());
     AsyncCallback<Void> sjWebCB = new AsyncCallback<Void>() 
     {

        @Override
        public void onFailure(Throwable caught)
        {
           
        }

      @Override
      public void onSuccess(Void result)
      {
         
         
      }

     };

     emotionWebService.saveJob(selectedItem, sjWebCB);
      
   }

   private void prepareNavigation()
   {
      navigationPanel.add(wordListTable);
      navigationPanel.addStyleName("navigationPanel");
      
      ScrollPanel scrollPanel = new ScrollPanel();
      scrollPanel.setWidget(navigationPanel);
      scrollPanel.addStyleName("scrollPanel");
      
      
      splitLayoutPanel.addWest(scrollPanel,208);
   }

   private void prepareHeader()
   { 
      prepareMainHeader();
      prepareMiddleHeader();
      prepareRightHeader();
      headerHoldPanel.add(headerPanel);
      headerHoldPanel.add(headerMiddlePanel);
      headerHoldPanel.add(headerRightPanel);
      headerRightPanel.setVisible(false);
      headerHoldPanel.addStyleName("headerHoldPanel");
      splitLayoutPanel.addNorth(headerHoldPanel, 96);
   }

   private void prepareMainHeader()
   {
      headerLabel.setStyleName("headerLabel");
      headerLabel.addStyleName("common_first_color");
      
      searchButton.setStyleName("searchButton");
      searchButton.addStyleName("common_second_color");
      
      searchButton.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) 
        {
          searchTweet();
        }
      });
      
      searchTextBox.setStyleName("searchTextBox");
      searchTextBox.addStyleName("common_second_color");
      
      searchTextBox.addKeyPressHandler(new KeyPressHandler() {
         public void onKeyPress(KeyPressEvent event)
         {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)        
            {
               searchTweet();
            }
         }
      });
      
      headerPanel.add(headerLabel);
      headerPanel.add(searchTextBox);
      headerPanel.add(searchButton);
      
      headerPanel.setSpacing(10);
      headerPanel.setStyleName("headerPanel");
      headerPanel.addStyleName("common_background_color");
      headerPanel.addStyleName("common_panel_border_style");
   }
   
   private void prepareRightHeader()
   {
      headerRightPanel.setStyleName("headerRightPanel");
      headerRightPanel.addStyleName("common_background_color");
      headerRightPanel.addStyleName("common_panel_border_style");
      
      rightJobListBox.addStyleName("listBox");
      
      headerRightPanel.add(rightJobListBox);
      
      setJobButton.setStyleName("saveJobButton");
      setJobButton.addStyleName("common_second_color");
      
      setJobButton.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) 
        {
          setSaveJob();
        }
      });
      headerRightPanel.add(setJobButton);
   }   
   
   private void prepareMiddleHeader()
   {
      updateArcJobList();
      
      headerMiddlePanel.setStyleName("headerMiddlePanel");
      headerMiddlePanel.addStyleName("common_background_color");
      headerMiddlePanel.addStyleName("common_panel_border_style");
      
      middleArcListBox.addStyleName("middleArcListBox");
      
      headerMiddlePanel.add(middleArcListBox);
      
      getJobButton.setStyleName("getJobButton");
      getJobButton.addStyleName("common_second_color");
      
      getJobButton.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) 
        {
          getJob();
        }
      });
      headerMiddlePanel.add(getJobButton);
   }

   private void updateArcJobList()
   {
      AsyncCallback<String[]> emoWebCB = new AsyncCallback<String[]>() 
      {

         @Override
         public void onFailure(Throwable caught)
         {
          
         }

         @Override
         public void onSuccess(String[] jobs)
         {
            setArcJobList(jobs);
         }
      };
      emotionWebService.getAllJobs(emoWebCB);
      
   }

   protected void setArcJobList(String[] jobs)
   {
      middleArcListBox.clear();
      for (int i = 0; i < jobs.length; i++)
      {
         middleArcListBox.addItem(jobs[i]);
      } 
   }
   
   
   protected void performAnalysis()
   {
      final EmoAnalysisDO emoAnalysisDO= new EmoAnalysisDO();
      emoAnalysisDO.setKeyword(middleArcListBox.getItemText(middleArcListBox.getSelectedIndex()));
      emoAnalysisDO.setSemanticAnalysis(semRadioButtonValue);
      emoAnalysisDO.setStartDate(startDateBoxLabel.getValue());
      emoAnalysisDO.setEndDate(endDateBoxLabel.getValue());
      
      final String selectedItem  = middleArcListBox.getItemText(middleArcListBox.getSelectedIndex());
      AsyncCallback<EmoResultDO> sjWebCB = new AsyncCallback<EmoResultDO>() 
      {

         @Override
         public void onFailure(Throwable caught)
         {
            
            
         }

         @Override
         public void onSuccess(EmoResultDO emoResultDO)
         {
            if(emoResultDO.isSemantic())
            {
               updateWikiandTweetTables(selectedItem,emoResultDO);
            }
            else
            {
               updateTweetsTable(selectedItem,emoResultDO);
            }
         }
      };
      emotionWebService.performAnalysis(emoAnalysisDO, sjWebCB); 
   }   
   

   protected void getJob()
   {
      final String selectedItem  = middleArcListBox.getItemText(middleArcListBox.getSelectedIndex());
      AsyncCallback<EmoResultDO> sjWebCB = new AsyncCallback<EmoResultDO>() 
      {

         @Override
         public void onFailure(Throwable caught)
         {
         
            
         }

         @Override
         public void onSuccess(EmoResultDO emoResultDO)
         {
            updateResultTable(emoResultDO,selectedItem);
            
         }
      };
      emotionWebService.getJob(selectedItem, sjWebCB); 
   }

   protected void searchTweet()
   {
      final String searchedWord = searchTextBox.getText();
      //&& !tabHashMap.containsKey(searchedWord)
      if(searchedWord!= null && !tabHashMap.containsKey(searchedWord))
      {
         AsyncCallback<EmoDO> emoWebCB = new AsyncCallback<EmoDO>() 
         {

            @Override
            public void onFailure(Throwable caught)
            {
 
            }

            @Override
            public void onSuccess(EmoDO result)
            {
               if(result!=null && result.getEmoTweets()!=null && result.getEmoTweets().size()>0)
               {
                  updateTables(searchedWord,result);
               }
            }
         };
         
         emotionWebService.searchWordOnTwitter(searchTextBox.getText(), emoWebCB);
      }
   }

   private void updateTables(String searchedWord, EmoDO result)
   {
      updateTabHashMap(searchedWord, result);
      updateTweetTable(searchedWord, result.getEmoTweets());
      updateWordTable(result.getSortedEmotionHashMapByWord());

      
      updateLisBox();
      headerRightPanel.setVisible(true);
   }

   private void updateLisBox()
   {
      rightJobListBox.clear();
      Set<String> keySet=tabHashMap.keySet();  
      Iterator<String> iterator=keySet.iterator();  
      while(iterator.hasNext()) 
      {
         String val = (String) iterator.next();
         rightJobListBox.addItem(val);
      }
   }

   private void updateTabHashMap(String searchedWord, EmoDO result)
   {
      tabHashMap.put(searchedWord, result);
      
   }

   private void updateWordTable(LinkedHashMap<String, Integer> sortedEmotionHashMapByWord)
   {
      wordListTable.setText(0, 0, "Word");
      wordListTable.setText(0, 1, "Count");
      wordListTable.setCellPadding(6);
      wordListTable.getRowFormatter().addStyleName(0,"wordListTableHeader");
      wordListTable.setStyleName("wordListTable");
      wordListTable.addStyleName("common_panel_border_style");
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      int row = 1;
      while(HashmapIterator.hasNext()) 
      {
         Object key = HashmapIterator.next(); 
         Integer val = sortedEmotionHashMapByWord.get(key);
         wordListTable.setText(row, 0, (String) key);
         wordListTable.setText(row, 1, Integer.toString(val));
         row++;
      } 
   }
   
   private void updateResultTable(EmoResultDO emoResultDO, String selectedItem)
   {
      LinkedHashMap<String,ArrayList<EmoResultValue>> emoResultValueLinkedHMap = emoResultDO.getEmoResultValues();
      ArrayList<String> hashTags = emoResultDO.getHashTags();
      TabLayoutPanel resulTabLayoutPanel = new TabLayoutPanel(1.9, Unit.EM);
      
      Iterator<String> HashmapIterator = emoResultValueLinkedHMap.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         ArrayList<EmoResultValue> emoResultValuArrList = emoResultValueLinkedHMap.get(key);
         FlexTable resultWikiTable = new FlexTable();
         if(emoResultValuArrList.size()>0)
         {
            resultWikiTable.setTitle(key);
            resultWikiTable.setText(0, 0, "#");
            resultWikiTable.setText(0, 1, "Image");
            resultWikiTable.setText(0, 2, "Name");
            resultWikiTable.setText(0, 3, "Description");
       //     resultWikiTable.setText(0, 4, "Date");
            resultWikiTable.setCellPadding(6);
            resultWikiTable.getRowFormatter().addStyleName(0, "twitterTableHeader");
            
            for (int i =1; i <=emoResultValuArrList.size(); i++)
            {
               EmoResultValue emoResultValue = emoResultValuArrList.get(i-1);
               resultWikiTable.setText(i, 0, Integer.toString(i));
               Image img=null;
               if(emoResultValue.getImageUrl()!=null)
               {
                  img = new Image(emoResultValue.getImageUrl());   
               }
               else
               {
                  img = new Image("image/noImageAvailable.jpg");
               }   
               
               img.setSize("50px", "50px");
               resultWikiTable.setWidget(i, 1, img);
               
               Hyperlink hyperLink = new Hyperlink(emoResultValue.getName(), emoResultValue.getWikiUrl());
               resultWikiTable.setWidget(i, 2, hyperLink);
               resultWikiTable.setText(i, 3, emoResultValue.getAbstractText());
            } 
         }
         ScrollPanel scrollPanel = new ScrollPanel();
         scrollPanel.setWidget(resultWikiTable);
         resulTabLayoutPanel.add(scrollPanel, key);
      }
      mainPanel.add(resulTabLayoutPanel, selectedItem+"_Res");

/*      
      final String sWord = searchedWord;
      
      HorizontalPanel tabPanel = new HorizontalPanel();
      
      Label tabLabel = new Label(searchedWord);
      tabLabel.addClickHandler(new ClickHandler() {
         public void onClick(ClickEvent event) {
            ((Image)((HorizontalPanel)((Label)event.getSource()).getParent()).getWidget(1)).setVisible(true);
            ((HorizontalPanel)mainPanel.getTabWidget(mainPanel.getSelectedIndex())).getWidget(1).setVisible(false);
            updateWordTable((tabHashMap.get(sWord)).getSortedEmotionHashMapByWord());
          }
        });
      tabPanel.add(tabLabel);
      
      Image closeImg = new Image("image/Close File icon.png");
      closeImg.setSize("15px", "15px");
      closeImg.setTitle(searchedWord);
      closeImg.setVisible(false);
      closeImg.addClickHandler(new ClickHandler() {
         public void onClick(ClickEvent event) {
            removeTab(((Image)event.getSource()).getTitle());
          }
        });
      tabPanel.add(closeImg);
      tabPanel.setTitle(searchedWord);
      ScrollPanel scrollPanel = new ScrollPanel();
      scrollPanel.setWidget(resultWikiTable);
      
      mainPanel.add(scrollPanel, tabPanel);
      ((HorizontalPanel)mainPanel.getTabWidget(mainPanel.getSelectedIndex())).getWidget(1).setVisible(false);
      mainPanel.selectTab(mainPanel.getWidgetCount()-1);
      ((HorizontalPanel)mainPanel.getTabWidget(mainPanel.getSelectedIndex())).getWidget(1).setVisible(true);
*/      
      
   }   

   private void updateTweetTable(String searchedWord, ArrayList<EmoTweet> emoTweetArr)
   {
      FlexTable twitterTable = new FlexTable();
      if(emoTweetArr.size()>0)
      {
         twitterTable.setTitle(searchedWord);
         twitterTable.setText(0, 0, "#");
         twitterTable.setText(0, 1, "Image");
         twitterTable.setText(0, 2, "User");
         twitterTable.setText(0, 3, "Tweet");
         twitterTable.setText(0, 4, "Date");
         twitterTable.setCellPadding(6);
         twitterTable.getRowFormatter().addStyleName(0, "twitterTableHeader");
         
         for (int i =1; i <=emoTweetArr.size(); i++)
         {
            EmoTweet emoTweet = emoTweetArr.get(i-1);
            twitterTable.setText(i, 0, Integer.toString(i));
            Image img = new Image(emoTweet.getImageUrl());
            img.setSize("50px", "50px");
            twitterTable.setWidget(i, 1, img);
            twitterTable.setText(i, 2, emoTweet.getUserName());
            twitterTable.setText(i, 3, emoTweet.getTweetText());
            twitterTable.setText(i, 4, emoTweet.getTweetDate().toString());
         } 
      }

      final String sWord = searchedWord;
      
      HorizontalPanel tabPanel = new HorizontalPanel();
      
      Label tabLabel = new Label(searchedWord);
      tabLabel.addClickHandler(new ClickHandler() {
         public void onClick(ClickEvent event) {
            ((Image)((HorizontalPanel)((Label)event.getSource()).getParent()).getWidget(1)).setVisible(true);
            ((HorizontalPanel)mainPanel.getTabWidget(mainPanel.getSelectedIndex())).getWidget(1).setVisible(false);
            updateWordTable((tabHashMap.get(sWord)).getSortedEmotionHashMapByWord());
          }
        });
      tabPanel.add(tabLabel);
      
      Image closeImg = new Image("image/Close File icon.png");
      closeImg.setSize("15px", "15px");
      closeImg.setTitle(searchedWord);
      closeImg.setVisible(false);
      closeImg.addClickHandler(new ClickHandler() {
         public void onClick(ClickEvent event) {
            removeTab(((Image)event.getSource()).getTitle());
          }
        });
      tabPanel.add(closeImg);
      tabPanel.setTitle(searchedWord);
      ScrollPanel scrollPanel = new ScrollPanel();
      scrollPanel.setWidget(twitterTable);
      
      mainPanel.add(scrollPanel, tabPanel);
      ((HorizontalPanel)mainPanel.getTabWidget(mainPanel.getSelectedIndex())).getWidget(1).setVisible(false);
      mainPanel.selectTab(mainPanel.getWidgetCount()-1);
      ((HorizontalPanel)mainPanel.getTabWidget(mainPanel.getSelectedIndex())).getWidget(1).setVisible(true);
   }

   protected void removeTab(String title)
   {
      if(mainPanel.getWidgetCount()!=1)
      {
         mainPanel.remove(mainPanel.getSelectedIndex());
         mainPanel.selectTab(0);
         tabHashMap.remove(title);
         updateWordTable((tabHashMap.get(mainPanel.getTabWidget(0).getTitle())).getSortedEmotionHashMapByWord());
         ((HorizontalPanel)mainPanel.getTabWidget(mainPanel.getSelectedIndex())).getWidget(1).setVisible(true);
         updateLisBox();
      }
   }
}
