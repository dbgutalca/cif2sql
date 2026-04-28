
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
public class Strand implements SQLStructure{
    private List<StrandInfo> strands = new ArrayList();
    private AminoAcid aminoacids;
    private boolean hasStrand = true;
   
    public void setAminoAcids(AminoAcid aminoacids){
        this.aminoacids = aminoacids;
    }
    
    
    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading Strand info... ");
        String entryId = data.getEntry().getId().get(0);
        String currSheetFK = "";        
        int j = 0;
        for (int i = 0; i < data.getStructSheetRange().getRowCount(); i++) {
            
            try{
                String sheetFK = entryId+"_"+data.getStructSheetRange().getSheetId().get(i);
                String sense = "";
                String currResFK = "";
                String prevResFK = "";
                String currAtom = "";
                String prevAtom = "";                
                if (currSheetFK.equals(sheetFK)) {
                   sense = data.getStructSheetOrder().getSense().get(j);
                   currResFK = entryId +"_"+data.getPdbxStructSheetHbond().getRange2LabelAsymId().get(j)+"_"+data.getPdbxStructSheetHbond().getRange2LabelSeqId().get(j);
                   prevResFK = entryId +"_"+data.getPdbxStructSheetHbond().getRange1LabelAsymId().get(j)+"_"+data.getPdbxStructSheetHbond().getRange1LabelSeqId().get(j);
                   currAtom = data.getPdbxStructSheetHbond().getRange2LabelAtomId().get(j);
                   prevAtom = data.getPdbxStructSheetHbond().getRange1LabelAtomId().get(j);
                   j++;
                }else{
                    currSheetFK = sheetFK;
                }
                String strandId = sheetFK+"_"+data.getStructSheetRange().getId().get(i);
                String initResFK = entryId + "_" + data.getStructSheetRange().getBegLabelAsymId().get(i) +"_"+data.getStructSheetRange().getBegLabelSeqId().get(i);
                String endResFK = entryId + "_" + data.getStructSheetRange().getEndLabelAsymId().get(i) +"_"+data.getStructSheetRange().getEndLabelSeqId().get(i);                        
                String aminoChain = aminoacids.getAminoChain(data.getStructSheetRange().getBegLabelSeqId().get(i), data.getStructSheetRange().getEndLabelSeqId().get(i), entryId+"_"+data.getStructSheetRange().getBegLabelAsymId().get(i));
                List<String> strandSubChainId = aminoacids.getAminoChainId(data.getStructSheetRange().getBegLabelSeqId().get(i), data.getStructSheetRange().getEndLabelSeqId().get(i), entryId+"_"+data.getStructSheetRange().getBegLabelAsymId().get(i));
                String sqlValues = "('" +
                                strandId + "', '" +
                                sheetFK + "', '" +
                                initResFK + "', '" +
                                endResFK + "', '" +
                                sense + "', '" +
                                currResFK + "', '" +
                                prevResFK + "', '" +
                                currAtom + "', '" +
                                prevAtom + "', '" +
                                aminoChain + "')";
                if(!GlobalData.checkAminoID(initResFK) && 
                   !GlobalData.checkAminoID(endResFK) && 
                   !GlobalData.checkAminoID(currResFK) && 
                   !GlobalData.checkAminoID(prevResFK)){
                        sqlWriter.writeSQLFile("strand(strand_id,sheet_fk,initRes_FK,endRes_FK,sense,currRes_FK,prevRes_FK,currAtom,prevAtom,seqResChain)", sqlValues);
                        writeStrandSubChain(strandSubChainId, strandId, sqlWriter);
                }                
            }catch(Exception e){
                System.out.println("No Strand found");
                strands.add(new StrandInfo(entryId+"_"+data.getStructSheetRange().getSheetId().get(i)+"_"+data.getStructSheetRange().getId().get(i), entryId+"_"+data.getStructSheetRange().getSheetId().get(i),"", "", "","","", "", "", ""));
                this.hasStrand = false;
            }            
        }
        System.out.println(" Done!");
    }
    
    private void writeStrandSubChain(List<String> strandSubChainId, String strandID, SQLWriter sqlWriter) throws IOException{
        for (String aminoId : strandSubChainId) {
            sqlWriter.writeSQLFile("strand_subchain(strand_id, res_id)", "('"+strandID+"','"+aminoId+"')");
        }
    }



    @Override
    public void printData() {
        for (StrandInfo info: strands) {
            System.out.println(info.strandId+" "+info.sheetFK+" "+info.initResFK+" "+info.endResFK+" "+info.sense+" "+info.currResFK
                    +" "+info.prevResFK+" "+info.currAtom+" "+info.prevAtom+" ");
            System.out.println(info.seqResChain);
        }
    }
    
    
    private class StrandInfo{
        private String strandId;
        private String sheetFK;
        private String initResFK;
        private String endResFK;
        private String sense;
        private String currResFK;
        private String prevResFK;
        private String currAtom;
        private String prevAtom;
        private String seqResChain;  

        public StrandInfo(String strandId, String sheetFK, String initResFK, String endResFK, String sense, String currResFK, String prevResFK, String currAtom, String prevAtom, String seqResChain) {
            this.strandId = strandId;
            this.sheetFK = sheetFK;
            this.initResFK = initResFK;
            this.endResFK = endResFK;
            this.sense = sense;
            this.currResFK = currResFK;
            this.prevResFK = prevResFK;
            this.currAtom = currAtom;
            this.prevAtom = prevAtom;
            this.seqResChain = seqResChain;
        }
        
        
    }
}
