
package Parser;

import ProteinStructure.ProteinTableStructure;
import Tools.GlobalData;
import Tools.Reader;
import static Tools.Reader.sep;
import Tools.URLProvider;
import Tools.Writer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Diego
 */
public class SQL4PDBParser {
    
    public static void main(String[] args) throws MalformedURLException, IOException {  
        LocalDateTime initDate = LocalDateTime.now();
        int count = 0;
        String mmCIF_URL = Reader.readFile(System.getProperty("user.dir")+sep+"path.txt").get(0);                
        Writer fileManager = Writer.getInstance();
        URLProvider urls = new URLProvider(mmCIF_URL);
        List<String> usedUrls = Reader.readUsedFiles();
        List<String> Urls = urls.getURLs(usedUrls);    
        if(!Urls.isEmpty()){
            Writer.getInstance().writeLog("New execution started at "+initDate);
            GlobalData.createDateFolder(System.getProperty("user.dir")+sep+"sql-files");
                for (String url : Urls) {
                    System.out.println("Count: "+count+"\\"+(Urls.size()));                                         
                    System.out.println("Reading: "+url);
                    ProteinTableStructure structure = new ProteinTableStructure(url);
                    fileManager.useUrl(url);                
                    count++;                                          
                    System.gc();
                    System.out.print("\033[H\033[2J");
                    GlobalData.resetCounter();
                }
            Writer.getInstance().writeLog("Total cif files: "+ GlobalData.logTotalData);
            Writer.getInstance().writeLog("Total new files: "+ GlobalData.logTotalNew);
            Writer.getInstance().writeLog("Total processed files: "+ GlobalData.logTotalAdded);
            Writer.getInstance().writeLog("Total omitted files: "+ GlobalData.logTotalOmited);

            Writer.getInstance().writeLog("Execution finished at "+LocalDateTime.now());
            Writer.getInstance().writeLog("==============================\n");
        }else{
            System.out.println("No new mmcif files found.");
        }        
                        
    }
}
