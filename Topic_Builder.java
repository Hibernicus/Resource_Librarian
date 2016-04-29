/**
 * Almost identical to Tag_Builder but easier to keep
 * them separated - 
 * ****** TO_DO ********
 * ****** get this to read and write from dbase only!!!!
 * @author (John Nelson) 
 * @version (2013)
 */
import greenfoot.*;  
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


public class Topic_Builder extends JFrame implements ActionListener
{
     //public String fileName; edit 25.6.14
   public static ArrayList<String> MATHS_TOPICS = new ArrayList<String>();
   public static ArrayList<String> ENGLISH_TOPICS = new ArrayList<String>();
 public static ArrayList<String> ICT_TOPICS = new ArrayList<String>();
   public  ArrayList<JCheckBox> cb;
   public  ArrayList<String> topicNames;
    JScrollPane scroll;
    JPanel p;
    JPanel panel;
   public  StringBuilder sb;
    public char[] chars;
   public  String activePage;
    JLabel label;
    public JTextArea userTopics;
    Scanner scanner;
    String t;
    String dBase;
    public String [] sorted;
    //public Topic_Builder(String home, String aPage, String t, String dBaseHome) edit 25.6.14
    public Topic_Builder(String aPage, String t, String dBaseHome) //edit 25.6.14
    {
        super(new String (aPage + " topic list"));
       setSize(400, 210);
       try 
    { 
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
    } 
    catch(Exception e){ 
    }
       this.activePage = aPage;
       this.t = t;
        //fileName = new String(home + "XML//topics//" + activePage + "//" + t + "s.xml"); //edit 25.6.14
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
        topicNames = new ArrayList<String>();
       // readTopics(fileName); edit 25.6.14
         readTopics(dBase); //edit 25.6.14
        for(int i = 0;i < cb.size(); i++){
            p.add(cb.get(i));
        }
        label = new JLabel("Add new topics. Separate with ~ ");
        userTopics = new JTextArea();
        userTopics .setEditable(true);
        userTopics .setLineWrap(true);
        userTopics .setWrapStyleWord(true);
        userTopics.setPreferredSize(new Dimension(20, 20));
        panel.add(label);
        
        int subj;
         if(!activePage.equals("ICT")){
             subj = activePage.toLowerCase().hashCode();
            }else {
                subj = activePage.hashCode();
            }
                switch(subj){
                   case 103668331 : label = new JLabel("e.g. Fractions, Decimals & Percentages ~ Estimation");
                   break;
                   case -1603757456 : label = new JLabel("e.g.commas in a list ~ apostophes");
                   break;
                   case 72314 : label = new JLabel("e.g. attaching a file ~ COUNTIF");
                   break;
                }
        
        panel.add(label);
        panel.add(userTopics);
        JButton submit = new JButton("Submit");
        submit.addActionListener(this);
       
        p.add(panel,BorderLayout.WEST);
         p.add(submit);
        scroll = new JScrollPane(p);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
       getContentPane().add(scroll, BorderLayout.WEST);
       setVisible(true);

    }
    
    
    
    public void readTopics(String fName){
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
                          topicNames.add(getTagValue("name", eElement).toLowerCase());
                              
                        }
               }
                    sorted = new String[topicNames.size()];
                    for(int s = 0; s < topicNames.size(); s++){
                        sorted[s] = topicNames.get(s);
                    }
                    topicNames.clear();
					for(int s = 0; s < sorted.length; s++){
						topicNames.add(sorted[s]);
						cb.add(new JCheckBox(sorted[s]));
					} 
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
     
       buildTopics();
        if(!userTopics.getText().equals(null)){
            getUserTopics(userTopics.getText());
        }
        setVisible(false);
}
    public void buildTopics(){
        for(int i = 0;i < cb.size(); i++){
            if(cb.get(i).isSelected()){
                
                if(activePage.equals("Maths")){
                    if(!Topic_Builder.MATHS_TOPICS.contains(cb.get(i).getText())){
                        Topic_Builder.MATHS_TOPICS.add(cb.get(i).getText());
                    }
                }else if(activePage.equals("English")){
                    if(!Topic_Builder.ENGLISH_TOPICS.contains(cb.get(i).getText())){
                        Topic_Builder.ENGLISH_TOPICS.add(cb.get(i).getText());
                    }
                }else if(activePage.equals("ICT")){
                    if(!Topic_Builder.ICT_TOPICS.contains(cb.get(i).getText())){
                        Topic_Builder.ICT_TOPICS.add(cb.get(i).getText());
                    }
                }
            }   
        }
    }
    
 public void getUserTopics(String uTopics){
    scanner = new Scanner(uTopics);
    scanner.useDelimiter("~");
    while(scanner.hasNext()){
        String str = scanner.next().toLowerCase();
       if(!topicNames.contains(str)){
            //writeTopics(str, fileName); edit 25.6.14
            writeTopics(str, dBase);
        }
       if(activePage.equals("Maths")){
                    if(!Topic_Builder.MATHS_TOPICS.contains(str)){
                        Topic_Builder.MATHS_TOPICS.add(str);
                    }
                }else if(activePage.equals("English")){
                    if(!Topic_Builder.ENGLISH_TOPICS.contains(str)){
                        Topic_Builder.ENGLISH_TOPICS.add(str);
                    }
                }else if(activePage.equals("ICT")){
                    if(!Topic_Builder.ICT_TOPICS.contains(str)){
                        Topic_Builder.ICT_TOPICS.add(str);
                    }
                } 
    }
 }

    public void writeTopics(String userTopic, String fName){
        File docFile = new File(fName);

        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(docFile);
        }catch (java.io.IOException e){
            System.out.println(fName);
            System.out.println("Can't find the file");
        }catch (Exception e){
            System.out.print("Problem parsing the file.");
        }

        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();
        
        Element topicElement = doc.createElement(t);
        
        Node updateText = doc.createTextNode("");
        topicElement.appendChild(updateText);
        Element name = doc.createElement("name");
        String str_Name= userTopic;
        Node nameNode = doc.createTextNode(str_Name);
        name.appendChild(nameNode);

        topicElement.appendChild(name);
        
        root.appendChild(topicElement);


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
              Topic_Builder.MATHS_TOPICS.clear();
              Topic_Builder.ENGLISH_TOPICS.clear();
              Topic_Builder.ICT_TOPICS.clear();
            }

        });
    }
}