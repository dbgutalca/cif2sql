package Tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Diego
 */
public class SQLWriter {
    private String entryID;
    private BufferedWriter sqlWriter;    
    private String sep = (System.getProperty("os.name").contains("Windows")) ? "\\" : "/";
    private String homeDir = System.getProperty("user.dir")+sep+"sql-files";
    
    public SQLWriter(String entryID) throws IOException{
        this.sqlWriter = new BufferedWriter(new FileWriter(homeDir+sep+GlobalData.todayDate+sep+entryID+".sql"));
        this.entryID = entryID;
    }

    public void writeSQLFile(String tableName, String sentence) throws IOException{      
        sqlWriter.write("INSERT INTO "+tableName+" VALUES "+ sentence+";");
        sqlWriter.newLine();
        sqlWriter.flush();
    }
    
    public void updateProteinTable(String protId) throws IOException{
        sqlWriter.write("UPDATE protein p");
        sqlWriter.newLine();
        sqlWriter.write("SET num_heterogen = "+GlobalData.numLig+",");
        sqlWriter.newLine();
        sqlWriter.write("num_helix = "+GlobalData.numHelix+",");        
        sqlWriter.newLine();
        sqlWriter.write("num_coord = "+GlobalData.numCoord+",");
        sqlWriter.newLine();
        sqlWriter.write("num_sheet = "+GlobalData.numSheet);  
        sqlWriter.newLine();
        sqlWriter.write("WHERE p.prot_id = '"+protId+"'");
        sqlWriter.flush();
        
    }
    
    public void deleteSQLFile() throws IOException{
        closeSQLFile();
        Path path = Paths.get(homeDir+sep+GlobalData.todayDate+sep+entryID+".sql");
        try {
            Files.delete(path);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void closeSQLFile() throws IOException{
        sqlWriter.close();
    }
}
