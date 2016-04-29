/**
 * This is a little text pane that allows the user to write a 
 * description of the resource to be catalogued
 * quite straightforward so there's no annotation
 * @author (John Nelson) 
 * @version (2013)
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;


public class Resource_Descriptor extends JFrame implements ActionListener{

    public JPanel pnl;
    public JPanel subPnl;
    public JTextArea txt;
    public JButton btn;
    public JLabel lbl;
    public String description = "no description"; // the programme throws an exception if this is blank
    public JLabel ncd;
    public JCheckBox cBox;
    public static boolean IS_NCD_RESOURCE = false;
    
    public Resource_Descriptor(){
        super("Resource Description");
        buildIt();
    }
        
    
    public void buildIt(){
        
        this.setBounds(150,50, 50, 50);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pnl = new JPanel();
        subPnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        lbl = new JLabel("Type your description in the field below and hit 'Submit'");
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt = new JTextArea(10, 10);
        txt.setEditable(true);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setPreferredSize(new Dimension(40,40));
        txt.setText(description);
        ncd = new JLabel("Self-authored Resource: ");
        cBox = new JCheckBox();
        btn = new JButton("Submit");
        btn.addActionListener(this);
        pnl.add(lbl);
        pnl.add(txt);
        subPnl.add(ncd);
        subPnl.add(cBox);
        subPnl.add(btn);
        pnl.add(subPnl);
        this.getContentPane().add(pnl);
        this.pack();
        this.setVisible(true);
        
    }

    public void actionPerformed(ActionEvent e){
        setDescription(txt.getText());
        if(cBox.isSelected()){
             setIsNCDResource(true);
          }else{
               setIsNCDResource(false);
              }
        this.setVisible(false);
    }

    public void setDescription(String desc){
        description = desc;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setIsNCDResource(boolean b){
        IS_NCD_RESOURCE = b;
       }
       
       public boolean getIsNCDResource(){
           return IS_NCD_RESOURCE;
       }
}