package command;

import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

public class ShowAtomsCommand extends ACommand {

    private ProteinView proteinView;
    private PDBViewerView pdbViewerView;

    public ShowAtomsCommand(ProteinView proteinView, PDBViewerView pdbVieweView) {
        this.proteinView = proteinView;
        this.pdbViewerView = pdbVieweView;
        this.setName("Show Atoms");
    }

    @Override
    public void execute() throws Exception {
        if (pdbViewerView.getShowAtomsCB().isSelected()) {
            proteinView.getAminoAcidViewGroup().visibleProperty().setValue(true);
        } else {
            proteinView.getAminoAcidViewGroup().visibleProperty().setValue(false);
        }
    }

    @Override
    public void undo() throws Exception {
        if (!pdbViewerView.getShowAtomsCB().isSelected()) {
            proteinView.getAminoAcidViewGroup().visibleProperty().setValue(true);
            pdbViewerView.getShowAtomsCB().setSelected(true);
        } else {
            proteinView.getAminoAcidViewGroup().visibleProperty().setValue(false);
            pdbViewerView.getShowAtomsCB().setSelected(false);
        }
    }

    @Override
    public void redo() throws Exception {
        if (!pdbViewerView.getShowAtomsCB().isSelected()) {
            proteinView.getAminoAcidViewGroup().visibleProperty().setValue(true);
            pdbViewerView.getShowAtomsCB().setSelected(true);
        } else {
            proteinView.getAminoAcidViewGroup().visibleProperty().setValue(false);
            pdbViewerView.getShowAtomsCB().setSelected(false);
        }
    }


}
