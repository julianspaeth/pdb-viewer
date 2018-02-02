package command;

import view.proteinview.ProteinView;

public class HighlightStructuresCommand extends ACommand {

    private ProteinView proteinView;
    private String oldHighlighting;

    public HighlightStructuresCommand(ProteinView proteinView) {
        this.proteinView = proteinView;
        setName("Color Atoms");
    }

    @Override
    public void execute() throws Exception {
        oldHighlighting = proteinView.getCurrentHighlighting();
        proteinView.setHighlighting("structures");
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
