/**
 * This class makes a copy of a directory
 * I am using it a back-up for the resources.xml
 * which records the meta-data about resources
 * I have commented out some lines because I have different
 * versions of the app - one does not include a back-up facility.
 * Needs a 3rd party library as per import statement
 * @author (Apache Foundation) 
 * @version (2013)
*/

import org.apache.commons.io.FileUtils; // 3rd party library for JRE/../ext and Greentfoot/../userlib/../ext
import java.io.*;
 
public class BackItUp  
{
    public BackItUp(File srcDir, File destDir)
    {   
        try{
        FileUtils.copyFile(srcDir, destDir);
    }catch(Exception e){}
    }
}