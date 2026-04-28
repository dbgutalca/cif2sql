
package ProteinStructure.PrimaryStructure;

import ProteinStructure.SQLStructure;
import Tools.SQLWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.rcsb.cif.schema.mm.MmCifBlock;

/**
 *
 * @author Diego
 */
public class SSBond implements SQLStructure{
    private List<SSBondInfo> ssBonds = new ArrayList();

    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading SSBond info... ");
        String entryID = data.getStruct().getEntryId().get(0);
        int seqNum = 1;
        for (int i = 0; i < data.getStructConn().getRowCount(); i++) {
            if(data.getStructConn().getConnTypeId().get(i).equals("disulf")){
                String bondID = entryID+"_"+seqNum;
                String sym1 = data.getStructConn().getPtnr1Symmetry().get(i);
                String sym2 = data.getStructConn().getPtnr2Symmetry().get(i);
                int seqId1 = data.getStructConn().getPtnr1LabelSeqId().get(i);
                int seqId2 = data.getStructConn().getPtnr2LabelSeqId().get(i);
                String chainId1 = data.getStructConn().getPtnr1LabelAsymId().get(i);
                String chainId2 = data.getStructConn().getPtnr2LabelAsymId().get(i);
                double length = data.getStructConn().getPdbxDistValue().get(i);
                if (seqId1 != 0 && seqId2 != 0) {
                    String amino1FK = entryID+"_"+chainId1+"_"+seqId1;      
                    String amino2FK = entryID+"_"+chainId2+"_"+seqId2;      
                    ssBonds.add(new SSBondInfo(bondID,sym1,sym2,length,amino1FK,amino2FK));
                    String sqlValue = "('"+bondID+"','"+amino1FK+"','"+amino2FK+"','"+sym1+"','"+sym2+"','"+length+"')";
                    try {
                        sqlWriter.writeSQLFile("ssBond(ssBond_id, res1_fk, res2_fk, sym1, sym2, ssBond_length)", sqlValue);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    seqNum++;                    
                }                                
            }
        }
        System.out.println(" Done!");
    }


    @Override
    public void printData() {
        for (SSBondInfo info: ssBonds) {
            System.out.println(info.bondID);
            System.out.println(info.amino1FK + " " + info.amino2FK+ " "+ info.sym1+" "+info.sym2 + " "+info.length);
        }
    }
    
    private class SSBondInfo{
            private String bondID;
            private String sym1;
            private String sym2;
            private double length;
            private String amino1FK;
            private String amino2FK;

        public SSBondInfo(String bondID, String sym1, String sym2, double length, String amino1FK, String amino2FK) {
            this.bondID = bondID;
            this.sym1 = sym1;
            this.sym2 = sym2;
            this.length = length;
            this.amino1FK = amino1FK;
            this.amino2FK = amino2FK;
        }
            
            
    }
    
}
