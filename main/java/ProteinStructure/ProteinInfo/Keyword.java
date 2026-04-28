
package ProteinStructure.ProteinInfo;

import ProteinStructure.SQLStructure;
import Tools.SQLWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import org.rcsb.cif.schema.mm.MmCifBlock;

/**
 *
 * @author Diego
 */
public class Keyword implements SQLStructure{
    private String prot_id;
    private String keyword;

    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {        
        this.prot_id = data.getStruct().getEntryId().get(0);
        data.getStructKeywords().getText().values().forEach(k -> {
                try {
                    sqlWriter.writeSQLFile("keyword(protein_id,keyword)", "('"+prot_id+"','"+k.replaceAll("'", "''")+"')");
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            });  
    }

    @Override
    public void printData() {
        System.out.println("KeyWords");
        System.out.println(this.keyword);
        System.out.println("");
    }
}
