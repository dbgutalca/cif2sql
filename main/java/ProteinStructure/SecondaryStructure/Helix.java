
package ProteinStructure.SecondaryStructure;

import ProteinStructure.PrimaryStructure.AminoAcid;
import ProteinStructure.SQLStructure;
import Tools.GlobalData;
import Tools.SQLWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.rcsb.cif.schema.mm.MmCifBlock;

/**
 *
 * @author Diego
 */
public class Helix implements SQLStructure{        
    private List<HelixInfo> helixes = new ArrayList();
    private AminoAcid aminoacids;
   
    public void setAminoAcids(AminoAcid aminoacids){
        this.aminoacids = aminoacids;
    }
    
    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading Helix info... ");
        String entryId = data.getEntry().getId().get(0);
        int serialNumber = 1;
        for (int i = 0; i < data.getStructConf().getRowCount(); i++) {            
            String protFK = entryId;
            String initAminoFK = entryId + "_" + data.getStructConf().getBegLabelAsymId().get(i) + "_" + data.getStructConf().getBegLabelSeqId().get(i);
            String endAminoFK = entryId + "_" + data.getStructConf().getEndLabelAsymId().get(i) + "_" + data.getStructConf().getEndLabelSeqId().get(i);
            if(!GlobalData.checkAminoID(initAminoFK) && !GlobalData.checkAminoID(endAminoFK)){
               String helixID = entryId + "_" + data.getStructConf().getPdbxPDBHelixId().get(i) + "_" + serialNumber;
               String helixComment = data.getStructConf().getDetails().get(i);
               int helixClassCode = data.getStructConf().getPdbxPDBHelixClass().get(i).isEmpty() ? 
                       -1 : 
                       Integer.parseInt(data.getStructConf().getPdbxPDBHelixClass().get(i)) ;
               String helixClass = getHelixClass(helixClassCode);
               int length = data.getStructConf().getPdbxPDBHelixLength().get(i);            
               String aminoChain = aminoacids.getAminoChain(data.getStructConf().getBegLabelSeqId().get(i), data.getStructConf().getEndLabelSeqId().get(i), entryId+"_"+data.getStructConf().getBegLabelAsymId().get(i));
               List<String> helixSubChainId = aminoacids.getAminoChainId(data.getStructConf().getBegLabelSeqId().get(i), data.getStructConf().getEndLabelSeqId().get(i), entryId+"_"+data.getStructConf().getBegLabelAsymId().get(i));
               String sqlValues = "('"+helixID+"','"+helixComment+"','"+helixClass+"','"+length+"','"+initAminoFK+"','"+endAminoFK+"','"+aminoChain+"','"+protFK+"')";   
               try {                
                sqlWriter.writeSQLFile("Helix(helix_id,helix_comment,helix_class,helix_length,initRes_fk,endRes_fk,seqResChain,prot_fk)", sqlValues);                                
                writeHelixSubChain(helixSubChainId, helixID, sqlWriter);
                GlobalData.numHelix+=1;
                //helixes.add(new HelixInfo(helixID, protFK, helixComment, helixClass, length, initAminoFK, endAminoFK, aminoChain));
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                serialNumber++;
            }            
            
        }        
        System.out.println(" Done!");        
    }
    
    private void writeHelixSubChain(List<String> helixSubChainId, String helixID, SQLWriter sqlWriter) throws IOException{
        for (String aminoId : helixSubChainId) {
            sqlWriter.writeSQLFile("helix_subchain(helix_id, res_id)", "('"+helixID+"','"+aminoId+"')");
        }
    }
    
    public String getHelixClass(int classNumber){        
        switch(classNumber){
            case 1:
                return "Right-handed alpha";
            case 2:
                return "Right-handed omega";
            case 3:
                return "Right-handed pi";
            case 4:
                return "Right-handed gamma";
            case 5:
                return "Right-handed 3-10";
            case 6:
                return "Left-handed alpha";
            case 7:
                return "Left-handed omega";
            case 8:
                return "Left-handed gamma";
            case 9:
                return "2-7 ribbon/helix";
            case 10:
                return "Polyproline";
            default:
                return "";
                
        }
    }   


    @Override
    public void printData() {
        for(HelixInfo info : helixes){
            System.out.println(info.helixID +" "+info.helixClass+" "+ info.initAminoFK + " "
                    +info.endAminoFK+" "+info.length+ " ");
            System.out.println(info.seqResChain);
        }
    }
    
    
    
    
    
    
    private class HelixInfo{
        private String helixID;
        private String protFK;
        private String helixComment;
        private String helixClass;
        private int length;
        private String initAminoFK;
        private String endAminoFK;
        private String seqResChain;

        public HelixInfo(String helixID, String protFK, String helixComment, String helixClass, int length, String initAminoFK, String endAminoFK, String seqResChain) {
            this.helixID = helixID;
            this.protFK = protFK;
            this.helixComment = helixComment;
            this.helixClass = helixClass;
            this.length = length;
            this.initAminoFK = initAminoFK;
            this.endAminoFK = endAminoFK;
            this.seqResChain = seqResChain;
        }
        
        
    }
}
