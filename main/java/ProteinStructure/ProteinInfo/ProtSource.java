
package ProteinStructure.ProteinInfo;

import ProteinStructure.SQLStructure;
import Tools.SQLWriter;
import java.io.IOException;
import org.rcsb.cif.schema.mm.MmCifBlock;

/**
 *
 * @author Diego
 */
public class ProtSource implements SQLStructure{
    private String prot_id;
    private String source;
    
    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        this.prot_id = data.getStruct().getEntryId().get(0);
        data.getEntitySrcGen().getPdbxGeneSrcScientificName().values().forEach(s -> {
            try {
                sqlWriter.writeSQLFile("prot_source(prot_source, protein_id)", "('"+s.replaceAll("'", "''")+"','"+prot_id+"')");
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });
        
        
    }

    @Override
    public void printData() {
        System.out.println("Source");
        System.out.println("Source : "+ source);
        System.out.println("");
    }
    
}
