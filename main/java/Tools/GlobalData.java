/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;




/**
 *
 * @author Diego
 */
public class GlobalData {
    //Entry global data
    public static int numAtom = 0;
    public static int numHetAtom = 0;
    public static int numCoord = 0;
    public static int numLig = 0;
    public static int numSheet = 0;
    public static int numHelix = 0;
    
    //Execution global data
    public static int logTotalData = 0;
    public static int logTotalNew = 0;
    public static int logTotalAdded = 0;
    public static int logTotalOmited = 0;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static String todayDate = "";
    
    
    public static List<String> chainNames = new ArrayList();
    public static Set<String> nonStandardAminoID = new HashSet<>();
    
    public static String createDateFolder(String URL){    
        todayDate = LocalDate.now().format(formatter);
        File dateFolder = new File(URL, todayDate);
        if(!dateFolder.exists()){
            boolean created = dateFolder.mkdirs();
            if(!created){
                System.out.println("Error creating the date folder");
                return null;
            }else{
                return todayDate;
            }
        }
        return null;
    }
    
    public static void resetCounter(){        
        numAtom = 0;
        numHetAtom = 0;
        numCoord = 0;
        numLig = 0;
        numSheet = 0;
        numHelix = 0;
        nonStandardAminoID.clear();
    }
    
    public static void addChainName(String name){
        chainNames.add(name);        
    }
    
    public static boolean containsChainName(String name){
        return chainNames.contains(name);
    }

    public static void addNonStandardAminoID(String aminoID){
        nonStandardAminoID.add(aminoID);
    }
    
    public static boolean checkAminoID(String aminoID){        
        return nonStandardAminoID.contains(aminoID);
    }
           
}
