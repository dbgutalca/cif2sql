
package Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 * Provides every Path of the mmCIF files in a folder and sub-folder
 */
public class URLProvider {
    private String folderURL;
    private List<String> URLPaths = new ArrayList();
    private String sep = (System.getProperty("os.name").contains("Windows")) ? "\\" : "/";
    
    public URLProvider(String folderURL){
        this.folderURL = folderURL;
    }
    
    public List<String> getURLs(List<String> usedUrls){
        File dir = new File(this.folderURL);        
        String [] dirList = dir.list();
        System.out.println("Checking for used files...");       
        System.out.println(this.folderURL);        
        for (String folder : dirList) {            
            File subDir = new File(folderURL+sep+folder);
            String[] subDirList = subDir.list();
            for (String s: subDirList) {
                GlobalData.logTotalData++;
                System.out.println("Total transformed files: "+usedUrls.size());      
                System.out.println("Looking for new files...");
                if (usedUrls.contains(folderURL+sep+folder+sep+s)) {                    
                    usedUrls.remove(folderURL+sep+folder+sep+s);
                }else{
                    GlobalData.logTotalNew++;                                        
                    URLPaths.add(folderURL+sep+folder+sep+s);
                }                
                System.out.println(GlobalData.logTotalNew+" new files found");
                System.out.print("\033[H\033[2J");
            }
        }        
        return this.URLPaths;
    }
    
    
    
}
