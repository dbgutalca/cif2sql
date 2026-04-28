
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
public class HetAtom implements SQLStructure{
    private List<HetAtomInfo> hetAtoms = new ArrayList();

    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading HetAtom info... ");
        String entryID = data.getStruct().getEntryId().get(0);
        for (int i = GlobalData.numAtom; i < GlobalData.numCoord; i++) {  
            if (data.getAtomSite().getGroupPDB().get(i).equals("HETATM")) {
                String chainID = data.getAtomSite().getLabelAsymId().get(i);
                int aminoSeqNum = data.getAtomSite().getAuthSeqId().get(i);
                int model = data.getAtomSite().getPdbxPDBModelNum().get(i);
                String hetAtomName = data.getAtomSite().getTypeSymbol().get(i);
                String altLoc = "";
                double xCoord = data.getAtomSite().getCartnX().get(i);
                double yCoord = data.getAtomSite().getCartnY().get(i);
                double zCoord = data.getAtomSite().getCartnZ().get(i);
                double tempFactor = data.getAtomSite().getBIsoOrEquiv().get(i);
                double occupancy = data.getAtomSite().getOccupancy().get(i);
                int charge = data.getAtomSite().getPdbxFormalCharge().get(i);
                int serialNumber = data.getAtomSite().getId().get(i);
                String aminoFK = entryID + "_" + chainID+"_"+aminoSeqNum;
                String hetAtomID =aminoFK +"_"+model+"_"+serialNumber;

                String sqlValues = "('" + hetAtomID + "', " +
                   model + ", '" +
                   hetAtomName + "', '" +
                   altLoc + "', " +
                   xCoord + ", " +
                   yCoord + ", " +
                   zCoord + ", " +
                   tempFactor + ", " +
                   occupancy + ", " +
                   charge + ", " +
                   serialNumber + ", '" +
                   aminoFK + "')";
                String tableData = "hetAtom(hetAtom_id,model,hetAtom_name,altLoc, X_coord,Y_coord,Z_coord,tempFactor,occupancy,charge,serial_number,ligand_FK) ";
                try {
                    sqlWriter.writeSQLFile(tableData, sqlValues);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }            
        }        
        System.out.println(" Done!");
    }


    @Override
    public void printData() {
        for (HetAtomInfo info: hetAtoms) {
            System.out.println(info.hetAtomID +" "+info.hetAtomName+" "+info.xCoord+" "+info.yCoord+" "+info.zCoord+" "+info.charge+ " "+info.occupancy+" "+info.tempFactor);
            
        }
    }
    
    private class HetAtomInfo{
        private String hetAtomID;
        private String hetAtomName;
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

        public HetAtomInfo(String hetAtomID, String hetAtomName, String altLoc, double xCoord, double yCoord, double zCoord, double tempFactor, double occupancy, int charge, int serialNumber, String aminoFK, int model) {
            this.hetAtomID = hetAtomID;
            this.hetAtomName = hetAtomName;
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
