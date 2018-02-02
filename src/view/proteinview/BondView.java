package view.proteinview;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import model.proteinmodel.AminoAcidProperties;
import model.proteinmodel.Bond;

public class BondView extends Group {

    private ALine3D aLine3D;
    private AtomView source, target;
    private Bond bond;
    private Color color = Color.web("WHITE");

    public BondView(Bond bond, AtomView source, AtomView target) {
        this.source = source;
        this.target = target;
        this.bond = bond;
        this.aLine3D = new ALine3D(source.getTranslateX(),
                source.getTranslateY(),
                source.getTranslateZ(),
                target.getTranslateX(),
                target.getTranslateY(),
                target.getTranslateZ(),
                color);

        this.getChildren().add(aLine3D);
    }

    public void changeBondSize(String type) {
        aLine3D.changeRadius(type);
    }

    public Bond getBond() {
        return bond;
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
        aLine3D.setColor(Color.web("FFFFFF"));
    }

    private void highlightAminoacids() {
        color = AminoAcidProperties.aminoAcidColors.get(bond.getSource().getAa().getLetter());
        aLine3D.setColor(color);
    }

    private void highlightStructures() {
        switch (bond.getStructure()) {
            case 'H':
                color = Color.web("FF0080");
                aLine3D.setColor(color);
                break;
            case 'E':
                color = Color.web("FFC800");
                aLine3D.setColor(color);
                break;
            default:
                color = Color.web("FFFFFF");
                aLine3D.setColor(color);
                break;
        }
    }

    private void highlightChains() {
        switch (bond.getSource().getChainId()) {
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
        aLine3D.setColor(color);
    }

    public void setModel(String model) {
        this.visibleProperty().setValue(true);
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

    private void setBackboneModel() {
        if (bond.getTarget().getAtom().equals("CB") || bond.getTarget().getAtom().equals("O")) {
            this.visibleProperty().setValue(false);
        }
        aLine3D.setRadius(3);
    }

    private void setRibbonModel() {
        this.visibleProperty().setValue(false);
    }

    private void setStructureModel() {
        if (bond.getTarget().getAtom().equals("CB") || bond.getTarget().getAtom().equals("O") || this.getBond().getStructure() != '-') {
            this.visibleProperty().setValue(false);
        }
        aLine3D.setRadius(3);
    }

    private void setMainChainAtomModel() {
        aLine3D.setRadius(3);
    }

    private void setSpacingFillingModel() {
        this.visibleProperty().setValue(false);
        aLine3D.setRadius(3);
    }

}
