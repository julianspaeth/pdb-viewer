package model.proteinmodel;

import io.PDBParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Protein {

    private static final Logger LOG = Logger.getLogger(Protein.class.getName());
    private ObservableList<AminoAcid> aminoAcids;
    private ObservableList<Atom> atoms;
    private ObservableList<Bond> bonds;
    private HashMap<Character, Integer> numberOfAminoAcids = new HashMap<>();
    private StringProperty sequence = new SimpleStringProperty("");
    private StringProperty structure = new SimpleStringProperty("");
    private StringProperty title = new SimpleStringProperty("");
    private String id;

    public Protein() {
        aminoAcids = FXCollections.observableList(new ArrayList<>());
        atoms = FXCollections.observableList(new ArrayList<>());
        bonds = FXCollections.observableList(new ArrayList<>());
        title.setValue("Protein");
    }

    public void read(BufferedReader br) {
        aminoAcids.clear();
        atoms.clear();
        bonds.clear();
        try {
            PDBParser.parseFile(br, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        combineAas();
        LOG.info("File was succesfully parsed! ");
        LOG.info("Amino acids: " + this.getAminoAcids().size());
        LOG.info("Atoms: " + this.getAtoms().size());
        LOG.info("Bonds: " + this.getBonds().size());
    }

    private void combineAas() {
        AminoAcid source = null;
        for (AminoAcid aa : aminoAcids) {
            aa.setBonds(this);
            //connect only atoms with the same chain ID
            if (source != null && (source.getAtoms().get("C").getChainId().equals(aa.getAtoms().get("N").getChainId()))) {
                Bond c_n2 = new Bond(source.getAtoms().get("C"), aa.getAtoms().get("N"), source.getStructure());
                this.bonds.add(c_n2);
            }
            source = aa;
        }
    }

    public ObservableList<AminoAcid> getAminoAcids() {
        return aminoAcids;
    }

    public ObservableList<Atom> getAtoms() {
        return atoms;
    }

    public StringProperty sequenceProperty() {
        return sequence;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public ObservableList<Bond> getBonds() {
        return bonds;
    }

    public void setSequence(String sequence) {
        this.sequence.set(sequence);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setStructure(String structure) {
        this.structure.set(structure);
    }

    public StringProperty structureProperty() {
        return structure;
    }

    public HashMap<Character, Integer> getNumberOfAminoAcids() {
        return this.numberOfAminoAcids;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
