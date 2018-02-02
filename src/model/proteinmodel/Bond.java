package model.proteinmodel;

public class Bond {

    private Atom source, target;
    private char structure;

    public Bond(Atom source, Atom target, Character structure) {
        this.source = source;
        this.target = target;
        this.structure = structure;
    }

    public Atom getSource() {
        return source;
    }

    public Atom getTarget() {
        return target;
    }

    public char getStructure() {
        return structure;
    }
}
