import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.*;

/**
 * This is a generic, reusable Button for a user interface. This button is part of the "MSG" 
 * Greenfoot GUI toolkit.
 * 
 * Scenarios using these buttons must also contain the ButtonListener interface, and the World
 * class must implement that interface. When a button of this class is clicked, the 'buttonClicked()' 
 * method in the world class will be called.
 * 
 * Note that positioning places the button's top left corner at the target location.
 * 
 * @author Michael Kolling 
 * @version 1.0
 */
public class Misc_Button extends Actor
{
    // colours with default values
    private Color textColor = Color.BLACK;
    private Color backgroundColor = new Color(184, 184, 184);
    private Color backgroundColorPressed = new Color(152, 152, 152);
    private Color bottomBorderColor = new Color(128, 128, 128);
    private Color topBorderColor = new Color(220, 220, 220);
    
    // spacing, with defaiult values
    private int inset = 8;  // space between text and edge
    
    private String text;
    private int ID;
    private boolean down = false;
    private int baseline;   // the y-coordinate of the text baseline
    private int textX;      // the x-coordinate of the left edge of the text
    private int width = -1;
    private int height = -1;
    private GreenfootImage img1;
    private GreenfootImage img2;
     private String name = null;
    
    public Misc_Button()
    {
        this("         ");
    }
    
    public Misc_Button(String text)
    {
        this.text = text;
        createImage();
    }
    
    
    public Misc_Button(String image1, String image2, String n, int IDNumber)
    {
        //this.text = text;
        this.img1 = new GreenfootImage(image1);
        this.img2 = new GreenfootImage(image2);
        setImage(img1);
        this.ID = IDNumber;
        setName(n);
        //createImage();
    }
    
    /**
     * Act - check whether any mouse events are detected onthis button and react to them.
     */
    public void act() 
    {
        if (Greenfoot.mousePressed(this)) {
            setDown(true);
            setImage(img2);
        }
        else if (Greenfoot.mouseDragEnded(this)) {
            // mouse released outside of button - ignore
            setDown(false);
            setImage(img1);
        }
        else if (Greenfoot.mouseClicked(this)) {
           setDown(false);
            setImage(img1);
            buttonClicked();
        }
    }
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    public GreenfootImage getImg1(){
        return img1;
    }
    
    /**
     * This button was clicked by a user. Perform whatever action this button should do.
     */
    protected void buttonClicked()
    {
        ((Misc_ButtonListener)getWorld()).buttonClicked(this);
    }
    
    /** 
     * Set the text to be displayed.
     */
    public void setText(String text)
    {
        this.text = text;
        createImage();
    }
    
    /**
     * Return this button's text.
     */
    public String getText()
    {
        return text;
    }
    
    /** 
     * Set the font for the button text.
     */
    public void setFont(Font font)
    {
        getImage().setFont(font);
        createImage();
    }
    
    /**
     * Return this button's font.
     */
    public Font getFont()
    {
        return getImage().getFont();
    }
    
    /** 
     * Set the background color.
     */
    public void setBackground(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
        createImage();
    }
    
    /** 
     * Set the foreground color.
     */
    public void setForeground(Color foregroundColor)
    {
        this.textColor = foregroundColor;
        createImage();
    }
    
    /** 
     * Set the width of this button to a fixed width.
     */
    public void setWidth(int width)
    {
        this.width = width;
        createImage();
    }
    
    /** 
     * Set the height of this button to a fixed height.
     */
    public void setHeight(int height)
    {
        this.height = height;
        createImage();
    }
    public void setIDNumber(int i){
        ID = i;
    }
    /**
     * Return this button's ID number.
     */
    public int getIDNumber()
    {
        return ID;
    }
    public void setDown(boolean b){
        down = b;
    }
    
    public boolean getDown(){
        return down;
    }
    /**
     * Override setLocation to position the button's top left corner at the given location.
     */
    public void setLocation(int x, int y)
    {
        super.setLocation (x + getImage().getWidth()/2, y + getImage().getHeight()/2);
    }
    
    // --------------------------- private methods ---------------------------------
    /**
     * Paint the button image, including the button text and decorations.
     */
    private void repaint()
    {
        paintBackground();
        paintBorder();
        paintText();
    }

    /**
     * Paint the button's background.
     */
    private void paintBackground()
    {
        GreenfootImage img = getImage();
        if (down) {
            img.setColor(backgroundColorPressed);
        }
        else {
            img.setColor(backgroundColor);
        }
        img.fill();
    }
    
    /**
     * Paint the button's text.
     */
    private void paintText()
    {
        GreenfootImage img = getImage();
        img.setColor(textColor);
        if (down) {
            img.drawString(text, textX, baseline+1);
        }
        else {
            img.drawString(text, textX, baseline);
        }
    }
    
    /**
     * Paint the button's border.
     */
    private void paintBorder()
    {
        paintBevel(0);
        paintBevel(1);
        paintBevel(2);
    }
    
    /**
     * Paint one 1-pixel-line of the border bevel.
     */
    private void paintBevel(int offset)
    {
        GreenfootImage img = getImage();
        int w = img.getWidth()-1;
        int h = img.getHeight()-1;
        if (down) {
            img.setColor(bottomBorderColor);
        }
        else {
            img.setColor(topBorderColor);
        }
        img.drawLine(offset, offset, offset, h-offset);
        img.drawLine(offset, offset, w-offset, offset);
        if (down) {
            img.setColor(topBorderColor);
        }
        else {
            img.setColor(bottomBorderColor);
        }
        img.drawLine(offset, h-offset, w-offset, h-offset);
        img.drawLine(w-offset, offset, w-offset, h-offset);
    }
    
    /**
     * Create the actor's image at the right size. If the size has been set explicitly, that size 
     * is used. If not, the size is calculated  to fit the button text. The size calculation takes 
     * the text on the button and the current font into account.
     */
    private void createImage()
    {
        int imgWidth = width;
        int imgHeight = height;
        
        GreenfootImage img = new GreenfootImage(10, 10);
        Graphics gc = img.getAwtImage().getGraphics();
        FontMetrics fm = gc.getFontMetrics();
        
        int textWidth = fm.stringWidth(text);
        if (imgWidth <= 0) {
            imgWidth = textWidth + 2 * inset;
        }
        if (imgHeight <= 0) {
            imgHeight = fm.getHeight() + 2 * inset;
        }
        
        baseline = (imgHeight/2) + (fm.getAscent()/2);
        textX = (imgWidth/2) - (textWidth/2);
        img.scale(imgWidth, imgHeight);
        setImage(img);
        repaint();
    }
}
