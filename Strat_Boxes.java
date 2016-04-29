/**
 * This class creates checkboxes in a frame
 * It reads strategy references and creates a label and check box for each one
 * Array lists are eventually written to an XML file
 * Trimmed down version of the CBoxes class - so no annotation
 * @author (John Nelson) 
 * @version (2013)
*/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.UIManager.*;

public class Strat_Boxes extends JFrame implements ActionListener{
    
   public  static ArrayList<String> ECM_CODES = new ArrayList<String>();
   public static ArrayList<String> SEAL_CODES = new ArrayList<String>();
   public static ArrayList<String> REGARDS_CODES = new ArrayList<String>();
   public static ArrayList<String> SMSC_CODES = new ArrayList<String>();
    public String fileName;
   public String strat;
   public ArrayList<JCheckBox> cb;
    JScrollPane scroll;
    JPanel p;
    
    public Strat_Boxes(String home, String strat){
        super(new String(strat + " Codes"));
        setSize(170, 220);
        
        
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
    /*    try 
    { 
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
    } 
    catch(Exception e){ 
    }*/
        this.strat = strat;
        fileName = new String(home + "//codes//" + strat + "//codes.txt");
    }
    
     public void buildIt(){
        p = new JPanel();
        BoxLayout box = new BoxLayout(p, BoxLayout.Y_AXIS);
        p.setLayout(box);
        cb = new ArrayList<JCheckBox>();
        
        try{
            Scanner scan = new Scanner(new File(fileName));
            while(scan.hasNextLine()){
                cb.add(new JCheckBox(scan.nextLine())); 
            }

            for(int i = 0; i < cb.size(); i++){
                p.add(cb.get(i));
            }
        }catch(Exception e){
            System.out.print(e);
        }
        JButton submit = new JButton("Submit");
        submit.addActionListener(this);
        p.add(submit);
        scroll = new JScrollPane(p); 
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scroll, BorderLayout.WEST);
        setVisible(true);
    }
        public synchronized void actionPerformed(ActionEvent e){
            getStrats();
            setVisible(false);   
        }
    public void getStrats(){
        for(int i = 0;i < cb.size(); i++){
            if(cb.get(i).isSelected()){
                
                if(strat.equals("SEAL")){
                    if(!Strat_Boxes.SEAL_CODES.contains(cb.get(i).getText())){
                        Strat_Boxes.SEAL_CODES.add(cb.get(i).getText());
                    }
                }else if(strat.equals("ECM")){
                    if(!Strat_Boxes.ECM_CODES.contains(cb.get(i).getText())){
                       Strat_Boxes.ECM_CODES.add(cb.get(i).getText());
                    }
                }else if(strat.equals("REGARDS")){
                    if(!Strat_Boxes.REGARDS_CODES.contains(cb.get(i).getText())){
                       Strat_Boxes.REGARDS_CODES.add(cb.get(i).getText());
                    }
                }else if(strat.equals("SMSC")){
                    if(!Strat_Boxes.SMSC_CODES.contains(cb.get(i).getText())){
                       Strat_Boxes.SMSC_CODES.add(cb.get(i).getText());
                    }
                }
            }  
        }
    }
   public static void killIt(){
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
             Strat_Boxes.ECM_CODES.clear();
              Strat_Boxes.SEAL_CODES.clear(); 
              Strat_Boxes.SMSC_CODES.clear(); 
              Strat_Boxes.REGARDS_CODES.clear(); 
            }

        });
    }
    }

   
