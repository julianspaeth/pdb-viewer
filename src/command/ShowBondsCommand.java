package command;

import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

public class ShowBondsCommand extends ACommand {

    private ProteinView proteinView;
    private PDBViewerView pdbViewerView;

    public ShowBondsCommand(ProteinView proteinView, PDBViewerView pdbViewerView) {
        this.proteinView = proteinView;
        this.pdbViewerView = pdbViewerView;
        this.setName("Show Bonds");
    }

    @Override
    public void execute() throws Exception {
        if (pdbViewerView.getShowBondsCB().isSelected()) {
            proteinView.getBondViewGroup().visibleProperty().setValue(true);
        } else {
            proteinView.getBondViewGroup().visibleProperty().setValue(false);
        }
    }

    @Override
    public void undo() throws Exception {
        if (!pdbViewerView.getShowBondsCB().isSelected()) {
            proteinView.getBondViewGroup().visibleProperty().setValue(true);
            pdbViewerView.getShowBondsCB().setSelected(true);
        } else {
            proteinView.getBondViewGroup().visibleProperty().setValue(false);
            pdbViewerView.getShowBondsCB().setSelected(false);
        }
    }

    @Override
    public void redo() throws Exception {
        if (!pdbViewerView.getShowBondsCB().isSelected()) {
            proteinView.getBondViewGroup().visibleProperty().setValue(true);
            pdbViewerView.getShowBondsCB().setSelected(true);
        } else {
            proteinView.getBondViewGroup().visibleProperty().setValue(false);
            pdbViewerView.getShowBondsCB().setSelected(false);
        }
    }


}
