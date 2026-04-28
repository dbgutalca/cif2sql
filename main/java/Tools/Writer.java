package Tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class writes into the logs text files. 
 * Primarily used to avoid reading files that have already been read.
 * @author Diego
 */
public class Writer {
    private static Writer instance;
    private BufferedWriter addedWriter;
    private BufferedWriter omitedWriter;
    private BufferedWriter usedWriter;
    private BufferedWriter sqlWriter;
    private BufferedWriter execLog;
    private String sep = (System.getProperty("os.name").contains("Windows")) ? "\\" : "/";
    private String homeDir = System.getProperty("user.dir")+sep+"logs";
    
    private Writer() throws IOException{      
        //Used to save the added files
        addedWriter = new BufferedWriter(new FileWriter(homeDir+sep+"processed.txt", true));
        //Used to save the invalid files such as RNA/ARN entries
        omitedWriter = new BufferedWriter(new FileWriter(homeDir+sep+"omitted.txt", true));
        //Used to save every file readed. Should be added + omitted
        usedWriter = new BufferedWriter(new FileWriter(homeDir+sep+"checked.txt", true));        
        execLog = new BufferedWriter(new FileWriter(homeDir+sep+"general_log.txt", true));  
    }
    
    /**
     * Singleton instance of Writer
     * @return
     * @throws IOException 
     */
    public static Writer getInstance() throws IOException{
        if(instance == null) instance = new Writer();
        return instance;                    
    }
    
    /**
     * Put the URL into "added" text file
     * @param url
     * @throws IOException 
     */
    public void addUrl(String url) throws IOException{
        addedWriter.write(url);
        addedWriter.newLine();
        addedWriter.flush();
    }
    
    /**
     * Put the URL into "omitted" text file
     * @param url
     * @throws IOException 
     */
    public void omitUrl(String url) throws IOException{
        omitedWriter.write(url);
        omitedWriter.newLine();
        omitedWriter.flush();
    }
    
    /**
     * Put the URL into "used" text file
     * @param url
     * @throws IOException 
     */
    public void useUrl(String url) throws IOException{
        usedWriter.write(url);
        usedWriter.newLine();
        usedWriter.flush();
    }
    
    /**
     * Close all files
     * @throws IOException 
     */
    public void close() throws IOException{
        addedWriter.close();
        omitedWriter.close();
        usedWriter.close();
    }
    
    public void writeLog(String text) throws IOException{
        execLog.write(text);
        execLog.newLine();
        execLog.flush();
    }
    
}
