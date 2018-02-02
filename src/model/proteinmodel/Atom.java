package model.proteinmodel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Atom {

    private DoubleProperty x = new SimpleDoubleProperty(), y = new SimpleDoubleProperty(), z = new SimpleDoubleProperty();
    private StringProperty atom = new SimpleStringProperty();
    private String chainId;
    private int aaId, atomId;
    private AminoAcid aa;
    private Character type;

    public Atom() {
        atom.setValue("");
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public double getZ() {
        return z.get();
    }

    public DoubleProperty zProperty() {
        return z;
    }

    public int getAaId() {
        return aaId;
    }

    public void setAaId(int aaId) {
        this.aaId = aaId;
    }

    public int getAtomId() {
        return atomId;
    }

    public void setAtomId(int atomId) {
        this.atomId = atomId;
    }

    public String getAtom() {
        return atom.get();
    }

    public void setAtom(String atom) {
        this.atom.setValue(atom);
    }

    public AminoAcid getAa() {
        return aa;
    }

    public void setAa(AminoAcid aa) {
        this.aa = aa;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }
}
