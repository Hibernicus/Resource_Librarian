/**
 * This class will read the parameters and variables from xml files
 * for setting up all the active buttons in the main application - 
 * it's not complicated and doesn't need much explaining
 * @author (John Nelson) 
 * @version (2013)
 */

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

public class XML_Button_Reader
{
    public ArrayList<String> dImage;
    public ArrayList<String> sImage;
    public ArrayList<String> names;
    public ArrayList<Integer> ids;
    public ArrayList<Integer> ex;
    public ArrayList<Integer> wy;
   
    public XML_Button_Reader(String fileName, String elem)
    {
        dImage = new  ArrayList<String>();
        sImage = new  ArrayList<String>();
        names = new  ArrayList<String>();
        ids = new  ArrayList<Integer>();
        ex = new  ArrayList<Integer>();
        wy = new  ArrayList<Integer>();
     try {
        
        File fXmlFile = new File(fileName);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        
        NodeList nList = doc.getElementsByTagName(elem);
       
        for (int temp = 0; temp < nList.getLength(); temp++) {
 
           Node nNode = nList.item(temp);
           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                defaultButtons(eElement);
          }
          
        }
      } catch (Exception e) {
         System.out.print("XML_Button_Reader exception 1");
         
      }
     
  }
  
  private static String getTagValue(String sTag, Element eElement) {
    NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
 
        Node nValue = (Node) nlList.item(0);
 
    return nValue.getNodeValue();
  }
  
  public void defaultButtons(Element e){
      dImage.add(getTagValue("defaultImage", e)); 
      sImage.add(getTagValue("selectedImage", e));
      names.add(getTagValue("name", e));
      ids.add(Integer.parseInt(getTagValue("ID", e)));
      ex.add(Integer.parseInt(getTagValue("X", e))); 
      wy.add(Integer.parseInt(getTagValue("Y", e))); 
  }
}

    

