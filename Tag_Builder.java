/**
 * This class works very similarly to CBoxes and Strat_Boxes
 * with the addition that users can write their own tags and extend the 
 * tag library - straight forward enough - limited annotation
 * @author (John Nelson) 
 * @version (2013)
*/
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
import java.awt.Desktop;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.lang.reflect.Field;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult; 
import org.w3c.dom.Attr;
import javax.xml.transform.OutputKeys;
import javax.swing.UIManager.*;

public class Tag_Builder extends JFrame implements ActionListener
{
     //public String fileName; edit 25.6.14
   public static ArrayList<String> MATHS_TAGS = new ArrayList<String>();
    public static ArrayList<String> ENGLISH_TAGS = new ArrayList<String>();
    public static ArrayList<String> ICT_TAGS = new ArrayList<String>();
    public static ArrayList<String> USER_TAGS = new ArrayList<String>();
   public  ArrayList<JCheckBox> cb;
    public ArrayList<String> tagNames;
    JScrollPane scroll;
    JPanel p;
    JPanel panel;
     public StringBuilder sb;
     public char[] chars;
    public String activePage;
    public JLabel label;
     public JTextArea userTags;
    Scanner scanner;
    public String t;
    public String [] sorted;
    String dBase;
    
    ///public Tag_Builder(String home, String aPage, String t, String dBaseHome) edit 25.6.14
    public Tag_Builder(String aPage, String t, String dBaseHome) //edit 25.6.14
    {
        super(new String (aPage + " tag list"));
       setSize(400, 210);

       try {
    	for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        	if ("Nimbus".equals(info.getName())) {
           	 UIManager.setLookAndFeel(info.getClassName());
            	break;
        	}
    	}
	} catch (Exception e) {
    		// If Nimbus is not available, you can set the GUI to another look and feel.
	}

       this.activePage = aPage;
       this.t = t;
        // fileName = new String(home + "XML//topics//" + activePage + "//" + t + "s.xml"); edit 25.6.14
        dBase = new String(dBaseHome + activePage + "//" + t + "s.xml");
        
    }
     public void buildIt(){
        p = new JPanel();
        panel = new JPanel();
        panel.setSize(50, 50);
        BoxLayout box = new BoxLayout(p, BoxLayout.Y_AXIS);
        p.setLayout(box);
        panel.setLayout(new GridLayout(3, 1,0,0));
        cb = new ArrayList<JCheckBox>();
        tagNames = new ArrayList<String>();
        //readTags(fileName); edit 25.6.14
        readTags(dBase); //edit 25.6.14
        for(int i = 0;i < cb.size(); i++){
            p.add(cb.get(i));
        }
        label = new JLabel("Add new tags. Separate with ~ ");
        //p.add(label);
        //label = new JLabel("e.g. Fractions, Decimals & Percentages ~ Estimation");
        //p.add(label);
        userTags = new JTextArea();
        userTags .setEditable(true);
        userTags .setLineWrap(true);
        userTags .setWrapStyleWord(true);
        userTags.setPreferredSize(new Dimension(20, 20));
        //panel.add(scroll);
        panel.add(label);
        
       int subj;
         if(!activePage.equals("ICT")){
             subj = activePage.toLowerCase().hashCode();
            }else {
                subj = activePage.hashCode();
            }
            
                switch(subj){
                   case 103668331 : label = new JLabel("e.g. Mind Gym ~ Tarsia Puzzle");
                   break;
                   case -1603757456 : label = new JLabel("e.g.Scattergories ~ Word ladder");
                   break;
                   case 72314 : label = new JLabel("e.g. Spreadsheet ~ Powerpoint");
                   break;
                   case -1224280683 : label = new JLabel("e.g. Employability ~ Personal Finance");
                   break;
                }
        panel.add(label);
        panel.add(userTags);
       JButton submit = new JButton("Submit");
        submit.addActionListener(this);
       
        p.add(panel,BorderLayout.WEST);
         p.add(submit);
        scroll = new JScrollPane(p);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
       getContentPane().add(scroll, BorderLayout.WEST);
        
        
       // panel.add(userTags);
       // panel.add(submit);
        //getContentPane().add(panel);
        setVisible(true);

    }
    
    
    
    public void readTags(String fName){
        try {
        
                File fXmlFile = new File(fName);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
        
                NodeList nList = doc.getElementsByTagName(t);
       
                for (int temp = 0; temp < nList.getLength(); temp++) {
 
                    Node nNode = nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                           Element eElement = (Element) nNode;
                          
                          //cb.add(new JCheckBox(getTagValue("name", eElement))); 
                          tagNames.add(getTagValue("name", eElement).toLowerCase());
                        }
               }
               //start of new stuff
					sorted = new String[tagNames.size()];
					for(int s = 0; s < tagNames.size(); s++){
						sorted[s] = tagNames.get(s);
					}
					tagNames.clear();
					Arrays.sort(sorted);
					for(int s = 0; s < sorted.length; s++){
						tagNames.add(sorted[s]);
						cb.add(new JCheckBox(sorted[s]));
					} // end of new stuff
            } catch (Exception e) {
                System.out.print(e);
          }
        
    }
    private static String getTagValue(String sTag, Element eElement) {
    NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
 
        Node nValue = (Node) nlList.item(0);
 
    return nValue.getNodeValue();
  }
  
   public synchronized void actionPerformed(ActionEvent e){
    
       buildTags();
        if(!userTags.getText().equals(null)){
            getUserTags(userTags.getText());
        }
        setVisible(false);
  }
   
  public void buildTags(){
      for(int i = 0;i < cb.size(); i++){
            if(cb.get(i).isSelected()){
                
                if(activePage.equals("Maths")){
                    if(!Tag_Builder.MATHS_TAGS.contains(cb.get(i).getText())){
                        Tag_Builder.MATHS_TAGS.add(cb.get(i).getText());
                    }
                }else if(activePage.equals("English")){
                    if(!Tag_Builder.ENGLISH_TAGS.contains(cb.get(i).getText())){
                        Tag_Builder.ENGLISH_TAGS.add(cb.get(i).getText());
                    }
                }else if(activePage.equals("ICT")){
                    if(!Tag_Builder.ICT_TAGS.contains(cb.get(i).getText())){
                        Tag_Builder.ICT_TAGS.add(cb.get(i).getText());
                    }
                }else if(activePage.equals("User_Defined")){
                    if(!Tag_Builder.USER_TAGS.contains(cb.get(i).getText())){
                        Tag_Builder.USER_TAGS.add(cb.get(i).getText());
                    }
                }
            }   
        }
    }
   /* the following method allows users to write their own tags
    * (individual or multiple) if the new tags don't already exist, they are
    * added to the tag library and available the next time the tag button is engaged
    */
    public void getUserTags(String uTags){
    scanner = new Scanner(uTags);
    scanner.useDelimiter("~");
     while(scanner.hasNext()){
        String str = scanner.next().toLowerCase();
       if(!tagNames.contains(str)){
            //writeTags(str, fileName); edit 25.6.14
            writeTags(str, dBase);
        }
       if(activePage.equals("Maths")){
                    if(!Tag_Builder.MATHS_TAGS.contains(str)){
                        Tag_Builder.MATHS_TAGS.add(str);
                    }
       }else if(activePage.equals("English")){
                    if(!Tag_Builder.ENGLISH_TAGS.contains(str)){
                        Tag_Builder.ENGLISH_TAGS.add(str);
                    }
       }else if(activePage.equals("ICT")){
                    if(!Tag_Builder.ICT_TAGS.contains(str)){
                        Tag_Builder.ICT_TAGS.add(str);
                    }
       }else if(activePage.equals("User_Defined")){
                    if(!Tag_Builder.USER_TAGS.contains(str)){
                        Tag_Builder.USER_TAGS.add(str);
                    }
      } 
            }
     }


    public void writeTags(String userTag, String fName){
        File docFile = new File(fName);

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
        
        Element tagElement = doc.createElement(t);
        
        
        Node updateText = doc.createTextNode("");
        tagElement.appendChild(updateText);

        Element name = doc.createElement("name");
        String str_Name= userTag;
        Node nameNode = doc.createTextNode(str_Name);
        name.appendChild(nameNode);

        tagElement.appendChild(name);
        
        root.appendChild(tagElement);


        try{
            String outputURL = fName;
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
     public static void killIt(){
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
              Tag_Builder.MATHS_TAGS.clear();
              Tag_Builder.ENGLISH_TAGS.clear();
              Tag_Builder.ICT_TAGS.clear();
              Tag_Builder.USER_TAGS.clear();
            }

        });
    }
}