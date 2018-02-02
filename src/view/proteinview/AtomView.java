package view.proteinview;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import model.proteinmodel.AminoAcidProperties;
import model.proteinmodel.Atom;

public class AtomView extends Sphere {

    private Color color;
    private double radius = 6;
    private Atom atom;
    private String type;

    public AtomView(Atom atom) {
        this.atom = atom;

        this.setTranslateX(atom.getX());
        this.setTranslateY(atom.getY());
        this.setTranslateZ(atom.getZ());
        highlightAtoms();
        addTooltip(atom.getAtomId(), atom.getAtom());

        this.setRadius(radius);
        this.visibleProperty().setValue(true);
    }

    public void scaleAtom(String type) {
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setNode(this);
        scaleTransition.setDuration(Duration.millis(300));
        if (type.equals("decrease")) {
            scaleTransition.setToX(this.getScaleX() - 0.2 * this.getScaleX());
            scaleTransition.setToY(this.getScaleX() - 0.2 * this.getScaleY());
            scaleTransition.setToZ(this.getScaleX() - 0.2 * this.getScaleZ());
        } else if (type.equals("increase")) {
            scaleTransition.setToX(this.getScaleX() + 0.2 * this.getScaleX());
            scaleTransition.setToY(this.getScaleX() + 0.2 * this.getScaleY());
            scaleTransition.setToZ(this.getScaleX() + 0.2 * this.getScaleZ());
        }

        scaleTransition.play();
    }

    public void addTooltip(int id, String atom) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("ID: " + id + ", Atom: " + atom + ", AA: " + this.atom.getAa().getLetter() + ", Chain: " + this.atom.getChainId() + ", Structure: " + this.atom.getAa().getStructure());
        Tooltip.install(this, tooltip);
    }

    public Atom getAtom() {
        return atom;
    }

    public void setHighlighting(String highlighting) {
        switch (highlighting) {
            case "atoms":
                highlightAtoms();
                break;
            case "aminoacids":
                highlightAminoacids();
                break;
            case "structures":
                highlightStructures();
                break;
            case "chains":
                highlightChains();
                break;
        }
    }

    private void highlightAtoms() {
        switch (atom.getType()) {
            case 'O':
                this.color = Color.web("#FF0D0D");
                break;
            case 'N':
                this.color = Color.web("#3050F8");
                break;
            case 'S':
                this.color = Color.web("FFFF30");
                break;
            default:
                this.color = Color.web("909090");
                break;
        }
        this.setMaterial(new PhongMaterial(color));
    }

    private void highlightAminoacids() {
        this.color = AminoAcidProperties.aminoAcidColors.get(this.getAtom().getAa().getLetter());
        this.setMaterial(new PhongMaterial(color));
    }

    private void highlightStructures() {
        switch (this.getAtom().getAa().getStructure()) {
            case 'H':
                this.color = Color.web("FF0080");
                break;
            case 'E':
                this.color = Color.web("FFC800");
                break;
            default:
                this.color = Color.web("FFFFFF");
                break;
        }
        this.setMaterial(new PhongMaterial(color));
    }

    private void highlightChains() {
        switch (this.getAtom().getChainId()) {
            case "A":
                this.color = Color.web("#7FDBFF");
                break;
            case "B":
                this.color = Color.web("#2ECC40");
                break;
            case "C":
                this.color = Color.web("#FFDC00");
                break;
            case "D":
                this.color = Color.web("#F012BE");
                break;
            case "E":
                this.color = Color.web("#39CCCC");
                break;
            case "F":
                this.color = Color.web("#01FF70");
                break;
            case "G":
                this.color = Color.web("#FF851B");
                break;
            default:
                this.color = Color.web("FFFFFF");
                break;
        }
        this.setMaterial(new PhongMaterial(color));
    }

    public void setModel(String model) {
        resetScale();
        this.visibleProperty().setValue(true);
        if (type.equals("side")) {
            this.visibleProperty().setValue(false);
        }
        switch (model) {
            case "spacingfilling":
                setSpacingFillingModel();
                break;
            case "mainchainatom":
                setMainChainAtomModel();
                break;
            case "backbone":
                setBackboneModel();
                break;
            case "ribbon":
                setRibbonModel();
                break;
            case "structure":
                setStructureModel();
                break;
        }
    }

    private void setSpacingFillingModel() {
        if (type.equals("side")) {
            this.visibleProperty().setValue(true);
        }
        switch (atom.getType()) {
            case 'O':
                this.radiusProperty().setValue(40 * 0.72);
                break;
            case 'N':
                this.radiusProperty().setValue(40 * 0.93);
                break;
            case 'S':
                this.radiusProperty().setValue(40 * 1.35);
                break;
            default:
                this.radiusProperty().setValue(40);
                break;
        }
    }

    private void setRibbonModel() {
        this.setRadius(3);
    }

    private void setStructureModel() {
        this.setRadius(3);
        if (this.getAtom().getAa().getStructure() != '-' || atom.getAtom().equals("CB") || atom.getAtom().equals("O")){
            this.visibleProperty().setValue(false);
        }
    }

    private void setMainChainAtomModel() {
        switch (atom.getType()) {
            case 'O':
                this.radiusProperty().setValue(10 * 0.72);
                break;
            case 'N':
                this.radiusProperty().setValue(10 * 0.93);
                break;
            case 'S':
                this.radiusProperty().setValue(10 * 1.35);
                break;
            case 'P':
                this.radiusProperty().setValue(10 * 1.42);
                break;
            default:
                this.radiusProperty().setValue(10);
                break;
        }
    }

    private void setBackboneModel() {
        this.setRadius(3);
        if (atom.getAtom().equals("CB") || atom.getAtom().equals("O")) {
            this.visibleProperty().setValue(false);
        }
    }

    private void resetScale() {
        this.scaleXProperty().set(1);
        this.scaleYProperty().set(1);
        this.scaleZProperty().set(1);
    }

    public void setType(String type) {
        this.type = type;
    }
}
