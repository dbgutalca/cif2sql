
package ProteinStructure.SecondaryStructure;

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
public class Sheet implements SQLStructure{
    private List<SheetInfo> sheets = new ArrayList();
    
    @Override
    public void setData(MmCifBlock data, SQLWriter sqlWriter) {
        System.out.print("Reading sheet info... ");
        String entryId = data.getEntry().getId().get(0);
        for (int i = 0; i < data.getStructSheet().getRowCount(); i++) {
            String sheetId = entryId + "_" + data.getStructSheet().getId().get(i);
            int numStrands = data.getStructSheet().getNumberStrands().get(i);
            try {
                sqlWriter.writeSQLFile("sheet(sheet_id, num_strands, prot_fk)", "('"+sheetId+"','"+numStrands+"','"+entryId+"')");
                GlobalData.numSheet+=1;
                //sheets.add(new SheetInfo(sheetId, numStrands, entryId));
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        System.out.println(" Done!");
    }


    @Override
    public void printData() {
        for (SheetInfo info: sheets) {
            System.out.println(info.sheetId + " "+info.numStrands+" "+info.protFK);
        }
    }
    
    private class SheetInfo{
        private String sheetId;
        private int numStrands;
        private String protFK;

        public SheetInfo(String sheetId, int numStrands, String protFK) {
            this.sheetId = sheetId;
            this.numStrands = numStrands;
            this.protFK = protFK;
        }

    }
}
