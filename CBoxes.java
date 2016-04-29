/**
 * This class creates checkboxes in a frame
 * It reads curriculum codes and creates a label
 * and check box for each one
 * Array lists are eventually written to an XML file
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

public class CBoxes extends JFrame implements ActionListener{
// static array lists to store the references that users check
    public static ArrayList<String> ALL_CORE_CODES = new ArrayList<String>();
    public static ArrayList<String> ALL_FS_CODES = new ArrayList<String>();
    public static ArrayList<String> LEVELS = new ArrayList<String>();
    public static ArrayList<String> MATHS_LEVELS = new ArrayList<String>();
    public static ArrayList<String> ENGLISH_LEVELS = new ArrayList<String>();
    public static ArrayList<String> ICT_LEVELS = new ArrayList<String>();
   
    // check box array list - varies depending on the number of references in a list
    public ArrayList<JCheckBox> cb;

    public String [] files; // this is to keep the paths to files that are selected
    JScrollPane scroll;
    JPanel p;
    private String cf; 
    private String subject;
    
     /*  constructor arguments:
     * 'j' references tne number of files (levels) that have been selected
     *'cf' references to whether it is core curriculum or functional skills
     *'dirs' references the sub-division of the subjects e.g. "Analyse"
     */
    
    public CBoxes(int j, String cf, String subj, String dirs) {
        super(new String (cf + " " + subj + " " + dirs + " Codes")); // Title for the frame
       setSize(950, 800);
       //a little aesthetics
       
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
    
        /* the frames are only setVisible(false) on close
         * so the static LEVELS arraylist needs to be cleared
         * with each new launch - This happens here in case 
         * a user closes the frame prior to hitting
         * the submit button later on.
         */
       CBoxes.LEVELS.clear();
       
      /* the string literals for 'cf' and 'subject'
       * will need to avaialble outside this method
       * to fill the static array lists with chosen codes.
       * see actionPerformed() method
       */
       this.cf= cf;
       subject = subj;
        files  = new String[j];   
    }

   public void buildIt(){
        p = new JPanel();
        BoxLayout box = new BoxLayout(p, BoxLayout.Y_AXIS);
        p.setLayout(box);
    cb = new ArrayList<JCheckBox>();
    
    /* this for loop iterates over the files arraylist
     * each file is scanned and a check box and label
     * is added to the panel for each reference in each file
     */
  for(int k = 0; k < files.length; k++){
    try{
        Scanner scan = new Scanner(new File(files[k]));
        while(scan.hasNextLine()){
            cb.add(new JCheckBox(scan.nextLine()));  
        }

        for(int i = 0; i < cb.size(); i++){
            p.add(cb.get(i));
        }
    }catch(Exception e){
        System.out.print(e);
    }
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
        getCodes();// see below
        getLevels(); // see below
        setVisible(false);
        
    }
    
    public void getCodes(){
        /* iterate over the checkbox array
         * if a reference is selected and has not already been stored, 
         * add to the appropriate array list
         */
        for(int i = 0;i < cb.size(); i++){
            if(cb.get(i).isSelected()){
                
                if(cf.equals("Core Curriculum")){
                    if(!CBoxes.ALL_CORE_CODES.contains(cb.get(i).getText())){
                       
                        CBoxes.ALL_CORE_CODES.add(cb.get(i).getText());
                    }
                }else if(cf.equals("Functional Skills")){
                    if(!CBoxes.ALL_FS_CODES.contains(cb.get(i).getText())){
                        
                        CBoxes.ALL_FS_CODES.add(cb.get(i).getText());
                    }
                }
            }    
        } 
    }
    public void getLevels(){
        /* iterate over the generic levels array
         * if a reference is selected and has not already been stored, 
         * add to the appropriate subject_level array list
         */
        for(int l = 0; l < CBoxes.LEVELS.size(); l++){
            if(subject.equals("Maths") && !CBoxes.MATHS_LEVELS.contains(CBoxes.LEVELS.get(l))){
                CBoxes.MATHS_LEVELS.add(CBoxes.LEVELS.get(l));
            }else if(subject.equals("English")&& !CBoxes.ENGLISH_LEVELS.contains(CBoxes.LEVELS.get(l))){
                CBoxes.ENGLISH_LEVELS.add(CBoxes.LEVELS.get(l));
            }else if(subject.equals("ICT")&& !CBoxes.ICT_LEVELS.contains(CBoxes.LEVELS.get(l))){
                CBoxes.ICT_LEVELS.add(CBoxes.LEVELS.get(l));
            }
        }
       CBoxes.LEVELS.clear(); //possibly superfluous as included earlier in the script
    }

    /* static method called at the end of execution
     * by the final 'submit' action
     */
    public static void killIt(){
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
              CBoxes.MATHS_LEVELS.clear();
               CBoxes.ENGLISH_LEVELS.clear();
               CBoxes.ICT_LEVELS.clear();
               CBoxes.ALL_CORE_CODES.clear();
               CBoxes.ALL_FS_CODES.clear();
            }

        });
    }
    

}
