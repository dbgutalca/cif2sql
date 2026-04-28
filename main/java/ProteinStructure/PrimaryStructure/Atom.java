
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
public class Atom implements SQLStructure{
    private List<AtomInfo> atoms = new ArrayList();

    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading Atom info... ");
        String entryID = data.getStruct().getEntryId().get(0);
        int numCoord = data.getAtomSite().getRowCount();
        GlobalData.numCoord = numCoord;
        int numAtom = 0;
        for (int i = 0; i < numCoord; i++) {
            if(data.getAtomSite().getGroupPDB().get(i).equals("ATOM")){                
                String chainID = data.getAtomSite().getLabelAsymId().get(i);
                int aminoSeqNum = data.getAtomSite().getLabelSeqId().get(i);
                String aminoFK = entryID + "_" + chainID+"_"+aminoSeqNum;
                if(!GlobalData.checkAminoID(aminoFK)){
                    int model = data.getAtomSite().getPdbxPDBModelNum().get(i);
                    String atomName = data.getAtomSite().getTypeSymbol().get(i);
                    String altLoc = "";
                    double xCoord = data.getAtomSite().getCartnX().get(i);
                    double yCoord = data.getAtomSite().getCartnY().get(i);
                    double zCoord = data.getAtomSite().getCartnZ().get(i);
                    double tempFactor = data.getAtomSite().getBIsoOrEquiv().get(i);
                    double occupancy = data.getAtomSite().getOccupancy().get(i);
                    int charge = data.getAtomSite().getPdbxFormalCharge().get(i);
                    int serialNumber = data.getAtomSite().getId().get(i);                
                    String atomID = aminoFK +"_"+model+"_"+serialNumber;
                    String sqlValues = "('" + atomID + "', " +
                       model + ", '" +
                       atomName + "', '" +
                       altLoc + "', " +
                       xCoord + ", " +
                       yCoord + ", " +
                       zCoord + ", " +
                       tempFactor + ", " +
                       occupancy + ", " +
                       charge + ", " +
                       serialNumber + ",'" +
                       aminoFK + "')";
                    try {
                        sqlWriter.writeSQLFile("atom(atom_id,model,atom_name,altLoc, X_coord,Y_coord,Z_coord,tempFactor,occupancy,charge,serial_number,residue_FK) ", sqlValues);                        
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }                              
            }   
            numAtom++;  
        }
        GlobalData.numAtom = numAtom;
        GlobalData.numHetAtom = numCoord - numAtom;
        System.out.println(" Done!");
    }



    @Override
    public void printData() {
        for (AtomInfo info: atoms) {
            System.out.println(info.atomID +" "+info.atomName+" "+info.xCoord+" "+info.yCoord+" "+info.zCoord+" "+info.charge+ " "+info.occupancy+" "+info.tempFactor);
            
        }
    }
    
    private class AtomInfo{
        private String atomID;
        private String atomName;
        private String altLoc;
        private double xCoord;
        private double yCoord;
        private double zCoord;
        private double tempFactor;
        private double occupancy;
        private int charge;
        private int serialNumber;
        private String aminoFK;
        private int model;

        public AtomInfo(String atomID, String atomName, String altLoc, double xCoord, double yCoord, double zCoord, double tempFactor, double occupancy, int charge, int serialNumber, String aminoFK, int model) {
            this.atomID = atomID;
            this.atomName = atomName;
            this.altLoc = altLoc;
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.zCoord = zCoord;
            this.tempFactor = tempFactor;
            this.occupancy = occupancy;
            this.charge = charge;
            this.serialNumber = serialNumber;
            this.aminoFK = aminoFK;
            this.model = model;
        }    
    }
    
    
    
}
