package command;

import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

public class StructureModelCommand extends ACommand {

    private ProteinView proteinView;
    private PDBViewerView pdbView;
    private String oldModel;

    public StructureModelCommand(ProteinView proteinView, PDBViewerView pdbView) {
        this.proteinView = proteinView;
        this.pdbView = pdbView;
        setName("Structure model");
    }

    @Override
    public void execute() throws Exception {
        oldModel = proteinView.getCurrentModel();
        proteinView.setModel("structure");
        setToolbar(pdbView, proteinView, proteinView.getCurrentModel());
    }

    @Override
    public void undo() throws Exception {
        setToolbar(pdbView, proteinView, oldModel);
    }

    @Override
    public void redo() throws Exception {
        execute();
    }

}
