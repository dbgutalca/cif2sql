
package ProteinStructure.ProteinInfo;

import ProteinStructure.SQLStructure;
import Tools.GlobalData;
import Tools.SQLWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.rcsb.cif.schema.mm.MmCifBlock;
import org.rcsb.cif.schema.mm.PdbxDatabaseStatus;
import org.rcsb.cif.schema.mm.Struct;

/**
 *
 * @author Diego
 */
public class Protein implements SQLStructure{
    private String ID = "";
    private String title = "";
    private String depDate = "";
    private String classification;
    private int numHet;
    private int numHelix;
    private int numSheet;
    private int numCoord;
    private int numSeqRes;
    private LocalDate insDate = LocalDate.now();
        
    

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDepDate() {
        return depDate;
    }

    public String getClassification() {
        return classification;
    }

    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {        
        Struct structData = data.getStruct();
        PdbxDatabaseStatus dbData = data.getPdbxDatabaseStatus();
        this.classification = String.join(",", data.getStructKeywords().getPdbxKeywords().values().collect(Collectors.toList())).replaceAll("'", "''");
        this.depDate = dbData.getRecvdInitialDepositionDate().get(0);
        this.ID = structData.getEntryId().get(0);
        this.title = structData.getTitle().get(0).replaceAll("'", "''");
        this.numSeqRes = data.getPdbxPolySeqScheme().getRowCount();
            try {
                sqlWriter.writeSQLFile("protein(prot_id,title,depDate,classification,num_heterogen,num_helix,num_coord,num_seqRes,num_sheet,insDate)", 
              "('" + ID + "', '" 
              + title + "', '" 
              + depDate + "', '" 
              + classification + "', " 
              + 0 + ", " 
              + 0 + ", " 
              + 0 + ", " 
              + numSeqRes + ", " 
              + 0 + ", '" 
              + insDate + "')");
            } catch (IOException ex) {
                System.out.println(ex);
            }
    }
    
    public void updateValues(SQLWriter sqlWriter, MmCifBlock data) throws IOException{
        sqlWriter.updateProteinTable(data.getEntry().getId().get(0));
    }

    
    @Override
    public void printData(){
        System.out.println("Protein Data");
        System.out.println("Protein ID: "+this.ID);
        System.out.println("Protein Title: "+this.title);      
        System.out.println("Classification: "+this.classification);
        System.out.println("Departure Date: "+this.depDate);        
        System.out.println("Num seq res: "+this.numSeqRes);
        System.out.println("");
    }
    
}
