/**
       * This class is for opening resources and checking
       * if they have been added to the resource library catalog
       * @author (John Nelson) 
       * @version (2013)
    */

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Desktop;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.lang.reflect.Field;
//import sun.swing.FilePane;  // deprecated library
import org.apache.commons.io.FilenameUtils; // 3rd party library - add to JRE/lib/ext and Greenfoot/userlib/ext
import javax.swing.UIManager.*;

public class Resource_Finder extends JFileChooser implements ActionListener{

    
    Desktop dt; // this will be used to open files in their relevant OS application
    public String dirName; // this will be used to remember the last directory that was accessed during runtime
    public ArrayList<String> resources; // an arraylist to hold all resource names and paths
    private String resource_name;
    private String res_home; // this variable is kept in an external text file for easy editing
    private String path_name;
    public static boolean IN_CATALOG;
    public static String FILE_BASE; // to log the name of the resource without the path and extension
    public static String FILE_EXT; // to log the extension of the resource
    public XReader xr;
    
    public Resource_Finder(String home){
        res_home = home;
        resources = new ArrayList<String>();
        setApproveButtonText("Select"); // override from the java JFileChooser api
        addActionListener(this);
    }
    public synchronized void actionPerformed(ActionEvent ev){
            //attached to all buttons and 'exit' in the frame
            dt = Desktop.getDesktop();
            
        try{
            // xr = new XReader(res_home + "XML\\Resources\\Resources.xml", "Resource"); // see inner class below //edit 25.06.2014
            
            xr = new XReader(res_home + "Resources.xml", "Resource"); //edit 25.06.2014
            dt.open(new File(getSelectedFile().getAbsolutePath()));
            setDirName(getSelectedFile().getParent());
        }catch(Exception e){
            System.out.println(e + "The path to the resources xml file is wrong - i.e., 'outWrite' in the openFile() method of the 'world' java file ");
        }
        try{
            Thread.sleep(1000);
           if(resources.contains(getSelectedFile().getAbsolutePath())){
                 IN_CATALOG = true;
                 Prompt prompt = new Prompt(); // see inner class below
               
             }else{
                 setName(getSelectedFile().getName());
                 setPathName(getSelectedFile().getAbsolutePath());
                }
        }catch(Exception ex){ System.out.println(ex + " or user cancelled or exited without selecting file");}
    }
    
    // method to store most recently accessed directory during runtime
    public void setDirName(String dir){
        dirName = dir;
    }
    
    // method to recall most recently accessed directory during runtime
    public String getDirName(){
            return dirName;
        }
    
    // method to store the absolute path of the most recently accessed resource
   public void setName(String n){
        resource_name = n;
    }
    
     // method to recall the absolute path of the most recently accessed resource
    public String getName(){
        return resource_name;
    }
    
    public void setPathName(String pn){
      path_name = pn;
    }
    
    public String getPathName(){
        return path_name;
    }
    
    public void setFileBase(String file){
        FilenameUtils.getBaseName(file);
    }
   
    
    /* inner class to read an xml file; add file names to an arraylist;
     * check if the selected resource has already been added; prompt user if true;
     * store resource path if false;
     * mainly cut and pasted from the Internet
    */
   public class XReader{
        public XReader(String file, String el){
            try {
        
                File fXmlFile = new File(file);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
        
                NodeList nList = doc.getElementsByTagName(el);
       
                for (int temp = 0; temp < nList.getLength(); temp++) {
 
                    Node nNode = nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                           Element eElement = (Element) nNode;
                           resources.add(getTagValue("name",eElement));
                        }
               }
            } catch (Exception e) {
                System.out.print(e + "no file selected?");
          }
        }
    }
    private static String getTagValue(String sTag, Element eElement) {
    NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
 
        Node nValue = (Node) nlList.item(0);
 
    return nValue.getNodeValue();
  }
   
  // a small class to prompt users if the resource has already been added
  public class Prompt extends JFrame{
      JTextField label;
      JPanel panel;
      public Prompt(){
        super("Prompt");
        try {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception e) {
    }
        setBounds(400,400,600,80);
        panel = new JPanel();
        label = new JTextField(" This resource has already been added to the library");
        label.setEditable(false);
        panel.add(label);
        getContentPane().add(panel);
        pack();
        setVisible(true); 
    }
    
    
    }
    /* the following is all copied and pasted from the Internet
     * and gives the JFileChooser the OS look and feel
     */
    
    public void updateUI(){
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
    
      super.updateUI();

     
    }




 }
