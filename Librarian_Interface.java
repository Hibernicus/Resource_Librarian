import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
//import com.resources.*;
import java.io.*;
import java.awt.*;
import java.util.Scanner;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.JFrame;

//import com.resources.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.lang.reflect.Field;
//import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult; 
import org.w3c.dom.Attr;

//import java.io.FileOutputStream;

import javax.xml.transform.OutputKeys;
/**
 * This application is a tool to catalog and classify educational resources in accordance with 
 * the Adult Literacy and Numeracy Core Curricula and the Functional Skills Standards for
 * English, Maths & ICT
 * @author (John Nelson) 
 * @version (2013)
 */
public class Librarian_Interface extends World implements Misc_ButtonListener, TagButtonListener
{
    String home; // this String holds the path to the host directory for the files used by this app
    File librarian_home; // the file where the librarian home string can be found
    String cataloging;
    Scanner scanner; // a scanner to read text from files
    Resource_Finder res_finder; // see Resource_Finder.java for details
    
    String dBase; // path to Tomcat
    
    // variables to set up interface buttons and graphics
    XML_Button_Reader xMBR; //xml file miscellaneous button reader
    XML_Button_Reader xSBR; //xml file subject button reader
    XML_Button_Reader xLBR; //xml file level button reader
    XML_Button_Reader xCFBR; //xml file miscellaneous button reader
    XML_Button_Reader xCCodesBR; //xml file core curriculum code button reader
    XML_Button_Reader xFCodesBR; //xml file functional skills code button reader
    private Misc_Button open;
    private ArrayList<Misc_Button> mButtons;
    private ArrayList<TagButton> sButtons;
    private ArrayList<String> subjects;
    private ArrayList<TagButton> lButtons;
    private ArrayList<String> levels;
    private ArrayList<Misc_Button> cButtons;
    private ArrayList<String> cCodes;
    private ArrayList<Misc_Button> fButtons;
    private ArrayList<String> fCodes;
    
    /* a string array to hold directory names for curriculum sub divisions
     * there are 2 null strings and 18 literals whose index corresponds to
     * the id number of a "misc_button" (Greenfoot UI library buttons)
     */
    private String[] dirs; 
    private String resDir; // the path to the resources directory (from external text file)
    private String resource_name; //see "openFile()"
    private String resource_path; //see "openFile()"
    private String resource_dir; //see "openFile()"
    private CBoxes core_boxes; // see CBoxes.java
    private String activePage; // tracks the current page of the interface
    private Strat_Boxes strats; // see Strat_Boxes.java
    private Resource_Descriptor desc_frame; // see  Resource_Descriptor.java
   // a hashed, serialised key is stored externally
   private final static String auth_key = "Author: John Nelson, February 2013";
   //private String outWrite; // used to find resources and to write xml data - may be a duplicate of resDir
   public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss"; // to date-stamp backed-up folders
   public StringBuilder sb; // to remove invalid chars from date stamp (convert to string to be used as a directory name)
   
    /*
     * Constructor for objects of class Librarian_Interface.
     * 
     */
    public Librarian_Interface()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(710, 800, 1, false); 
        setBackground("images//bricks2.jpg");
        
        setHomePaths(); // method description below
        /// backItUp(); // method description below 22 Feb 2016
        // try statement to read the authentication key
        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new String("home_files//authentication_key.ser")));
            int key = (Integer) is.readObject();
            if(key == auth_key.hashCode()){
                loadHomePage(); // method description below
               // searchInt = 0;
             }
             
        }catch(Exception ex){
            ex.printStackTrace();
          }
       
    }
    
    // a method to set up the home paths
    public void setHomePaths(){
        try{
            librarian_home = new File("home_files\\librarian_home.txt");
            scanner = new Scanner(librarian_home);
            home = scanner.nextLine();
            scanner = new Scanner(new File("home_files\\resources_home.txt"));
            // 14.8 outWrite = new String(scanner.nextLine());
            resDir = scanner.nextLine(); //14.8
            scanner = new Scanner(new File("home_files\\database_home.txt"));
            dBase = new String(resDir + scanner.nextLine()); // 22 Feb 2016
            // Tomcat might not be in the reources folder???
            //dBase = new String(scanner.nextLine()); //22 Feb 2016 say exactly where Tomcat is
            //System.out.println(dBase);
           // outWrite = new String(resDir + "Resource Librarian\\"); //14.8
           // outWrite = new String(scanner.nextLine() + "//Resource Librarian//");
            
        }catch(Exception e){
            //System.out.println(e);
            System.out.print("one or more of the home files are missing or corrupted");    
        }
    }
    //convert the date to a string
    public static String now() {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                    return sdf.format(cal.getTime());
                }
    public void backItUp(){
        // strip colons from the date string (directory names don't allow colons)
        sb = new StringBuilder();
           for (int sbd = 0; sbd < now().length(); sbd++) {
                char c = now().charAt(sbd);
                if(c != ':'){
                    sb.append(c);
                }
            }
          
          //back up the tags/topics and resource XML folder (sent to Tomcat/bin)
          // new BackItUp(new File(outWrite + "XML"), new File(dBase + "backup//"+ sb.toString())); //edit 26.6.14
          new BackItUp(new File(dBase + "resources.xml"), new File(dBase + "backup//resources//-resources-"+ sb.toString() +".xml")); //edit 26.6.14
        /* rethink the above line - I appear to be trying to copy a file.xml and make it into a .dir*///edit 26.6.14
    }
    
    // actionListener for Misc_Buttons
    // button listener for action buttons from Greenfoot UI library
    public void buttonClicked(Misc_Button mButton){
        /*if "Open" is selected (button id: 0), check that the file has
         * not already been catalogued. If yes, reset homescreen, if no, loadSubjectPage().
         */
        
        if(mButton.getIDNumber() == 0){
          openFile();// method description below
          if (Resource_Finder.IN_CATALOG == true){
            Greenfoot.setWorld(new Librarian_Interface());
            Resource_Finder.IN_CATALOG = false;
          } else{
              resource_name = res_finder.getName();
              resource_path = res_finder.getPathName();
              resource_dir = res_finder.getDirName();
              loadSubjectPage(); // method description below
               }
        }
        
        /* button id: 1 is the 'submit' button on the subjects page
         * this counts how many subjects have been selected
         * and adds their names to an ArrayList (utilised in the subsequent load<Subject>Page() methods)
         * then, takes the name of the first item in the ArrayList and uses a switch statemnt to determine 
         * which subject page to load (using the hashCodes of "maths", "english" & "ICT"
         * users can select more than one subject
         */
        else if(mButton.getIDNumber() == 1){
            subjects = new ArrayList<String>();
            for(int i = 0; i < sButtons.size(); i++){
                if(sButtons.get(i).getDown()){
                    subjects.add(sButtons.get(i).getName());
                }
            }
            if(subjects.size()!=0){
                int subj = subjects.get(0).hashCode();
            
                switch(subj){
                   case 74115659 : loadMathsPage();// method description below
                   break;
                   case -1603757456 : loadEnglishPage();// method description below
                   break;
                   case 72314 : loadICTPage();// method description below
                   break;
                }
            }
        }else if(mButton.getIDNumber() > 1 && mButton.getIDNumber() <11){
            getCoreBoxes(dirs[mButton.getIDNumber()], "Core Curriculum");
         }else if(mButton.getIDNumber() > 10 && mButton.getIDNumber() <20){
            getCoreBoxes(dirs[mButton.getIDNumber()], "Functional Skills");
         }
         
         //if only one subject is selected
         else if(mButton.getIDNumber() == 20){
             loadEndPage();
            }
         
            //if maths and english are selected maths gets loaded first
         else if(mButton.getIDNumber() == 21){
             loadEnglishPage();
            }
        
            // if maths and/or english and ICT are selected, ICT is last
            else if(mButton.getIDNumber() == 22){
             loadICTPage();
            }
            //set the topics for the respective page
            else if(mButton.getIDNumber() == 23){
             loadTopics("topic"); // method description below
            }
            
            //SEAL button
            else if(mButton.getIDNumber() == 24){
               getStratBoxes(home, mButtons.get(6).getName());
            }
            //ECM button
            else if(mButton.getIDNumber() == 25){
               getStratBoxes(home, mButtons.get(7).getName());
            }
            //Tag button
            else if(mButton.getIDNumber() == 26){
             loadTags("tag");
            }
             //REGARDS button
            else if(mButton.getIDNumber() == 27){
               getStratBoxes(home, mButtons.get(9).getName());
            }
            //SMSC button
            else if(mButton.getIDNumber() == 28){
               getStratBoxes(home, mButtons.get(10).getName());
               
            }
            //User defined "other" button (ID 30)
            else if(mButton.getIDNumber() == 30){
              loadUserDefined();
              //final 'Submit'
            }else if(mButton.getIDNumber() == 29){
               writeResource();
               killEmAll();
            }
   
    }
    
    // need to rethink this one. I don't want to add all the misc buttons to page 1
    public void getMisc_Buttons(){
        
       xMBR = new XML_Button_Reader(new String(home + "XML//misc_buttons//misc_buttons.xml"), "MiscButton");
        mButtons = new ArrayList<Misc_Button>();
        
        for(int k = 0; k < xMBR.dImage.size(); k++){
           mButtons.add(new Misc_Button(new String(home + xMBR.dImage.get(k)), 
           (new String(home + xMBR.sImage.get(k))), xMBR.names.get(k), xMBR.ids.get(k)));
           
         }   
    }
    
    public void loadHomePage(){
        dirs = new String[]{"", "", "R", "W", "SL", "N1", "N2", "MSS1", "MSS2", "HD1", "HD2", "R", 
                                "W", "SL", "Find", "Use", "DPC", "Interpret", "Analyse", "Represent" };
        /*english_levels = new ArrayList<String>();
        maths_levels = new ArrayList<String>();
        ict_levels = new ArrayList<String>();*/
        try{
            getMisc_Buttons();
            addObject(mButtons.get(0),xMBR.ex.get(0),xMBR.wy.get(0));
        }catch(Exception e){
           System.out.println(e);
            System.out.print("home page failed to load");
            System.out.print(home + " " + dBase + " " + resDir);
        }
    }
    
    public void openFile(){
        // create a new JFileChooser (my Resource_Finder.class)
        //res_finder = new Resource_Finder(outWrite); edit 25.6.14
        res_finder = new Resource_Finder(dBase); //edit 25.6.14
        try{
            // read an external text file that can change the JFileChooser directory
            scanner = new Scanner(new File("home_files//cataloging.txt")); 
            cataloging = new String(resDir + scanner.nextLine());
             res_finder.setCurrentDirectory(new File(cataloging));
           }catch(Exception e){
            System.out.println("something up with the file chooser");
        }
        res_finder.setDialogTitle("Add a file");
        res_finder.setPreferredSize(new Dimension(700,400));
        // peg the JFileChooser to a JFrame to make it visible
        res_finder.showOpenDialog(new JFrame());
        
    }
    // start of subject page methods
    
    /* TagButton listener - Tag Buttons do nothing except return their selected state
     * - from the Greenfoot UI Library
     */
    
    public void buttonClicked(TagButton tagButton){
    }
    
    public void loadSubjectPage(){
        clearPage(); //see method - clears the 'Open' button here
        try{
            getSubject_Buttons(); //adds the subject buttons
        }catch(Exception e){
            System.out.println("problem loading subject page");
        }
        addObject(mButtons.get(1),xMBR.ex.get(1),xMBR.wy.get(1)); // adds the subject 'submit' button
    }
    
    public void clearPage(){
        this.removeObjects( this.getObjects(Misc_Button.class) );
        this.removeObjects( this.getObjects(TagButton.class) );
        this.removeObjects( this.getObjects(CCFS.class) ); //// CCFS is just a non dynamic Greenfoot Actor with an image that acts as a label
    }
    
    /*set up subject page graphics
     * read the xml file, count the number of references, 
     * create array, add button info - such as image/path, xY coordinates,
     * name and numerical ID
     */
    
    public void getSubject_Buttons(){
        
       xSBR = new XML_Button_Reader(new String(home + "XML//subject_buttons//subject_buttons.xml"), "SubjectButton");
        sButtons = new ArrayList<TagButton>();
        
        for(int k = 0; k < xSBR.dImage.size(); k++){
           sButtons.add(new TagButton((new String(home + xSBR.dImage.get(k))), 
           (new String(home + xSBR.sImage.get(k))), xSBR.names.get(k)));
           addObject(sButtons.get(k),xSBR.ex.get(k),xSBR.wy.get(k));
         }   
    }
    
    public void loadMathsPage(){
        activePage = "Maths";
        getLevelButtons(activePage); // see method description
        
                int subj = 0;
                // maths on it's own - button id: 20
                if(!subjects.contains("ICT") && !subjects.contains("english")){ 
                    subj = 3;
                    // maths and ICT - button id: 22
                }else if(subjects.contains("ICT") && !subjects.contains("english")){
                    subj = 2;
                    // maths and English - button id: 21
                }else if(subjects.contains("english")){
                    subj = 1;
                }
            
                switch(subj){
                   /*each 'continue' button references the same image
                    * but has a different id number for action listening
                    */
                   
                   case 1 : addObject(mButtons.get(3),xMBR.ex.get(3),xMBR.wy.get(3)); //button id: 21
                   break;
                   case 2: addObject(mButtons.get(4),xMBR.ex.get(4),xMBR.wy.get(4)); //button id: 22
                   break;
                   case 3: addObject(mButtons.get(2),xMBR.ex.get(2),xMBR.wy.get(2)); // button id: 20
                   break;
                }      
                
             /*  Test code - leave in plaec for now
              * Misc_Button pop_tags = new Misc_Button();
              *  pop_tags.setImage(home + "images//misc_images//pop_tags1.png");
              *  addObject(pop_tags, 300, 340);
                */
                
    }
    public void loadEnglishPage(){
        activePage = "English";
        getLevelButtons(activePage); // see method description
       int subj = 0;
                // english on it's own: button id: 20
                if(!subjects.contains("ICT")){
                    subj = 3;
                 //english and ICT - button id: 22
                }else if(subjects.contains("ICT")){
                    subj = 2;
                }
            
                switch(subj){
                    
                   case 2: addObject(mButtons.get(4),xMBR.ex.get(4),xMBR.wy.get(4)); //button id: 22
                   break;
                   case 3: addObject(mButtons.get(2),xMBR.ex.get(2),xMBR.wy.get(2)); // button id: 20
                   break;
                }       
    }
    public void loadICTPage(){
        activePage = "ICT";
        getLevelButtons(activePage); // see method description
       //ICT on it's own - button id: 20
        addObject(mButtons.get(2),xMBR.ex.get(2),xMBR.wy.get(2)); // button id: 20
    }
    
    /*set up level button graphics
     * read the xml file, count the number of references, 
     * create array, add button info - such as image/path, XY coordinates,
     * name and numerical ID
     */
    public void getLevelButtons(String sub){ // 'sub' references the active subject page
        clearPage();
         addObject(mButtons.get(5),xMBR.ex.get(5),xMBR.wy.get(5));
         addObject(mButtons.get(8),xMBR.ex.get(8),xMBR.wy.get(8));
        try{
             xLBR = new XML_Button_Reader(new String(home + "XML//level_buttons//level_buttons.xml"), "LevelButton");
             lButtons = new ArrayList<TagButton>(); 
             /* 
             * iterate over lButtons if(!lButtons.contains(lButton.get(x).getName()){
             *     levels.add(lButton.get(x).getName());
             */
        
             for(int k = 0; k < xLBR.dImage.size(); k++){
                 lButtons.add(new TagButton((new String(home + xLBR.dImage.get(k))), 
                 (new String(home + xLBR.sImage.get(k))), xLBR.names.get(k)));
                 addObject(lButtons.get(k),xLBR.ex.get(k),xLBR.wy.get(k));
                }  
            }catch(Exception e){
                System.out.println("problem with loading level buttons");
            }
            loadCoreFunc(sub); // see method below
    }
    
    /*set up curriculum reference button graphics
     * read the xml file, add button info - such as image/path, xy coordinates,
     * name and numerical ID
     */
    public void loadCoreFunc(String subs){
        xCFBR = new XML_Button_Reader(new String(home + "XML//subject_buttons//ccfs_buttons.xml"), "CCFSButton");
           CCFS f = new CCFS(); // just a non dynamic Greenfoot Actor with an image that acts as a label
           f.setImage(new String(home + xCFBR.dImage.get(0)));
           addObject(f,xCFBR.ex.get(0),xCFBR.wy.get(0));
        if(subs.equals("Maths")|| subs.equals("English")){ 
           CCFS c = new CCFS(); // just a non dynamic Greenfoot Actor with an image that acts as a label
           c.setImage(new String(home + xCFBR.dImage.get(1)));
           addObject(c,xCFBR.ex.get(1),xCFBR.wy.get(1));
         }
         getFuncCodes(subs);// see method below
          
         // no core curriculum ICT codes so ignore 'ICT' as an argument
         if(!subs.equals("ICT")){
             getCoreCodes(subs); // see method below
           }
    }
    
    /*set up curriculum reference button graphics
     * read the xml file, count the number of references, 
     * create array, add button info - such as image/path, XY coordinates,
     * name and numerical ID
     */
    public void getCoreCodes(String subjekt){
        xCCodesBR = new XML_Button_Reader(new String(home + "XML//core_buttons//" + 
                        subjekt + "_buttons.xml"), new String(subjekt+"Button"));
        cButtons = new ArrayList<Misc_Button>();
        
        for(int k = 0; k < xCCodesBR.dImage.size(); k++){
           cButtons.add(new Misc_Button((new String(home + xCCodesBR.dImage.get(k))), 
           (new String(home + xCCodesBR.sImage.get(k))), xCCodesBR.names.get(k),xCCodesBR.ids.get(k)));
           addObject(cButtons.get(k),xCCodesBR.ex.get(k),xCCodesBR.wy.get(k));
         }
    }
    
    /*set up curriculum reference button graphics
     * read the xml file, count the number of references, 
     * create array, add button info - such as image/path, XY coordinates,
     * name and numerical ID
     */
    public void getFuncCodes(String subjekt){
        xFCodesBR = new XML_Button_Reader(new String(home + "XML//func_buttons//" + 
                        subjekt + "_buttons.xml"), new String(subjekt+"Button"));
        fButtons = new ArrayList<Misc_Button>();
        
        for(int k = 0; k < xFCodesBR.dImage.size(); k++){
           fButtons.add(new Misc_Button((new String(home + xFCodesBR.dImage.get(k))), 
           (new String(home + xFCodesBR.sImage.get(k))), xFCodesBR.names.get(k),xFCodesBR.ids.get(k)));
           addObject(fButtons.get(k),xFCodesBR.ex.get(k),xFCodesBR.wy.get(k)); 
         }
    }
    /*
     * read selected level buttons and add to arraylist
     * CBoxes.class uses the constructor arguments to
     * navigate to to txt files and store information in an ArrayList<String>
     * CBoxes.buildIt() creates a frame with checkbox and labels for each item in the 
     * array. The submit button adds selected labels to a static arraylist which will
     * be written to the resources.xml file at the end of execution
     */
    public void getCoreBoxes(String dirs, String cf){
        int j = 0;
        ArrayList<String> levs = new ArrayList<String>();
        for(int k = 0; k < lButtons.size(); k++){
            if(lButtons.get(k).getDown()){
                levs.add(lButtons.get(k).getName());
                j++;
            }
        }
        core_boxes = new CBoxes(j, cf, activePage, dirs);
        
        for(int c = 0; c < core_boxes.files.length;c++){
             core_boxes.files[c] = new String(home + "//codes//" + activePage + "//" +cf +"//" + 
                                       levs.get(c) + "//" + dirs + "//codes.txt");
                    //prevent a selected level from being added twice                   
                    if(!CBoxes.LEVELS.contains(levs.get(c))){
                        CBoxes.LEVELS.add(levs.get(c));
                    }               
        }
        core_boxes.buildIt();
    }    
    
    //described in Topic_Builder.java
    public void loadTopics(String t){
        //Topic_Builder topics = new Topic_Builder(outWrite, activePage, t, dBase); edit 25.6.14
        Topic_Builder topics = new Topic_Builder(activePage, t, dBase); //edit 25.6.14
        topics.buildIt();
    }
    
     //described in Tag_Builder.java
    public void loadTags(String t){
        //Tag_Builder tags = new Tag_Builder(outWrite, activePage, t, dBase); edit 25.6.14
        Tag_Builder tags = new Tag_Builder(activePage, t, dBase); // edit 25.6.14
        tags.buildIt();
    }
    
     //described in Tag_Builder.java
    public void loadUserDefined(){
        //Tag_Builder userTags = new Tag_Builder(outWrite, "User_Defined", "user_def", dBase); edit 25.6.14
        Tag_Builder userTags = new Tag_Builder("User_Defined", "user_def", dBase); //edit 25.6.14
        userTags.buildIt();
    }
    
    public void loadEndPage(){
        clearPage();
        desc_frame = new Resource_Descriptor(); // described in Resource_Descriptor.java
        loadStrategyButtons();// see below
        }
    
    /*set up strategy button graphics
     * read the xml file, count the number of references, 
     * create array, add button info - such as image/path, XY coordinates,
     * name and numerical ID
     */
    public void loadStrategyButtons(){
        addObject(mButtons.get(6),xMBR.ex.get(6),xMBR.wy.get(6)); // SEAL - button ID:24
        addObject(mButtons.get(7),xMBR.ex.get(7),xMBR.wy.get(7)); // ECM - button ID: 25
        addObject(mButtons.get(9),xMBR.ex.get(9),xMBR.wy.get(9)); // REGARDS - button ID: 27
        addObject(mButtons.get(10),xMBR.ex.get(10),xMBR.wy.get(10)); // SMSC- button ID: 28
        addObject(mButtons.get(11),xMBR.ex.get(11),xMBR.wy.get(11)); // Submit - button ID: 29
        addObject(mButtons.get(12),xMBR.ex.get(12),xMBR.wy.get(12)); // User_Defined 'other' - button ID: 30
    }
    
    //described in Strat_Boxes.java and CBoxes.java
    public void getStratBoxes(String ho_me, String str_at){
        strats = new Strat_Boxes(ho_me, str_at);
        strats.buildIt();
    }
    
    /*
     * write/append an xml file
     *  copied and pasted from the Internet
     *  very repetitive
     */
    class Resource_Writer{
        public void writeResource(String fileName){ 
        File docFile = new File(fileName);

        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(docFile);
        }catch (java.io.IOException e){
            System.out.println("Can't find the file");
        }catch (Exception e){
            System.out.print("Problem parsing the file.");
        }

        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();
        
        Element resElement = doc.createElement("Resource");

        Node updateText = doc.createTextNode("");
        resElement.appendChild(updateText);

            Element name = doc.createElement("resource_name");
            name.appendChild(updateText);
            Node nameNode = doc.createTextNode(resource_name);
            name.appendChild(nameNode);
            resElement.appendChild(name);
            
            Element absolute_path = doc.createElement("name");
            absolute_path.appendChild(updateText);
            Node pathNode = doc.createTextNode(resource_path);
            absolute_path.appendChild(pathNode);
            resElement.appendChild(absolute_path);
            
            Element subsElement = doc.createElement("subjects");
            subsElement.appendChild(updateText);
            Element subjectAllElement = doc.createElement("sub");
                Node subjectAllNode = doc.createTextNode("all");
                subjectAllElement.appendChild(subjectAllNode);
                subsElement.appendChild(subjectAllElement);
            for(int a = 0; a < subjects.size(); a++){
                Element subjectElement = doc.createElement("sub");
                Node subjectNode = doc.createTextNode(subjects.get(a));
                subjectElement.appendChild(subjectNode);
                subsElement.appendChild(subjectElement);
            } 
            resElement.appendChild(subsElement);
            
           Element mLevels = doc.createElement("maths_levels");
            mLevels.appendChild(updateText);
            Element mAllLev = doc.createElement("mLevel");
                Node mAllLevNode = doc.createTextNode("all");
                mAllLev.appendChild(mAllLevNode);
                mLevels.appendChild(mAllLev);
            for(int a = 0; a < CBoxes.MATHS_LEVELS.size(); a++){
                Element mLev = doc.createElement("mLevel");
                Node mLevNode = doc.createTextNode(CBoxes.MATHS_LEVELS.get(a));
                mLev.appendChild(mLevNode);
                mLevels.appendChild(mLev);
            }
            resElement.appendChild(mLevels);
            
            Element eLevels = doc.createElement("english_levels");
            eLevels.appendChild(updateText);
            Element eAllLev = doc.createElement("eLevel");
                Node eAllLevNode = doc.createTextNode("all");
                eAllLev.appendChild(eAllLevNode);
                eLevels.appendChild(eAllLev);
            for(int a = 0; a < CBoxes.ENGLISH_LEVELS.size(); a++){
                Element eLev = doc.createElement("eLevel");
                Node eLevNode = doc.createTextNode(CBoxes.ENGLISH_LEVELS.get(a));
                eLev.appendChild(eLevNode);
                eLevels.appendChild(eLev);
            }
            resElement.appendChild(eLevels);
                
            Element iLevels = doc.createElement("ict_levels");
            iLevels.appendChild(updateText);
            Element iAllLev = doc.createElement("iLevel");
                Node iAllLevNode = doc.createTextNode("all");
                iAllLev.appendChild(iAllLevNode);
                iLevels.appendChild(iAllLev);
            for(int a = 0; a < CBoxes.ICT_LEVELS.size(); a++){
                Element iLev = doc.createElement("iLevel");
                Node iLevNode = doc.createTextNode(CBoxes.ICT_LEVELS.get(a));
                iLev.appendChild(iLevNode);
                iLevels.appendChild(iLev);
            }
            resElement.appendChild(iLevels);
            
            Element curElements =  doc.createElement("core_codes");
            curElements.appendChild(updateText);
            for(int a = 0; a < CBoxes.ALL_CORE_CODES.size(); a++){
                Element curEl = doc.createElement("cc");
                Node curNode = doc.createTextNode(CBoxes.ALL_CORE_CODES.get(a));
                curEl.appendChild(curNode);
                curElements.appendChild(curEl);
            }
            resElement.appendChild(curElements);
            
            Element fsElements =  doc.createElement("fs_codes");
            fsElements.appendChild(updateText);
            for(int a = 0; a < CBoxes.ALL_FS_CODES.size(); a++){
                Element fsEl = doc.createElement("fs");
                Node fsNode = doc.createTextNode(CBoxes.ALL_FS_CODES.get(a));
                fsEl.appendChild(fsNode);
                fsElements.appendChild(fsEl);
            }
            resElement.appendChild(fsElements);
            
            Element mTopics =  doc.createElement("maths_topics"); 
             mTopics.appendChild(updateText);
             Element mathsAllEl = doc.createElement("m_topic");
                Node mathsAllNode = doc.createTextNode("all");
                mathsAllEl.appendChild(mathsAllNode);
                mTopics.appendChild(mathsAllEl);
            for(int a = 0; a < Topic_Builder.MATHS_TOPICS.size(); a++){
                Element mathsEl = doc.createElement("m_topic");
                Node mathsNode = doc.createTextNode(Topic_Builder.MATHS_TOPICS.get(a));
                mathsEl.appendChild(mathsNode);
                mTopics.appendChild(mathsEl);
            }
            resElement.appendChild(mTopics);
            
            Element eTopics =  doc.createElement("english_topics"); 
             eTopics.appendChild(updateText);
             Element englishAllEl = doc.createElement("e_topic");
                Node englishAllNode = doc.createTextNode("all");
                englishAllEl.appendChild(englishAllNode);
                eTopics.appendChild(englishAllEl);
            for(int a = 0; a < Topic_Builder.ENGLISH_TOPICS.size(); a++){
                Element englishEl = doc.createElement("e_topic");
                Node englishNode = doc.createTextNode(Topic_Builder.ENGLISH_TOPICS.get(a));
                englishEl.appendChild(englishNode);
                eTopics.appendChild(englishEl);
            }
            resElement.appendChild(eTopics);
            
            Element iTopics =  doc.createElement("ict_topics"); 
             iTopics.appendChild(updateText);
             Element ictAllEl = doc.createElement("i_topic");
                Node ictAllNode = doc.createTextNode("all");
                ictAllEl.appendChild(ictAllNode);
               iTopics.appendChild(ictAllEl);
            for(int a = 0; a < Topic_Builder.ICT_TOPICS.size(); a++){
                Element ictEl = doc.createElement("i_topic");
                Node ictNode = doc.createTextNode(Topic_Builder.ICT_TOPICS.get(a));
                ictEl.appendChild(ictNode);
               iTopics.appendChild(ictEl);
            }
            resElement.appendChild(iTopics);
            
            Element mTags =  doc.createElement("maths_tags"); 
             mTags.appendChild(updateText);
             Element mathsAllTag = doc.createElement("m_tag");
                Node mathsAllTagNode = doc.createTextNode("all");
                mathsAllTag.appendChild(mathsAllTagNode);
                mTags.appendChild(mathsAllTag);
            for(int a = 0; a < Tag_Builder.MATHS_TAGS.size(); a++){
                Element mathsEl = doc.createElement("m_tag");
                Node mathsNode = doc.createTextNode(Tag_Builder.MATHS_TAGS.get(a));
                mathsEl.appendChild(mathsNode);
                mTags.appendChild(mathsEl);
            }
            resElement.appendChild(mTags);
            
            Element eTags =  doc.createElement("english_tags"); 
             eTags.appendChild(updateText);
             Element englishAllTag = doc.createElement("e_tag");
                Node englishAllTagNode = doc.createTextNode("all");
                englishAllTag.appendChild(englishAllTagNode);
                eTags.appendChild(englishAllTag);
            for(int a = 0; a < Tag_Builder.ENGLISH_TAGS.size(); a++){
                Element englishEl = doc.createElement("e_tag");
                Node englishNode = doc.createTextNode(Tag_Builder.ENGLISH_TAGS.get(a));
                englishEl.appendChild(englishNode);
                eTags.appendChild(englishEl);
            }
            resElement.appendChild(eTags);
            
            Element iTags =  doc.createElement("ict_tags"); 
             iTags.appendChild(updateText);
             Element ictAllTag = doc.createElement("i_tag");
                Node ictAllTagNode = doc.createTextNode("all");
                ictAllTag.appendChild(ictAllTagNode);
                iTags.appendChild(ictAllTag);
            for(int a = 0; a < Tag_Builder.ICT_TAGS.size(); a++){
                Element ictEl = doc.createElement("i_tag");
                Node ictNode = doc.createTextNode(Tag_Builder.ICT_TAGS.get(a));
                ictEl.appendChild(ictNode);
               iTags.appendChild(ictEl);
            }
            resElement.appendChild(iTags);
            
             Element uTags =  doc.createElement("user_tags"); 
             uTags.appendChild(updateText);
             Element userAllTag = doc.createElement("u_tag");
                Node userAllTagNode = doc.createTextNode("all");
                userAllTag.appendChild(userAllTagNode);
                uTags.appendChild(userAllTag);
            for(int a = 0; a < Tag_Builder.USER_TAGS.size(); a++){
                Element userEl = doc.createElement("u_tag");
                Node userNode = doc.createTextNode(Tag_Builder.USER_TAGS.get(a));
                userEl.appendChild(userNode);
               uTags.appendChild(userEl);
            }
            resElement.appendChild(uTags);
            
             Element sealTags =  doc.createElement("seal"); 
             sealTags.appendChild(updateText);
            for(int a = 0; a < Strat_Boxes.SEAL_CODES.size(); a++){
                Element sealEl = doc.createElement("seal_tag");
                Node sealNode = doc.createTextNode(Strat_Boxes.SEAL_CODES.get(a));
                sealEl.appendChild(sealNode);
               sealTags.appendChild(sealEl);
            }
            resElement.appendChild(sealTags);
            
            Element smscTags =  doc.createElement("smsc"); 
             smscTags.appendChild(updateText);
            for(int a = 0; a < Strat_Boxes.SMSC_CODES.size(); a++){
                Element smscEl = doc.createElement("smsc_tag");
                Node smscNode = doc.createTextNode(Strat_Boxes.SMSC_CODES.get(a));
                smscEl.appendChild(smscNode);
               smscTags.appendChild(smscEl);
            }
            resElement.appendChild(smscTags);
            
            Element regTags =  doc.createElement("regards"); 
            regTags.appendChild(updateText);
            for(int a = 0; a < Strat_Boxes.REGARDS_CODES.size(); a++){
                Element regEl = doc.createElement("regards_tag");
                Node regNode = doc.createTextNode(Strat_Boxes.REGARDS_CODES.get(a));
                regEl.appendChild(regNode);
                regTags.appendChild(regEl);
            }
            resElement.appendChild(regTags);
            
            Element ecmTags =  doc.createElement("ecm"); 
             ecmTags.appendChild(updateText);
            for(int a = 0; a < Strat_Boxes.ECM_CODES.size(); a++){
                Element ecmEl = doc.createElement("ecm_tag");
                Node ecmNode = doc.createTextNode(Strat_Boxes.ECM_CODES.get(a));
                ecmEl.appendChild(ecmNode);
                ecmTags.appendChild(ecmEl);
            }
            resElement.appendChild(ecmTags);
            
            Element desc = doc.createElement("description");
            desc.appendChild(updateText);
            Node descNode = doc.createTextNode(desc_frame.getDescription());
            desc.appendChild(descNode);
            resElement.appendChild(desc);
            
            Element added = doc.createElement("added_by");
            added.appendChild(updateText);
         
            Node userName = doc.createTextNode(System.getProperty("user.name"));
            added.appendChild(userName);
            resElement.appendChild(added);
            
            Element date = doc.createElement("date_added");
            date.appendChild(updateText);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date today = Calendar.getInstance().getTime(); 
            String theDate = formatter.format(today);
            Node dateAdded = doc.createTextNode(theDate);
            date.appendChild(dateAdded);
            resElement.appendChild(date);
            
            String isNCD = new Boolean(Resource_Descriptor.IS_NCD_RESOURCE).toString();
            Element ncd = doc.createElement("ncd");
            ncd.appendChild(updateText);
            Node nResource = doc.createTextNode(isNCD);
            ncd.appendChild(nResource);
            resElement.appendChild(ncd);
            
            Element rating = doc.createElement("rating");
            rating.appendChild(updateText);
            resElement.appendChild(rating);
            
            Element lastAccess = doc.createElement("last_Access");
            lastAccess.appendChild(updateText);
            resElement.appendChild(lastAccess);
            
            Element lastAccessBy = doc.createElement("last_accessed_by");
            lastAccessBy.appendChild(updateText);
            resElement.appendChild(lastAccessBy);
            
            Element accessFrequency = doc.createElement("access_frequency");
            accessFrequency.appendChild(updateText);
            resElement.appendChild(accessFrequency);
            
        root.appendChild(resElement);


        try{
            String outputURL = fileName;
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(outputURL));
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
       }
    }
    
    public void writeResource(){
        Resource_Writer rw = new Resource_Writer();
        //rw.writeResource(new String(outWrite + "XML//resources//resources.xml")); // edit 25.6.14
        rw.writeResource(dBase + "resources.xml");
    }
    
    
    public void killEmAll(){
        //clear all static arraylists and refresh the application
        CBoxes.killIt();
        Strat_Boxes.killIt();
        Topic_Builder.killIt();
        Tag_Builder.killIt();
        Greenfoot.setWorld(new Librarian_Interface());
        
    }
    
}
    

