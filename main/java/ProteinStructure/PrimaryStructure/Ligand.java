
package ProteinStructure.PrimaryStructure;

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
public class Ligand implements SQLStructure{
    private List<LigandInfo> ligands = new ArrayList();
    
    private class LigandInfo {
        private String ligID;
        private String ligFullName;
        private String ligChemFormul;
        private int ligSeqNum;
        private String ligShortName;
        private String ligSynonyms;
        private String chainFK;

        public LigandInfo(String ligID, String ligFullName, String ligChemFormul, int ligSeqNum, String ligShortName, String ligSynonyms, String chainFK) {
            this.ligID = ligID;
            this.ligFullName = ligFullName;
            this.ligChemFormul = ligChemFormul;
            this.ligSeqNum = ligSeqNum;
            this.ligShortName = ligShortName;
            this.ligSynonyms = ligSynonyms;
            this.chainFK = chainFK;
        }
        
        
        
        
    }
    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading ligand info... ");
        String entryID = data.getStruct().getEntryId().get(0);
        String ligandID = "";
        for (int i = GlobalData.numAtom; i < GlobalData.numCoord; i++) {            
            if(data.getAtomSite().getGroupPDB().get(i).equals("HETATM")){
                String ligName = data.getAtomSite().getLabelCompId().get(i).replaceAll("'", "''");
                int seqNum = data.getAtomSite().getAuthSeqId().get(i);
                String chainID = data.getAtomSite().getLabelAsymId().get(i);
                if(!ligandID.equals(entryID+"_"+chainID+"_"+seqNum)){
                    ligandID = entryID+"_"+chainID+"_"+seqNum;                    
                    for (int j = 0; j < data.getChemComp().getRowCount(); j++) {
                        if(data.getChemComp().getId().get(j).equals(ligName)){
                            String fullName = data.getChemComp().getName().get(j).replaceAll("'", "''");
                            String chemFormula = data.getChemComp().getFormula().get(j).replaceAll("'","''");
                            String synonims = data.getChemComp().getPdbxSynonyms().get(j).replaceAll("'", "''");                              
                            String sqlValues = "('"+ligandID+"','"+fullName+"','"+chemFormula+"','"+seqNum+"','"+ligName+"','"+synonims+"','"+entryID+"_"+chainID+"')";
                            try {
                                sqlWriter.writeSQLFile("ligand(lig_id,lig_name,lig_formul,lig_seqNum,lig_name3,lig_synonims,chain_FK)", sqlValues);
                                GlobalData.numLig+=1;
                            } catch (IOException ex) {
                                System.out.println(ex);
                            }
                            ligands.add(new LigandInfo(ligandID, fullName, chemFormula, seqNum, ligName, synonims, entryID+"_"+chainID));
                            break;
                        }
                    }
                }
                
            }            
        }     
        System.out.println(" Done!");
    }


    @Override
    public void printData() {
        for (LigandInfo ligand: ligands){
            System.out.println(ligand.ligID);
            System.out.println(ligand.ligFullName);
            System.out.println(ligand.ligShortName);
            System.out.println(ligand.ligChemFormul);
            System.out.println(ligand.ligSynonyms);
            System.out.println(ligand.chainFK);
        }
    }
}
