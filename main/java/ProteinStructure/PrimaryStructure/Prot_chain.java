package ProteinStructure.PrimaryStructure;

import ProteinStructure.SQLStructure;
import Tools.SQLWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.rcsb.cif.schema.mm.MmCifBlock;

/**
 *
 * @author Diego
 */
public class Prot_chain implements SQLStructure{
    private String protFK;
    private List<ChainInfo> chainsInfo = new ArrayList();
    private Map<String,Integer> entityLength = new HashMap<>();

    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading chain info... ");
        setEntityLength(data);
        String protID = data.getStruct().getEntryId().get(0);        
        this.protFK = protID;
        for(int i = 0 ; i < data.getStructAsym().getRowCount(); i++ ){
            String chainName = data.getStructAsym().getId().get(i);                            
            String chainID = this.protFK + "_" + chainName;            
            int length = entityLength.getOrDefault(data.getStructAsym().getEntityId().get(i), -1);                
            String sqlValues = "('"+chainID+"','"+protID+"','"+chainName+"','"+length+"')";
                try {
                    sqlWriter.writeSQLFile("prot_chain(chain_id,prot_fk,chain_name,chain_length)", sqlValues);
                } catch (IOException ex) {
                    System.out.println(ex);
                }                        
            //chainsInfo.add(new ChainInfo(chainID, chainName, length));
        }                   
        System.out.println(" Done!");
    }
    
    private void setEntityLength(MmCifBlock data){
        for (int i = 0; i < data.getEntityPoly().getRowCount(); i++) {
            String entityId = data.getEntityPoly().getEntityId().get(i);
            int length = data.getEntityPoly().getPdbxSeqOneLetterCodeCan().get(i).length();
            this.entityLength.put(entityId, length);            
        }
    }

    @Override
    public void printData() {
        System.out.println("Chains");
        for(ChainInfo info : this.chainsInfo){
            System.out.println("ID: "+info.chainID+" Name: "+info.chainName+ " Length: "+info.chainLength);
        }
        System.out.println("");
    }
    
    private void setHetChainsNames(List<String> hetChainIds, SQLWriter sqlWriter) throws IOException{
        for (String id: hetChainIds) {
            sqlWriter.writeSQLFile("prot_chain(chain_id,prot_fk,chain_name,chain_length)",
                    "('"+this.protFK+"_"+id+"_"+"HET"+"','"+this.protFK+"','"+id+"',0)");
        }     
    }
       
    
    
    private class ChainInfo{        
        private String chainID;
        private String chainName;
        private int chainLength;
        
        public ChainInfo(String chainID, String chainName, int chainLength){
            this.chainID = chainID;
            this.chainName = chainName;
            this.chainLength = chainLength;
        }
    }
    
}
