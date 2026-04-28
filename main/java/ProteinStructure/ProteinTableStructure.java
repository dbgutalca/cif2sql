
package ProteinStructure;

import ProteinStructure.PrimaryStructure.AminoAcid;
import ProteinStructure.PrimaryStructure.Atom;
import ProteinStructure.PrimaryStructure.HetAtom;
import ProteinStructure.PrimaryStructure.Ligand;
import ProteinStructure.PrimaryStructure.Prot_chain;
import ProteinStructure.PrimaryStructure.SSBond;
import ProteinStructure.ProteinInfo.Protein;
import ProteinStructure.ProteinInfo.Keyword;
import ProteinStructure.ProteinInfo.ProtSource;
import ProteinStructure.SecondaryStructure.Helix;
import ProteinStructure.SecondaryStructure.Sheet;
import ProteinStructure.SecondaryStructure.Strand;
import Tools.GlobalData;
import Tools.SQLWriter;
import Tools.Writer;
import java.io.IOException;
import java.nio.file.Paths;
import org.rcsb.cif.CifIO;
import org.rcsb.cif.model.CifFile;
import org.rcsb.cif.schema.StandardSchemata;
import org.rcsb.cif.schema.mm.MmCifBlock;
import org.rcsb.cif.schema.mm.MmCifFile;

/**
 *  Contains a representation of the SQL4PDB SQL Tables in the form of java Classes for a PDB entry
 * @author Diego
 */
public class ProteinTableStructure {    
    private Protein protein = new Protein();
    private MmCifBlock cifData;
    private Keyword keyword = new Keyword();
    private ProtSource source = new ProtSource();
    private Prot_chain protChain = new Prot_chain();
    private AminoAcid aminoacid = new AminoAcid();
    private Ligand ligand = new Ligand();
    private Atom atom = new Atom();
    private HetAtom hetAtom = new HetAtom();
    private SSBond ssBond = new SSBond();
    private Helix helix = new Helix();
    private Sheet sheet = new Sheet();
    private Strand strand = new Strand();
    private Writer fileManager = Writer.getInstance();
    private String Url;

    public ProteinTableStructure(String URL) throws IOException{        
        this.Url = URL;
        System.out.print("Reading file... ");
        //Creates the cifFileData
        CifFile cifFile = CifIO.readFromPath(Paths.get(this.Url));
        MmCifFile mmCifFile = cifFile.as(StandardSchemata.MMCIF);
        this.cifData = mmCifFile.getFirstBlock();      
        System.out.println(" Done");
        //Gets classification and experiment
        String classification = this.cifData.getStructKeywords().getPdbxKeywords().get(0);
        String expData = this.cifData.getExptl().getMethod().get(0);   
        //Checks experiment and classification
        if (ignoreEntry(expData)) {   
            System.out.println("Invalid Experiment. Omitting");
            GlobalData.logTotalOmited++;
            this.fileManager.omitUrl(URL);
        }else if(isDNA_RNA(classification)){
            setGeneralData();
            GlobalData.logTotalAdded++;
        }else{
            setData();            
            GlobalData.logTotalAdded++;
        }
        
    }
    
    /**
     * Sets the general data of a PDB entry
     * @throws IOException 
     */
    private void setGeneralData() throws IOException{
        SQLWriter sqlWriter = new SQLWriter(cifData.getStruct().getEntryId().get(0));        
        this.protein.setData(cifData, sqlWriter);
        this.keyword.setData(cifData, sqlWriter);
        this.source.setData(cifData, sqlWriter);
    }
    
    /**
     * Sets the full data of a PDB entry.
     * @throws IOException 
     */
    private void setData() throws IOException{
        SQLWriter sqlWriter = new SQLWriter(cifData.getStruct().getEntryId().get(0));        
        this.aminoacid.setData(cifData, sqlWriter);
        if (this.aminoacid.HasStandardAmino()) {    
            this.protein.setData(cifData, sqlWriter);
            this.keyword.setData(cifData, sqlWriter);
            this.source.setData(cifData, sqlWriter);
            this.protChain.setData(cifData, sqlWriter);       
            this.aminoacid.toSQL(sqlWriter);
            this.atom.setData(cifData, sqlWriter);
            this.ligand.setData(cifData, sqlWriter);            
            this.hetAtom.setData(cifData, sqlWriter);            
            this.helix.setAminoAcids(this.aminoacid);
            this.helix.setData(cifData, sqlWriter);  
            this.sheet.setData(cifData, sqlWriter);
            this.strand.setAminoAcids(this.aminoacid);
            this.strand.setData(cifData, sqlWriter);
            this.ssBond.setData(cifData, sqlWriter);
            this.protein.updateValues(sqlWriter, cifData);
            fileManager.addUrl(this.Url);
        }else{
            fileManager.omitUrl(this.Url);
        }
        
    }
    
    private boolean isDNA_RNA(String classification){
        return classification.equals("RNA") || classification.equals("DNA");
    }
    
    private boolean ignoreEntry(String expData){
        return expData.contains("NMR") || expData.contains("CRYSTALLOGRAPHY");
    }
    
    public void printData(){
        //this.protein.printData();
        //this.keyword.printData();        
        //this.source.printData();
        //this.protChain.printData();
        //this.aminoacid.printData();
        //this.ligand.printData();
        //this.atom.printData();
       // this.hetAtom.printData();
        //this.ssBond.printData(); 
        //this.helix.printData();
        //this.sheet.printData();
        //this.strand.printData();
    }
 }
