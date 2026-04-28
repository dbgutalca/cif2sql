package Tools;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.nio.file.Paths;

/**
 *
 * @author Diego
 */
public class Reader {
    public static String sep = (System.getProperty("os.name").contains("Windows")) ? "\\" : "/";
    public static String homeDir = System.getProperty("user.dir")+sep+"logs"+sep+"checked.txt";
    
    public static List<String> readUsedFiles(){
        try{
            return Files.readAllLines(Paths.get(homeDir));
        }catch(IOException e){
            return null;
        }
    }
    
    public static List<String> readFile(String Url){
        try{
            return Files.readAllLines(Paths.get(Url).normalize());            
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
