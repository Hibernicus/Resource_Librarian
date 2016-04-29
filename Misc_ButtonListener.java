/**
 * This interface defines the listener method - buttonClicked - for reacting to button
 * presses. It is part of the "MSG" Greenfoot GUI toolkit.
 * 
 * This listener interface must be used whenever the MSG Button class is used. The world class must
 * implement this interface, and provide an implementation for the buttonClicked method.
 * 
 * @author Michael Kolling
 * @version 1.0
 */
public interface Misc_ButtonListener
{
    /**
     * A button in the world was clicked.
     * 
     * @param button  The button object that was clicked.
     */
    public void buttonClicked(Misc_Button mbutton);
}
