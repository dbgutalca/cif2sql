package ProteinStructure;

import Tools.SQLWriter;
import org.rcsb.cif.schema.mm.MmCifBlock;

/**
 *
 * @author Diego
 */
public interface SQLStructure {
    void setData(MmCifBlock data, SQLWriter sqlWriter); //Sets the data into a SQL file    
    void printData();
}
