
package Parser;

import ProteinStructure.ProteinTableStructure;
import Tools.GlobalData;
import Tools.Reader;
import static Tools.Reader.sep;
import Tools.URLProvider;
import Tools.Writer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Diego
 */
public class SQL4PDBParser {
    
    public static void main(String[] args) throws MalformedURLException, IOException {  
        LocalDateTime initDate = LocalDateTime.now();                
        Writer fileManager = Writer.getInstance();                
        if(args.length < 1){            
            int count = 0;            
            String mmCIF_URL = "/home/pdb/mmcif-files";
            URLProvider urls = new URLProvider(mmCIF_URL);
            List<String> usedUrls = Reader.readUsedFiles();
            List<String> Urls = urls.getURLs(usedUrls);   
            
            if(!Urls.isEmpty()){
                completeExecution(initDate, mmCIF_URL, Urls, count, fileManager);
            }else{
                System.out.println("No new mmcif files found.");
            }               
        }else{
            String todayDate = GlobalData.createDateFolder(System.getProperty("user.dir")+sep+"sql-files");  
            singleExecution(System.getProperty("user.dir")+sep+args[0], todayDate);
        }
            
                        
    }
    
    /**
     * Process a single cif file given it args
     * @param url
     * @param todayDate
     * @throws IOException 
     */
    public static void singleExecution(String url, String todayDate) throws IOException{
        try{
            String fileName = url.substring(url.lastIndexOf(sep) + 1);
            System.out.println("Processing file: "+ fileName);
            System.out.println("Writing file into: "+System.getProperty("user.dir")+sep+"sql-files");
            ProteinTableStructure structure = new ProteinTableStructure(url);
            System.out.println("SQL files saved in: "+ System.getProperty("user.dir")+sep+"sql-files"+sep+todayDate);        
        }catch(NoSuchFileException e){
            System.out.println("\nFile not found: "+e.getFile());
        }        
    }
    
    /**
     * Gets a folder loaded with cif files and process every file in it
     * @param initDate
     * @param mmCIF_URL
     * @param Urls
     * @param count
     * @param fileManager
     * @throws java.io.IOException
     */
    public static void completeExecution(LocalDateTime initDate, String mmCIF_URL, 
                                        List<String> Urls, int count, Writer fileManager) throws IOException{
        logWriter("New execution started at ", initDate);        
        String todayDate = GlobalData.createDateFolder(System.getProperty("user.dir")+sep+"sql-files");            
        for (String url : Urls) {
            System.out.println("Reading CIF files from: "+mmCIF_URL);
            System.out.println("Writing files into: "+System.getProperty("user.dir")+sep+"sql-files");
            System.out.println("Count: "+(count+1)+"\\"+(Urls.size()));                                         
            System.out.println("Processing file: "+url.substring(url.lastIndexOf(sep) + 1));
            ProteinTableStructure structure = new ProteinTableStructure(url);
            fileManager.useUrl(url);                
            count++;                                          
            System.gc();
            GlobalData.resetCounter();
        }
        logWriter("Total cif files: ", GlobalData.logTotalData);
        logWriter("Total new files: ", GlobalData.logTotalNew);
        logWriter("Total processed files: ", GlobalData.logTotalAdded);
        logWriter("Total omitted files: ", GlobalData.logTotalOmited);

        logWriter("Execution finished at ",LocalDateTime.now());
        logWriter("==============================","\n");          
        System.out.println("SQL files saved in: "+ System.getProperty("user.dir")+sep+"sql-files"+sep+todayDate);
    }
    
    public static void logWriter(String text, Object data) throws IOException{
        Writer.getInstance().writeLog(text+data.toString());
    }
            
}
