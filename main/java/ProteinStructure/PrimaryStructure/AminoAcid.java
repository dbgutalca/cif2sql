package ProteinStructure.PrimaryStructure;

import ProteinStructure.SQLStructure;
import Tools.GlobalData;
import Tools.SQLWriter;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.escape;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.rcsb.cif.schema.mm.MmCifBlock;
import org.rcsb.cif.schema.mm.PdbxPolySeqScheme;

/**
 *
 * @author Diego
 */
public class AminoAcid implements SQLStructure{
    private List<AminoAcidInfo> aminoacids = new ArrayList();
    private List<String> aminoList = Arrays.asList(
            "ALA","ARG","ASN","ASP", "CYS","GLN",
            "GLU","GLY","HIS","ILE","LEU","LYS",
            "MET","PHE","PRO","SER","THR","TRP","TYR","VAL"
    );
    private boolean hasStandardAmino = true;

    public boolean HasStandardAmino() {
        return hasStandardAmino;
    }
    
    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading amino Info... ");
        String entityName = data.getStruct().getEntryId().get(0);
        int seqLength = data.getPdbxPolySeqScheme().getRowCount();    
        PdbxPolySeqScheme seqResData = data.getPdbxPolySeqScheme();        
        for (int i = 0; i < seqLength; i++) {
            String aminoID = seqResData.getMonId().get(i);            
            int seqNum = seqResData.getSeqId().get(i);
            String chainName = seqResData.getAsymId().get(i);
            String chainFK = entityName + "_"  + chainName;
            String residueID = chainFK + "_" + seqNum;  
            if (!isStandard(aminoID)) {
                GlobalData.addNonStandardAminoID(residueID);                
            }else{                                  
                this.aminoacids.add(new AminoAcidInfo(residueID, seqNum, aminoID, chainFK));                
            }                        
        }    
        if(!this.hasStandardAmino){
            System.out.println("Omitting " +entityName);
        }else{
            System.out.println(" Done!");
        }
    }

   
    public void toSQL(SQLWriter sqlWriter) throws IOException {
        for (AminoAcidInfo amino : aminoacids) {
            sqlWriter.writeSQLFile("residue(residue_id,residue_seqNum,standardAmino_fk,chain_fk) ", String.format(
                    "('%s', '%d', '%s', '%s')",
                    escape(amino.ID),
                    amino.seqNum,
                    escape(amino.StandardAminoFK),
                    escape(amino.chainFK)
            ));
        }        
    }
   

    @Override
    public void printData() {
        for (AminoAcidInfo aminoInfo : aminoacids) {
            System.out.println("ID: " + aminoInfo.ID+" Amino Name: "+aminoInfo.StandardAminoFK);            
        }
    }
    
    /**
     * Returns an aminoacids chain from the initial sequence number to the end.
     * In example: ALA-ARG-ASN-ASP
     * @param initSeq Initial sequence number of an aminoacid
     * @param endSeq End sequence number of an aminoacid
     * @param chainID Chain ID. In example: 1AFG_H
     * @return A String sequence of aminoacid names.
     */
    public String getAminoChain(int initSeq, int endSeq, String chainID){
        String chain = "";
        for (AminoAcidInfo info: aminoacids) {
            if(info.chainFK.equals(chainID) && info.seqNum >= initSeq && info.seqNum <= endSeq){
                chain = chain.concat(info.StandardAminoFK+"-");
            }
        }
        return chain;
    }
    
    /**
     * Returns an aminoacids chain IDs from the initial sequence number to the end.
     * In example: 1AFG_H_ALA-1AFG_H_ARG-1AFG_H_ASN-1AFG_H_ASP
     * @param initSeq Initial sequence number of an aminoacid
     * @param endSeq End sequence number of an aminoacid
     * @param chainID Chain ID. In example: 1AFG_H
     * @return A String sequence of aminoacid names.
     */
    public List<String> getAminoChainId(int initSeq, int endSeq, String chainID){
        ArrayList<String> chain = new ArrayList();
        for (AminoAcidInfo info: aminoacids) {
            if(info.chainFK.equals(chainID) && info.seqNum >= initSeq && info.seqNum <= endSeq){
                chain.add(info.ID);                
            }
        }
        return chain;
    }
    
    public boolean isStandard(String aminoName){        
        return aminoList.contains(aminoName);
    }
    
    
    private class AminoAcidInfo{
        private String ID;
        private int seqNum;
        private String StandardAminoFK;
        private String chainFK;

        public AminoAcidInfo(String ID, int seqNum, String StandardAminoFK, String chainFK) {
            this.ID = ID;
            this.seqNum = seqNum;
            this.StandardAminoFK = StandardAminoFK;
            this.chainFK = chainFK;
        }
    }   
}
