package command;

import view.proteinview.ProteinView;

public class HighlightAtomsCommand extends ACommand {

    private ProteinView proteinView;
    private String oldHighlighting;

    public HighlightAtomsCommand(ProteinView proteinView) {
        this.proteinView = proteinView;
        setName("Color Atoms");
    }

    @Override
    public void execute() throws Exception {
        oldHighlighting = proteinView.getCurrentHighlighting();
        proteinView.setHighlighting("atoms");
    }

    @Override
    public void undo() throws Exception {
        proteinView.setHighlighting(oldHighlighting);
    }

    @Override
    public void redo() throws Exception {
        execute();
    }

}
