package command;

import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

/**
 * command pattern
 * Daniel Huson, 12.2017
 */
public abstract class ACommand {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract public void execute() throws Exception;

    abstract public void undo() throws Exception;

    abstract public void redo() throws Exception;

    public boolean isExecutable() {
        return true;
    }

    public boolean isUndoable() {
        return true;
    }

    public boolean isRedoable() {
        return true;
    }

    public void setToolbar(PDBViewerView pdbView, ProteinView proteinView, String oldModel) {
        switch (oldModel) {
            case "spacingfilling":
                pdbView.getShowAtomsCB().setDisable(false);
                pdbView.getShowAtomsCB().setSelected(true);
                pdbView.getShowBondsCB().setDisable(true);
                pdbView.getShowBondsCB().setSelected(false);
                pdbView.getShowRibbonsCB().setSelected(false);
                pdbView.getShowRibbonsCB().setDisable(true);
                pdbView.getIncreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseBondsBu().setDisable(true);
                pdbView.getIncreaseBondsBu().setDisable(true);
                break;
            case "mainchainatom":
                pdbView.getShowAtomsCB().setDisable(false);
                pdbView.getShowAtomsCB().setSelected(true);
                pdbView.getShowBondsCB().setDisable(false);
                pdbView.getShowBondsCB().setSelected(true);
                pdbView.getShowRibbonsCB().setSelected(false);
                pdbView.getShowRibbonsCB().setDisable(true);
                pdbView.getIncreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseBondsBu().setDisable(false);
                pdbView.getIncreaseBondsBu().setDisable(false);
                break;
            case "backbone":
                pdbView.getShowAtomsCB().setDisable(false);
                pdbView.getShowAtomsCB().setSelected(true);
                pdbView.getShowBondsCB().setDisable(false);
                pdbView.getShowBondsCB().setSelected(true);
                pdbView.getShowRibbonsCB().setSelected(false);
                pdbView.getShowRibbonsCB().setDisable(true);
                pdbView.getIncreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseBondsBu().setDisable(false);
                pdbView.getIncreaseBondsBu().setDisable(false);
                break;
            case "ribbon":
                pdbView.getShowAtomsCB().setDisable(false);
                pdbView.getShowAtomsCB().setSelected(true);
                pdbView.getShowBondsCB().setDisable(true);
                pdbView.getShowBondsCB().setSelected(false);
                pdbView.getShowRibbonsCB().setDisable(false);
                pdbView.getShowRibbonsCB().setSelected(true);
                pdbView.getIncreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseBondsBu().setDisable(false);
                pdbView.getIncreaseBondsBu().setDisable(false);
                break;
            case "structure":
                pdbView.getShowAtomsCB().setDisable(false);
                pdbView.getShowAtomsCB().setSelected(true);
                pdbView.getShowBondsCB().setDisable(false);
                pdbView.getShowBondsCB().setSelected(true);
                pdbView.getShowRibbonsCB().setDisable(false);
                pdbView.getShowRibbonsCB().setSelected(true);
                pdbView.getIncreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseAtomsBu().setDisable(false);
                pdbView.getDecreaseBondsBu().setDisable(false);
                pdbView.getIncreaseBondsBu().setDisable(false);
                break;
        }
        proteinView.setModel(oldModel);
    }
}
