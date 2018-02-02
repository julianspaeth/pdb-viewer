package model.proteinmodel;

import java.util.*;

public class AminoAcid {

    private static final Set<String> mainChainAtoms = new HashSet<>(Arrays.asList("N", "CA", "C", "O", "CB"));
    private char letter, structure;
    private HashMap<String, Atom> atoms;
    private int id;
    private String chainId;

    public AminoAcid(int id) {
        this.id = id;
        this.atoms = new HashMap<>();
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public HashMap<String, Atom> getAtoms() {
        return atoms;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Atom> getMainChainAtoms() {
        ArrayList<Atom> mainChainAtomsList = new ArrayList<>();
        for (String atom : atoms.keySet()) {
            if (mainChainAtoms.contains(atom)) {
                mainChainAtomsList.add(atoms.get(atom));
            }
        }
        return mainChainAtomsList;
    }

    public ArrayList<Atom> getSideChainAtoms() {
        ArrayList<Atom> sideChainAtomsList = new ArrayList<>();
        for (String atom : atoms.keySet()) {
            if (!mainChainAtoms.contains(atom)) {
                sideChainAtomsList.add(atoms.get(atom));
            }
        }
        return sideChainAtomsList;
    }

    public void setBonds(Protein protein) {
        Bond ca_n = new Bond(this.getAtoms().get("CA"), this.getAtoms().get("N"), this.getStructure());
        Bond ca_c = new Bond(this.getAtoms().get("CA"), this.getAtoms().get("C"), this.getStructure());
        Bond c_o = new Bond(this.getAtoms().get("C"), this.getAtoms().get("O"), this.getStructure());
        protein.getBonds().addAll(ca_n, ca_c, c_o);
        if (this.getAtoms().containsKey("CB")) {
            Bond ca_cb = new Bond(this.getAtoms().get("CA"), this.getAtoms().get("CB"), this.getStructure());
            protein.getBonds().add(ca_cb);
        }
    }

    public char getStructure() {
        return structure;
    }

    public void setStructure(char structure) {
        this.structure = structure;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
}
