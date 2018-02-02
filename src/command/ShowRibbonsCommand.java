package command;

import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

public class ShowRibbonsCommand extends ACommand {

    private ProteinView proteinView;
    private PDBViewerView pdbViewerView;

    public ShowRibbonsCommand(ProteinView proteinView, PDBViewerView pdbVieweView) {
        this.proteinView = proteinView;
        this.pdbViewerView = pdbVieweView;
        this.setName("Show Ribbons");
    }

    @Override
    public void execute() throws Exception {
        if (pdbViewerView.getShowRibbonsCB().isSelected()) {
            proteinView.getRibbonViewGroup().visibleProperty().setValue(true);
        } else {
            proteinView.getRibbonViewGroup().visibleProperty().setValue(false);
        }
    }

    @Override
    public void undo() throws Exception {
        if (!pdbViewerView.getShowRibbonsCB().isSelected()) {
            proteinView.getRibbonViewGroup().visibleProperty().setValue(true);
            pdbViewerView.getShowRibbonsCB().setSelected(true);
        } else {
            proteinView.getRibbonViewGroup().visibleProperty().setValue(false);
            pdbViewerView.getShowRibbonsCB().setSelected(false);
        }
    }

    @Override
    public void redo() throws Exception {
        if (!pdbViewerView.getShowRibbonsCB().isSelected()) {
            proteinView.getRibbonViewGroup().visibleProperty().setValue(true);
            pdbViewerView.getShowRibbonsCB().setSelected(true);
        } else {
            proteinView.getRibbonViewGroup().visibleProperty().setValue(false);
            pdbViewerView.getShowRibbonsCB().setSelected(false);
        }
    }


}
